package com.gymflow.application.usecases;

import com.gymflow.domain.exceptions.PagosPendientesException;
import com.gymflow.domain.exceptions.SocioNoEncontradoException;
import com.gymflow.domain.model.SocioId;
import com.gymflow.domain.model.Suscripcion;
import com.gymflow.domain.ports.in.RenovarSuscripcionUseCase;
import com.gymflow.domain.ports.out.PagoRepository;
import com.gymflow.domain.ports.out.SocioRepository;
import com.gymflow.domain.ports.out.SuscripcionRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RenovarSuscripcionUseCaseImpl implements RenovarSuscripcionUseCase {

    private final SocioRepository socioRepository;
    private final SuscripcionRepository suscripcionRepository;
    private final PagoRepository pagoRepository;

    @Override
    public Suscripcion ejecutar(SocioId socioId) {
        if (socioRepository.buscarPorId(socioId).isEmpty()) {
            throw new SocioNoEncontradoException(socioId.getValue().toString());
        }

        if (!pagoRepository.buscarPendientesPorSocioId(socioId).isEmpty()) {
            throw new PagosPendientesException(socioId.getValue().toString());
        }

        Suscripcion suscripcion = suscripcionRepository.buscarPorSocioId(socioId)
                .orElseGet(() -> Suscripcion.crear(socioId));

        suscripcion.extender30Dias();

        return suscripcionRepository.guardar(suscripcion);
    }
}