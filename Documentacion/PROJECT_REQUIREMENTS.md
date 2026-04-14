# 🏋️ GymFlow: Sistema de Gestión de Suscripciones (Hexagonal)

Este proyecto tiene como objetivo desarrollar una API robusta para la gestión de socios y accesos de un gimnasio, aplicando **Arquitectura Hexagonal (Puertos y Adaptadores)** con **Spring Boot**.

## 🎯 Objetivo de Aprendizaje

Separar la lógica de negocio (dominio) de los detalles técnicos (infraestructura/framework), asegurando que el núcleo de la aplicación sea testeable y fácil de mantener.

---

## 🛠 Requerimientos Funcionales (Casos de Uso)

### 1. Registro de Socios (`RegistrarSocio`)

- **Descripción:** El sistema debe permitir dar de alta a un nuevo socio.
- **Reglas de Negocio:**
  - No pueden existir dos socios con el mismo email.
  - Al registrarse, el socio comienza en estado `INACTIVO` hasta que se verifique su pago.
  - Se debe emitir un mensaje de "Bienvenida" (notificación) al finalizar el proceso.

### 2. Gestión de Suscripciones (`RenovarSuscripcion`)

- **Descripción:** Actualizar la fecha de fin de membresía de un socio.
- **Reglas de Negocio:**
  - Si el socio tiene pagos pendientes, no se puede renovar.
  - La renovación extiende la membresía 30 días a partir de la fecha actual o de la fecha de vencimiento (lo que sea posterior).

### 3. Validación de Acceso (`VerificarAcceso`)

- **Descripción:** Determinar si un socio puede entrar al gimnasio (Check-in).
- **Reglas de Negocio:**
  - El acceso se deniega si la fecha actual es posterior a la fecha de vencimiento de su suscripción.
  - El acceso se deniega si el estado del socio es `BLOQUEADO`.

---

## 🏗 Especificaciones de Arquitectura (El Hexágono)

### 1. El Dominio (Capa Central)

- **Entidades:** `Socio`, `Suscripcion`.
- **Value Objects:** `Email`, `SocioId`, `EstadoSocio`.
- **Restricción:** **PROHIBIDO** usar anotaciones de Spring (`@Service`, `@Component`, `@Entity`) o librerías externas (Jackson, JPA) en esta capa. Solo Java puro.

### 2. Puertos (Interfaces)

- **Puertos de Entrada (Driving):** Interfaces que definen los casos de uso.
- **Puertos de Salida (Driven):** - `SocioRepository`: Para persistencia.
  - `NotificationService`: Para envío de alertas.

### 3. Adaptadores (Capa Externa)

- **Web:** Controlador REST que maneja las peticiones HTTP.
- **Persistence:** Implementación con **Spring Data JPA** y base de datos H2 o PostgreSQL.
- **Notificación:** Implementación inicial mediante `Log` por consola.

---

## 🗂 Estructura de Directorios Propuesta

```text
src/main/java/com/gymflow
│
├── domain/                    <-- Núcleo (No conoce Spring)
│   ├── model/                 <-- Entidades y Value Objects
│   └── ports/
│       ├── in/                <-- Puertos de entrada (Interfaces UseCases)
│       └── out/               <-- Puertos de salida (Interfaces Repositorios)
│
├── application/               <-- Orquestación
│   └── usecases/              <-- Implementación de lógica de negocio
│
└── infrastructure/            <-- Detalles técnicos (Conoce Spring)
    ├── adapters/
    │   ├── in/web/            <-- RestControllers y DTOs de entrada
    │   └── out/persistence/   <-- Entidades JPA, Mappers y Adaptadores de DB
    └── config/                <-- Configuración de Beans de Spring (@Configuration)
