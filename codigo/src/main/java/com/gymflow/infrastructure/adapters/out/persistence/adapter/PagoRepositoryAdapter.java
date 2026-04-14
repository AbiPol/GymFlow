package com.gymflow.infrastructure.adapters.out.persistence.adapter;

import com.gymflow.domain.model.Pago;
import com.gymflow.domain.model.SocioId;
import com.gymflow.domain.ports.out.PagoRepository;
import com.gymflow.infrastructure.adapters.out.persistence.mapper.PagoMapper;
import com.gymflow.infrastructure.adapters.out.persistence.repository.PagoJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class PagoRepositoryAdapter implements PagoRepository {

    private final PagoJpaRepository pagoJpaRepository;

    @Override
    public List<Pago> buscarPendientesPorSocioId(SocioId socioId) {
        return pagoJpaRepository.findBySocioIdAndEstado(socioId.getValue(), "PENDIENTE")
                .stream()
                .map(PagoMapper::toDomain)
                .toList();
    }
}