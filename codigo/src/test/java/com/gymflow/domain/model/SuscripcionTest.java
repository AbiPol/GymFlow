package com.gymflow.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Suscripcion - Entidad Tests")
class SuscripcionTest {

    @Test
    @DisplayName("SS1: Suscripción creada debe durar 30 días")
    void crear_SuscripcionNueva_Duracion30Dias() {
        SocioId socioId = SocioId.generate();
        Suscripcion suscripcion = Suscripcion.crear(socioId);
        assertTrue(suscripcion.getFechaFin().isAfter(suscripcion.getFechaInicio()));
        assertEquals(suscripcion.getFechaInicio().plusDays(30), suscripcion.getFechaFin());
    }

    @Test
    @DisplayName("SS2: Suscripción creada debe tener estado ACTIVA")
    void crear_SuscripcionNueva_EstadoActiva() {
        SocioId socioId = SocioId.generate();
        Suscripcion suscripcion = Suscripcion.crear(socioId);
        assertEquals("ACTIVA", suscripcion.getEstado());
    }

    @Test
    @DisplayName("SS3: extender30Dias() cuando NO vencida extiende desde fechaFin")
    void extender30Dias_NoVencida_ExtiendeDesdeFechaFin() {
        SocioId socioId = SocioId.generate();
        Suscripcion suscripcion = Suscripcion.crear(socioId);
        LocalDate fechaFinOriginal = suscripcion.getFechaFin();
        suscripcion.extender30Dias();
        assertEquals(fechaFinOriginal.plusDays(30), suscripcion.getFechaFin());
    }

    @Test
    @DisplayName("SS4: extender30Dias() cuando vencida extiende desde hoy")
    void extender30Dias_Vencida_ExtiendeDesdeHoy() {
        SocioId socioId = SocioId.generate();
        Suscripcion suscripcion = new Suscripcion(
            UUID.randomUUID(),
            socioId,
            LocalDate.now().minusDays(10),
            LocalDate.now().minusDays(5),
            "ACTIVA",
            LocalDateTime.now()
        );
        LocalDate hoyEsperado = LocalDate.now().plusDays(30);
        suscripcion.extender30Dias();
        assertEquals(hoyEsperado, suscripcion.getFechaFin());
    }

    @Test
    @DisplayName("SS5: estaVencida() debe retornar true cuando fechaFin es pasada")
    void estaVencida_FechaPasada_True() {
        SocioId socioId = SocioId.generate();
        Suscripcion suscripcion = new Suscripcion(
            UUID.randomUUID(),
            socioId,
            LocalDate.now().minusDays(35),
            LocalDate.now().minusDays(5),
            "ACTIVA",
            LocalDateTime.now()
        );
        assertTrue(suscripcion.estaVencida());
    }

    @Test
    @DisplayName("SS6: estaVencida() debe retornar false cuando fechaFin es futura")
    void estaVencida_FechaFutura_False() {
        SocioId socioId = SocioId.generate();
        Suscripcion suscripcion = Suscripcion.crear(socioId);
        assertFalse(suscripcion.estaVencida());
    }

    @Test
    @DisplayName("SS7: estaVencida() debe retornar false cuando fechaFin es hoy (límite)")
    void estaVencida_Hoy_False() {
        SocioId socioId = SocioId.generate();
        Suscripcion suscripcion = new Suscripcion(
            UUID.randomUUID(),
            socioId,
            LocalDate.now().minusDays(30),
            LocalDate.now(),
            "ACTIVA",
            LocalDateTime.now()
        );
        assertFalse(suscripcion.estaVencida());
    }

    @Test
    @DisplayName("SS8: isActiva() debe retornar true cuando ACTIVA y no vencida")
    void isActiva_ActivaYNoVencida_True() {
        SocioId socioId = SocioId.generate();
        Suscripcion suscripcion = Suscripcion.crear(socioId);
        assertTrue(suscripcion.isActiva());
    }

    @Test
    @DisplayName("SS9: isActiva() debe retornar false cuando vencida aunque ACTIVA")
    void isActiva_Vencida_False() {
        SocioId socioId = SocioId.generate();
        Suscripcion suscripcion = new Suscripcion(
            UUID.randomUUID(),
            socioId,
            LocalDate.now().minusDays(35),
            LocalDate.now().minusDays(5),
            "ACTIVA",
            LocalDateTime.now()
        );
        assertFalse(suscripcion.isActiva());
    }
}
