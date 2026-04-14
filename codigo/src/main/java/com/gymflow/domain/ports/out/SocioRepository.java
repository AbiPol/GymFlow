package com.gymflow.domain.ports.out;

import com.gymflow.domain.model.Socio;
import com.gymflow.domain.model.SocioId;

import java.util.Optional;

public interface SocioRepository {
    Socio guardar(Socio socio);
    Optional<Socio> buscarPorId(SocioId id);
    boolean existePorEmail(String email);
    boolean existePorEmailExcluyendoId(String email, SocioId id);
}