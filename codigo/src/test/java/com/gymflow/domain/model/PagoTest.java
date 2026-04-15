package com.gymflow.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Pago - Entidad Tests")
class PagoTest {

    @Test
    @DisplayName("P1: Pago creado debe tener estado PENDIENTE")
    void crear_PagoNuevo_EstadoPendiente() {
        SocioId socioId = SocioId.generate();
        Pago pago = Pago.crear(socioId, new BigDecimal("50.00"));
        assertEquals("PENDIENTE", pago.getEstado());
    }

    @Test
    @DisplayName("P2: Pago creado debe tener monto correcto")
    void crear_PagoNuevo_MontoCorrecto() {
        SocioId socioId = SocioId.generate();
        BigDecimal monto = new BigDecimal("50.00");
        Pago pago = Pago.crear(socioId, monto);
        assertEquals(monto, pago.getMonto());
    }

    @Test
    @DisplayName("P3: marcarPagado() debe cambiar estado a PAGADO")
    void marcarPagado_CambiaEstadoAPagado() {
        SocioId socioId = SocioId.generate();
        Pago pago = Pago.crear(socioId, new BigDecimal("50.00"));
        pago.marcarPagado();
        assertEquals("PAGADO", pago.getEstado());
    }

    @Test
    @DisplayName("P4: tienePagosPendientes() debe retornar true cuando estado es PENDIENTE")
    void tienePagosPendientes_Pendiente_True() {
        SocioId socioId = SocioId.generate();
        Pago pago = Pago.crear(socioId, new BigDecimal("50.00"));
        assertTrue(pago.tienePagosPendientes());
    }

    @Test
    @DisplayName("P5: tienePagosPendientes() debe retornar false cuando estado es PAGADO")
    void tienePagosPendientes_Pagado_False() {
        SocioId socioId = SocioId.generate();
        Pago pago = Pago.crear(socioId, new BigDecimal("50.00"));
        pago.marcarPagado();
        assertFalse(pago.tienePagosPendientes());
    }
}
