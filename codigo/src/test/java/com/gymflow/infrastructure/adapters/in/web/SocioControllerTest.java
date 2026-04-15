package com.gymflow.infrastructure.adapters.in.web;

import com.gymflow.domain.exceptions.EmailDuplicadoException;
import com.gymflow.domain.model.Email;
import com.gymflow.domain.model.Socio;
import com.gymflow.domain.model.SocioId;
import com.gymflow.domain.model.Suscripcion;
import com.gymflow.domain.ports.in.RegistrarSocioUseCase;
import com.gymflow.domain.ports.in.RenovarSuscripcionUseCase;
import com.gymflow.domain.ports.in.VerificarAccesoUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SocioController.class)
@DisplayName("SocioController - Integration Tests")
class SocioControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @MockBean
        private RegistrarSocioUseCase registrarSocioUseCase;

        @MockBean
        private RenovarSuscripcionUseCase renovarSuscripcionUseCase;

        @MockBean
        private VerificarAccesoUseCase verificarAccesoUseCase;

        @Test
        @DisplayName("CT1: POST /api/socios - Registro exitoso")
        void registrarSocio_DatosValidos_Returns201() throws Exception {
                String jsonRequest = "{\"nombre\": \"Juan Pérez\", \"email\": \"juan@test.com\"}";

                Socio socio = Socio.crear("Juan Pérez", Email.of("juan@test.com"));

                when(registrarSocioUseCase.ejecutar(any(), any())).thenReturn(socio);

                mockMvc.perform(post("/api/socios")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonRequest))
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.nombre").value("Juan Pérez"))
                                .andExpect(jsonPath("$.email").value("juan@test.com"))
                                .andExpect(jsonPath("$.estado").value("INACTIVO"));
        }

        @Test
        @DisplayName("CT2: POST /api/socios - Email duplicado retorna 409")
        void registrarSocio_EmailDuplicado_Returns409() throws Exception {
                String jsonRequest = "{\"nombre\": \"Juan Pérez\", \"email\": \"juan@test.com\"}";

                when(registrarSocioUseCase.ejecutar(any(), any()))
                                .thenThrow(new EmailDuplicadoException("juan@test.com"));

                mockMvc.perform(post("/api/socios")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonRequest))
                                .andExpect(status().isConflict());
        }

        @Test
        @DisplayName("CT3: POST /api/socios - Datos inválidos retorna 400")
        void registrarSocio_DatosInvalidos_Returns400() throws Exception {
                String jsonRequest = "{\"nombre\": \"\", \"email\": \"email-invalido\"}";

                mockMvc.perform(post("/api/socios")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonRequest))
                                .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("CT4: PUT /api/socios/{id}/suscripcion/renovar - Renovación exitosa")
        void renovarSuscripcion_SocioExiste_Returns200() throws Exception {
                UUID socioId = UUID.randomUUID();
                SocioId socioIdDomain = SocioId.from(socioId);
                Suscripcion suscripcion = new Suscripcion(
                                UUID.randomUUID(),
                                socioIdDomain,
                                LocalDate.now(),
                                LocalDate.now().plusDays(30),
                                "ACTIVA",
                                LocalDateTime.now());

                when(renovarSuscripcionUseCase.ejecutar(any(SocioId.class))).thenReturn(suscripcion);

                mockMvc.perform(put("/api/socios/{id}/suscripcion/renovar", socioId))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.estado").value("ACTIVA"))
                                .andExpect(jsonPath("$.fechaFin").exists());
        }

        @Test
        @DisplayName("CT5: GET /api/socios/{id}/acceso - Acceso permitido")
        void verificarAcceso_AccesoPermitido_Returns200() throws Exception {
                UUID socioId = UUID.randomUUID();
                SocioId socioIdDomain = SocioId.from(socioId);

                when(verificarAccesoUseCase.ejecutar(any(SocioId.class))).thenReturn(true);

                mockMvc.perform(get("/api/socios/{id}/acceso", socioId))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.permitido").value(true))
                                .andExpect(jsonPath("$.mensaje").value("Acceso concedido"));
        }

        @Test
        @DisplayName("CT6: GET /api/socios/{id}/acceso - Acceso denegado")
        void verificarAcceso_AccesoDenegado_Returns200() throws Exception {
                UUID socioId = UUID.randomUUID();
                SocioId socioIdDomain = SocioId.from(socioId);

                when(verificarAccesoUseCase.ejecutar(any(SocioId.class))).thenReturn(false);

                mockMvc.perform(get("/api/socios/{id}/acceso", socioId))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.permitido").value(false))
                                .andExpect(jsonPath("$.mensaje").value("Acceso denegado"));
        }
}
