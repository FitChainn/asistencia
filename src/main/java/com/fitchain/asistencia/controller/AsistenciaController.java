package com.fitchain.asistencia.controller;

import com.fitchain.asistencia.assembler.AsistenciaModelAssembler;
import com.fitchain.asistencia.dto.AsistenciaRequestDTO;
import com.fitchain.asistencia.dto.AsistenciaResponseDTO;
import com.fitchain.asistencia.service.AsistenciaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Slf4j
@Tag(name = "ASISTENCIAS", description = "GESTIÓN DE ASISTENCIAS")
@RestController
@RequestMapping("/v1/asistencias")
@RequiredArgsConstructor
public class AsistenciaController {

    private final AsistenciaService asistenciaService;
    private final AsistenciaModelAssembler assembler;

    @Operation(summary = "CREAR ASISTENCIA", description = "Registra una nueva asistencia. Acceso: ADMIN, ENTRENADOR")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "ASISTENCIA REGISTRADA EXITOSAMENTE"),
            @ApiResponse(responseCode = "400", description = "DATOS INVÁLIDOS"),
            @ApiResponse(responseCode = "404", description = "CLIENTE O HORARIO NO ENCONTRADO"),
            @ApiResponse(responseCode = "503", description = "MICROSERVICIO NO DISPONIBLE")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'ENTRENADOR')")
    @PostMapping
    public ResponseEntity<EntityModel<AsistenciaResponseDTO>> crear(@Valid @RequestBody AsistenciaRequestDTO requestDTO) {
        log.info("POST /v1/asistencias - REGISTRAR ASISTENCIA clienteId={}", requestDTO.getClienteId());
        AsistenciaResponseDTO creada = asistenciaService.crear(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(assembler.toModel(creada));
    }

    @Operation(summary = "OBTENER TODAS LAS ASISTENCIAS", description = "Retorna la lista de todas las asistencias. Acceso: ADMIN, ENTRENADOR")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "LISTA OBTENIDA CON ÉXITO"),
            @ApiResponse(responseCode = "403", description = "SIN PERMISOS SUFICIENTES")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'ENTRENADOR')")
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<AsistenciaResponseDTO>>> obtenerTodas() {
        log.info("GET /v1/asistencias - LISTAR TODAS");
        List<EntityModel<AsistenciaResponseDTO>> asistencias = asistenciaService.obtenerTodas().stream()
                .map(assembler::toModel)
                .toList();
        return ResponseEntity.ok(CollectionModel.of(asistencias,
                linkTo(methodOn(AsistenciaController.class).obtenerTodas()).withSelfRel()));
    }

    @Operation(summary = "OBTENER ASISTENCIA POR ID", description = "Retorna una asistencia específica por su ID. Acceso: ADMIN, ENTRENADOR, CLIENTE")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "ASISTENCIA ENCONTRADA"),
            @ApiResponse(responseCode = "404", description = "ASISTENCIA NO ENCONTRADA")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'ENTRENADOR', 'CLIENTE')")
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<AsistenciaResponseDTO>> obtenerPorId(@PathVariable Long id) {
        log.info("GET /v1/asistencias/{} - BUSCAR POR ID", id);
        return ResponseEntity.ok(assembler.toModel(asistenciaService.obtenerPorId(id)));
    }

    @Operation(summary = "OBTENER ASISTENCIAS POR CLIENTE", description = "Retorna todas las asistencias de un cliente. Acceso: ADMIN, ENTRENADOR, CLIENTE")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "LISTA OBTENIDA CON ÉXITO"),
            @ApiResponse(responseCode = "404", description = "CLIENTE NO ENCONTRADO")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'ENTRENADOR', 'CLIENTE')")
    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<CollectionModel<EntityModel<AsistenciaResponseDTO>>> obtenerPorCliente(@PathVariable Long clienteId) {
        log.info("GET /v1/asistencias/cliente/{} - BUSCAR POR CLIENTE", clienteId);
        List<EntityModel<AsistenciaResponseDTO>> asistencias = asistenciaService.obtenerPorCliente(clienteId).stream()
                .map(assembler::toModel)
                .toList();
        return ResponseEntity.ok(CollectionModel.of(asistencias,
                linkTo(methodOn(AsistenciaController.class).obtenerPorCliente(clienteId)).withSelfRel()));
    }

    @Operation(summary = "OBTENER ASISTENCIAS POR HORARIO", description = "Retorna todas las asistencias de un horario. Acceso: ADMIN, ENTRENADOR")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "LISTA OBTENIDA CON ÉXITO"),
            @ApiResponse(responseCode = "404", description = "HORARIO NO ENCONTRADO")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'ENTRENADOR')")
    @GetMapping("/horario/{horarioId}")
    public ResponseEntity<CollectionModel<EntityModel<AsistenciaResponseDTO>>> obtenerPorHorario(@PathVariable Long horarioId) {
        log.info("GET /v1/asistencias/horario/{} - BUSCAR POR HORARIO", horarioId);
        List<EntityModel<AsistenciaResponseDTO>> asistencias = asistenciaService.obtenerPorHorario(horarioId).stream()
                .map(assembler::toModel)
                .toList();
        return ResponseEntity.ok(CollectionModel.of(asistencias,
                linkTo(methodOn(AsistenciaController.class).obtenerPorHorario(horarioId)).withSelfRel()));
    }

    @Operation(summary = "ELIMINAR ASISTENCIA", description = "Elimina una asistencia por su ID. Acceso: ADMIN")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "ASISTENCIA ELIMINADA EXITOSAMENTE"),
            @ApiResponse(responseCode = "404", description = "ASISTENCIA NO ENCONTRADA")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<EntityModel<AsistenciaResponseDTO>> eliminar(@PathVariable Long id) {
        log.info("DELETE /v1/asistencias/{} - ELIMINAR ASISTENCIA", id);
        AsistenciaResponseDTO asistencia = asistenciaService.obtenerPorId(id);
        asistenciaService.eliminar(id);
        return ResponseEntity.ok(assembler.toModel(asistencia));
    }
}