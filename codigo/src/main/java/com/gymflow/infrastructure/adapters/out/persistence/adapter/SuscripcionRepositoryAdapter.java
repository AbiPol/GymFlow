package com.gymflow.infrastructure.adapters.out.persistence.adapter;

import com.gymflow.domain.model.SocioId;
import com.gymflow.domain.model.Suscripcion;
import com.gymflow.domain.ports.out.SuscripcionRepository;
import com.gymflow.infrastructure.adapters.out.persistence.mapper.SuscripcionMapper;
import com.gymflow.infrastructure.adapters.out.persistence.repository.SuscripcionJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class SuscripcionRepositoryAdapter implements SuscripcionRepository {

    private final SuscripcionJpaRepository suscripcionJpaRepository;

    @Override
    public Suscripcion guardar(Suscripcion suscripcion) {
        var entity = SuscripcionMapper.toEntity(suscripcion);
        if (entity != null) {
            var saved = suscripcionJpaRepository.save(entity);
            return SuscripcionMapper.toDomain(saved);
        }
        return null;
    }

    @Override
    public Optional<Suscripcion> buscarPorSocioId(SocioId socioId) {
        return suscripcionJpaRepository.findBySocioId(socioId.getValue())
                .map(SuscripcionMapper::toDomain);
    }
}