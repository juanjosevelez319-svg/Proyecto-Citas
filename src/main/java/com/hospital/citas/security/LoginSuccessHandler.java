package com.hospital.citas.security;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response, 
                                        Authentication authentication) throws IOException{


        boolean esAdmin = authentication.getAuthorities().stream()
                                            .map(GrantedAuthority::getAuthority)
                                            .anyMatch(a -> a.equals("ROLE_ADMIN"));

                                           if (esAdmin){
                                             response.sendRedirect("/inicio");
                                           } else {
                                            response.sendRedirect("/pantalla-usuarios");
        }
    }
}
