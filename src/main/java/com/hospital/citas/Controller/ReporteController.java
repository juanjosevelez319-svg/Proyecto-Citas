package com.hospital.citas.Controller;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.hospital.citas.Model.Citas;
import com.hospital.citas.Service.MedicoService;
import com.hospital.citas.Service.ReporteService;
@Controller
@RequestMapping("/reportes")
public class ReporteController {

    @Autowired
    private ReporteService reporteService;

    @Autowired
    private MedicoService medicoService;


    @GetMapping("/citas")
    public String panelReportes(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate fechaInicio,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate fechaFin,

            @RequestParam(required = false)
            Long medicoId,

            @RequestParam(required = false)
            String especialidad,

            @RequestParam(required = false)
            Citas.EstadoCita estado,

            Model model) {

                System.out.println("========== FILTROS ==========");
                System.out.println("Fecha inicio: " + fechaInicio);
                System.out.println("Fecha fin: " + fechaFin);
    System.out.println("Médico ID: " + medicoId);
    System.out.println("Especialidad: " + especialidad);
    System.out.println("Estado: " + estado);
    System.out.println("=============================");



        List<Citas> citas =
            reporteService.buscarCitas(
                fechaInicio,
                fechaFin,
                medicoId,
                especialidad,
                estado
            );


        // Datos necesarios para los filtros
        model.addAttribute(
            "medicos",
            medicoService.listarTodas()
        );


        model.addAttribute(
            "citas",
            citas
        );


        // Mantener valores seleccionados
        model.addAttribute(
            "fechaInicio",
            fechaInicio
        );

        model.addAttribute(
            "fechaFin",
            fechaFin
        );

        model.addAttribute(
            "medicoSeleccionado",
            medicoId
        );

        model.addAttribute(
            "especialidadSeleccionada",
            especialidad
        );

        model.addAttribute(
            "estadoSeleccionado",
            estado
        );


        // Totales
        long total =
            citas.size();

        long pendientes =
            citas.stream()
                 .filter(c -> c.getEstado()
                     == Citas.EstadoCita.PENDIENTE)
                 .count();

        long confirmadas =
            citas.stream()
                 .filter(c -> c.getEstado()
                     == Citas.EstadoCita.CONFIRMADA)
                 .count();

        long canceladas =
            citas.stream()
                 .filter(c -> c.getEstado()
                     == Citas.EstadoCita.CANCELADA)
                 .count();


        model.addAttribute(
            "total",
            total
        );

        model.addAttribute(
            "pendientes",
            pendientes
        );

        model.addAttribute(
            "confirmadas",
            confirmadas
        );

        model.addAttribute(
            "canceladas",
            canceladas
        );


        return "ReportesCitas";
    }
}
