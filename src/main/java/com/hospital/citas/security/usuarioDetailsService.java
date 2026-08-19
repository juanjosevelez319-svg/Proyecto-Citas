package com.hospital.citas.security;

import com.hospital.citas.Model.Usuario;
import com.hospital.citas.Repository.UsuariosRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class usuarioDetailsService implements UserDetailsService {
    private final UsuariosRepository usuariosRepository;

    public usuarioDetailsService(UsuariosRepository usuariosRepository) {
        this.usuariosRepository = usuariosRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuariosRepository.findByCorreo(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        return org.springframework.security.core.userdetails.User.builder()
                .username(usuario.getCorreo())
                .password(usuario.getPassword())
                .authorities(new SimpleGrantedAuthority(usuario.getRol()))
                .build();
    }
}
