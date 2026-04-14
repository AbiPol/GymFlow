package com.gymflow.domain.model;

import lombok.Value;

import java.util.regex.Pattern;

@Value
public class Email {
    String value;

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );

    public Email(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Email no puede ser null o vacío");
        }
        if (!EMAIL_PATTERN.matcher(value).matches()) {
            throw new IllegalArgumentException("Formato de email inválido: " + value);
        }
        this.value = value;
    }

    public static Email of(String email) {
        return new Email(email);
    }
}