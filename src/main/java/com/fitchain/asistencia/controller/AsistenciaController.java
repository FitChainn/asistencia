package com.fitchain.asistencia.controller;

import com.fitchain.asistencia.dto.AsistenciaRequestDTO;
import com.fitchain.asistencia.dto.AsistenciaResponseDTO;
import com.fitchain.asistencia.service.AsistenciaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/asistencias")
@RequiredArgsConstructor
public class AsistenciaController {

    private final AsistenciaService asistenciaService;

    @PostMapping
    public ResponseEntity<AsistenciaResponseDTO> crear(@Valid @RequestBody AsistenciaRequestDTO requestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(asistenciaService.crear(requestDTO));
    }

    @GetMapping
    public ResponseEntity<List<AsistenciaResponseDTO>> obtenerTodas() {
        return ResponseEntity.ok(asistenciaService.obtenerTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AsistenciaResponseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(asistenciaService.obtenerPorId(id));
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<AsistenciaResponseDTO>> obtenerPorCliente(@PathVariable Long clienteId) {
        return ResponseEntity.ok(asistenciaService.obtenerPorCliente(clienteId));
    }

    @GetMapping("/horario/{horarioId}")
    public ResponseEntity<List<AsistenciaResponseDTO>> obtenerPorHorario(@PathVariable Long horarioId) {
        return ResponseEntity.ok(asistenciaService.obtenerPorHorario(horarioId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        asistenciaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
