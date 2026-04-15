# 🏛️ Arquitectura Hexagonal (Ports & Adapters)

## Guía Formativa

---

## 📖 ¿Qué es la Arquitectura Hexagonal?

La **Arquitectura Hexagonal**, también conocida como **Ports & Adapters** o **Arquitectura de Puertos y Adaptadores**, es un patrón arquitectónico creado por **Alistair Cockburn** en 2005. Su objetivo principal es **separar la lógica de negocio pura** del resto de la aplicación, permitiendo que el núcleo de la aplicación sea independiente de frameworks, bases de datos y servicios externos.

### La Analogía del Hexágono

```text
                    ╱══════════════════╲
                   ╱   ADAPTADORES      ╲
                  ╱     DE ENTRADA       ╲
                 ╱   (Drivers/Primary)    ╲
                ╱══════════════════════════╲
               ╱         ║        ║         ╲
              ║          ║        ║          ║
              ║   PUERTO ║        ║ PUERTO   ║
              ║  ENTRADA ║        ║ SALIDA   ║
              ║          ║        ║          ║
              ║          ╚════════╝          ║
              ║      ╔═════════════╗         ║
              ║      ║             ║         ║
              ║      ║   DOMINIO   ║         ║
              ║      ║   (Núcleo)  ║         ║
              ║      ║             ║         ║
              ║      ╚═════════════╝         ║
              ║          ╔════════╗          ║
              ║          ║ PUERTO ║          ║
              ║          ║ SALIDA ║          ║
              ║          ╚════════╝          ║
              ║          ╔════════╗          ║
              ║          ║ PUERTO ║          ║
              ║          ║ SALIDA ║          ║
              ╚══════════╩════════╩══════════╝
                 ╲      ADAPTADORES        ╱
                  ╲      DE SALIDA        ╱
                   ╲  (Driven/Secondary) ╱    
                    ╲═══════════════════╱
```

---

## 🧩 Conceptos Fundamentales

### 1. Dominio (El Centro)

El **dominio** es el corazón de la aplicación. Contiene:

- **Entidades**: Objetos con identidad única (ej: `Socio`, `Pedido`)
- **Value Objects**: Objetos sin identidad (ej: `Email`, `Dinero`)
- **Servicios de Dominio**: Lógica de negocio pura
- **Reglas de Negocio**: Validaciones y restricciones

**Características:**

- ✅ No tiene dependencias externas
- ✅ No conoce frameworks (Spring, Hibernate, etc.)
- ✅ Solo Java puro (o el lenguaje que uses)
- ✅ Completamente testeable

```java
// ✅ CORRECTO - Dominio puro
public class Socio {
    private SocioId id;
    private Email email;
    
    public void activar() {
        this.estado = EstadoSocio.ACTIVO;
    }
    
    public boolean puedeAcceder() {
        return this.estado == EstadoSocio.ACTIVO;
    }
}
```

```java
// ❌ INCORRECTO - Acoplamiento con framework
@Entity
public class SocioEntity {
    @Id
    private Long id;
    
    @Autowired  // ¡NO! Spring en el dominio
    private SocioRepository repository;
}
```

---

### 2. Puertos (Interfaces)

Los **puertos** son interfaces que definen **contratos** sin implementación.

#### Puertos de Entrada (Driving/Primary)

Definen **qué puede hacer** la aplicación hacia afuera.

```java
public interface RegistrarSocioUseCase {
    Socio ejecutar(String nombre, Email email);
}
```

#### Puertos de Salida (Driven/Secondary)

Definen **qué necesita** la aplicación para funcionar.

```java
public interface SocioRepository {
    Socio guardar(Socio socio);
    Optional<Socio> buscarPorId(SocioId id);
}

public interface NotificationService {
    void enviarBienvenida(String email);
}
```

---

### 3. Adaptadores (Implementaciones)

Los **adaptadores** son las implementaciones concretas de los puertos.

#### Adaptadores de Entrada

Implementan los puertos de entrada. **Reciben** órdenes del exterior.

