# 🏋️ GymFlow: Especificaciones Técnicas

Este documento define las especificaciones técnicas del proyecto GymFlow, un sistema de gestión de suscripciones de gimnasio construido con **Spring Boot** y **PostgreSQL**.

---

## 🖥 Configuración del Entorno de Desarrollo

### 1. Requisitos del Sistema

- **Java:** JDK 17 (OpenJDK o Adoptium)
- **Maven:** 3.8+
- **PostgreSQL:** 15+ (Docker o instalación local)
- **IDE:** VS Code, IntelliJ IDEA o Eclipse

### 2. Dependencias Principal (pom.xml)

```xml
<dependencies>
    <!-- Spring Boot Starters -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-validation</artifactId>
    </dependency>
    
    <!-- PostgreSQL Driver -->
    <dependency>
        <groupId>org.postgresql</groupId>
        <artifactId>postgresql</artifactId>
        <scope>runtime</scope>
    </dependency>
    
    <!-- Lombok (opcional) -->
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <optional>true</optional>
    </dependency>
    
    <!-- Testing -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>com.h2database</groupId>
        <artifactId>h2</artifactId>
        <scope>test</scope>
    </dependency>
</dependencies>
```

---

## 🗄 Configuración de Base de Datos

### 1. Esquema de Datos (PostgreSQL)

```sql
-- Tabla: socios
CREATE TABLE socios (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    email VARCHAR(255) NOT NULL UNIQUE,
    nombre VARCHAR(255) NOT NULL,
    estado VARCHAR(20) NOT NULL DEFAULT 'INACTIVO',
    fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Tabla: suscripciones
CREATE TABLE suscripciones (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    socio_id UUID NOT NULL REFERENCES socios(id),
    fecha_inicio DATE NOT NULL,
    fecha_fin DATE NOT NULL,
    estado VARCHAR(20) NOT NULL DEFAULT 'ACTIVA',
    fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Tabla: pagos (para validar pendientes)
CREATE TABLE pagos (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    socio_id UUID NOT NULL REFERENCES socios(id),
    monto DECIMAL(10,2) NOT NULL,
    estado VARCHAR(20) NOT NULL DEFAULT 'PENDIENTE',
    fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Índices
CREATE INDEX idx_socios_email ON socios(email);
CREATE INDEX idx_suscripciones_socio_id ON suscripciones(socio_id);
CREATE INDEX idx_pagos_socio_id ON pagos(socio_id);
```

### 2. Configuración application.yml

```yaml
server:
  port: 8080

spring:
  application:
    name: gymflow
  
  datasource:
    url: jdbc:postgresql://localhost:5432/gymflow
    username: postgres
    password: postgres
    driver-class-name: org.postgresql.Driver
  
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
    properties:
      hibernate:
        dialect: org.hibernate.dialect.PostgreSQLDialect
        format_sql: true
    open-in-view: false
```

---

## 🏗 Estructura del Proyecto

```text
src/main/java/com/gymflow/
│
├── GymFlowApplication.java          <-- Punto de entrada
│
├── domain/                          <-- Capa de Dominio (Núcleo)
│   ├── model/
│   │   ├── Socio.java               (Entidad)
│   │   ├── Suscripcion.java        (Entidad)
│   │   └── EstadoSocio.java         (Value Object - Enum)
│   │
│   └── ports/
│       ├── in/
│       │   ├── RegistrarSocioUseCase.java
│       │   ├── RenovarSuscripcionUseCase.java
│       │   └── VerificarAccesoUseCase.java
│       └── out/
│           ├── SocioRepository.java
│           ├── SuscripcionRepository.java
│           └── NotificationService.java
│
├── application/                    <-- Capa de Aplicación
│   └── usecases/
│       ├── RegistrarSocioUseCaseImpl.java
│       ├── RenovarSuscripcionUseCaseImpl.java
│       └── VerificarAccesoUseCaseImpl.java
│
└── infrastructure/                 <-- Capa de Infraestructura
    ├── adapters/
    │   ├── in/
    │   │   └── web/
    │   │       ├── dto/
    │   │       │   ├── RegistrarSocioRequest.java
    │   │       │   └── VerificarAccesoResponse.java
    │   │       └── SocioController.java
    │   │
    │   └── out/
    │       └── persistence/
    │           ├── entity/
    │           │   ├── SocioEntity.java
    │           │   └── SuscripcionEntity.java
    │           ├── mapper/
    │           │   └── SocioMapper.java
    │           └── adapter/
    │               ├── SocioRepositoryAdapter.java
    │               └── NotificationServiceAdapter.java
    │
    └── config/
        └── BeanConfiguration.java
```

