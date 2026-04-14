package com.gymflow.domain.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class Socio {
    private SocioId id;
    private Email email;
    private String nombre;
    private EstadoSocio estado;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;

    public Socio() {}

    public Socio(SocioId id, Email email, String nombre, EstadoSocio estado,
                LocalDateTime fechaCreacion, LocalDateTime fechaActualizacion) {
        this.id = id;
        this.email = email;
        this.nombre = nombre;
        this.estado = estado;
        this.fechaCreacion = fechaCreacion;
        this.fechaActualizacion = fechaActualizacion;
    }

    public static Socio crear(String nombre, Email email) {
        LocalDateTime now = LocalDateTime.now();
        Socio socio = new Socio();
        socio.id = SocioId.generate();
        socio.email = email;
        socio.nombre = nombre;
        socio.estado = EstadoSocio.INACTIVO;
        socio.fechaCreacion = now;
        socio.fechaActualizacion = now;
        return socio;
    }

    public void activar() {
        this.estado = EstadoSocio.ACTIVO;
        this.fechaActualizacion = LocalDateTime.now();
    }

    public void bloquear() {
        this.estado = EstadoSocio.BLOQUEADO;
        this.fechaActualizacion = LocalDateTime.now();
    }

    public boolean puedeAcceder() {
        return this.estado == EstadoSocio.ACTIVO;
    }
}