package com.hospital.citas.Controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class InicioController {
    @GetMapping("/inicio")
    public String inicio(){
        return "inicio";
    }

    @GetMapping("/pantalla-usuarios")
    public String usuario(){
        return "pantalla-usuarios";
    }
}
