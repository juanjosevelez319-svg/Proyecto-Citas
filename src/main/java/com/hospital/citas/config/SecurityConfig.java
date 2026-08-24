package com.hospital.citas.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.hospital.citas.security.LoginSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final LoginSuccessHandler loginSuccessHandler;

    public SecurityConfig(LoginSuccessHandler loginSuccessHandler){
        this.loginSuccessHandler = loginSuccessHandler;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth

    .requestMatchers("/login", "/h2-console/**", "/usuarios/nuevo", "/usuarios/recuperar").permitAll()

    .requestMatchers("/inicio").hasRole("ADMIN")

    .requestMatchers("/pantalla-usuarios", "/usuarios/**","/citas/*","/citas/mis-citas","/citas/nuevaCita","/citas/guardarCita",
                "/citas/cancelarCita/**").hasAnyRole("ADMIN","USUARIO")

    .anyRequest().hasRole("ADMIN")
)
            .formLogin(form -> form 
                .loginPage("/login")
                .successHandler(loginSuccessHandler)
                .permitAll()
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/login?logout")
                .permitAll()
            )
            .exceptionHandling(ex -> ex.accessDeniedPage("/pantalla-usuarios"))
            .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()))
            .csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**"));

            return http.build();
    }

}