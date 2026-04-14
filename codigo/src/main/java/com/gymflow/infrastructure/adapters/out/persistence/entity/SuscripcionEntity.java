package com.gymflow.infrastructure.adapters.out.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "suscripciones")
@Getter
@Setter
public class SuscripcionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID socioId;

    @Column(nullable = false)
    private LocalDate fechaInicio;

    @Column(nullable = false)
    private LocalDate fechaFin;

    @Column(nullable = false)
    private String estado;

    @Column(nullable = false)
    private LocalDateTime fechaCreacion;
}