```java
@RestController
@RequiredArgsConstructor
public class SocioController implements RegistrarSocioUseCase {
    
    private final RegistrarSocioUseCase registrarSocioUseCase;
    
    @PostMapping("/api/socios")
    public ResponseEntity<SocioResponse> registrar(@RequestBody Request request) {
        Socio socio = registrarSocioUseCase.ejecutar(request.getNombre(), request.getEmail());
        return ResponseEntity.ok(toResponse(socio));
    }
}
```

#### Adaptadores de Salida

Implementan los puertos de salida. **Responden** a las necesidades del dominio.

```java
@Repository
public class SocioRepositoryAdapter implements SocioRepository {
    
    private final SocioJpaRepository jpaRepository;
    
    @Override
    public Socio guardar(Socio socio) {
        SocioEntity entity = SocioMapper.toEntity(socio);
        SocioEntity saved = jpaRepository.save(entity);
        return SocioMapper.toDomain(saved);
    }
}
```

---

## 📁 Estructura de Paquetes

```text
src/main/java/com/gymflow/
│
├── domain/                              # 🌵 EL NÚCLEO (Puro)
│   ├── model/                          # Entidades y Value Objects
│   │   ├── Socio.java
│   │   ├── Email.java
│   │   └── EstadoSocio.java
│   └── ports/
│       ├── in/                         # 🎯 Interfaces de entrada (Use Cases)
│       │   ├── RegistrarSocioUseCase.java
│       │   └── RenovarSuscripcionUseCase.java
│       └── out/                        # 🚪 Interfaces de salida (Repositorios)
│           ├── SocioRepository.java
│           └── NotificationService.java
│
├── application/                         # 🎭 ORQUESTACIÓN
│   └── usecases/
│       ├── RegistrarSocioUseCaseImpl.java
│       └── RenovarSuscripcionUseCaseImpl.java
│
└── infrastructure/                      # 🔌 ADAPTADORES
    ├── adapters/
    │   ├── in/web/                    # 🌐 REST Controllers
    │   │   ├── SocioController.java
    │   │   └── dto/
    │   └── out/persistence/           # 💾 Base de datos
    │       ├── entity/
    │       ├── mapper/
    │       └── adapter/
    └── config/                        # ⚙️ Configuración Spring
```

---

## 🔄 Flujo de Datos

### Caso de Uso: Registrar Socio

```text
┌─────────────────────────────────────────────────────────────────┐
│                        PETICIÓN HTTP                            │
│                    POST /api/socios                             │
└──────────────────────────┬──────────────────────────────────────┘
                           │
                           ▼
┌─────────────────────────────────────────────────────────────────┐
│                   ADAPTADOR DE ENTRADA                          │
│                   SocioController                               │
│  1. Recibe JSON del cliente                                     │
│  2. Convierte a DTO                                             │
│  3. Llama al Use Case                                           │
└──────────────────────────┬──────────────────────────────────────┘
                           │
                           ▼
┌─────────────────────────────────────────────────────────────────┐
│                      USE CASE                                   │
│              RegistrarSocioUseCaseImpl                          │
│  1. Valida reglas de negocio                                    │
│  2. Delega persistencia al repositorio                          │
│  3. Solicita notificación                                       │
└──────┬──────────────────┬───────────────────────────────────────┘
       │                  │                     │
       ▼                  ▼                     ▼
┌───────────────┐  ┌──────────────┐  ┌─────────────────────┐
│   PUERTO      │  │   PUERTO     │  │     PUERTO          │
│  SALIDA 1     │  │  SALIDA 2    │  │    SALIDA 3         │
│SocioRepository│  │PagoRepository│  │ NotificationService │
└──────┬────────┘  └──────┬───────┘  └──────────┬──────────┘
       │                  │                     │
       ▼                  ▼                     ▼
┌───────────────┐  ┌──────────────┐  ┌─────────────────────┐
│   ADAPTADOR   │  │   ADAPTADOR  │  │     ADAPTADOR       │
│   JPA         │  │    JPA       │  │   CONSOLE/LOG       │
│   (BD)        │  │    (BD)      │  │   (Notificación)    │
└───────────────┘  └──────────────┘  └─────────────────────┘
```

---

## ✅ ¿Por qué usar Arquitectura Hexagonal?

### Ventajas

