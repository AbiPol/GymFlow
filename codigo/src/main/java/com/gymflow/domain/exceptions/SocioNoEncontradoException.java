package com.gymflow.domain.exceptions;

public class SocioNoEncontradoException extends RuntimeException {
    public SocioNoEncontradoException(String id) {
        super("Socio no encontrado con ID: " + id);
    }
}