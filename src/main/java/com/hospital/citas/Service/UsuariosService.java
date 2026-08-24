package com.hospital.citas.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.hospital.citas.Model.Usuario;
import com.hospital.citas.Repository.UsuariosRepository;

@Service
public class UsuariosService {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UsuariosRepository usuariosRepository;
    

     public List<Usuario> listarTodos() {
        return usuariosRepository.findAll();
    }

    public Usuario guardarUsuario(Usuario usuario) {

    // NUEVO USUARIO
    if (usuario.getId() == null) {

        if (usuariosRepository.existsByCorreo(usuario.getCorreo())) {
            return null;
        }

        if (usuariosRepository.existsByCedula(usuario.getCedula())) {
            return null;
        }

        usuario.setActivo(true);
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));

    } else {

        // EDITAR USUARIO
        Usuario usuarioExistente =
                usuariosRepository.findById(usuario.getId()).orElse(null);

        if (usuarioExistente == null) {
            return null;
        }

        // Mantener el rol
        usuario.setRol(usuarioExistente.getRol());

        // Mantener si está activo
        usuario.setActivo(usuarioExistente.isActivo());

        // Si no escribió una nueva contraseña,
        // conservar la anterior
        if (usuario.getPassword() == null || usuario.getPassword().isBlank()) {

            usuario.setPassword(usuarioExistente.getPassword());

        } else {

            usuario.setPassword(
                    passwordEncoder.encode(usuario.getPassword()));
        }

    }

    return usuariosRepository.save(usuario);
}
    public Usuario obtenerPorId(Long id) {
        return usuariosRepository.findById(id).orElse(null);
    }

    public void eliminar(Long id) {
        usuariosRepository.deleteById(id);
    }

    public Usuario obtenerPorCorreo(String correo) {
    return usuariosRepository.findByCorreo(correo).orElse(null);
}

//Recuperar contraseña
public String recuperarPassword(String correo) {

    Usuario usuario = usuariosRepository.findByCorreo(correo).orElse(null);

    if (usuario == null) {
        return null;
    }
    String nuevaPassword = generarPassword();
    //Encripta la contraseña antes de guardar
    usuario.setPassword(passwordEncoder.encode(nuevaPassword));

    usuariosRepository.save(usuario);

    return nuevaPassword;
}
//Crea la contraseña temporal
private String generarPassword() {

    return "temporal123";
}
   
}
   
