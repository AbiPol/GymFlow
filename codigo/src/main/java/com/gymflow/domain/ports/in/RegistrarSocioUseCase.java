package com.gymflow.domain.ports.in;

import com.gymflow.domain.model.Email;
import com.gymflow.domain.model.Socio;

public interface RegistrarSocioUseCase {
    Socio ejecutar(String nombre, Email email);
}