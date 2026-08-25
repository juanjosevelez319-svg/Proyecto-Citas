package com.hospital.citas.Service;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hospital.citas.Model.Horarios;
import com.hospital.citas.Repository.CitaRepository;
import com.hospital.citas.Repository.HorarioRepository;

@Service
public class HorarioService {
    @Autowired
    private HorarioRepository horarioRepository;

    @Autowired
    private CitaRepository citaRepository;

    // Método para listar todos los horarios
    public List<Horarios> listarHorarios() {
        return horarioRepository.findAll();
    }
    
    // Método para listar los horarios de un médico específico
    public List<Horarios> obtenerPorMedico(Long medicoId) {
    return horarioRepository.findByMedicoId(medicoId);
    }

    public void guardarHorario(Horarios horario) {

    // Calculamos automáticamente el día de la semana
    if (horario.getFecha() != null) {
        horario.setDiaSemana(
            horario.getFecha().getDayOfWeek()
        );
    }

    // Validar que la hora de inicio sea menor que la hora final
    if (!horario.getHoraInicio().isBefore(horario.getHoraFin())) {
        throw new IllegalArgumentException(
            "La hora de inicio debe ser anterior a la hora de finalización."
        );
    }

    // Buscar horarios del mismo médico en la misma fecha
    List<Horarios> horarios =
        horarioRepository.findByMedicoAndFecha(
            horario.getMedico(),
            horario.getFecha()
        );

    // Comprobar si existe solapamiento
    for (Horarios existente : horarios) {

        // Si estamos editando, ignoramos el propio horario
        if (horario.getIdHorario() != null &&
            horario.getIdHorario().equals(existente.getIdHorario())) {
            continue;
        }

        boolean seSolapan =
            horario.getHoraInicio().isBefore(existente.getHoraFin())
            &&
            horario.getHoraFin().isAfter(existente.getHoraInicio());

        if (seSolapan) {
            throw new IllegalArgumentException(
                "El médico ya tiene un horario que se solapa con ese horario."
            );
        }
    }

    horarioRepository.save(horario);
    }

    public Horarios buscarHorario(Long id) {
        return horarioRepository.findById(id)
                .orElse(null);
    }

    public void eliminarHorario(Long id) {
        Horarios horario = horarioRepository.findById(id)
            .orElseThrow(() ->
                new IllegalArgumentException(
                    "El horario no existe."
                )
            );

    boolean tieneCitas =
            citaRepository.existsByHorario(horario);

    if (tieneCitas) {

        throw new IllegalArgumentException(
            "No se puede eliminar este horario porque tiene una cita asociada."
        );
    }

    horarioRepository.delete(horario);
    }
}