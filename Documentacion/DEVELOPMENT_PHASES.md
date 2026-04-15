# 🏋️ GymFlow: Fases de Desarrollo

Este documento define las fases de desarrollo para construir el proyecto GymFlow con Arquitectura Hexagonal.

---

## 📊 Resumen de Fases

| Fase | Duración Estimada |       Objetivo Principal                  |    Estado    |
|------|-------------------|-------------------------------------------|--------------|
| 1    | 1-2 días          | Configuración del proyecto                |     [x]      |
| 2    | 2-3 días          | Capa de Dominio                           |     [x]      |
| 3    | 2-3 días          | Capa de Aplicación (Use Cases)            |     [x]      |
| 4    | 3-4 días          | Capa de Infraestructura - Persistencia    |     [x]      |
| 5    | 2-3 días          | Capa de Infraestructura - Web (API)       |     [x]      |
| 6    | 1-2 días          | Testing y Validación                      |     [x]      |
| 7    | 1 día             | Documentación final                       |     [x]      |

### **Total estimado: 12 a 18 días** | **Progreso: 7/7 fases (100%)** ✅

---

## 🔧 Fase 1: Configuración del Proyecto

### Objetivo

Montar el esqueleto del proyecto Spring Boot con todas las dependencias necesarias.

### Tareas

- [x] **F1-T1** Crear proyecto Maven
  - Generar proyecto desde Spring Initializr o manualmente
  - Configurar `pom.xml` con dependencias

- [x] **F1-T2** Configurar application.yml
  - Conexión a PostgreSQL (desarrollo)
  - Configuración JPA/Hibernate

- [x] **F1-T3** Estructurar paquetes
  - Crear estructura de directorios hexagonal

- [x] **F1-T4** Verificar entorno
  - Compilar proyecto vacío
  - Verificar conexión a BD

### Entregable

Proyecto compilable con estructura base.

---

## 🏗 Fase 2: Capa de Dominio

### F2-Objetivo

Implementar el núcleo de negocio sin dependencias externas.

### F2-Tareas

- [x] **F2-T1** Value Objects
  - `EstadoSocio` (enum: ACTIVO, INACTIVO, BLOQUEADO)
  - `Email` (validación de formato)
  - `SocioId` (UUID)

- [x] **F2-T2** Entidades
  - `Socio` (id, email, nombre, estado, fechas)
  - `Suscripcion` (id, socioId, fechaInicio, fechaFin, estado)
  - `Pago` (id, socioId, monto, estado)

- [x] **F2-T3** Reglas de Negocio (en entidades)
  - Validar email único en constructor
  - Verificar estado para acceso

### Reglas

- ❌ PROHIBIDO usar anotaciones Spring
- ❌ PROHIBIDO usar librerías externas (Jackson, JPA)
- ✅ Solo Java puro

### F2-Entregable

Clases de dominio compilables y testeables unitariamente.

---

## 🚀 Fase 3: Capa de Aplicación (Use Cases)

### F3-Objetivo

Implementar la lógica de negocio orquestando el dominio.

### F3-Tareas

- [x] **F3-T1** Puertos de Entrada (Interfaces)
  - `RegistrarSocioUseCase`
  - `RenovarSuscripcionUseCase`
  - `VerificarAccesoUseCase`

- [x] **F3-T2** Puertos de Salida (Interfaces)
  - `SocioRepository`
  - `SuscripcionRepository`
  - `NotificationService`
  - `PagoRepository`

- [x] **F3-T3** Implementaciones de Use Cases
  - `RegistrarSocioUseCaseImpl`
  - `RenovarSuscripcionUseCaseImpl`
  - `VerificarAccesoUseCaseImpl`

### Reglas de Negocio a Implementar

| Use Case           | Regla                                                              |
|--------------------|--------------------------------------------------------------------|
| RegistrarSocio     | Email único, estado inicial INACTIVO, emitir notificación          |
| RenovarSuscripcion | Verificar pagos pendientes, extender 30 días                       |
| VerificarAcceso    | Denegar si vencido o BLOQUEADO                                     |

### F3-Entregable

Use Cases implementados con sus interfaces.

---

## 💾 Fase 4: Capa de Infraestructura - Persistencia

### F4-Objetivo

Implementar adaptadores de persistencia con JPA y PostgreSQL.

### F4-Tareas

- [x] **F4-T1** Entidades JPA
  - `SocioEntity` (mapeo a tabla `socios`)
  - `SuscripcionEntity` (mapeo a tabla `suscripciones`)
  - `PagoEntity` (mapeo a tabla `pagos`)

