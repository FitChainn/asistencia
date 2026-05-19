package com.fitchain.asistencia.repository;

import com.fitchain.asistencia.model.Asistencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AsistenciaRepository extends JpaRepository<Asistencia, Long> {

    List<Asistencia> findByClienteId(Long clienteId);

    List<Asistencia> findByHorarioId(Long horarioId);
}
