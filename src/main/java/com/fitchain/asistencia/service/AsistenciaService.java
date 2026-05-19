package com.fitchain.asistencia.service;

import com.fitchain.asistencia.WebClient.ClienteClient;
import com.fitchain.asistencia.WebClient.HorarioClient;
import com.fitchain.asistencia.dto.AsistenciaRequestDTO;
import com.fitchain.asistencia.dto.AsistenciaResponseDTO;
import com.fitchain.asistencia.dto.ClienteDTO;
import com.fitchain.asistencia.dto.HorarioDTO;
import com.fitchain.asistencia.model.Asistencia;
import com.fitchain.asistencia.repository.AsistenciaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@Service
@RequiredArgsConstructor
public class AsistenciaService {

    private final AsistenciaRepository asistenciaRepository;
    private final ClienteClient clienteClient;
    private final HorarioClient horarioClient;

    private AsistenciaResponseDTO toResponseDTO(Asistencia asistencia, ClienteDTO cliente, HorarioDTO horario) {
        AsistenciaResponseDTO dto = new AsistenciaResponseDTO();
        dto.setId(asistencia.getId());
        dto.setFecha(asistencia.getFecha());
        dto.setCliente(cliente);
        dto.setHorario(horario);
        return dto;
    }

    public AsistenciaResponseDTO crear(AsistenciaRequestDTO requestDTO) {
        log.info("Registrando asistencia para clienteId {}", requestDTO.getClienteId());

        ClienteDTO cliente = clienteClient.obtenerClientePorId(requestDTO.getClienteId());
        HorarioDTO horario = horarioClient.obtenerHorarioPorId(requestDTO.getHorarioId());

        Asistencia asistencia = new Asistencia();
        asistencia.setClienteId(requestDTO.getClienteId());
        asistencia.setHorarioId(requestDTO.getHorarioId());
        asistencia.setFecha(requestDTO.getFecha());

        Asistencia guardada = asistenciaRepository.save(asistencia);
        log.info("Asistencia registrada con id {}", guardada.getId());
        return toResponseDTO(guardada, cliente, horario);
    }

    public List<AsistenciaResponseDTO> obtenerTodas() {
        log.info("Obteniendo todas las asistencias");
        return asistenciaRepository.findAll().stream()
                .map(a -> toResponseDTO(a,
                        clienteClient.obtenerClientePorId(a.getClienteId()),
                        horarioClient.obtenerHorarioPorId(a.getHorarioId())))
                .toList();
    }

    public AsistenciaResponseDTO obtenerPorId(Long id) {
        log.info("Buscando asistencia con id {}", id);
        Asistencia asistencia = asistenciaRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Asistencia con id " + id + " no encontrada"));
        return toResponseDTO(asistencia,
                clienteClient.obtenerClientePorId(asistencia.getClienteId()),
                horarioClient.obtenerHorarioPorId(asistencia.getHorarioId()));
    }

    public List<AsistenciaResponseDTO> obtenerPorCliente(Long clienteId) {
        log.info("Buscando asistencias del cliente {}", clienteId);
        ClienteDTO cliente = clienteClient.obtenerClientePorId(clienteId);
        return asistenciaRepository.findByClienteId(clienteId).stream()
                .map(a -> toResponseDTO(a, cliente, horarioClient.obtenerHorarioPorId(a.getHorarioId())))
                .toList();
    }

    public List<AsistenciaResponseDTO> obtenerPorHorario(Long horarioId) {
        log.info("Buscando asistencias del horario {}", horarioId);
        HorarioDTO horario = horarioClient.obtenerHorarioPorId(horarioId);
        return asistenciaRepository.findByHorarioId(horarioId).stream()
                .map(a -> toResponseDTO(a, clienteClient.obtenerClientePorId(a.getClienteId()), horario))
                .toList();
    }

    public void eliminar(Long id) {
        log.info("Eliminando asistencia con id {}", id);
        if (!asistenciaRepository.existsById(id)) {
            throw new NoSuchElementException("Asistencia con id " + id + " no encontrada");
        }
        asistenciaRepository.deleteById(id);
        log.info("Asistencia {} eliminada", id);
    }
}
