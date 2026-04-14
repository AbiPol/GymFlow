# AGENTS.md - GymFlow Development Guidelines

## 📦 Project Overview

GymFlow is a Spring Boot REST API project using Hexagonal Architecture with PostgreSQL. The project follows clean architecture principles with clear separation between Domain, Application, and Infrastructure layers.

---

## 🛠 Build & Test Commands

### Maven Commands

```bash
# Build the project
mvn clean install

# Run the application
mvn spring-boot:run

# Run tests
mvn test

# Run a single test class
mvn test -Dtest=SocioServiceTest

# Run a single test method
mvn test -Dtest=SocioServiceTest#testRegistrarSocio

# Run tests with verbose output
mvn test -X

# Skip tests during build
mvn clean install -DskipTests

# Generate JAR file
mvn package -DskipTests
```

### Docker Commands

```bash
# Start PostgreSQL container
docker run -d --name gymflow-postgres \
  -e POSTGRES_DB=gymflow \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_PASSWORD=postgres \
  -p 5432:5432 postgres:15
```

---

## 📏 Code Style Guidelines

### 1. Package Structure (Hexagonal Architecture)

```text
src/main/java/com/gymflow/
├── domain/                    # Core business logic (NO Spring annotations)
│   ├── model/                 # Entities & Value Objects
│   └── ports/
│       ├── in/                # Use case interfaces (driving ports)
│       └── out/               # Repository interfaces (driven ports)
├── application/              # Use case implementations
│   └── usecases/
└── infrastructure/            # Adapters (Spring-aware)
    ├── adapters/
    │   ├── in/web/            # REST Controllers & DTOs
    │   └── out/persistence/   # JPA Entities, Mappers, Repositories
    └── config/                # Spring configuration
```

### 2. Naming Conventions

| Element | Convention | Example |
|---------|------------|---------|
| Packages | lowercase, singular | `com.gymflow.domain.model` |
| Classes | UpperCamelCase | `Socio`, `RegistrarSocioUseCase` |
| Interfaces | UpperCamelCase | `SocioRepository`, `NotificationService` |
| Methods | lowerCamelCase | `registrarSocio()`, `findByEmail()` |
| Variables | lowerCamelCase | `socio`, `fechaInicio` |
| Constants | UPPER_SNAKE_CASE | `ESTADO_ACTIVO`, `DEFAULT_PAGE_SIZE` |
| Entities (JPA) | Entity suffix | `SocioEntity`, `SuscripcionEntity` |
| DTOs | Request/Response suffix | `RegistrarSocioRequest`, `SocioResponse` |
| Tests | Test suffix | `SocioServiceTest`, `SocioControllerTest` |

### 3. Imports

**Organize imports in this order:**

1. Java standard library (`java.*`)
2. Spring framework (`org.springframework.*`)
3. Other external libraries
4. Project internal packages

**Example:**

```java
import java.time.LocalDate;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.gymflow.domain.model.Socio;
import com.gymflow.application.usecases.RegistrarSocioUseCase;
```

**PROHIBITED in Domain layer:**

- `@Component`, `@Service`, `@Repository`
- `@Entity`, `@Table` (use in infrastructure only)
- Jackson, JPA annotations in domain

### 4. Formatting

- **Indentation:** 4 spaces (no tabs)
- **Line length:** Max 120 characters
- **Blank lines:** Between methods, between import groups
- **Braces:** Same-line opening brace
- **Annotations:** Put on separate line before class/method

```java
@Service
public class SocioService {

    public Socio buscarPorId(UUID id) {
        return repository.findById(id);
    }
}
```

### 5. Types & Variables

- Use interfaces over concrete types when possible
- Prefer `List<T>` over `ArrayList<T>`
- Use `LocalDate`, `LocalDateTime` for dates (NOT `Date`)
- Use `UUID` for identifiers
- Avoid primitive wrappers when not needed (`int` not `Integer`)

```java
// ✅ Good
private final SocioRepository socioRepository;
private List<Suscripcion> suscripciones;

// ❌ Bad
private SocioRepositoryImpl socioRepository;
private ArrayList<Suscripcion> suscripciones;
```

### 6. Error Handling

- Use custom domain exceptions
- Return appropriate HTTP status codes:
  - `200 OK` - Success
  - `201 Created` - Resource created
  - `400 Bad Request` - Invalid input
  - `404 Not Found` - Resource not found
  - `409 Conflict` - Business rule violation
  - `500 Internal Server Error` - Unexpected error

- Use `@ControllerAdvice` for global exception handling

```java
@ExceptionHandler(EmailDuplicadoException.class)
@ResponseStatus(HttpStatus.CONFLICT)
public ErrorResponse handleEmailDuplicado(EmailDuplicadoException ex) {
    return new ErrorResponse(ex.getMessage());
}
```

### 7. Layer Responsibilities

| Layer | Responsibility | Examples |
|-------|---------------|----------|
| Domain | Business logic, entities, rules | `Socio`, `EstadoSocio` enum |
| Application | Orchestration, use cases | `RegistrarSocioUseCaseImpl` |
| Infrastructure | External concerns | `SocioEntity`, REST Controllers |

**Important:** Domain layer must NOT depend on Spring or any framework.

### 8. Testing Guidelines

- Test naming: `MethodName_StateUnderTest_ExpectedResult`
- One assertion per test when possible
- Use `@SpringBootTest` for integration tests
- Use `@DataJpaTest` for repository tests
- Mock external dependencies in unit tests

```java
@Test
void testRegistrarSocio_EmailDuplicado_ThrowsExcepcion() {
    // Arrange
    when(repository.existsByEmail(email)).thenReturn(true);
    
    // Act & Assert
    assertThrows(EmailDuplicadoException.class, 
        () -> useCase.registrarSocio(request));
}
```

### 9. Documentation

- Use Javadoc for public APIs
- Document DTOs with field descriptions
- Keep README.md updated with setup instructions

---

## 🔧 Project Configuration

### application.yml (Development)

```yaml
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
    url: jdbc:h2:mem:testdb
  jpa:
    hibernate:
      ddl-auto: create-drop
```

---

## 📝 Common Tasks

### Adding a new Use Case

1. Create port interface in `domain/ports/in/`
2. Create use case implementation in `application/usecases/`
3. Create adapter in `infrastructure/adapters/out/persistence/`
4. Expose via REST controller in `infrastructure/adapters/in/web/`

### Adding a new Entity

1. Create domain entity in `domain/model/`
2. Create JPA entity in `infrastructure/adapters/out/persistence/entity/`
3. Create mapper in `infrastructure/adapters/out/persistence/mapper/`
4. Create repository interface in `domain/ports/out/`
5. Create repository adapter implementation

---

## ⚠️ Rules for Agents

1. **NEVER** commit secrets or credentials to version control
2. **ALWAYS** run tests before committing
3. **NEVER** add Spring annotations to domain layer
4. **ALWAYS** use meaningful variable names
5. **NEVER** leave TODO comments in production code
6. **ALWAYS** follow the package structure defined above
7. **ALWAYS** use Javadoc for public APIs
8. **ALWAYS** follow the package structure defined above
