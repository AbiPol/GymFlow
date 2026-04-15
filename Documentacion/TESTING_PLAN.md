# 🧪 GymFlow: Plan de Testing - Fase 6

## Resumen de Cobertura

| Clase a Testear | Tipo | Tests | Estado |
|-----------------|------|-------|--------|
| EmailTest | Value Object | 7 | ✅ |
| SocioIdTest | Value Object | 4 | ✅ |
| SocioTest | Entidad | 7 | ✅ |
| SuscripcionTest | Entidad | 9 | ✅ |
| PagoTest | Entidad | 5 | ✅ |
| RegistrarSocioUseCaseTest | Use Case | 4 | ✅ |
| RenovarSuscripcionUseCaseTest | Use Case | 5 | ✅ |
| VerificarAccesoUseCaseTest | Use Case | 6 | ✅ |
| SocioControllerTest | Integración | 6 | ✅ |

**Total: 53 tests** | **Estado: ✅ COMPLETADO**

---

## 1. EmailTest.java

### Responsabilidad

Validar que el value object `Email` solo acepta emails con formato correcto y lanza excepciones apropiadas para datos inválidos.

### Casos de Test

| ID | Nombre                                                  | Descripción                                                              | Input               | Esperado                                                                 |
|----|---------------------------------------------------------|--------------------------------------------------------------------------|---------------------|--------------------------------------------------------------------------|
| E1 | `crearEmail_Valido_SinExcepcion`                        | Email con formato válido debe crearse                                    | `"test@test.com"`   | Objeto Email creado, sin excepción                                       |
| E2 | `crearEmail_Nulo_ThrowsIllegalArgumentException`        | Email null debe lanzar excepción                                         | `null`              | `IllegalArgumentException` con mensaje                                 |
| E3 | `crearEmail_Vacio_ThrowsIllegalArgumentException`        | Email vacío debe lanzar excepción                                         | `""`                | `IllegalArgumentException`                                             |
| E4 | `crearEmail_EnBlanco_ThrowsIllegalArgumentException`        | Email en blanco debe lanzar excepción                                    | `"   "`             | `IllegalArgumentException`                                             |
| E5 | `crearEmail_SinArroba_ThrowsIllegalArgumentException`        | Email sin @ inválido                                                     | `"testtest.com"`    | `IllegalArgumentException`                                             |
| E6 | `crearEmail_SinDominio_ThrowsIllegalArgumentException`        | Email sin dominio inválido                                               | `"test@"`           | `IllegalArgumentException`                                             |
| E7 | `crearEmail_SinExtension_ThrowsIllegalArgumentException` | Email sin extensión inválido | `"test@test"` | `IllegalArgumentException` |

### Reglas de Negocio Probadas

- Email no puede ser null
- Email no puede estar vacío
- Email debe tener formato válido: `texto@dominio.extension`

---

## 2. SocioIdTest.java

### Responsabilidad

Validar que el value object `SocioId` solo acepta UUIDs no nulos y proporciona factory methods para creación.

### Casos de Test


| ID | Nombre | Descripción | Input | Esperado |
|----|--------|-------------|-------|----------|
| S1 | `crearSocioId_Valido_SinExcepcion` | UUID válido debe crearse | `UUID.randomUUID()` | Objeto SocioId creado |
| S2 | `crearSocioId_Nulo_ThrowsIllegalArgumentException` | null debe lanzar excepción | `null` | `IllegalArgumentException` |
| S3 | `generate_CreaUUIDUnico` | `SocioId.generate()` crea UUID | (sin input) | UUID no null, único |
| S4 | `from_UUID_RetornaSocioIdIgual` | `SocioId.from(uuid)` funciona | `UUID` | SocioId con valor igual al input |

### Reglas de Negocio Probadas
- SocioId no puede ser null
- `generate()` crea IDs únicos
- `from()` preserva el UUID original

---

## 3. SocioTest.java

### Responsabilidad
Validar las transiciones de estado del socio y las reglas de acceso.

### Casos de Test