- [x] **F4-T2** Repositories Spring Data
  - `SocioJpaRepository`
  - `SuscripcionJpaRepository`
  - `PagoJpaRepository`

- [x] **F4-T3** Mappers (Dominio ↔ Entity)
  - `SocioMapper`
  - `SuscripcionMapper`
  - `PagoMapper`

- [x] **F4-T4** Adaptadores de Repositorio
  - `SocioRepositoryAdapter` (implementa `SocioRepository`)
  - `SuscripcionRepositoryAdapter`
  - `PagoRepositoryAdapter`

- [x] **F4-T5** Servicio de Notificación
  - `NotificationServiceAdapter` (implementa `NotificationService`)
  - Implementación inicial por consola (Log)

### F4-Entregable

Adaptadores de persistencia funcionales.

---

## 🌐 Fase 5: Capa de Infraestructura - Web

### F5-Objetivo

Exponer los casos de uso mediante API REST.

### F5-Tareas

- [x] **F5-T1** DTOs de Request
  - `RegistrarSocioRequest`

- [x] **F5-T2** DTOs de Response
  - `SocioResponse`
  - `SuscripcionResponse`
  - `VerificarAccesoResponse`

- [x] **F5-T3** Controladores REST
  - `SocioController`

- [x] **F5-T4** Configuración de Beans
  - `BeanConfiguration` (inyectar dependencias)

- [x] **F5-T5** Manejo de Errores
  - Exception Handler para respuestas 400, 404, 409

### Endpoints a implementar

| Método | Endpoint                                       | Use Case           |
|--------|------------------------------------------------|--------------------|
| POST   | /api/socios                                    | RegistrarSocio     |
| PUT    | /api/socios/{id}/suscripcion/renovar           | RenovarSuscripcion |
| GET    | /api/socios/{id}/acceso                        | VerificarAcceso    |

### F5-Entregable

API REST funcional y documentada.

---

## 🧪 Fase 6: Testing y Validación

### F6-Objetivo

Verificar que el sistema funciona correctamente.

### F6-Tareas

- [x] **F6-T1** Tests Unitarios (Dominio)
  - Tests de entidades y value objects
  - Tests de reglas de negocio
  - 32 tests implementados (Email, SocioId, Socio, Suscripcion, Pago)

- [x] **F6-T2** Tests de Use Cases
  - Tests de lógica de aplicación
  - Mock de puertos de salida
  - 15 tests (RegistrarSocio: 4, RenovarSuscripcion: 5, VerificarAcceso: 6)

- [x] **F6-T3** Tests de Integración (API)
  - Tests de controladores
  - Uso de H2 en memoria
  - 6 tests de SocioController

- [x] **F6-T4** Pruebas Manuales
  - Verificar endpoints con curl
  - Casos de éxito y error
  - Documento MANUAL_TESTS.md creado

### Cobertura objetivo

- Dominio: 80%+
- Use Cases: 70%+

### F6-Entregable

Proyecto con tests Passing.

---

## 📚 Fase 7: Documentación Final

### F7-Objetivo

Completar la documentación del proyecto.

### F7-Tareas

- [x] **F7-T1** README.md
  - Descripción del proyecto ✅
  - Instrucciones de instalación ✅
  - Guía de uso de API ✅

- [x] **F7-T2** Documentación API
  - Endpoints documentados en README.md ✅

- [x] **F7-T3** Revisión final
  - Verificar coherencia entre documentos ✅
  - Revisar convenciones de código ✅

### F7-Entregable

- PROJECT_REQUIREMENTS.md ✅
- TECHNICAL_SPECIFICATIONS.md ✅
- DEVELOPMENT_PHASES.md ✅
- TESTING_PLAN.md ✅
- MANUAL_TESTS.md ✅
- README.md ✅ (completado)

---

## ⚠️ Notas Importantes

### Secuencia Obligatoria

Las fases deben ejecutarse en orden. Cada fase construye sobre la anterior.

### Criterios de Paso

- Cada fase debe compilar independently
- Los tests de la fase anterior deben pasar
- No avanzar hasta validar la fase actual

### Problemas Comunes

- **Errores de compilación**: Revisar imports y dependencias
- **Errores de conexión BD**: Verificar configuración application.yml
- **Errores 404**: Verificar mapping de controladores
- **Errores 409**: Revisar lógica de validación de negocio
