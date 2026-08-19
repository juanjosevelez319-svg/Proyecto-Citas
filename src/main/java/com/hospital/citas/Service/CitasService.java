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

        boolean ocupado = citaRepository.existsByHorarioAndEstadoIn(
                horario,
                Arrays.asList(
                        EstadoCita.PENDIENTE,
                        EstadoCita.CONFIRMADA));

        if (ocupado) {
            throw new RuntimeException("El horario ya fue reservado.");
        }

        horario.setDisponible(false);

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
