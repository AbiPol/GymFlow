package com.gymflow.application.usecases;

import com.gymflow.domain.exceptions.SocioNoEncontradoException;
import com.gymflow.domain.model.Email;
import com.gymflow.domain.model.Socio;
import com.gymflow.domain.model.SocioId;
import com.gymflow.domain.model.Suscripcion;
import com.gymflow.domain.ports.out.SocioRepository;
import com.gymflow.domain.ports.out.SuscripcionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("VerificarAccesoUseCase - Tests")
class VerificarAccesoUseCaseTest {

    @Mock
    private SocioRepository socioRepository;

    @Mock
    private SuscripcionRepository suscripcionRepository;

    private VerificarAccesoUseCaseImpl useCase;

    private SocioId socioId;
    private Socio socioActivo;

    @BeforeEach
    void setUp() {
        useCase = new VerificarAccesoUseCaseImpl(socioRepository, suscripcionRepository);
        socioId = SocioId.generate();
        Email email = Email.of("test@test.com");
        socioActivo = Socio.crear("Test User", email);
        socioActivo.activar();
    }

    @Test
    @DisplayName("VA1: Socio activo con suscripción válida debe tener acceso")
    void ejecutar_SocioActivoSuscripcionValida_AccesoPermitido() {
        Suscripcion suscripcionValida = Suscripcion.crear(socioId);

        when(socioRepository.buscarPorId(socioId)).thenReturn(Optional.of(socioActivo));
        when(suscripcionRepository.buscarPorSocioId(socioId)).thenReturn(Optional.of(suscripcionValida));

        boolean resultado = useCase.ejecutar(socioId);

        assertTrue(resultado);
    }

    @Test
    @DisplayName("VA2: Socio no encontrado debe lanzar SocioNoEncontradoException")
    void ejecutar_SocioNoExiste_ThrowsException() {
        when(socioRepository.buscarPorId(socioId)).thenReturn(Optional.empty());

        assertThrows(
                SocioNoEncontradoException.class,
                () -> useCase.ejecutar(socioId));
    }

    @Test
    @DisplayName("VA3: Socio inactivo debe tener acceso denegado")
    void ejecutar_SocioInactivo_AccesoDenegado() {
        Email email = Email.of("test@test.com");
        Socio socioInactivo = Socio.crear("Test User", email);

        when(socioRepository.buscarPorId(socioId)).thenReturn(Optional.of(socioInactivo));

        boolean resultado = useCase.ejecutar(socioId);

        assertFalse(resultado);
    }

    @Test
    @DisplayName("VA4: Socio bloqueado debe tener acceso denegado")
    void ejecutar_SocioBloqueado_AccesoDenegado() {
        Email email = Email.of("test@test.com");
        Socio socioBloqueado = Socio.crear("Test User", email);
        socioBloqueado.bloquear();

        when(socioRepository.buscarPorId(socioId)).thenReturn(Optional.of(socioBloqueado));

        boolean resultado = useCase.ejecutar(socioId);

        assertFalse(resultado);
    }

    @Test
    @DisplayName("VA5: Socio activo sin suscripción debe tener acceso denegado")
    void ejecutar_SinSuscripcion_AccesoDenegado() {
        when(socioRepository.buscarPorId(socioId)).thenReturn(Optional.of(socioActivo));
        when(suscripcionRepository.buscarPorSocioId(socioId)).thenReturn(Optional.empty());

        boolean resultado = useCase.ejecutar(socioId);

        assertFalse(resultado);
    }

    @Test
    @DisplayName("VA6: Socio activo con suscripción vencida debe tener acceso denegado")
    void ejecutar_SuscripcionVencida_AccesoDenegado() {
        Suscripcion suscripcionVencida = new Suscripcion(
                UUID.randomUUID(),
                socioId,
                LocalDate.now().minusDays(60),
                LocalDate.now().minusDays(30),
                "ACTIVA",
                LocalDateTime.now());

        when(socioRepository.buscarPorId(socioId)).thenReturn(Optional.of(socioActivo));
        when(suscripcionRepository.buscarPorSocioId(socioId)).thenReturn(Optional.of(suscripcionVencida));

        boolean resultado = useCase.ejecutar(socioId);

        assertFalse(resultado);
    }
}
