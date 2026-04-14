package com.gymflow.infrastructure.adapters.out.persistence.mapper;

import com.gymflow.domain.model.Email;
import com.gymflow.domain.model.Socio;
import com.gymflow.domain.model.SocioId;
import com.gymflow.infrastructure.adapters.out.persistence.entity.SocioEntity;

import java.time.LocalDateTime;

public class SocioMapper {

    public static Socio toDomain(SocioEntity entity) {
        if (entity == null)
            return null;

        Socio socio = new Socio();
        socio.setId(SocioId.from(entity.getId()));
        socio.setEmail(Email.of(entity.getEmail()));
        socio.setNombre(entity.getNombre());
        socio.setEstado(entity.getEstado());
        socio.setFechaCreacion(entity.getFechaCreacion());
        socio.setFechaActualizacion(entity.getFechaActualizacion());
        return socio;
    }

    public static SocioEntity toEntity(Socio socio) {
        if (socio == null)
            return null;

        SocioEntity entity = new SocioEntity();
        if (socio.getId() != null) {
            entity.setId(socio.getId().getValue());
        }
        entity.setEmail(socio.getEmail().getValue());
        entity.setNombre(socio.getNombre());
        entity.setEstado(socio.getEstado());
        entity.setFechaCreacion(socio.getFechaCreacion() != null ? socio.getFechaCreacion() : LocalDateTime.now());
        entity.setFechaActualizacion(
                socio.getFechaActualizacion() != null ? socio.getFechaActualizacion() : LocalDateTime.now());
        return entity;
    }
}