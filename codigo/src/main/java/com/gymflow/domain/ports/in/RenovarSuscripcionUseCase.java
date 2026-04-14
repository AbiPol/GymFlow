package com.gymflow.domain.ports.in;

import com.gymflow.domain.model.SocioId;
import com.gymflow.domain.model.Suscripcion;

public interface RenovarSuscripcionUseCase {
    Suscripcion ejecutar(SocioId socioId);
}