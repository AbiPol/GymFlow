package com.gymflow.domain.ports.out;

import com.gymflow.domain.model.SocioId;
import com.gymflow.domain.model.Suscripcion;

import java.util.Optional;

public interface SuscripcionRepository {
    Suscripcion guardar(Suscripcion suscripcion);
    Optional<Suscripcion> buscarPorSocioId(SocioId socioId);
}