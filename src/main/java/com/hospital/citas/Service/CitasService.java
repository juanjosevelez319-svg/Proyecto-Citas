package com.hospital.citas.Service;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hospital.citas.Model.Citas;
import com.hospital.citas.Model.Citas.EstadoCita;
import com.hospital.citas.Model.Horarios;
import com.hospital.citas.Model.Usuario;
import com.hospital.citas.Repository.CitaRepository;
import com.hospital.citas.Repository.HorarioRepository;

@Service
public class CitasService {
    @Autowired
    private CitaRepository citaRepository = null;

    @Autowired
    private HorarioRepository horarioRepository;

    public List<Citas> listarTodas() {
        return citaRepository.findAll();
    }

    public Optional<Citas> obtenerPorId(Long id) {
        return citaRepository.findById(id);
    }

    public List<Citas> obtenerPorUsuario(Usuario usuario) {
        return citaRepository.findByUsuario(usuario);
    }

    public List<Citas> listarPorMedico(Long idMedico) {
    return citaRepository.findByMedicoId(idMedico);
}

    

   @Transactional
    public void guardar(Citas cita) {

      Horarios horario = cita.getHorario();

      // Validar que exista un horario
       if (horario == null) {
        throw new IllegalArgumentException(
            "Debe seleccionar un horario."
        );
        }

      // Validar que el horario tenga fecha
      if (horario.getFecha() == null) {
        throw new IllegalArgumentException(
            "El horario no tiene una fecha válida."
        );
      }

      // Validar que tenga horas
      if (horario.getHoraInicio() == null ||
        horario.getHoraFin() == null) {

        throw new IllegalArgumentException(
            "El horario no tiene una hora válida."
        );
       }

      // El médico de la cita será siempre
      // el médico dueño del horario
      cita.setMedico(horario.getMedico());

      // Crear fecha y hora exacta de la cita
      LocalDateTime inicioCita =
        LocalDateTime.of(
            horario.getFecha(),
            horario.getHoraInicio()
        );

      LocalDateTime finCita =
        LocalDateTime.of(
            horario.getFecha(),
            horario.getHoraFin()
        );

      // No permitir citas en el pasado
      if (!inicioCita.isAfter(LocalDateTime.now())) {
        throw new IllegalArgumentException(
            "No se pueden crear citas en una fecha u hora pasada."
        );
       }

      // Verificar si el horario ya está ocupado
       boolean ocupado =
        citaRepository.existsByHorarioAndEstadoIn(
            horario,
            Arrays.asList(
                EstadoCita.PENDIENTE,
                EstadoCita.CONFIRMADA
            )
        );

      if (ocupado) {
        throw new IllegalArgumentException(
            "El horario ya fue reservado."
        );
       }

      // Verificar si el usuario tiene otra cita
      // que se solape en ese mismo horario
      List<Citas> citasUsuario =
        citaRepository.findByUsuarioAndEstadoIn(
            cita.getUsuario(),
            Arrays.asList(
                EstadoCita.PENDIENTE,
                EstadoCita.CONFIRMADA
            )
        );

      for (Citas existente : citasUsuario) {

        Horarios horarioExistente = existente.getHorario();

        LocalDateTime inicioExistente =
            LocalDateTime.of(
                horarioExistente.getFecha(),
                horarioExistente.getHoraInicio()
            );

        LocalDateTime finExistente =
            LocalDateTime.of(
                horarioExistente.getFecha(),
                horarioExistente.getHoraFin()
            );

        boolean seSolapan =
            inicioCita.isBefore(finExistente)
            &&
            finCita.isAfter(inicioExistente);

        if (seSolapan) {
            throw new IllegalArgumentException(
                "El usuario ya tiene una cita activa que se solapa con este horario."
            );
        }
    }

    // Marcar horario como ocupado
    horario.setDisponible(false);

    // Toda nueva cita comienza pendiente
    cita.setEstado(EstadoCita.PENDIENTE);

    citaRepository.save(cita);
    }

    

    @Transactional
    public void confirmar(Long id) {

        Citas cita = citaRepository.findById(id).orElseThrow();

        if (cita.getEstado() == EstadoCita.PENDIENTE) {

            cita.setEstado(EstadoCita.CONFIRMADA);

            citaRepository.save(cita);

        }

    }

    @Transactional
    public boolean cancelar(Long id, Usuario usuario) {
        Citas cita = citaRepository.findById(id).orElse(null);
        if (cita == null) {
            return false;
        }

        if (!usuario.getRol().equals("ROLE_ADMIN")) {
        if (!cita.getUsuario().getId().equals(usuario.getId())) {
            return false;
        }
    }

        // Cambiar estado de la cita
        cita.setEstado(EstadoCita.CANCELADA);
        citaRepository.save(cita);

        boolean ocupado = citaRepository.existsByHorarioAndEstadoIn(
        cita.getHorario(),
        Arrays.asList(
                EstadoCita.PENDIENTE,
                EstadoCita.CONFIRMADA));

       if (!ocupado) {

         Horarios horario = cita.getHorario();
         horario.setDisponible(true);

         horarioRepository.save(horario);

      }
        return true;
    }
}
