package com.hospital.citas.Controller;

import org.springframework.security.core.Authentication;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hospital.citas.Model.Citas;
import com.hospital.citas.Model.Horarios;
import com.hospital.citas.Model.Usuario;
import com.hospital.citas.Service.CitasService;
import com.hospital.citas.Service.HorarioService;
import com.hospital.citas.Service.MedicoService;
import com.hospital.citas.Service.UsuariosService;

@Controller
@RequestMapping("/citas")
public class CitaController {

    @Autowired
    private CitasService citaService;
    @Autowired
    private UsuariosService usuarioService;
    @Autowired
    private MedicoService medicoService;
    @Autowired
    private HorarioService horarioService;

    public CitaController( CitasService citaService,
            UsuariosService usuarioService,
            MedicoService medicoService,
            HorarioService horarioService) {

        this.citaService = citaService;
        this.usuarioService = usuarioService;
        this.medicoService = medicoService;
        this.horarioService = horarioService;

    }

    // =========================
    // LISTAR CITAS
    // =========================

    @GetMapping
    public String listarCitas(Model model) {

        model.addAttribute(
            "citas",
            citaService.listarTodas()
        );

        return "ListaCitas";
    }


    // =========================
    // NUEVA CITA
    // =========================

    @GetMapping("/nuevaCita")
    public String nuevaCita(Model model) {

        model.addAttribute(
            "cita",
            new Citas()
        );

        model.addAttribute(
            "horarios",
            horarioService.listarHorarios()
        );

        model.addAttribute(
            "medicos",
            medicoService.listarTodas()
        );

        model.addAttribute(
            "usuarios",
             usuarioService.listarTodos()
        );

        return "FormularioCitas";
    }


    // =========================
    // GUARDAR CITA
    // =========================

    @PostMapping("/guardarCita")
    public String guardar(
        @ModelAttribute Citas cita,
        Authentication authentication,
        RedirectAttributes redirectAttributes) {

    try {

        // Usuario actualmente autenticado
        Usuario usuarioActual =
            usuarioService.obtenerPorCorreo(
                authentication.getName()
            );

        /*
         * Si el usuario es ADMIN:
         * se utiliza el usuario seleccionado
         * en el formulario.
         */
        if (usuarioActual.getRol().equals("ROLE_ADMIN")) {

            if (cita.getUsuario() == null ||
                cita.getUsuario().getId() == null) {

                throw new IllegalArgumentException(
                    "Debe seleccionar un usuario."
                );
            }

            Usuario usuarioSeleccionado =
                usuarioService.obtenerPorId(
                    cita.getUsuario().getId()
                );

            if (usuarioSeleccionado == null) {

                throw new IllegalArgumentException(
                    "El usuario seleccionado no existe."
                );
            }

            cita.setUsuario(usuarioSeleccionado);

        } else {

            /*
             * Los usuarios normales solamente
             * pueden crear citas para ellos mismos.
             */
            cita.setUsuario(usuarioActual);
        }

        // Guardar aplicando las validaciones del Service
        citaService.guardar(cita);

        redirectAttributes.addFlashAttribute(
            "mensaje",
            "Cita creada correctamente."
        );

        return "redirect:/citas";

    } catch (IllegalArgumentException e) {

        redirectAttributes.addFlashAttribute(
            "error",
            e.getMessage()
        );

        return "redirect:/citas/nuevaCita";
    }
    }


    // =========================
    // CONFIRMAR CITA
    // =========================

    @GetMapping("/confirmarCita/{id}")
    public String confirmar(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {

        try {

            citaService.confirmar(id);

            redirectAttributes.addFlashAttribute(
                "mensaje",
                "Cita confirmada correctamente."
            );

        } catch (IllegalArgumentException e) {

            redirectAttributes.addFlashAttribute(
                "error",
                e.getMessage()
            );
        }

        return "redirect:/citas";
    }


    // =========================
    // CANCELAR CITA
    // =========================

    @GetMapping("/cancelarCita/{id}")
    public String cancelar(
            @PathVariable Long id,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {

        try {

            // Obtener usuario autenticado
            Usuario usuario =
                usuarioService.obtenerPorCorreo(
                    authentication.getName()
                );

            boolean cancelada =
                citaService.cancelar(id, usuario);

            if (cancelada) {

                redirectAttributes.addFlashAttribute(
                    "mensaje",
                    "La cita fue cancelada correctamente."
                );

            } else {

                redirectAttributes.addFlashAttribute(
                    "error",
                    "No tiene permiso para cancelar esta cita."
                );
            }

        } catch (IllegalArgumentException e) {

            redirectAttributes.addFlashAttribute(
                "error",
                e.getMessage()
            );
        }

        return "redirect:/citas";
    }

    // Listas citas por usuario
    @GetMapping("/mis-citas")
public String misCitas(Model model, Authentication authentication) {

    Usuario usuario = usuarioService.obtenerPorCorreo(
        authentication.getName()
    );

    model.addAttribute(
        "citas",
        citaService.listarPorUsuario(usuario.getId())
    );

    return "ListaCitas";
}


}