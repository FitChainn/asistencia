package com.fitchain.asistencia.controller;

import com.fitchain.asistencia.dto.AsistenciaRequestDTO;
import com.fitchain.asistencia.dto.AsistenciaResponseDTO;
import com.fitchain.asistencia.service.AsistenciaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "ASISTENCIAS", description = "GESTIÓN DE ASISTENCIAS")
@RestController
@RequestMapping("/v1/asistencias")
@RequiredArgsConstructor
public class AsistenciaController {

    private final AsistenciaService asistenciaService;

    @Operation(summary = "CREAR ASISTENCIA", description = "Registra una nueva asistencia. Acceso: ADMIN, ENTRENADOR")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "ASISTENCIA REGISTRADA EXITOSAMENTE"),
            @ApiResponse(responseCode = "400", description = "DATOS INVÁLIDOS"),
            @ApiResponse(responseCode = "404", description = "CLIENTE O HORARIO NO ENCONTRADO"),
            @ApiResponse(responseCode = "503", description = "MICROSERVICIO NO DISPONIBLE")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'ENTRENADOR')")
    @PostMapping
    public ResponseEntity<AsistenciaResponseDTO> crear(@Valid @RequestBody AsistenciaRequestDTO requestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(asistenciaService.crear(requestDTO));
    }

    @Operation(summary = "OBTENER TODAS LAS ASISTENCIAS", description = "Retorna la lista de todas las asistencias. Acceso: ADMIN, ENTRENADOR")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "LISTA OBTENIDA CON ÉXITO"),
            @ApiResponse(responseCode = "403", description = "SIN PERMISOS SUFICIENTES")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'ENTRENADOR')")
    @GetMapping
    public ResponseEntity<List<AsistenciaResponseDTO>> obtenerTodas() {
        return ResponseEntity.ok(asistenciaService.obtenerTodas());
    }

    @Operation(summary = "OBTENER ASISTENCIA POR ID", description = "Retorna una asistencia específica por su ID. Acceso: ADMIN, ENTRENADOR, CLIENTE")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "ASISTENCIA ENCONTRADA"),
            @ApiResponse(responseCode = "404", description = "ASISTENCIA NO ENCONTRADA")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'ENTRENADOR', 'CLIENTE')")
    @GetMapping("/{id}")
    public ResponseEntity<AsistenciaResponseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(asistenciaService.obtenerPorId(id));
    }

    @Operation(summary = "OBTENER ASISTENCIAS POR CLIENTE", description = "Retorna todas las asistencias de un cliente. Acceso: ADMIN, ENTRENADOR, CLIENTE")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "LISTA OBTENIDA CON ÉXITO"),
            @ApiResponse(responseCode = "404", description = "CLIENTE NO ENCONTRADO")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'ENTRENADOR', 'CLIENTE')")
    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<AsistenciaResponseDTO>> obtenerPorCliente(@PathVariable Long clienteId) {
        return ResponseEntity.ok(asistenciaService.obtenerPorCliente(clienteId));
    }

    @Operation(summary = "OBTENER ASISTENCIAS POR HORARIO", description = "Retorna todas las asistencias de un horario. Acceso: ADMIN, ENTRENADOR")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "LISTA OBTENIDA CON ÉXITO"),
            @ApiResponse(responseCode = "404", description = "HORARIO NO ENCONTRADO")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'ENTRENADOR')")
    @GetMapping("/horario/{horarioId}")
    public ResponseEntity<List<AsistenciaResponseDTO>> obtenerPorHorario(@PathVariable Long horarioId) {
        return ResponseEntity.ok(asistenciaService.obtenerPorHorario(horarioId));
    }

    @Operation(summary = "ELIMINAR ASISTENCIA", description = "Elimina una asistencia por su ID. Acceso: ADMIN")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "ASISTENCIA ELIMINADA EXITOSAMENTE"),
            @ApiResponse(responseCode = "404", description = "ASISTENCIA NO ENCONTRADA")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        asistenciaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}