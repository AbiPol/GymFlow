package com.gymflow.infrastructure.adapters.in.web;

import com.gymflow.domain.model.Email;
import com.gymflow.domain.model.SocioId;
import com.gymflow.domain.model.Suscripcion;
import com.gymflow.domain.ports.in.RegistrarSocioUseCase;
import com.gymflow.domain.ports.in.RenovarSuscripcionUseCase;
import com.gymflow.domain.ports.in.VerificarAccesoUseCase;
import com.gymflow.infrastructure.adapters.in.web.dto.request.RegistrarSocioRequest;
import com.gymflow.infrastructure.adapters.in.web.dto.response.SocioResponse;
import com.gymflow.infrastructure.adapters.in.web.dto.response.SuscripcionResponse;
import com.gymflow.infrastructure.adapters.in.web.dto.response.VerificarAccesoResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/socios")
@RequiredArgsConstructor
public class SocioController {

    private final RegistrarSocioUseCase registrarSocioUseCase;
    private final RenovarSuscripcionUseCase renovarSuscripcionUseCase;
    private final VerificarAccesoUseCase verificarAccesoUseCase;

    @PostMapping
    public ResponseEntity<SocioResponse> registrarSocio(@Valid @RequestBody RegistrarSocioRequest request) {
        var socio = registrarSocioUseCase.ejecutar(request.getNombre(), Email.of(request.getEmail()));

        SocioResponse response = new SocioResponse(
                socio.getId().getValue(),
                socio.getEmail().getValue(),
                socio.getNombre(),
                socio.getEstado(),
                socio.getFechaCreacion());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}/suscripcion/renovar")
    public ResponseEntity<SuscripcionResponse> renovarSuscripcion(@PathVariable UUID id) {
        SocioId socioId = SocioId.from(id);
        Suscripcion suscripcion = renovarSuscripcionUseCase.ejecutar(socioId);

        SuscripcionResponse response = new SuscripcionResponse(
                suscripcion.getId(),
                suscripcion.getSocioId().getValue(),
                suscripcion.getFechaInicio(),
                suscripcion.getFechaFin(),
                suscripcion.getEstado());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/acceso")
    public ResponseEntity<VerificarAccesoResponse> verificarAcceso(@PathVariable UUID id) {
        SocioId socioId = SocioId.from(id);
        boolean permitido = verificarAccesoUseCase.ejecutar(socioId);

        String mensaje;
        if (permitido) {
            mensaje = "Acceso concedido";
        } else {
            mensaje = "Acceso denegado";
        }
        VerificarAccesoResponse response = new VerificarAccesoResponse(permitido, mensaje);

        return ResponseEntity.ok(response);
    }
}