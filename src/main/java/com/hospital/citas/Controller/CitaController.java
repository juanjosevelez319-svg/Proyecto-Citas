package com.hospital.citas.Controller;

import org.springframework.security.core.Authentication;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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



    @GetMapping
    public String listar(Model model) {

        model.addAttribute("citas", citaService.listarTodas());

       return "ListaCitas";

    }

    @GetMapping("/nuevaCita")
    public String formulario(Model model) {

        model.addAttribute("cita", new Citas());

        model.addAttribute("usuarios", usuarioService.listarTodos());

        model.addAttribute("medicos", medicoService.listarTodas());

        model.addAttribute("horarios", horarioService.listarHorarios());

        return "FormularioCitas";

    }

    @PostMapping("/guardarCita")
    public String guardar(@ModelAttribute Citas cita, Model model, Authentication authentication) {

         try {

        Usuario usuario = usuarioService.obtenerPorCorreo(authentication.getName());

        // Si no es administrador, la cita siempre será para él mismo
        if (!"ROLE_ADMIN".equals(usuario.getRol())) {
            cita.setUsuario(usuario);
        }

        citaService.guardar(cita);

        return "redirect:/citas";

    } catch (RuntimeException e) {

        model.addAttribute("error", e.getMessage());

        model.addAttribute("cita", cita);
        model.addAttribute("usuarios", usuarioService.listarTodos());
        model.addAttribute("medicos", medicoService.listarTodas());
        model.addAttribute("horarios", horarioService.listarHorarios());

        return "FormularioCitas";
    }

    }

    @GetMapping("/confirmarCita/{id}")
    public String confirmar(@PathVariable Long id) {

        citaService.confirmar(id);

        return "redirect:/citas";

    }

    @GetMapping("/cancelarCita/{id}")
    public String cancelar(@PathVariable Long id, Authentication authentication) {

        Usuario usuario = usuarioService.obtenerPorCorreo(authentication.getName());

        citaService.cancelar(id, usuario);

        return "redirect:/citas";

    }

    @GetMapping("/mis-citas")
    public String misCitas(Authentication authentication, Model model) {

    Usuario usuario = usuarioService.obtenerPorCorreo(authentication.getName());

    model.addAttribute(
            "citas",
            citaService.obtenerPorUsuario(usuario));

    return "MisCitas";
    }

   @GetMapping("/horarios/{medicoId}")
   @ResponseBody
   public List<Horarios> obtenerHorarios(@PathVariable Long medicoId) {

   return horarioService.obtenerPorMedico(medicoId);

   }

}