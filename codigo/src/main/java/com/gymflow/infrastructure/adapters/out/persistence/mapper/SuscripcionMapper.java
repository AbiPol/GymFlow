package com.gymflow.infrastructure.adapters.out.persistence.mapper;

import com.gymflow.domain.model.SocioId;
import com.gymflow.domain.model.Suscripcion;
import com.gymflow.infrastructure.adapters.out.persistence.entity.SuscripcionEntity;

import java.time.LocalDateTime;

public class SuscripcionMapper {

    public static Suscripcion toDomain(SuscripcionEntity entity) {
        if (entity == null) return null;

        Suscripcion suscripcion = new Suscripcion();
        suscripcion.setId(entity.getId());
        suscripcion.setSocioId(SocioId.from(entity.getSocioId()));
        suscripcion.setFechaInicio(entity.getFechaInicio());
        suscripcion.setFechaFin(entity.getFechaFin());
        suscripcion.setEstado(entity.getEstado());
        suscripcion.setFechaCreacion(entity.getFechaCreacion());
        return suscripcion;
    }

    public static SuscripcionEntity toEntity(Suscripcion suscripcion) {
        if (suscripcion == null) return null;

        SuscripcionEntity entity = new SuscripcionEntity();
        entity.setId(suscripcion.getId());
        if (suscripcion.getSocioId() != null) {
            entity.setSocioId(suscripcion.getSocioId().getValue());
        }
        entity.setFechaInicio(suscripcion.getFechaInicio());
        entity.setFechaFin(suscripcion.getFechaFin());
        entity.setEstado(suscripcion.getEstado());
        entity.setFechaCreacion(suscripcion.getFechaCreacion() != null ? suscripcion.getFechaCreacion() : LocalDateTime.now());
        return entity;
    }
}