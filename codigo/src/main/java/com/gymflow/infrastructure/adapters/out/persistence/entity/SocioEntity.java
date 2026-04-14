package com.gymflow.infrastructure.adapters.out.persistence.entity;

import com.gymflow.domain.model.EstadoSocio;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "socios")
@Getter
@Setter
public class SocioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String nombre;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoSocio estado;

    @Column(nullable = false)
    private LocalDateTime fechaCreacion;

    @Column(nullable = false)
    private LocalDateTime fechaActualizacion;
}