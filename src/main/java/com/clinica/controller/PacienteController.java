package com.clinica.controller;

import com.clinica.model.Cita;
import com.clinica.model.Doctor;
import com.clinica.model.Paciente;
import com.clinica.service.CitaService;
import com.clinica.service.UsuarioService;
import com.clinica.service.impl.MemoryUsuarioService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;

@Controller
@RequestMapping("/paciente")
public class PacienteController {

    private final CitaService citaService;
    private final UsuarioService usuarioService;
    private final MemoryUsuarioService memoryUsuarioService;

    public PacienteController(CitaService citaService, UsuarioService usuarioService,
                              MemoryUsuarioService memoryUsuarioService) {
        this.citaService = citaService;
        this.usuarioService = usuarioService;
        this.memoryUsuarioService = memoryUsuarioService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, Authentication auth) {
        Long pacienteId = getPacienteId(auth.getName());
        if (pacienteId == null) {
            return "redirect:/login";
        }

        Paciente paciente = usuarioService.findPacienteById(pacienteId).orElse(null);
        if (paciente == null) {
            return "redirect:/login";
        }

        List<Cita> citasPendientes = citaService.findCitasByPacienteId(pacienteId);

        model.addAttribute("paciente", paciente);
        model.addAttribute("citas", citasPendientes);
        return "paciente/dashboard";
    }

    @GetMapping("/agendar")
    public String mostrarAgendar(Model model, Authentication auth) {
        Long pacienteId = getPacienteId(auth.getName());
        if (pacienteId == null) {
            return "redirect:/login";
        }

        List<Doctor> doctores = usuarioService.findAllDoctores();
        List<LocalTime> horarios = citaService.getHorariosDisponibles();

        model.addAttribute("doctores", doctores);
        model.addAttribute("horarios", horarios);
        return "paciente/agendar";
    }

    @PostMapping("/agendar")
    public String agendarCita(@RequestParam Long doctorId,
                              @RequestParam String fecha,
                              @RequestParam String hora,
                              Authentication auth,
                              RedirectAttributes redirectAttributes) {
        Long pacienteId = getPacienteId(auth.getName());
        if (pacienteId == null) {
            return "redirect:/login";
        }

        try {
            LocalDate fechaParsed = LocalDate.parse(fecha);
            LocalTime horaParsed = LocalTime.parse(hora);

            if (fechaParsed.isBefore(LocalDate.now())) {
                redirectAttributes.addFlashAttribute("error", "No puede agendar en fechas pasadas.");
                return "redirect:/paciente/agendar";
            }

            if (!citaService.isSlotDisponible(doctorId, fechaParsed, horaParsed)) {
                redirectAttributes.addFlashAttribute("error", "El horario seleccionado ya está ocupado.");
                return "redirect:/paciente/agendar";
            }

            citaService.crearCita(doctorId, pacienteId, fechaParsed, horaParsed);
            redirectAttributes.addFlashAttribute("success", "Cita agendada correctamente.");
            return "redirect:/paciente/dashboard";

        } catch (DateTimeParseException e) {
            redirectAttributes.addFlashAttribute("error", "Formato de fecha u hora inválido.");
            return "redirect:/paciente/agendar";
        }
    }

    @GetMapping("/mis-citas")
    public String misCitas(Model model, Authentication auth) {
        Long pacienteId = getPacienteId(auth.getName());
        if (pacienteId == null) {
            return "redirect:/login";
        }

        List<Cita> citas = citaService.findCitasByPacienteId(pacienteId);
        model.addAttribute("citas", citas);
        return "paciente/mis-citas";
    }

    @PostMapping("/cancelar-cita")
    public String cancelarCita(@RequestParam Long citaId,
                               Authentication auth,
                               RedirectAttributes redirectAttributes) {
        Long pacienteId = getPacienteId(auth.getName());
        if (pacienteId == null) {
            return "redirect:/login";
        }

        boolean resultado = citaService.cancelarCita(citaId, pacienteId);
        if (resultado) {
            redirectAttributes.addFlashAttribute("success", "Cita cancelada correctamente.");
        } else {
            redirectAttributes.addFlashAttribute("error", "No se pudo cancelar la cita.");
        }
        return "redirect:/paciente/mis-citas";
    }

    private Long getPacienteId(String email) {
        return memoryUsuarioService.findUserIdByEmail(email)
                .orElse(null);
    }
}
