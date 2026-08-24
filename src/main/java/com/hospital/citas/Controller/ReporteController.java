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

import java.nio.charset.StandardCharsets;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

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

if (especialidad != null && especialidad.isBlank()) {
    especialidad = null;
}

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


    @GetMapping("/citas/csv")
public ResponseEntity<byte[]> exportarCSV(

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
        Citas.EstadoCita estado) {


    // Obtener exactamente las mismas citas filtradas
    List<Citas> citas =
        reporteService.buscarCitas(
            fechaInicio,
            fechaFin,
            medicoId,
            especialidad,
            estado
        );


    // Construir el CSV
    StringBuilder csv = new StringBuilder();


    // Encabezados
    csv.append("ID,Paciente,Medico,Especialidad,Fecha,Hora Inicio,Hora Fin,Estado\n");


    // Datos
    for (Citas cita : citas) {

        csv.append(cita.getId()).append(",");

        csv.append(cita.getUsuario().getNombre()).append(",");

        csv.append(cita.getMedico().getNombre()).append(",");

        csv.append(cita.getMedico().getEspecialidad()).append(",");


        if (cita.getHorario().getFecha() != null) {

            csv.append(cita.getHorario().getFecha());

        } else {

            csv.append("");

        }

        csv.append(",");


        if (cita.getHorario().getHoraInicio() != null) {

            csv.append(cita.getHorario().getHoraInicio());

        } else {

            csv.append("");

        }

        csv.append(",");


        if (cita.getHorario().getHoraFin() != null) {

            csv.append(cita.getHorario().getHoraFin());

        } else {

            csv.append("");

        }

        csv.append(",");

        csv.append(cita.getEstado());

        csv.append("\n");
    }


    byte[] archivo =
        csv.toString().getBytes(StandardCharsets.UTF_8);


    return ResponseEntity.ok()
        .header(
            HttpHeaders.CONTENT_DISPOSITION,
            "attachment; filename=reporte_citas.csv"
        )
        .contentType(
            MediaType.parseMediaType("text/csv")
        )
        .body(archivo);
}
}
