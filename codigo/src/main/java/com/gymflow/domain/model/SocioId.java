package com.gymflow.domain.model;

import lombok.NonNull;
import lombok.Value;

import java.util.UUID;

@Value
public class SocioId {
    @NonNull
    UUID value;

    public SocioId(UUID value) {
        if (value == null) {
            throw new IllegalArgumentException("SocioId no puede ser null");
        }
        this.value = value;
    }

    public static SocioId generate() {
        return new SocioId(UUID.randomUUID());
    }

    public static SocioId from(UUID uuid) {
        return new SocioId(uuid);
    }
}