package com.hospital.citas.Controller;


import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model; 
import org.springframework.web.bind.annotation.GetMapping; 
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping; 

import com.hospital.citas.Model.Medico;
import com.hospital.citas.Service.MedicoService;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;



@Controller
@RequestMapping("/medico/")
public class MedicoController {
    
    @Autowired
    private MedicoService MedicoService;


    @GetMapping
    public String listarMedicos(Model model){
        model.addAttribute("medicos", MedicoService.listarTodas());
        return "ListaMedico";
    }

    @PostMapping("/guardar")
    public String guardarMedico(@ModelAttribute Medico medico) {
        MedicoService.guardarMedico(medico);
        
        return "redirect:/medico/";
    }
    
    @GetMapping("/nueva")
    public String mostrarFormulario(Model model){
        model.addAttribute("medico", new Medico());
        return "FormularioMedico";
    }

    //editar 
    @GetMapping("/editar/{id}")
    public String editarMedico(@PathVariable Long id, Model model){
        model.addAttribute("medico", MedicoService.obtenerPorId(id));
        return "FormularioMedico";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarMedico(@PathVariable Long id, Model model){
        MedicoService.eliminar(id);
        return "redirect:/medico/";
    }
    
}
