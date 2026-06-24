package com.fitchain.asistencia;

import com.fitchain.asistencia.WebClient.ClienteClient;
import com.fitchain.asistencia.WebClient.HorarioClient;
import com.fitchain.asistencia.dto.AsistenciaRequestDTO;
import com.fitchain.asistencia.dto.AsistenciaResponseDTO;
import com.fitchain.asistencia.dto.ClienteDTO;
import com.fitchain.asistencia.dto.HorarioDTO;
import com.fitchain.asistencia.model.Asistencia;
import com.fitchain.asistencia.repository.AsistenciaRepository;
import com.fitchain.asistencia.service.AsistenciaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("PRUEBAS UNITARIAS DEL SERVICE DE ASISTENCIA")
public class AsistenciaServiceTest {

    @Mock
    private AsistenciaRepository asistenciaRepository;

    @Mock
    private ClienteClient clienteClient;

    @Mock
    private HorarioClient horarioClient;

    @InjectMocks
    private AsistenciaService asistenciaService;

    private Asistencia asistencia;
    private ClienteDTO clienteDTO;
    private HorarioDTO horarioDTO;
    private AsistenciaRequestDTO aRequest;

    @BeforeEach
    void setUp() {
        asistencia = new Asistencia(1L, 1L, 1L, LocalDate.of(2026, 6, 12));

        clienteDTO = new ClienteDTO();
        clienteDTO.setId(1L);
        clienteDTO.setNombre("JUAN PEREZ");

        horarioDTO = new HorarioDTO();
        horarioDTO.setId(1L);
        horarioDTO.setHoraApertura(LocalTime.of(9, 0));

        aRequest = new AsistenciaRequestDTO();
        aRequest.setClienteId(1L);
        aRequest.setHorarioId(1L);
        aRequest.setFecha(LocalDate.of(2026, 6, 12));
    }

    @Test
    @DisplayName("DEBE REGISTRAR UNA ASISTENCIA")
    void shouldCrearAsistencia() {
        when(clienteClient.obtenerClientePorId(1L)).thenReturn(clienteDTO);
        when(horarioClient.obtenerHorarioPorId(1L)).thenReturn(horarioDTO);
        when(asistenciaRepository.save(any(Asistencia.class))).thenReturn(asistencia);

        AsistenciaResponseDTO result = asistenciaService.crear(aRequest);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("JUAN PEREZ", result.getCliente().getNombre());
        verify(asistenciaRepository, times(1)).save(any(Asistencia.class));
    }

    @Test
    @DisplayName("DEBE OBTENER TODAS LAS ASISTENCIAS")
    void shouldObtenerTodas() {
        when(asistenciaRepository.findAll()).thenReturn(List.of(asistencia));
        when(clienteClient.obtenerClientePorId(1L)).thenReturn(clienteDTO);
        when(horarioClient.obtenerHorarioPorId(1L)).thenReturn(horarioDTO);

        List<AsistenciaResponseDTO> result = asistenciaService.obtenerTodas();

        assertEquals(1, result.size());
        verify(asistenciaRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("DEBE OBTENER ASISTENCIA POR ID")
    void shouldObtenerPorId() {
        when(asistenciaRepository.findById(1L)).thenReturn(Optional.of(asistencia));
        when(clienteClient.obtenerClientePorId(1L)).thenReturn(clienteDTO);
        when(horarioClient.obtenerHorarioPorId(1L)).thenReturn(horarioDTO);

        AsistenciaResponseDTO result = asistenciaService.obtenerPorId(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(asistenciaRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("DEBE LANZAR EXCEPCION SI ASISTENCIA NO EXISTE POR ID")
    void shouldThrowWhenNotFoundById() {
        when(asistenciaRepository.findById(99L)).thenReturn(Optional.empty());

        NoSuchElementException ex = assertThrows(NoSuchElementException.class,
                () -> asistenciaService.obtenerPorId(99L));

        assertEquals("Asistencia con id 99 no encontrada", ex.getMessage());
    }

    @Test
    @DisplayName("DEBE OBTENER ASISTENCIAS POR CLIENTE")
    void shouldObtenerPorCliente() {
        when(clienteClient.obtenerClientePorId(1L)).thenReturn(clienteDTO);
        when(asistenciaRepository.findByClienteId(1L)).thenReturn(List.of(asistencia));
        when(horarioClient.obtenerHorarioPorId(1L)).thenReturn(horarioDTO);

        List<AsistenciaResponseDTO> result = asistenciaService.obtenerPorCliente(1L);

        assertEquals(1, result.size());
        verify(asistenciaRepository, times(1)).findByClienteId(1L);
    }

    @Test
    @DisplayName("DEBE OBTENER ASISTENCIAS POR HORARIO")
    void shouldObtenerPorHorario() {
        when(horarioClient.obtenerHorarioPorId(1L)).thenReturn(horarioDTO);
        when(asistenciaRepository.findByHorarioId(1L)).thenReturn(List.of(asistencia));
        when(clienteClient.obtenerClientePorId(1L)).thenReturn(clienteDTO);

        List<AsistenciaResponseDTO> result = asistenciaService.obtenerPorHorario(1L);

        assertEquals(1, result.size());
        verify(asistenciaRepository, times(1)).findByHorarioId(1L);
    }

    @Test
    @DisplayName("DEBE ELIMINAR UNA ASISTENCIA")
    void shouldEliminarAsistencia() {
        when(asistenciaRepository.existsById(1L)).thenReturn(true);

        asistenciaService.eliminar(1L);

        verify(asistenciaRepository, times(1)).existsById(1L);
        verify(asistenciaRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("DEBE LANZAR EXCEPCION AL ELIMINAR ASISTENCIA QUE NO EXISTE")
    void shouldThrowWhenEliminarNotFound() {
        when(asistenciaRepository.existsById(99L)).thenReturn(false);

        NoSuchElementException ex = assertThrows(NoSuchElementException.class,
                () -> asistenciaService.eliminar(99L));

        assertEquals("Asistencia con id 99 no encontrada", ex.getMessage());
        verify(asistenciaRepository, never()).deleteById(99L);
    }
}