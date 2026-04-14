package com.gymflow.infrastructure.adapters.in.web.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class RegistrarSocioRequest {

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El formato del email es inválido")
    private String email;
}