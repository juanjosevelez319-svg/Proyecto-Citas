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
     // Verificar si el usuario logueado es administrador o si está editando su propio perfil
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
    // Verificar si el usuario logueado es administrador
    // Si no es administrador, redirigir a la lista de usuarios
     if (!usuarioLogueado.getRol().equals("ROLE_ADMIN")) {
        return "redirect:/usuarios";
      }

      usuariosService.eliminar(id);

    return "redirect:/usuarios";
    }

    @GetMapping("/recuperar")
public String mostrarRecuperar() {

    return "RecuperarPassword";
}

@PostMapping("/recuperar")
public String recuperarPassword(
        @RequestParam String correo,
        Model model) {

    String nuevaPassword =
            usuariosService.recuperarPassword(correo);
     // Si la nueva contraseña es null, significa que el correo no existe
    if (nuevaPassword == null) {

        model.addAttribute(
                "error",
                "El correo no existe."
        );

        return "RecuperarPassword";
    }

    model.addAttribute(
            "mensaje",
            "Su nueva contraseña es: " + nuevaPassword
    );

    return "RecuperarPassword";
}


}