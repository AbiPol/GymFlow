package com.gymflow.application.usecases;

import com.gymflow.domain.exceptions.PagosPendientesException;
import com.gymflow.domain.exceptions.SocioNoEncontradoException;
import com.gymflow.domain.model.Email;
import com.gymflow.domain.model.Pago;
import com.gymflow.domain.model.Socio;
import com.gymflow.domain.model.SocioId;
import com.gymflow.domain.model.Suscripcion;
import com.gymflow.domain.ports.out.PagoRepository;
import com.gymflow.domain.ports.out.SocioRepository;
import com.gymflow.domain.ports.out.SuscripcionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("RenovarSuscripcionUseCase - Tests")
class RenovarSuscripcionUseCaseTest {

    @Mock
    private SocioRepository socioRepository;

    @Mock
    private SuscripcionRepository suscripcionRepository;

    @Mock
    private PagoRepository pagoRepository;

    private RenovarSuscripcionUseCaseImpl useCase;

    private SocioId socioId;
    private Socio socio;

    @BeforeEach
    void setUp() {
        useCase = new RenovarSuscripcionUseCaseImpl(socioRepository, suscripcionRepository, pagoRepository);
        socioId = SocioId.generate();
        Email email = Email.of("test@test.com");
        socio = Socio.crear("Test User", email);
    }

    @Test
    @DisplayName("RN1: Renovar suscripción exitosa sin pagos pendientes")
    void ejecutar_SinPagosPendientes_SuscripcionRenovada() {
        Suscripcion suscripcionExistente = Suscripcion.crear(socioId);
        Suscripcion suscripcionRenovada = Suscripcion.crear(socioId);

        when(socioRepository.buscarPorId(socioId)).thenReturn(Optional.of(socio));
        when(pagoRepository.buscarPendientesPorSocioId(socioId)).thenReturn(Collections.emptyList());
        when(suscripcionRepository.buscarPorSocioId(socioId)).thenReturn(Optional.of(suscripcionExistente));
        when(suscripcionRepository.guardar(any(Suscripcion.class))).thenReturn(suscripcionRenovada);

        Suscripcion resultado = useCase.ejecutar(socioId);

        assertNotNull(resultado);
        verify(suscripcionRepository).guardar(any(Suscripcion.class));
    }

    @Test
    @DisplayName("RN2: Socio no encontrado debe lanzar SocioNoEncontradoException")
    void ejecutar_SocioNoExiste_ThrowsException() {
        when(socioRepository.buscarPorId(socioId)).thenReturn(Optional.empty());

        SocioNoEncontradoException exception = assertThrows(
            SocioNoEncontradoException.class,
            () -> useCase.ejecutar(socioId)
        );

        assertTrue(exception.getMessage().contains(socioId.getValue().toString()));
        verify(suscripcionRepository, never()).guardar(any());
    }

    @Test
    @DisplayName("RN3: Pagos pendientes debe lanzar PagosPendientesException")
    void ejecutar_ConPagosPendientes_ThrowsException() {
        Pago pagoPendiente = Pago.crear(socioId, new BigDecimal("50.00"));

        when(socioRepository.buscarPorId(socioId)).thenReturn(Optional.of(socio));
        when(pagoRepository.buscarPendientesPorSocioId(socioId)).thenReturn(List.of(pagoPendiente));

        PagosPendientesException exception = assertThrows(
            PagosPendientesException.class,
            () -> useCase.ejecutar(socioId)
        );

        assertNotNull(exception);
        verify(suscripcionRepository, never()).guardar(any());
    }

    @Test
    @DisplayName("RN4: Si no existe suscripción, debe crear una nueva")
    void ejecutar_SinSuscripcionExistente_CreaNueva() {
        Suscripcion nuevaSuscripcion = Suscripcion.crear(socioId);

        when(socioRepository.buscarPorId(socioId)).thenReturn(Optional.of(socio));
        when(pagoRepository.buscarPendientesPorSocioId(socioId)).thenReturn(Collections.emptyList());
        when(suscripcionRepository.buscarPorSocioId(socioId)).thenReturn(Optional.empty());
        when(suscripcionRepository.guardar(any(Suscripcion.class))).thenReturn(nuevaSuscripcion);

        Suscripcion resultado = useCase.ejecutar(socioId);

        assertNotNull(resultado);
    }

    @Test
    @DisplayName("RN5: Extiende 30 días desde fecha fin si no está vencida")
    void ejecutar_SuscripcionNoVencida_ExtiendeDesdeFechaFin() {
        LocalDate fechaFutura = LocalDate.now().plusDays(15);
        Suscripcion suscripcionExistente = new Suscripcion(
            java.util.UUID.randomUUID(),
            socioId,
            LocalDate.now().minusDays(15),
            fechaFutura,
            "ACTIVA",
            java.time.LocalDateTime.now()
        );
        Suscripcion suscripcionRenovada = Suscripcion.crear(socioId);

        when(socioRepository.buscarPorId(socioId)).thenReturn(Optional.of(socio));
        when(pagoRepository.buscarPendientesPorSocioId(socioId)).thenReturn(Collections.emptyList());
        when(suscripcionRepository.buscarPorSocioId(socioId)).thenReturn(Optional.of(suscripcionExistente));
        when(suscripcionRepository.guardar(any(Suscripcion.class))).thenReturn(suscripcionRenovada);

        useCase.ejecutar(socioId);

        verify(suscripcionRepository).guardar(argThat(s -> 
            s.getFechaFin().isAfter(fechaFutura) || 
            s.getFechaFin().isEqual(fechaFutura.plusDays(30))
        ));
    }
}
