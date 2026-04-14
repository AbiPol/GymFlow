package com.gymflow.domain.exceptions;

public class PagosPendientesException extends RuntimeException {
    public PagosPendientesException(String id) {
        super("El socio tiene pagos pendientes");
    }
}