---

## 🌐 API REST - Endpoints

### 1. Registrar Socio

```text
POST /api/socios
Content-Type: application/json

Request:
{
  "email": "juan@email.com",
  "nombre": "Juan Pérez"
}

Response (201 Created):
{
  "id": "uuid",
  "email": "juan@email.com",
  "nombre": "Juan Pérez",
  "estado": "INACTIVO"
}
```

### 2. Renovar Suscripción

```text
PUT /api/socios/{id}/suscripcion/renovar
Content-Type: application/json

Response (200 OK):
{
  "id": "uuid",
  "socioId": "uuid",
  "fechaInicio": "2026-04-14",
  "fechaFin": "2026-05-14",
  "estado": "ACTIVA"
}
```

### 3. Verificar Acceso

```text
GET /api/socios/{id}/acceso
Content-Type: application/json

Response (200 OK):
{
  "permitido": true,
  "mensaje": "Acceso concedido"
}

Response (403 Forbidden):
{
  "permitido": false,
  "mensaje": "Suscripción vencida"
}
```

---

## ✅ Códigos de Estado HTTP

| Código | Descripción                               |
|--------|-------------------------------------------|
| 200    | OK - Solicitud exitosa                    |
| 201    | Created - Recurso creado                  |
| 400    | Bad Request - Datos inválidos             |
| 404    | Not Found - Recurso no encontrado         |
| 409    | Conflict - Regla de negocio violada       |
| 500    | Internal Server Error - Error inesperado  |

---

## 🧪 Configuración para Testing

### application-test.yml

```yaml
spring:
  datasource:
    url: jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1
    driver-class-name: org.h2.Driver
    username: sa
    password:
  
  jpa:
    hibernate:
      ddl-auto: create-drop
    database-platform: org.hibernate.dialect.H2Dialect
```

### Ejecución de Tests

```bash
mvn test
```

---

## 🚀 Ejecución del Proyecto

### 1. Iniciar PostgreSQL con Docker

```bash
docker run -d \
  --name gymflow-postgres \
  -e POSTGRES_DB=gymflow \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_PASSWORD=postgres \
  -p 5432:5432 \
  postgres:15
```

### 2. Compilar y Ejecutar

```bash
mvn clean install
mvn spring-boot:run
```

### 3. Verificar que el servidor está activo

```text
http://localhost:8080/actuator/health
```

---

## 📋 Convenciones de Código

- **Paquetes:** lowerCamelCase (ej. `domain.model`, `infrastructure.adapters`)
- **Clases:** UpperCamelCase (ej. `SocioController`, `SocioRepository`)
- **Interfaces:** Prefijo `I` o sufijo `Port` (ej. `SocioRepository` o `ISocioPort`)
- **Entidades JPA:** Sufijo `Entity` (ej. `SocioEntity`)
- **DTOs:** Sufijo `Request` o `Response` (ej. `RegistrarSocioRequest`)
- **Variables:** lowerCamelCase (ej. `nombreVariable`)
- **Constantes:** UPPER_SNAKE_CASE (ej. `MAX_INTENTOS`)
- **Métodos:** lowerCamelCase (ej. `registrarSocio()`)
- **Paquetes:** lowerCamelCase (ej. `domain.model`, `infrastructure.adapters`)
