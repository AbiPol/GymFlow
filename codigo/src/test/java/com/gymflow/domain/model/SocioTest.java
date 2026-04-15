package com.gymflow.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Socio - Entidad Tests")
class SocioTest {

    @Test
    @DisplayName("ST1: Socio creado debe tener estado INACTIVO")
    void crear_SocioNuevo_EstadoInactivo() {
        Email email = Email.of("test@test.com");
        Socio socio = Socio.crear("Juan Pérez", email);
        assertEquals(EstadoSocio.INACTIVO, socio.getEstado());
    }

    @Test
    @DisplayName("ST2: Socio creado debe tener fechas de creación iguales")
    void crear_SocioNuevo_FechasIguales() {
        Email email = Email.of("test@test.com");
        Socio socio = Socio.crear("Juan Pérez", email);
        assertEquals(socio.getFechaCreacion(), socio.getFechaActualizacion());
    }

    @Test
    @DisplayName("ST3: activar() debe cambiar estado a ACTIVO")
    void activar_SocioActivo_EstadoCambia() {
        Email email = Email.of("test@test.com");
        Socio socio = Socio.crear("Juan Pérez", email);
        socio.activar();
        assertEquals(EstadoSocio.ACTIVO, socio.getEstado());
    }

    @Test
    @DisplayName("ST4: bloquear() debe cambiar estado a BLOQUEADO")
    void bloquear_SocioBloqueado_EstadoCambia() {
        Email email = Email.of("test@test.com");
        Socio socio = Socio.crear("Juan Pérez", email);
        socio.bloquear();
        assertEquals(EstadoSocio.BLOQUEADO, socio.getEstado());
    }

    @Test
    @DisplayName("ST5: puedeAcceder() debe retornar true cuando estado es ACTIVO")
    void puedeAcceder_CuandoActivo_True() {
        Email email = Email.of("test@test.com");
        Socio socio = Socio.crear("Juan Pérez", email);
        socio.activar();
        assertTrue(socio.puedeAcceder());
    }

    @Test
    @DisplayName("ST6: puedeAcceder() debe retornar false cuando estado es INACTIVO")
    void puedeAcceder_CuandoInactivo_False() {
        Email email = Email.of("test@test.com");
        Socio socio = Socio.crear("Juan Pérez", email);
        assertFalse(socio.puedeAcceder());
    }

    @Test
    @DisplayName("ST7: puedeAcceder() debe retornar false cuando estado es BLOQUEADO")
    void puedeAcceder_CuandoBloqueado_False() {
        Email email = Email.of("test@test.com");
        Socio socio = Socio.crear("Juan Pérez", email);
        socio.bloquear();
        assertFalse(socio.puedeAcceder());
    }
}
