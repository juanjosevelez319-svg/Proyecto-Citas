package com.hospital.citas.Service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hospital.citas.Model.Citas;
import com.hospital.citas.Repository.CitaRepository;

@Service
public class ReporteService {

    @Autowired
    private CitaRepository citaRepository;


    public List<Citas> buscarCitas(
            LocalDate fechaInicio,
            LocalDate fechaFin,
            Long medicoId,
            String especialidad,
            Citas.EstadoCita estado) {


        // Validar rango de fechas
        if (fechaInicio != null &&
            fechaFin != null &&
            fechaInicio.isAfter(fechaFin)) {

            throw new IllegalArgumentException(
                "La fecha inicial no puede ser posterior a la fecha final."
            );
        }


        return citaRepository.buscarParaReporte(
                fechaInicio,
                fechaFin,
                medicoId,
                especialidad,
                estado
        );
    }
}
