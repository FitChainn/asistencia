package com.fitchain.asistencia.assembler;

import com.fitchain.asistencia.controller.AsistenciaController;
import com.fitchain.asistencia.dto.AsistenciaResponseDTO;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class AsistenciaModelAssembler implements RepresentationModelAssembler<AsistenciaResponseDTO, EntityModel<AsistenciaResponseDTO>> {

    @Override
    public EntityModel<AsistenciaResponseDTO> toModel(AsistenciaResponseDTO dto) {
        Long clienteId = dto.getCliente().getId();
        Long horarioId = dto.getHorario().getId();
        return EntityModel.of(dto,
                linkTo(methodOn(AsistenciaController.class).obtenerPorId(dto.getId())).withSelfRel(),
                linkTo(methodOn(AsistenciaController.class).obtenerTodas()).withRel("asistencias"),
                linkTo(methodOn(AsistenciaController.class).obtenerPorCliente(clienteId)).withRel("asistencias-por-cliente"),
                linkTo(methodOn(AsistenciaController.class).obtenerPorHorario(horarioId)).withRel("asistencias-por-horario")
        );
    }
}