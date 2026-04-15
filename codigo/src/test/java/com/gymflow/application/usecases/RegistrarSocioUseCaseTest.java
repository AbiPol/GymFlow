package com.gymflow.application.usecases;

import com.gymflow.domain.exceptions.EmailDuplicadoException;
import com.gymflow.domain.model.Email;
import com.gymflow.domain.model.EstadoSocio;
import com.gymflow.domain.model.Socio;
import com.gymflow.domain.ports.out.NotificationService;
import com.gymflow.domain.ports.out.SocioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("RegistrarSocioUseCase - Tests")
class RegistrarSocioUseCaseTest {

    @Mock
    private SocioRepository socioRepository;

    @Mock
    private NotificationService notificationService;

    private RegistrarSocioUseCaseImpl useCase;

    @BeforeEach
    void setUp() {
        useCase = new RegistrarSocioUseCaseImpl(socioRepository, notificationService);
    }

    @Test
    @DisplayName("RC1: Registrar socio exitoso con email único")
    void ejecutar_EmailUnico_SocioCreado() {
        String nombre = "Juan Pérez";
        String emailStr = "juan@test.com";
        Email email = Email.of(emailStr);
        Socio socioCreado = Socio.crear(nombre, email);

        when(socioRepository.existePorEmail(emailStr)).thenReturn(false);
        when(socioRepository.guardar(any(Socio.class))).thenReturn(socioCreado);

        Socio resultado = useCase.ejecutar(nombre, email);

        assertNotNull(resultado);
        assertEquals(nombre, resultado.getNombre());
        assertEquals(emailStr, resultado.getEmail().getValue());
        verify(socioRepository).existePorEmail(emailStr);
        verify(socioRepository).guardar(any(Socio.class));
        verify(notificationService).enviarBienvenida(emailStr, nombre);
    }

    @Test
    @DisplayName("RC2: Email duplicado debe lanzar EmailDuplicadoException")
    void ejecutar_EmailDuplicado_ThrowsException() {
        String nombre = "Juan Pérez";
        String emailStr = "juan@test.com";
        Email email = Email.of(emailStr);

        when(socioRepository.existePorEmail(emailStr)).thenReturn(true);

        EmailDuplicadoException exception = assertThrows(
                EmailDuplicadoException.class,
                () -> useCase.ejecutar(nombre, email));

        assertTrue(exception.getMessage().contains(emailStr));
        verify(socioRepository, never()).guardar(any());
        verify(notificationService, never()).enviarBienvenida(any(), any());
    }

    @Test
    @DisplayName("RC3: Socio registrado debe tener estado INACTIVO")
    void ejecutar_SocioCreado_EstadoInactivo() {
        String nombre = "Ana García";
        String emailStr = "ana@test.com";
        Email email = Email.of(emailStr);
        Socio socioCreado = Socio.crear(nombre, email);

        when(socioRepository.existePorEmail(emailStr)).thenReturn(false);
        when(socioRepository.guardar(any(Socio.class))).thenReturn(socioCreado);

        Socio resultado = useCase.ejecutar(nombre, email);

        assertEquals(EstadoSocio.INACTIVO, resultado.getEstado());
    }

    @Test
    @DisplayName("RC4: Debe enviar notificación de bienvenida tras registro exitoso")
    void ejecutar_RegistroExitoso_EnviarNotificacion() {
        String nombre = "Carlos López";
        String emailStr = "carlos@test.com";
        Email email = Email.of(emailStr);
        Socio socioCreado = Socio.crear(nombre, email);

        when(socioRepository.existePorEmail(emailStr)).thenReturn(false);
        when(socioRepository.guardar(any(Socio.class))).thenReturn(socioCreado);

        useCase.ejecutar(nombre, email);

        verify(notificationService).enviarBienvenida(emailStr, nombre);
    }
}
