package com.hospital.citas.Controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.hospital.citas.Model.Usuario;
import com.hospital.citas.Service.UsuariosService;

import java.util.Collections;

@Controller
@RequestMapping("/usuarios")
public class UsuariosController {

    @Autowired
    private UsuariosService usuariosService;

    @GetMapping
      public String listarUsuarios(Model model, Principal principal) {

      Usuario usuario = usuariosService.obtenerPorCorreo(principal.getName());

     if (usuario.getRol().equals("ROLE_ADMIN")) {

        model.addAttribute("usuarios", usuariosService.listarTodos());

     } else {

        model.addAttribute("usuarios",
                Collections.singletonList(usuario));

     }

     return "Listausuarios";
    }



    @GetMapping("/nuevo")
    public String mostrarFormulario(Model model) {

        model.addAttribute("usuario", new Usuario());

        return "Formulariousuario";
    }

    @PostMapping("/guardar")
    public String guardarUsuario(@ModelAttribute Usuario usuario) {

        usuariosService.guardarUsuario(usuario);

        return "redirect:/usuarios";
    }

@GetMapping("/editar/{id}")
     public String editarUsuario(@PathVariable Long id, Model model, Principal principal) {

     Usuario usuarioLogueado = usuariosService.obtenerPorCorreo(principal.getName());

     if (!usuarioLogueado.getRol().equals("ROLE_ADMIN")
            && !usuarioLogueado.getId().equals(id)) {

        return "redirect:/usuarios";
      } 

      model.addAttribute("usuario", usuariosService.obtenerPorId(id));

     return "Formulariousuario";
    }


    @GetMapping("/eliminar/{id}")
     public String eliminarUsuario(@PathVariable Long id,
                              Principal principal) {

      Usuario usuarioLogueado = usuariosService.obtenerPorCorreo(principal.getName());

     if (!usuarioLogueado.getRol().equals("ROLE_ADMIN")) {
        return "redirect:/usuarios";
      }

      usuariosService.eliminar(id);

    return "redirect:/usuarios";
    }
}