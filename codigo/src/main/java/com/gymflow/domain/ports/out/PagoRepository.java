package com.gymflow.domain.ports.out;

import com.gymflow.domain.model.Pago;
import com.gymflow.domain.model.SocioId;

import java.util.List;

public interface PagoRepository {
    List<Pago> buscarPendientesPorSocioId(SocioId socioId);
}