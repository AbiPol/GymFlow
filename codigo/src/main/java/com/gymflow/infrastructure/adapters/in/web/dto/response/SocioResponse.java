package com.gymflow.infrastructure.adapters.in.web.dto.response;

import com.gymflow.domain.model.EstadoSocio;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SocioResponse {
    private UUID id;
    private String email;
    private String nombre;
    private EstadoSocio estado;
    private LocalDateTime fechaCreacion;
}