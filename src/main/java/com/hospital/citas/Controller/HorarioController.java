package com.hospital.citas.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;


import com.hospital.citas.Model.Horarios;
import com.hospital.citas.Service.HorarioService;
import com.hospital.citas.Service.MedicoService;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hospital.citas.Model.Medico;
@Controller

public class HorarioController {
    @Autowired
    private HorarioService horarioService;

    @Autowired
    private MedicoService medicoService;

    @GetMapping("/horarios")
    public String listarHorarios(Model model) {
        model.addAttribute("horarios", horarioService.listarHorarios());
        return "ListaHorario";
    }

    // Mostrar formulario
    @GetMapping("/nuevoHorario")
    public String nuevoHorario(Model model) {

        model.addAttribute("horario", new Horarios());

        // Lista de médicos
        model.addAttribute("medicos",medicoService.listarTodas());

        return "FormularioHorario";
    }

    // Guardar
    @PostMapping("/guardarHorario")
public String guardarHorario(
        @ModelAttribute Horarios horario,
        @RequestParam("medico") Long medicoId,
        RedirectAttributes redirectAttributes) {

    try {

        Medico medico = medicoService.obtenerPorId(medicoId);

        horario.setMedico(medico);

        horarioService.guardarHorario(horario);

        redirectAttributes.addFlashAttribute(
            "mensaje",
            "Horario guardado correctamente."
        );

        return "redirect:/horarios";

    } catch (IllegalArgumentException e) {

        redirectAttributes.addFlashAttribute(
            "error",
            e.getMessage()
        );

        return "redirect:/nuevoHorario";
    }
}
    // Editar
    @GetMapping("/editarHorario/{id}")
    public String editarHorario(@PathVariable Long id,Model model) {

        model.addAttribute("horario", horarioService.buscarHorario(id));

        model.addAttribute("medicos", medicoService.listarTodas());

        return "FormularioHorario";
    }

    // Eliminar
    @GetMapping("/eliminarHorario/{id}")
    public String eliminarHorario(@PathVariable Long id, RedirectAttributes redirectAttributes) {

        try {

        horarioService.eliminarHorario(id);

        redirectAttributes.addFlashAttribute(
            "mensaje",
            "Horario eliminado correctamente."
        );

    } catch (IllegalArgumentException e) {

        redirectAttributes.addFlashAttribute(
            "error",
            e.getMessage()
        );
    }

    return "redirect:/horarios";
    }

    @GetMapping("/medico/{id}")
       public String verHorariosMedico(@PathVariable Long id,
                                Model model){

         model.addAttribute("horarios",
            horarioService.obtenerPorMedico(id));

          model.addAttribute("medico",
            medicoService.obtenerPorId(id));

      return "ListaHorario";

    }
}