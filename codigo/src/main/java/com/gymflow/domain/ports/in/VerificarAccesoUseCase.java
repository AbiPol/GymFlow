package com.gymflow.domain.ports.in;

import com.gymflow.domain.model.SocioId;

public interface VerificarAccesoUseCase {
    boolean ejecutar(SocioId socioId);
}