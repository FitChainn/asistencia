package com.fitchain.asistencia.config;

import com.fitchain.asistencia.model.Asistencia;
import com.fitchain.asistencia.repository.AsistenciaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final AsistenciaRepository asistenciaRepository;

    @Override
    public void run(String... args) {
        if (asistenciaRepository.count() == 0) {
            log.info("Cargando datos de prueba para Asistencia...");

            LocalDate hoy = LocalDate.now();

            Asistencia a1 = new Asistencia();
            a1.setClienteId(1L);
            a1.setHorarioId(1L);
            a1.setFecha(hoy);

            Asistencia a2 = new Asistencia();
            a2.setClienteId(2L);
            a2.setHorarioId(1L);
            a2.setFecha(hoy);

            Asistencia a3 = new Asistencia();
            a3.setClienteId(1L);
            a3.setHorarioId(2L);
            a3.setFecha(hoy.minusDays(1));

            Asistencia a4 = new Asistencia();
            a4.setClienteId(3L);
            a4.setHorarioId(2L);
            a4.setFecha(hoy.minusDays(1));

            asistenciaRepository.saveAll(List.of(a1, a2, a3, a4));
            log.info("Datos de prueba cargados: {} asistencias", asistenciaRepository.count());
        } else {
            log.info("Ya existen datos en la base de datos, omitiendo inicialización");
        }
    }
}
