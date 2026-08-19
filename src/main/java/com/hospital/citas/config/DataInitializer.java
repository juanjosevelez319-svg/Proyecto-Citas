package com.hospital.citas.config;

import com.hospital.citas.Model.Usuario;
import com.hospital.citas.Repository.UsuariosRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UsuariosRepository usuariosRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UsuariosRepository usuariosRepository, PasswordEncoder passwordEncoder) {
        this.usuariosRepository = usuariosRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run (String... args){
        if (usuariosRepository.findByCorreo("admin@hospital.com").isEmpty()){
            Usuario admin = new Usuario();
            admin.setNombre("Admin");
            admin.setCorreo("admin@hospital.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRol("ROLE_ADMIN");
            usuariosRepository.save(admin);
        }
        if(usuariosRepository.findByCorreo("usuario@hospital.com").isEmpty()){
            Usuario usuario = new Usuario();
            usuario.setNombre("Usuario");
            usuario.setCorreo("usuario@hospital.com");
            usuario.setPassword(passwordEncoder.encode("usuario123"));
            usuario.setRol("ROLE_USUARIO");
            usuariosRepository.save(usuario);
        }
    }
}
