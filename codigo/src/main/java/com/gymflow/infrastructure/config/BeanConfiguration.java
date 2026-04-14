package com.gymflow.infrastructure.config;

import com.gymflow.application.usecases.RegistrarSocioUseCaseImpl;
import com.gymflow.application.usecases.RenovarSuscripcionUseCaseImpl;
import com.gymflow.application.usecases.VerificarAccesoUseCaseImpl;
import com.gymflow.domain.ports.out.NotificationService;
import com.gymflow.domain.ports.out.PagoRepository;
import com.gymflow.domain.ports.out.SocioRepository;
import com.gymflow.domain.ports.out.SuscripcionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class BeanConfiguration {

    private final SocioRepository socioRepository;
    private final SuscripcionRepository suscripcionRepository;
    private final PagoRepository pagoRepository;
    private final NotificationService notificationService;

    @Bean
    public RegistrarSocioUseCaseImpl registrarSocioUseCase() {
        return new RegistrarSocioUseCaseImpl(socioRepository, notificationService);
    }

    @Bean
    public RenovarSuscripcionUseCaseImpl renovarSuscripcionUseCase() {
        return new RenovarSuscripcionUseCaseImpl(socioRepository, suscripcionRepository, pagoRepository);
    }

    @Bean
    public VerificarAccesoUseCaseImpl verificarAccesoUseCase() {
        return new VerificarAccesoUseCaseImpl(socioRepository, suscripcionRepository);
    }
}