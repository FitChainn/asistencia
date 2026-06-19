package com.fitchain.asistencia;

import com.fitchain.asistencia.model.Asistencia;
import com.fitchain.asistencia.repository.AsistenciaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@TestPropertySource(properties = "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect")
@DisplayName("PRUEBAS UNITARIAS DEL REPOSITORY DE ASISTENCIA")
public class AsistenciaRepositoryTest {

    @Autowired
    private AsistenciaRepository repo;

    @Autowired
    private TestEntityManager em;

    @BeforeEach
    void limpiarBd() {
        repo.deleteAll();
        em.flush();
    }

    private Asistencia crearAsistencia(Long clienteId, Long horarioId, LocalDate fecha) {
        Asistencia a = new Asistencia();
        a.setClienteId(clienteId);
        a.setHorarioId(horarioId);
        a.setFecha(fecha);
        return em.persistAndFlush(a);
    }

    @Test
    @DisplayName("DEBE ENCONTRAR UNA ASISTENCIA POR ID")
    void findById_ShouldReturnAsistencia() {
        Asistencia a = crearAsistencia(1L, 1L, LocalDate.now());

        Optional<Asistencia> result = repo.findById(a.getId());

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getClienteId());
    }

    @Test
    @DisplayName("DEBE RETORNAR VACIO SI ASISTENCIA NO EXISTE")
    void findById_ShouldReturnEmpty() {
        Optional<Asistencia> result = repo.findById(999L);
        assertFalse(result.isPresent());
    }

    @Test
    @DisplayName("DEBE ENCONTRAR TODAS LAS ASISTENCIAS")
    void findAll_ShouldReturnAllAsistencias() {
        crearAsistencia(1L, 1L, LocalDate.now());
        crearAsistencia(2L, 1L, LocalDate.now());

        List<Asistencia> lista = repo.findAll();

        assertFalse(lista.isEmpty());
        assertTrue(lista.size() >= 2);
    }

    @Test
    @DisplayName("DEBE GUARDAR UNA ASISTENCIA")
    void save_ShouldPersistAsistencia() {
        Asistencia a = new Asistencia();
        a.setClienteId(3L);
        a.setHorarioId(2L);
        a.setFecha(LocalDate.now());

        Asistencia saved = repo.save(a);

        assertNotNull(saved.getId());
        assertEquals(3L, saved.getClienteId());
    }

    @Test
    @DisplayName("DEBE ENCONTRAR ASISTENCIAS POR CLIENTE")
    void findByClienteId_ShouldReturnAsistencias() {
        crearAsistencia(5L, 1L, LocalDate.now());
        crearAsistencia(5L, 2L, LocalDate.now());
        crearAsistencia(6L, 1L, LocalDate.now());

        List<Asistencia> result = repo.findByClienteId(5L);

        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(a -> a.getClienteId().equals(5L)));
    }

    @Test
    @DisplayName("DEBE RETORNAR LISTA VACIA SI CLIENTE NO TIENE ASISTENCIAS")
    void findByClienteId_ShouldReturnEmpty() {
        List<Asistencia> result = repo.findByClienteId(999L);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("DEBE ENCONTRAR ASISTENCIAS POR HORARIO")
    void findByHorarioId_ShouldReturnAsistencias() {
        crearAsistencia(1L, 7L, LocalDate.now());
        crearAsistencia(2L, 7L, LocalDate.now());
        crearAsistencia(3L, 8L, LocalDate.now());

        List<Asistencia> result = repo.findByHorarioId(7L);

        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(a -> a.getHorarioId().equals(7L)));
    }

    @Test
    @DisplayName("DEBE RETORNAR LISTA VACIA SI HORARIO NO TIENE ASISTENCIAS")
    void findByHorarioId_ShouldReturnEmpty() {
        List<Asistencia> result = repo.findByHorarioId(999L);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("DEBE ELIMINAR UNA ASISTENCIA")
    void delete_ShouldRemoveAsistencia() {
        Asistencia a = crearAsistencia(1L, 1L, LocalDate.now());
        Long id = a.getId();

        repo.deleteById(id);
        em.flush();

        assertFalse(repo.findById(id).isPresent());
    }
}