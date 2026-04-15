# 🏋️ GymFlow - Sistema de Gestión de Suscripciones

![Java](https://img.shields.io/badge/Java-17-blue)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.0-green)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15+-blue)
![Tests](https://img.shields.io/badge/Tests-54%20passing-brightgreen)
![Architecture](https://img.shields.io/badge/Architecture-Hexagonal-orange)

API REST para la gestión de socios y accesos de un gimnasio, implementada con **Arquitectura Hexagonal** y **Spring Boot**.

---

## 📋 Descripción

GymFlow permite registrar socios, gestionar suscripciones y verificar el acceso al gimnasio. El proyecto está diseñado para separar la lógica de negocio (dominio) de los detalles técnicos (infraestructura), siguiendo los principios de la Arquitectura Hexagonal (Ports & Adapters).

### Estado del Proyecto

| Fase | Estado |
|------|--------|
| Configuración | ✅ Completada |
| Capa de Dominio | ✅ Completada |
| Capa de Aplicación | ✅ Completada |
| Persistencia | ✅ Completada |
| Web API | ✅ Completada |
| Testing | ✅ Completada (54 tests) |
| Documentación | ✅ Completada |

---

## 🏗 Arquitectura

El proyecto sigue la **Arquitectura Hexagonal** (también conocida como Ports & Adapters):

```
src/main/java/com/gymflow/
│
├── domain/                          # 🌵 Núcleo de negocio (sin dependencias externas)
│   ├── model/
│   │   ├── Email.java              # Value Object - Email válido
│   │   ├── SocioId.java            # Value Object - Identificador único
│   │   ├── EstadoSocio.java        # Enum - ACTIVO, INACTIVO, BLOQUEADO
│   │   ├── Socio.java             # Entidad - Socio del gimnasio
│   │   ├── Suscripcion.java       # Entidad - Suscripción del socio
│   │   └── Pago.java              # Entidad - Pago del socio
│   └── ports/
│       ├── in/                     # 🎯 Puertos de entrada (Use Cases)
│       │   ├── RegistrarSocioUseCase.java
│       │   ├── RenovarSuscripcionUseCase.java
│       │   └── VerificarAccesoUseCase.java
│       └── out/                    # 🚪 Puertos de salida (Repositorios)
│           ├── SocioRepository.java
│           ├── SuscripcionRepository.java
│           ├── PagoRepository.java
│           └── NotificationService.java
│
├── application/                    # 🎭 Capa de aplicación (orquestación)
│   └── usecases/
│       ├── RegistrarSocioUseCaseImpl.java
│       ├── RenovarSuscripcionUseCaseImpl.java
│       └── VerificarAccesoUseCaseImpl.java
│
└── infrastructure/                  # 🔌 Capa de infraestructura (adaptadores)
    ├── adapters/
    │   ├── in/web/                 # 🌐 Adaptadores de entrada (REST)
    │   │   ├── SocioController.java
    │   │   └── dto/
    │   │       ├── request/
    │   │       └── response/
    │   └── out/persistence/        # 💾 Adaptadores de salida (BD)
    │       ├── entity/
    │       ├── mapper/
    │       ├── repository/
    │       └── adapter/
    └── config/
        └── BeanConfiguration.java
```

---

## 🛠 Tecnologías

| Tecnología | Versión | Uso |
|-------------|---------|-----|
| Java | 17 | Lenguaje principal |
| Spring Boot | 3.3.0 | Framework web |
| Spring Data JPA | - | Persistencia |
| PostgreSQL | 15+ | Base de datos (producción) |
| H2 | - | Base de datos (testing) |
| Maven | 3.8+ | Gestión de dependencias |
| JUnit 5 | - | Testing |
| Mockito | - | Mocks en tests |

---

## 🚀 Getting Started

### Prerrequisitos

- JDK 17+ ([Instalar](https://adoptium.net/))
- Maven 3.8+ ([Instalar](https://maven.apache.org/install.html))
- Docker (para PostgreSQL) o PostgreSQL local

### 1. Clonar el proyecto

```bash
git clone https://github.com/AbiPol/GymFlow.git
cd GymFlow/codigo
```

### 2. Iniciar PostgreSQL

```bash
# Usando Docker (recomendado)
docker run -d \
  --name gymflow-postgres \
  -e POSTGRES_DB=gymflow \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_PASSWORD=postgres \
  -p 5432:5432 \
  postgres:15

# O conectar a una instancia existente de PostgreSQL
```

### 3. Configurar conexión (opcional)

Editar `src/main/resources/application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/gymflow
    username: postgres
    password: postgres
```

### 4. Compilar y ejecutar

```bash
# Compilar
mvn clean install

# Ejecutar
mvn spring-boot:run
```

### 5. Verificar que está corriendo

```bash
curl http://localhost:8080/actuator/health
# Respuesta: {"status":"UP"}
```

---

## 📡 API Endpoints

### Base URL
```
http://localhost:8080/api/socios
```

### 1. Registrar Socio

**POST** `/api/socios`

Registra un nuevo socio en el gimnasio.

**Request:**
```bash
curl -X POST http://localhost:8080/api/socios \
  -H "Content-Type: application/json" \
  -d '{"nombre": "Juan Pérez", "email": "juan.perez@email.com"}'
```

**Response (201 Created):**
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "email": "juan.perez@email.com",
  "nombre": "Juan Pérez",
  "estado": "INACTIVO",
  "fechaCreacion": "2026-04-15T13:30:00"
}
```

---

### 2. Renovar Suscripción

**PUT** `/api/socios/{id}/suscripcion/renovar`

Renueva la suscripción del socio por 30 días adicionales.

**Request:**
```bash
curl -X PUT http://localhost:8080/api/socios/550e8400-e29b-41d4-a716-446655440000/suscripcion/renovar
```

**Response (200 OK):**
```json
{
  "id": "660e8400-e29b-41d4-a716-446655440001",
  "socioId": "550e8400-e29b-41d4-a716-446655440000",
  "fechaInicio": "2026-04-15",
  "fechaFin": "2026-05-15",
  "estado": "ACTIVA"
}
```

---

### 3. Verificar Acceso

**GET** `/api/socios/{id}/acceso`

Verifica si un socio puede acceder al gimnasio.

**Request:**
```bash
curl http://localhost:8080/api/socios/550e8400-e29b-41d4-a716-446655440000/acceso
```

**Response (200 OK):**
```json
{
  "permitido": true,
  "mensaje": "Acceso concedido"
}
```

---

## 📊 Códigos de Estado HTTP

| Código | Descripción | Uso |
|--------|-------------|-----|
| 200 | OK | Operación exitosa |
| 201 | Created | Recurso creado |
| 400 | Bad Request | Datos inválidos |
| 404 | Not Found | Recurso no encontrado |
| 409 | Conflict | Regla de negocio violada |
| 500 | Internal Server Error | Error inesperado |

---

## 🧪 Testing

### Ejecutar tests

```bash
# Todos los tests
mvn test

# Solo tests de dominio
mvn test -Dtest="com.gymflow.domain.model.*Test"

# Solo tests de use cases
mvn test -Dtest="com.gymflow.application.usecases.*Test"

# Solo tests de controller
mvn test -Dtest="SocioControllerTest"
```

### Cobertura de tests

| Suite | Tests |
|-------|-------|
| Domain (Email, SocioId, Socio, Suscripcion, Pago) | 32 |
| Use Cases (Registrar, Renovar, Verificar) | 15 |
| Controller Integration | 6 |
| Application Tests | 1 |
| **Total** | **54** |

---

## 📂 Documentación

| Documento | Descripción |
|----------|-------------|
| `PROJECT_REQUIREMENTS.md` | Requerimientos funcionales |
| `TECHNICAL_SPECIFICATIONS.md` | Especificaciones técnicas |
| `DEVELOPMENT_PHASES.md` | Fases de desarrollo |
| `TESTING_PLAN.md` | Plan de testing |
| `MANUAL_TESTS.md` | Pruebas manuales con curl |
| `AGENTS.md` | Guías para agentes de código |

---

## 📝 Reglas de Negocio Implementadas

### RegistrarSocio
- ✅ Email debe ser único
- ✅ Socio inicia en estado INACTIVO
- ✅ Se envía notificación de bienvenida

### RenovarSuscripcion
- ✅ Verifica que no haya pagos pendientes
- ✅ Extiende 30 días desde fecha fin (si no vencida)
- ✅ Extiende 30 días desde hoy (si vencida)

### VerificarAcceso
- ✅ Acceso denegado si socio INACTIVO
- ✅ Acceso denegado si socio BLOQUEADO
- ✅ Acceso denegado si suscripción vencida
- ✅ Acceso concedido si ACTIVO y suscripción vigente

---

## 🔧 Configuración de Desarrollo

### application.yml (Desarrollo)

```yaml
server:
  port: 8080

spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/gymflow
    username: postgres
    password: postgres
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
```

### application-test.yml (Testing)

```yaml
spring:
  datasource:
    url: jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1
  jpa:
    hibernate:
      ddl-auto: create-drop
```

---

## 📜 Licencia

Este proyecto es para fines educativos.

---

## 👤 Autor

Desarrollado como parte del curso de formación en Arquitectura Hexagonal.
