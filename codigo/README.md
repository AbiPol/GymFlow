# 🏋️ GymFlow - Sistema de Gestión de Suscripciones

API REST para la gestión de socios y accesos de un gimnasio, implementada con **Arquitectura Hexagonal** y **Spring Boot**.

## 📋 Descripción

GymFlow permite registrar socios, gestionar suscripciones y verificar el acceso al gimnasio. El proyecto está diseñado para separar la lógica de negocio (dominio) de los detalles técnicos (infraestructura), siguiendo los principios de la Arquitectura Hexagonal (Ports & Adapters).

## 🏗 Arquitectura

```
src/main/java/com/gymflow/
├── domain/                    # Núcleo de negocio (sin dependencias externas)
│   ├── model/                 # Entidades y Value Objects
│   └── ports/
│       ├── in/                # Puertos de entrada (Use Cases)
│       └── out/               # Puertos de salida (Repositorios)
├── application/              # Orquestación de casos de uso
│   └── usecases/
└── infrastructure/            # Adaptadores externos
    ├── adapters/
    │   ├── in/web/            # REST Controllers y DTOs
    │   └── out/persistence/   # JPA, Repositories
    └── config/                # Configuración de Spring
```

## 🛠 Tecnologías

| Tecnología | Versión |
|------------|---------|
| Java | 17 |
| Spring Boot | 3.3.0 |
| PostgreSQL | 15+ |
| Maven | 3.8+ |
| H2 (Testing) | - |

## 🚀 Getting Started

### Prerrequisitos

- JDK 17
- Maven 3.8+
- PostgreSQL 15+

### 1. Clonar el proyecto

```bash
git clone <repo-url>
cd GymFlow/codigo
```

### 2. Iniciar PostgreSQL

```bash
docker run -d \
  --name gymflow-postgres \
  -e POSTGRES_DB=gymflow \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_PASSWORD=postgres \
  -p 5432:5432 \
  postgres:15
```

### 3. Compilar y ejecutar

```bash
mvn clean install
mvn spring-boot:run
```

### 4. Verificar

```
http://localhost:8080
```

## 📡 API Endpoints

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `/api/socios` | Registrar nuevo socio |
| PUT | `/api/socios/{id}/suscripcion/renovar` | Renovar suscripción |
| GET | `/api/socios/{id}/acceso` | Verificar acceso al gimnasio |

## 🧪 Testing

```bash
# Ejecutar todos los tests
mvn test

# Ejecutar un test específico
mvn test -Dtest=SocioServiceTest

# Ejecutar un método de test
mvn test -Dtest=SocioServiceTest#testRegistrarSocio
```

## 📂 Estructura de Documentación

- `PROJECT_REQUIREMENTS.md` - Requerimientos funcionales
- `TECHNICAL_SPECIFICATIONS.md` - Especificaciones técnicas
- `DEVELOPMENT_PHASES.md` - Fases de desarrollo con control de progreso
- `AGENTS.md` - Guías para agentes de código

## 📝 Licencia

Este proyecto es para fines educativos.