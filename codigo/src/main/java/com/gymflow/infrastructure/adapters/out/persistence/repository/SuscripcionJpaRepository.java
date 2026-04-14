package com.gymflow.infrastructure.adapters.out.persistence.repository;

import com.gymflow.infrastructure.adapters.out.persistence.entity.SuscripcionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SuscripcionJpaRepository extends JpaRepository<SuscripcionEntity, UUID> {
    Optional<SuscripcionEntity> findBySocioId(UUID socioId);
}