package com.gymflow.infrastructure.adapters.out.persistence.adapter;

import com.gymflow.domain.model.Socio;
import com.gymflow.domain.model.SocioId;
import com.gymflow.domain.ports.out.SocioRepository;
import com.gymflow.infrastructure.adapters.out.persistence.mapper.SocioMapper;
import com.gymflow.infrastructure.adapters.out.persistence.repository.SocioJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class SocioRepositoryAdapter implements SocioRepository {

    private final SocioJpaRepository socioJpaRepository;

    @Override
    public Socio guardar(Socio socio) {
        var entity = SocioMapper.toEntity(socio);
        if (entity == null) {
            throw new IllegalArgumentException("El socio no puede ser null");
        }
        var saved = socioJpaRepository.save(entity);
        return SocioMapper.toDomain(saved);
    }

    @Override
    public Optional<Socio> buscarPorId(SocioId id) {
        UUID uuid = id.getValue();
        if (uuid == null) {
            return Optional.empty();
        }
        return socioJpaRepository.findById(Objects.requireNonNull(uuid))
                .map(SocioMapper::toDomain);
    }

    @Override
    public boolean existePorEmail(String email) {
        return socioJpaRepository.existsByEmail(email);
    }

    @Override
    public boolean existePorEmailExcluyendoId(String email, SocioId id) {
        return socioJpaRepository.existsByEmailAndIdNot(email, id.getValue());
    }
}