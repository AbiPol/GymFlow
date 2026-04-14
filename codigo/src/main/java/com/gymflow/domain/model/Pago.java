package com.gymflow.domain.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class Pago {
    private UUID id;
    private SocioId socioId;
    private BigDecimal monto;
    private String estado;
    private LocalDateTime fechaCreacion;

    public Pago() {}

    public Pago(UUID id, SocioId socioId, BigDecimal monto, String estado, LocalDateTime fechaCreacion) {
        this.id = id;
        this.socioId = socioId;
        this.monto = monto;
        this.estado = estado;
        this.fechaCreacion = fechaCreacion;
    }

    public static Pago crear(SocioId socioId, BigDecimal monto) {
        Pago pago = new Pago();
        pago.id = UUID.randomUUID();
        pago.socioId = socioId;
        pago.monto = monto;
        pago.estado = "PENDIENTE";
        pago.fechaCreacion = LocalDateTime.now();
        return pago;
    }

    public void marcarPagado() {
        this.estado = "PAGADO";
    }

    public boolean tienePagosPendientes() {
        return "PENDIENTE".equals(this.estado);
    }
}