| ID | Nombre | Descripción | Input | Esperado |
|----|--------|-------------|-------|----------|
| ST1 | `crear_SocioNuevo_EstadoInactivo` | Socio creado tiene estado inicial | `Socio.crear()` | `estado = INACTIVO` |
| ST2 | `crear_SocioNuevo_FechasIguales` | Fechas de creación se asignan | `Socio.crear()` | `fechaCreacion = fechaActualizacion` |
| ST3 | `activar_SocioActivo_EstadoCambia` | `activar()` cambia estado | socio existente | `estado = ACTIVO` |
| ST4 | `bloquear_SocioBloqueado_EstadoCambia` | `bloquear()` cambia estado | socio existente | `estado = BLOQUEADO` |
| ST5 | `puedeAcceder_CuandoActivo_True` | Acceso permitido si ACTIVO | estado = ACTIVO | `true` |
| ST6 | `puedeAcceder_CuandoInactivo_False` | Acceso denegado si INACTIVO | estado = INACTIVO | `false` |
| ST7 | `puedeAcceder_CuandoBloqueado_False` | Acceso denegado si BLOQUEADO | estado = BLOQUEADO | `false` |

### Reglas de Negocio Probadas
- Socio inicia en estado INACTIVO
- Solo socio ACTIVO puede acceder
- Métodos `activar()` y `bloquear()` cambian estado correctamente

---

## 4. SuscripcionTest.java

### Responsabilidad
Validar la creación de suscripciones, cálculo de fechas de vencimiento y extensión de 30 días.

### Casos de Test

| ID | Nombre | Descripción | Input | Esperado |
|----|--------|-------------|-------|----------|
| SS1 | `crear_SuscripcionNueva_Duracion30Dias` | Suscripción dura 30 días | `Suscripcion.crear()` | `fechaFin = fechaInicio + 30 días` |
| SS2 | `crear_SuscripcionNueva_EstadoActiva` | Estado inicial es ACTIVA | `Suscripcion.crear()` | `estado = "ACTIVA"` |
| SS3 | `extender30Dias_NoVencida_ExtiendeDesdeFechaFin` | Si no vencida, extiende desde fechaFin | fechaFin = hoy + 10 días | fechaFin = hoy + 40 días |
| SS4 | `extender30Dias_Vencida_ExtiendeDesdeHoy` | Si vencida, extiende desde hoy | fechaFin = hoy - 5 días | fechaFin = hoy + 30 días |
| SS5 | `estaVencida_FechaPasada_True` | Vencimiento pasado | fechaFin = ayer | `true` |
| SS6 | `estaVencida_FechaFutura_False` | Vencimiento futuro | fechaFin = mañana | `false` |
| SS7 | `estaVencida_Hoy_False` | Vence hoy (límite) | fechaFin = hoy | `false` |
| SS8 | `isActiva_ActivaYNoVencida_True` | ACTIVA y no vencida | estado="ACTIVA", fechaFin manana | `true` |
| SS9 | `isActiva_Vencida_False` | Vencida aunque ACTIVA | estado="ACTIVA", fechaFin ayer | `false` |

### Reglas de Negocio Probadas
- Suscripción dura 30 días
- `extender30Dias()` extiende desde fecha futura o desde hoy
- Una suscripción está vencida si `hoy > fechaFin`
- `isActiva()` requiere estado ACTIVA Y no vencida

---

## 5. PagoTest.java

### Responsabilidad
Validar la creación de pagos y transiciones de estado.

### Casos de Test

| ID | Nombre | Descripción | Input | Esperado |
|----|--------|-------------|-------|----------|
| P1 | `crear_PagoNuevo_EstadoPendiente` | Pago creado es PENDIENTE | `Pago.crear()` | `estado = "PENDIENTE"` |
| P2 | `crear_PagoNuevo_MontoCorrecto` | Monto se asigna correctamente | monto = 50.00 | `monto = 50.00` |
| P3 | `marcarPagado_CambiaEstadoAPagado` | `marcarPagado()` cambia estado | pago con estado PENDIENTE | `estado = "PAGADO"` |
| P4 | `tienePagosPendientes_Pendiente_True` | PENDIENTE es pendiente | estado = "PENDIENTE" | `true` |
| P5 | `tienePagosPendientes_Pagado_False` | PAGADO no es pendiente | estado = "PAGADO" | `false` |

### Reglas de Negocio Probadas
- Pago inicia en estado PENDIENTE
- `marcarPagado()` cambia estado a PAGADO
- `tienePagosPendientes()` refleja el estado correctamente

---

## 6. RegistrarSocioUseCaseTest.java

### Responsabilidad
Validar la lógica del caso de uso RegistrarSocio con dependencias mockeadas.

### Casos de Test

| ID | Nombre | Descripción |
|----|--------|-------------|
| RC1 | `ejecutar_EmailUnico_SocioCreado` | Registro exitoso con email único |
| RC2 | `ejecutar_EmailDuplicado_ThrowsException` | Email duplicado lanza excepción |
| RC3 | `ejecutar_SocioCreado_EstadoInactivo` | Socio inicia en INACTIVO |
| RC4 | `ejecutar_RegistroExitoso_EnviarNotificacion` | Envía notificación de bienvenida |

