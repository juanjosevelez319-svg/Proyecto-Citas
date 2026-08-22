package com.hospital.citas.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

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

    List<Citas> findByEstado(EstadoCita estado);

    boolean existsByHorarioAndEstadoIn(
            Horarios horario,
            List<Citas.EstadoCita> estados
    );

    Optional<Citas> findByHorarioAndEstadoNot(
            Horarios horario,
            Citas.EstadoCita estado
    );

}
