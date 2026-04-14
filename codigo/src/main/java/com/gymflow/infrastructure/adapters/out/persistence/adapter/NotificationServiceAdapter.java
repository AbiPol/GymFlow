package com.gymflow.infrastructure.adapters.out.persistence.adapter;

import com.gymflow.domain.ports.out.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NotificationServiceAdapter implements NotificationService {

    @Override
    public void enviarBienvenida(String email, String nombre) {
        log.info("📧 Mensaje de Bienvenida enviado a: {} ({})", nombre, email);
    }
}