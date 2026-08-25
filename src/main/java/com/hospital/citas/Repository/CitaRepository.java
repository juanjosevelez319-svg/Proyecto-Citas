package com.hospital.citas.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hospital.citas.Model.Citas;
import com.hospital.citas.Model.Citas.EstadoCita;
import com.hospital.citas.Model.Horarios;
import com.hospital.citas.Model.Usuario;

public interface CitaRepository extends JpaRepository<Citas, Long> {

    Optional<Citas> findByHorario(Horarios horario);

    List<Citas> findByUsuario(Usuario usuario);

    List<Citas> findByMedicoId(Long idMedico);

    List<Citas> findByUsuarioAndEstadoIn(
            Usuario usuario,
            List<Citas.EstadoCita> estados
    );

    List<Citas> findByUsuarioId(Long idUsuario);

    List<Citas> findByEstado(EstadoCita estado);

    boolean existsByHorarioAndEstadoIn(
            Horarios horario,
            List<Citas.EstadoCita> estados
    );

    Optional<Citas> findByHorarioAndEstadoNot(
            Horarios horario,
            Citas.EstadoCita estado
    );
    // Método para verificar si existe una cita con un horario específico
    boolean existsByHorario(Horarios horario);

    // Query para buscar citas con filtros opcionales
    @Query("""
    SELECT c
    FROM Citas c
    WHERE
        (:fechaInicio IS NULL OR c.horario.fecha >= :fechaInicio)
        AND
        (:fechaFin IS NULL OR c.horario.fecha <= :fechaFin)
        AND
        (:medicoId IS NULL OR c.medico.id = :medicoId)
        AND
        (:especialidad IS NULL OR :especialidad = ''
             OR c.medico.especialidad = :especialidad)
        AND
        (:estado IS NULL OR c.estado = :estado)
        ORDER BY c.horario.fecha ASC, c.horario.horaInicio ASC
    """)
       List<Citas> buscarParaReporte(
        @Param("fechaInicio") LocalDate fechaInicio,
        @Param("fechaFin") LocalDate fechaFin,
        @Param("medicoId") Long medicoId,
        @Param("especialidad") String especialidad,
        @Param("estado") EstadoCita estado
);


}
