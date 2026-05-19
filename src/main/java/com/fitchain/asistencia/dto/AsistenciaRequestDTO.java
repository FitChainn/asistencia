package com.fitchain.asistencia.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class AsistenciaRequestDTO {

    @NotNull(message = "El clienteId es obligatorio")
    private Long clienteId;

    @NotNull(message = "El horarioId es obligatorio")
    private Long horarioId;

    @NotNull(message = "La fecha es obligatoria")
    private LocalDate fecha;
}
