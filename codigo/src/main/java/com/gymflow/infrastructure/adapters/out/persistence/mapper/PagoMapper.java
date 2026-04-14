package com.gymflow.infrastructure.adapters.out.persistence.mapper;

import com.gymflow.domain.model.Pago;
import com.gymflow.domain.model.SocioId;
import com.gymflow.infrastructure.adapters.out.persistence.entity.PagoEntity;

import java.time.LocalDateTime;

public class PagoMapper {

    public static Pago toDomain(PagoEntity entity) {
        if (entity == null) return null;

        Pago pago = new Pago();
        pago.setId(entity.getId());
        pago.setSocioId(SocioId.from(entity.getSocioId()));
        pago.setMonto(entity.getMonto());
        pago.setEstado(entity.getEstado());
        pago.setFechaCreacion(entity.getFechaCreacion());
        return pago;
    }

    public static PagoEntity toEntity(Pago pago) {
        if (pago == null) return null;

        PagoEntity entity = new PagoEntity();
        entity.setId(pago.getId());
        if (pago.getSocioId() != null) {
            entity.setSocioId(pago.getSocioId().getValue());
        }
        entity.setMonto(pago.getMonto());
        entity.setEstado(pago.getEstado());
        entity.setFechaCreacion(pago.getFechaCreacion() != null ? pago.getFechaCreacion() : LocalDateTime.now());
        return entity;
    }
}