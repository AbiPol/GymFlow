package com.gymflow.application.usecases;

import com.gymflow.domain.exceptions.SocioNoEncontradoException;
import com.gymflow.domain.model.Socio;
import com.gymflow.domain.model.SocioId;
import com.gymflow.domain.model.Suscripcion;
import com.gymflow.domain.ports.in.VerificarAccesoUseCase;
import com.gymflow.domain.ports.out.SocioRepository;
import com.gymflow.domain.ports.out.SuscripcionRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class VerificarAccesoUseCaseImpl implements VerificarAccesoUseCase {

    private final SocioRepository socioRepository;
    private final SuscripcionRepository suscripcionRepository;

    @Override
    public boolean ejecutar(SocioId socioId) {
        Socio socio = socioRepository.buscarPorId(socioId)
                .orElseThrow(() -> new SocioNoEncontradoException(socioId.getValue().toString()));

        if (!socio.puedeAcceder()) {
            return false;
        }

        Suscripcion suscripcion = suscripcionRepository.buscarPorSocioId(socioId)
                .orElse(null);

        if (suscripcion == null || suscripcion.estaVencida()) {
            return false;
        }

        return true;
    }
}