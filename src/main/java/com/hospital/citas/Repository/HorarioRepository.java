package com.hospital.citas.Repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hospital.citas.Model.Horarios;
import com.hospital.citas.Model.Medico;

public interface HorarioRepository extends JpaRepository<Horarios, Long> {

    List<Horarios> findByMedicoId(Long medicoId);

    List<Horarios> findByMedicoAndFecha(
            Medico medico,
            LocalDate fecha
    );
}