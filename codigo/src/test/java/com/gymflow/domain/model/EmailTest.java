package com.gymflow.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Email - Value Object Tests")
class EmailTest {

    @Test
    @DisplayName("E1: Email válido debe crearse sin excepción")
    void crearEmail_Valido_SinExcepcion() {
        String emailValido = "test@test.com";
        Email email = Email.of(emailValido);
        assertNotNull(email);
        assertEquals(emailValido, email.getValue());
    }

    @Test
    @DisplayName("E2: Email null debe lanzar IllegalArgumentException")
    void crearEmail_Nulo_ThrowsIllegalArgumentException() {
        String emailNulo = null;
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> Email.of(emailNulo)
        );
        assertEquals("Email no puede ser null o vacío", exception.getMessage());
    }

    @Test
    @DisplayName("E3: Email vacío debe lanzar IllegalArgumentException")
    void crearEmail_Vacio_ThrowsIllegalArgumentException() {
        String emailVacio = "";
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> Email.of(emailVacio)
        );
        assertEquals("Email no puede ser null o vacío", exception.getMessage());
    }

    @Test
    @DisplayName("E4: Email en blanco debe lanzar IllegalArgumentException")
    void crearEmail_EnBlanco_ThrowsIllegalArgumentException() {
        String emailEnBlanco = "   ";
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> Email.of(emailEnBlanco)
        );
        assertEquals("Email no puede ser null o vacío", exception.getMessage());
    }

    @Test
    @DisplayName("E5: Email sin @ debe lanzar IllegalArgumentException")
    void crearEmail_SinArroba_ThrowsIllegalArgumentException() {
        String emailSinArroba = "testtest.com";
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> Email.of(emailSinArroba)
        );
        assertTrue(exception.getMessage().contains("Formato de email inválido"));
    }

    @Test
    @DisplayName("E6: Email sin dominio debe lanzar IllegalArgumentException")
    void crearEmail_SinDominio_ThrowsIllegalArgumentException() {
        String emailSinDominio = "test@";
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> Email.of(emailSinDominio)
        );
        assertTrue(exception.getMessage().contains("Formato de email inválido"));
    }

    @Test
    @DisplayName("E7: Email sin extensión debe lanzar IllegalArgumentException")
    void crearEmail_SinExtension_ThrowsIllegalArgumentException() {
        String emailSinExtension = "test@test";
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> Email.of(emailSinExtension)
        );
        assertTrue(exception.getMessage().contains("Formato de email inválido"));
    }
}