| Beneficio          | Descripción                                        |
|--------------------|----------------------------------------------------|
| **Testabilidad**   | El dominio se testa sin dependencias externas      |
| **Flexibilidad**   | Cambiar BD o framework no afecta el negocio        |
| **Mantenibilidad** | Código organizado y predecible                     |
| **Aislamiento**    | La lógica de negocio es independiente              |
| **Reusabilidad**   | Los Use Cases se pueden usar desde CLI, API, etc.  |

### Desventajas

| Desventaja                       | Solución                                           |
|----------------------------------|----------------------------------------------------|
| Curva de aprendizaje inicial     | Documentar y formar al equipo                      |
| Más archivos/interfaces          | Crear templates o generators                       |
| Overhead para proyectos pequeños | Evaluar si es necesario para el proyecto           |

---

## 🔄 Arquitectura Hexagonal vs Otras

### vs Arquitectura en Capas (Layered)

```text
┌──────────────────────────────────────────────────────┐
│              ARQUITECTURA EN CAPAS                   │
├──────────────────────────────────────────────────────┤
│  PRESENTATION (Controllers)                          │
│         ↓                                            │
│  BUSINESS LOGIC (Services) ←── Acoplamiento aquí     │
│         ↓                                            │
│  DATA ACCESS (Repositories) ←── Depende de BD        │
└──────────────────────────────────────────────────────┘

Problema: Los Services dependen de Repositories,
y ambos suelen conocer Annotations de Spring/JPA
```

```text
┌──────────────────────────────────────────────────────┐
│             ARQUITECTURA HEXAGONAL                   │
├──────────────────────────────────────────────────────┤
│                                                      │
│    ┌─────────────────────────────────────────┐       │
│    │              DOMINIO                    │       │
│    │  (Sin dependencias externas)            │       │
│    └─────────────────┬───────────────────────┘       │
│                      │                               │
│     ┌────────────────┼────────────────┐              │
│     ▼                ▼                ▼              │
│  PUERTO          PUERTO           PUERTO             │
│  ENTRADA        SALIDA           SALIDA              │
│     │                │                │              │
│     ▼                ▼                ▼              │
│  ADAPTADOR     ADAPTADOR        ADAPTADOR            │
│  (REST)        (JPA)            (LOG)                │
└──────────────────────────────────────────────────────┘

Solución: El dominio es independiente,
los adaptadores conocen al dominio, no al revés
```

---

## 💡 Cuándo USAR Arquitectura Hexagonal

✅ **Ideal para:**

- Proyectos con lógica de negocio compleja
- Aplicaciones que evolucionarán mucho
- Sistemas que necesitan múltiples interfaces (API, CLI, etc.)
- Equipos que requieren testabilidad alta
- Aplicaciones donde la BD o framework pueden cambiar

❌ **No necesaria para:**

- Scripts o aplicaciones simples
- CRUDs básicos sin lógica compleja
- Prototipos rápidos que se descartarán

---

## 📝 Resumen de Reglas

| Regla                                        | Descripción                               |
|----------------------------------------------|-------------------------------------------|
| ✅ El **Dominio** no conoce a nadie          | No imports de frameworks                  |
| ✅ Los **Puertos** son interfaces            | Solo contratos, sin implementación        |
| ✅ Los **Adaptadores** implementan puertos   | Conocen al dominio, no al revés           |
| ✅ Los **Use Cases** orquestan               | Sin lógica de negocio, solo coordinación  |
| ❌ No annotatear el Dominio                  | Sin `@Service`, `@Repository`, `@Entity`  |
| ❌ No inyección en Dominio                   | El dominio no conoce Spring               |

---

## 🎯 En Resumen

> **"La lógica de negocio debe poder funcionar en un proyecto de consola, en una API REST, en un servicio batch, sin cambiar una sola línea."**

La Arquitectura Hexagonal nos permite:

1. **Aislar** la lógica de negocio
2. **Independizar** de tecnologías
3. **Facilitar** los tests
4. **Hacer** el código mantenible a largo plazo

---

## 📚 Referencias

- [Alistair Cockburn - Hexagonal Architecture](https://alistair.cockburn.us/hexagonal-architecture/)
- [Ports and Adapters Pattern - Wikipedia](https://en.wikipedia.org/wiki/Adapter_pattern)
- [Clean Architecture - Robert C. Martin](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)

---

Documento creado para el proyecto GymFlow - Curso de Arquitectura Hexagonal
