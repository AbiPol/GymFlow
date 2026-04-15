package com.gymflow.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("SocioId - Value Object Tests")
class SocioIdTest {

    @Test
    @DisplayName("S1: UUID válido debe crearse sin excepción")
    void crearSocioId_Valido_SinExcepcion() {
        UUID uuidValido = UUID.randomUUID();
        SocioId socioId = SocioId.from(uuidValido);
        assertNotNull(socioId);
        assertEquals(uuidValido, socioId.getValue());
    }

    @Test
    @DisplayName("S2: null debe lanzar IllegalArgumentException")
    void crearSocioId_Nulo_ThrowsIllegalArgumentException() {
        UUID uuidNulo = null;
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> SocioId.from(uuidNulo)
        );
        assertEquals("SocioId no puede ser null", exception.getMessage());
    }

    @Test
    @DisplayName("S3: generate() debe crear UUID no null")
    void generate_CreaUUIDUnico() {
        SocioId socioId = SocioId.generate();
        assertNotNull(socioId);
        assertNotNull(socioId.getValue());
    }

    @Test
    @DisplayName("S4: from(UUID) debe retornar SocioId con valor igual al input")
    void from_UUID_RetornaSocioIdIgual() {
        UUID uuidOriginal = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        SocioId socioId = SocioId.from(uuidOriginal);
        assertEquals(uuidOriginal, socioId.getValue());
    }
}
