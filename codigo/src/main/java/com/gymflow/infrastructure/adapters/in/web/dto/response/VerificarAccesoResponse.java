package com.gymflow.infrastructure.adapters.in.web.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class VerificarAccesoResponse {
    private boolean permitido;
    private String mensaje;
}