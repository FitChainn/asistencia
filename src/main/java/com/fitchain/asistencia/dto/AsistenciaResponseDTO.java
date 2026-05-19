package com.fitchain.asistencia.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class AsistenciaResponseDTO {

    private Long id;
    private LocalDate fecha;

    private ClienteDTO cliente;
    private HorarioDTO horario;
}
