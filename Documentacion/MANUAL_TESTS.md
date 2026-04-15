# 🧪 Pruebas Manuales - GymFlow

Este documento contiene los comandos curl para probar manualmente los endpoints de la API.

## Prerequisites

1. **Iniciar PostgreSQL:**
```bash
docker run -d --name gymflow-postgres \
  -e POSTGRES_DB=gymflow \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_PASSWORD=postgres \
  -p 5432:5432 postgres:15
```

2. **Iniciar la aplicación:**
```bash
cd codigo
mvn spring-boot:run
```

3. **Verificar que está corriendo:**
```bash
curl http://localhost:8080/actuator/health
```

---

## Colección de Pruebas Manuales

### 1. Registrar Socio - Éxito

**Request:**
```bash
curl -X POST http://localhost:8080/api/socios \
  -H "Content-Type: application/json" \
  -d '{"nombre": "Juan Pérez", "email": "juan.perez@test.com"}' \
  -i
```

**Response esperada (201 Created):**
```http
HTTP/1.1 201 Created
Content-Type: application/json

{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "email": "juan.perez@test.com",
  "nombre": "Juan Pérez",
  "estado": "INACTIVO",
  "fechaCreacion": "2026-04-15T13:30:00"
}
```

---

### 2. Registrar Socio - Email Duplicado (409 Conflict)

**Request:**
```bash
curl -X POST http://localhost:8080/api/socios \
  -H "Content-Type: application/json" \
  -d '{"nombre": "Otro Usuario", "email": "juan.perez@test.com"}' \
  -i
```

**Response esperada (409 Conflict):**
```http
HTTP/1.1 409 Conflict
Content-Type: application/json

{
  "error": "Email duplicado",
  "mensaje": "El email juan.perez@test.com ya está registrado"
}
```

---

### 3. Registrar Socio - Datos Inválidos (400 Bad Request)

**Request:**
```bash
curl -X POST http://localhost:8080/api/socios \
  -H "Content-Type: application/json" \
  -d '{"nombre": "", "email": "email-invalido"}' \
  -i
```

**Response esperada (400 Bad Request):**
```http
HTTP/1.1 400 Bad Request
Content-Type: application/json

{
  "nombre": "El nombre es obligatorio",
  "email": "El formato del email es inválido"
}
```

---

### 4. Renovar Suscripción - Éxito

**Request:**
```bash
# Reemplazar {socio_id} con el ID obtenido en el paso 1
curl -X PUT http://localhost:8080/api/socios/{socio_id}/suscripcion/renovar \
  -H "Content-Type: application/json" \
  -i
```

**Response esperada (200 OK):**
```http
HTTP/1.1 200 OK
Content-Type: application/json

{
  "id": "660e8400-e29b-41d4-a716-446655440001",
  "socioId": "550e8400-e29b-41d4-a716-446655440000",
  "fechaInicio": "2026-04-15",
  "fechaFin": "2026-05-15",
  "estado": "ACTIVA"
}
```

---

### 5. Renovar Suscripción - Socio No Encontrado (404 Not Found)

**Request:**
```bash
curl -X PUT http://localhost:8080/api/socios/99999999-9999-9999-9999-999999999999/suscripcion/renovar \
  -i
```

**Response esperada (404 Not Found):**
```http
HTTP/1.1 404 Not Found
Content-Type: application/json

{
  "error": "Socio no encontrado",
  "mensaje": "Socio con ID 99999999-9999-9999-9999-999999999999 no encontrado"
}
```

---

### 6. Verificar Acceso - Socio Inactivo (Denegado)

**Request:**
```bash
curl -X GET http://localhost:8080/api/socios/{socio_id}/acceso \
  -i
```

**Response esperada (200 OK):**
```json
{
  "permitido": false,
  "mensaje": "Acceso denegado"
}
```

---

### 7. Verificar Acceso - Socio Sin Suscripción (Denegado)

Primero, activar el socio:
```bash
# Esto requeriría un endpoint adicional o modificar la BD directamente
# Por ahora, simulamos activando en la base de datos:
# UPDATE socios SET estado = 'ACTIVO' WHERE id = '{socio_id}';
```

**Request:**
```bash
curl -X GET http://localhost:8080/api/socios/{socio_id}/acceso \
  -i
```

**Response esperada (200 OK):**
```json
{
  "permitido": false,
  "mensaje": "Acceso denegado"
}
```

---

### 8. Flujo Completo - Happy Path

```bash
# Paso 1: Registrar socio
SOCIO_RESPONSE=$(curl -s -X POST http://localhost:8080/api/socios \
  -H "Content-Type: application/json" \
  -d '{"nombre": "María García", "email": "maria.garcia@test.com"}')

echo "Socio registrado: $SOCIO_RESPONSE"

# Extraer ID (requiere jq o parsing)
SOCIO_ID=$(echo $SOCIO_RESPONSE | grep -oP '"id":"\K[^"]+')

# Paso 2: Renovar suscripción
echo "Renovando suscripción..."
curl -X PUT http://localhost:8080/api/socios/$SOCIO_ID/suscripcion/renovar

# Paso 3: Verificar acceso
echo "Verificando acceso..."
curl -X GET http://localhost:8080/api/socios/$SOCIO_ID/acceso
```

---

## Casos de Error Comunes

| Escenario | Estado Esperado | Descripción |
|-----------|----------------|-------------|
| Email vacío | 400 | Validación de @NotBlank |
| Email sin formato | 400 | Validación de @Email |
| Email duplicado | 409 | Regla de negocio |
| ID inexistente | 404 | Recurso no encontrado |
| Método incorrecto | 405 | Method Not Allowed |

---

## Scripts de Automatización

### Linux/macOS (bash)
```bash
#!/bin/bash
BASE_URL="http://localhost:8080/api"

# Test registrar
echo "=== Registro de Socio ==="
curl -X POST $BASE_URL/socios \
  -H "Content-Type: application/json" \
  -d '{"nombre": "Test User", "email": "test@demo.com"}'

echo ""
echo "=== Verificar Acceso ==="
curl $BASE_URL/socios/TEST_ID/acceso
```

### Windows (PowerShell)
```powershell
$BASE_URL = "http://localhost:8080/api"

# Test registrar
Write-Host "=== Registro de Socio ==="
Invoke-RestMethod -Uri "$BASE_URL/socios" -Method POST `
  -ContentType "application/json" `
  -Body '{"nombre": "Test User", "email": "test@demo.com"}'
```

---

## Notas

- Asegúrate de que PostgreSQL esté corriendo antes de iniciar la aplicación
- La base de datos se crea automáticamente (ddl-auto: update)
- Los logs de Hibernate muestran las consultas SQL si `show-sql: true`
