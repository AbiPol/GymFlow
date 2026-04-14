package com.gymflow.infrastructure.adapters.out.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "pagos")
@Getter
@Setter
public class PagoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID socioId;

    @Column(nullable = false)
    private BigDecimal monto;

    @Column(nullable = false)
    private String estado;

    @Column(nullable = false)
    private LocalDateTime fechaCreacion;
}