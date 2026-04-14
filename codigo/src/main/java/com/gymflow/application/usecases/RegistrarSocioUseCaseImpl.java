package com.gymflow.application.usecases;

import com.gymflow.domain.exceptions.EmailDuplicadoException;
import com.gymflow.domain.model.Email;
import com.gymflow.domain.model.Socio;
import com.gymflow.domain.ports.in.RegistrarSocioUseCase;
import com.gymflow.domain.ports.out.NotificationService;
import com.gymflow.domain.ports.out.SocioRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RegistrarSocioUseCaseImpl implements RegistrarSocioUseCase {

    private final SocioRepository socioRepository;
    private final NotificationService notificationService;

    @Override
    public Socio ejecutar(String nombre, Email email) {
        if (socioRepository.existePorEmail(email.getValue())) {
            throw new EmailDuplicadoException(email.getValue());
        }

        Socio socioGuardado = socioRepository.guardar(Socio.crear(nombre, email));

        notificationService.enviarBienvenida(email.getValue(), nombre);

        return socioGuardado;
    }
}