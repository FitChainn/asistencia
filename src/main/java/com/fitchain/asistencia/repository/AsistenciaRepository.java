package com.fitchain.asistencia.repository;

import com.fitchain.asistencia.model.Asistencia;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;


public interface AsistenciaRepository extends JpaRepository<Asistencia, Long> {

    List<Asistencia> findByClienteId(Long clienteId);

    List<Asistencia> findByHorarioId(Long horarioId);
}
