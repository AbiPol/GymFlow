package com.gymflow.domain.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class Suscripcion {
    private UUID id;
    private SocioId socioId;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private String estado;
    private LocalDateTime fechaCreacion;

    public Suscripcion() {}

    public Suscripcion(UUID id, SocioId socioId, LocalDate fechaInicio,
                      LocalDate fechaFin, String estado, LocalDateTime fechaCreacion) {
        this.id = id;
        this.socioId = socioId;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.estado = estado;
        this.fechaCreacion = fechaCreacion;
    }

    public static Suscripcion crear(SocioId socioId) {
        LocalDate today = LocalDate.now();
        Suscripcion suscripcion = new Suscripcion();
        suscripcion.id = UUID.randomUUID();
        suscripcion.socioId = socioId;
        suscripcion.fechaInicio = today;
        suscripcion.fechaFin = today.plusDays(30);
        suscripcion.estado = "ACTIVA";
        suscripcion.fechaCreacion = LocalDateTime.now();
        return suscripcion;
    }

    public void extender30Dias() {
        LocalDate today = LocalDate.now();
        if (this.fechaFin.isAfter(today)) {
            this.fechaFin = this.fechaFin.plusDays(30);
        } else {
            this.fechaFin = today.plusDays(30);
        }
    }

    public boolean estaVencida() {
        return LocalDate.now().isAfter(this.fechaFin);
    }

    public boolean isActiva() {
        return "ACTIVA".equals(this.estado) && !estaVencida();
    }
}