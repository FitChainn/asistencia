package com.fitchain.asistencia;

import com.fitchain.asistencia.assembler.AsistenciaModelAssembler;
import com.fitchain.asistencia.config.SecurityConfig;
import com.fitchain.asistencia.controller.AsistenciaController;
import com.fitchain.asistencia.dto.AsistenciaRequestDTO;
import com.fitchain.asistencia.dto.AsistenciaResponseDTO;
import com.fitchain.asistencia.dto.ClienteDTO;
import com.fitchain.asistencia.dto.HorarioDTO;
import com.fitchain.asistencia.filter.RolHeaderFilter;
import com.fitchain.asistencia.service.AsistenciaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AsistenciaController.class)
@Import({SecurityConfig.class, RolHeaderFilter.class, AsistenciaModelAssembler.class})
@WithMockUser
public class AsistenciaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AsistenciaService asistenciaService;

    @Autowired
    private ObjectMapper objectMapper;

    private AsistenciaResponseDTO aResponse;
    private AsistenciaRequestDTO aRequest;

    @BeforeEach
    void setUp() {
        ClienteDTO cli = new ClienteDTO();
        cli.setId(1L);
        cli.setNombre("JUAN");

        HorarioDTO hor = new HorarioDTO();
        hor.setId(1L);
        hor.setHoraApertura(LocalTime.of(9, 0));

        aResponse = new AsistenciaResponseDTO();
        aResponse.setId(1L);
        aResponse.setFecha(LocalDate.of(2025, 6, 12));
        aResponse.setCliente(cli);
        aResponse.setHorario(hor);

        aRequest = new AsistenciaRequestDTO();
        aRequest.setClienteId(1L);
        aRequest.setHorarioId(1L);
        aRequest.setFecha(LocalDate.of(2025, 6, 12));
    }

    @Test
    void Post_crear201() throws Exception {
        when(asistenciaService.crear(any(AsistenciaRequestDTO.class))).thenReturn(aResponse);

        mockMvc.perform(post("/v1/asistencias")
                        .with(csrf())
                        .header("X-User-Rol", "ADMIN")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(aRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void Get_obtenerTodas() throws Exception {
        when(asistenciaService.obtenerTodas()).thenReturn(List.of(aResponse));

        mockMvc.perform(get("/v1/asistencias")
                        .header("X-User-Rol", "ADMIN"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.asistenciaResponseDTOList.length()").value(1));
    }

    @Test
    void Get_obtenerPorId() throws Exception {
        when(asistenciaService.obtenerPorId(1L)).thenReturn(aResponse);

        mockMvc.perform(get("/v1/asistencias/1")
                        .header("X-User-Rol", "ADMIN"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void Delete_eliminar() throws Exception {
        mockMvc.perform(delete("/v1/asistencias/1")
                        .with(csrf())
                        .header("X-User-Rol", "ADMIN"))
                .andExpect(status().isNoContent());
    }
}