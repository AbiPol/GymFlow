package com.gymflow.infrastructure.adapters.in.web.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SuscripcionResponse {
    private UUID id;
    private UUID socioId;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private String estado;
}