---

## 7. RenovarSuscripcionUseCaseTest.java

### Responsabilidad
Validar la lógica de renovación de suscripción.

### Casos de Test

| ID | Nombre | Descripción |
|----|--------|-------------|
| RN1 | `ejecutar_SinPagosPendientes_SuscripcionRenovada` | Renovación exitosa |
| RN2 | `ejecutar_SocioNoExiste_ThrowsException` | Socio no encontrado |
| RN3 | `ejecutar_ConPagosPendientes_ThrowsException` | Pagos pendientes bloquean |
| RN4 | `ejecutar_SinSuscripcionExistente_CreaNueva` | Crea suscripción si no existe |
| RN5 | `ejecutar_SuscripcionNoVencida_ExtiendeDesdeFechaFin` | Extiende desde fecha fin |

---

## 8. VerificarAccesoUseCaseTest.java

### Responsabilidad
Validar la lógica de verificación de acceso.

### Casos de Test

| ID | Nombre | Descripción |
|----|--------|-------------|
| VA1 | `ejecutar_SocioActivoSuscripcionValida_AccesoPermitido` | Acceso concedido |
| VA2 | `ejecutar_SocioNoExiste_ThrowsException` | Socio no encontrado |
| VA3 | `ejecutar_SocioInactivo_AccesoDenegado` | Socio inactivo bloquea |
| VA4 | `ejecutar_SocioBloqueado_AccesoDenegado` | Socio bloqueado bloquea |
| VA5 | `ejecutar_SinSuscripcion_AccesoDenegado` | Sin suscripción bloquea |
| VA6 | `ejecutar_SuscripcionVencida_AccesoDenegado` | Suscripción vencida bloquea |

---

## 9. SocioControllerTest.java (Integración)

### Responsabilidad
Validar los endpoints REST del controlador.

### Casos de Test

| ID | Nombre | Descripción |
|----|--------|-------------|
| CT1 | `registrarSocio_DatosValidos_Returns201` | Registro exitoso retorna 201 |
| CT2 | `registrarSocio_EmailDuplicado_Returns409` | Email duplicado retorna 409 |
| CT3 | `registrarSocio_DatosInvalidos_Returns400` | Datos inválidos retorna 400 |
| CT4 | `renovarSuscripcion_SocioExiste_Returns200` | Renovación exitosa retorna 200 |
| CT5 | `verificarAcceso_AccesoPermitido_Returns200` | Acceso permitido retorna 200 |
| CT6 | `verificarAcceso_AccesoDenegado_Returns200` | Acceso denegado retorna 200 |

---

## Cobertura Objetivo

| Métrica | Objetivo |
|---------|----------|
| Cobertura Dominio | >80% |
| Líneas cubiertas | >70% |
| Métodos cubiertos | 100% |

---

## Estructura de Archivos

```
src/test/java/com/gymflow/
├── domain/
│   └── model/
│       ├── EmailTest.java
│       ├── SocioIdTest.java
│       ├── SocioTest.java
│       ├── SuscripcionTest.java
│       └── PagoTest.java
├── application/
│   └── usecases/
│       ├── RegistrarSocioUseCaseTest.java
│       ├── RenovarSuscripcionUseCaseTest.java
│       └── VerificarAccesoUseCaseTest.java
└── infrastructure/
    └── adapters/
        └── in/
            └── web/
                └── SocioControllerTest.java
```

---

## Convenciones de Test

### Nomenclatura
```
test[Metodo]_[Estado]_[ResultadoEsperado]
```

### Ejemplo
```java
@Test
void crearEmail_SinArroba_ThrowsIllegalArgumentException() {
    // Arrange
    String emailInvalido = "testtest.com";
    
    // Act & Assert
    assertThrows(IllegalArgumentException.class, 
        () -> Email.of(emailInvalido));
}
```

### Framework
- **JUnit 5 (Jupiter)** - Incluido en `spring-boot-starter-test`
- **Assertions:** JUnit 5 built-in assertions

---

## Ejecución de Tests

```bash
# Ejecutar todos los tests
mvn test

# Ejecutar solo tests de dominio
mvn test -Dtest="com.gymflow.domain.model.*Test"

# Ejecutar un test específico
mvn test -Dtest="EmailTest"

# Ejecutar un método específico
mvn test -Dtest="EmailTest#crearEmail_Nulo_ThrowsIllegalArgumentException"
```
