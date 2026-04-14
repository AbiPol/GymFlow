package com.gymflow.domain.exceptions;

public class EmailDuplicadoException extends RuntimeException {
    public EmailDuplicadoException(String email) {
        super("El email " + email + " ya está registrado");
    }
}