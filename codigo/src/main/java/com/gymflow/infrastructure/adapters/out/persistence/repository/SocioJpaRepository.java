package com.gymflow.infrastructure.adapters.out.persistence.repository;

import com.gymflow.infrastructure.adapters.out.persistence.entity.SocioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SocioJpaRepository extends JpaRepository<SocioEntity, UUID> {
    boolean existsByEmail(String email);
    boolean existsByEmailAndIdNot(String email, UUID id);
}