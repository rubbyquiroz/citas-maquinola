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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/doctor")
public class DoctorController {

    private final CitaService citaService;
    private final UsuarioService usuarioService;
    private final MemoryUsuarioService memoryUsuarioService;

    public DoctorController(CitaService citaService, UsuarioService usuarioService,
                            MemoryUsuarioService memoryUsuarioService) {
        this.citaService = citaService;
        this.usuarioService = usuarioService;
        this.memoryUsuarioService = memoryUsuarioService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, Authentication auth) {
        Long doctorId = getDoctorId(auth.getName());
        if (doctorId == null) {
            return "redirect:/login";
        }

        Doctor doctor = usuarioService.findDoctorById(doctorId).orElse(null);
        if (doctor == null) {
            return "redirect:/login";
        }

        List<Cita> citas = citaService.findCitasByDoctorId(doctorId);

        model.addAttribute("doctor", doctor);
        model.addAttribute("citas", citas);
        return "doctor/dashboard";
    }

    @PostMapping("/confirmar-cita")
    public String confirmarCita(@RequestParam Long citaId,
                                Authentication auth,
                                RedirectAttributes redirectAttributes) {
        Long doctorId = getDoctorId(auth.getName());
        if (doctorId == null) {
            return "redirect:/login";
        }

        boolean resultado = citaService.confirmarCita(citaId, doctorId);
        if (resultado) {
            redirectAttributes.addFlashAttribute("success", "Cita confirmada correctamente.");
        } else {
            redirectAttributes.addFlashAttribute("error", "No se pudo confirmar la cita.");
        }
        return "redirect:/doctor/dashboard";
    }

    @PostMapping("/cancelar-cita")
    public String cancelarCita(@RequestParam Long citaId,
                               Authentication auth,
                               RedirectAttributes redirectAttributes) {
        Long doctorId = getDoctorId(auth.getName());
        if (doctorId == null) {
            return "redirect:/login";
        }

        boolean resultado = citaService.cancelarCitaByDoctor(citaId, doctorId);
        if (resultado) {
            redirectAttributes.addFlashAttribute("success", "Cita cancelada correctamente.");
        } else {
            redirectAttributes.addFlashAttribute("error", "No se pudo cancelar la cita.");
        }
        return "redirect:/doctor/dashboard";
    }

    @GetMapping("/paciente/{id}")
    public String detallePaciente(@PathVariable Long id, Model model, Authentication auth) {
        Long doctorId = getDoctorId(auth.getName());
        if (doctorId == null) {
            return "redirect:/login";
        }

        Paciente paciente = usuarioService.findPacienteById(id).orElse(null);
        if (paciente == null) {
            return "redirect:/doctor/dashboard";
        }

        List<Cita> citasPaciente = citaService.findCitasByPacienteId(id);
        // Filtrar solo citas con este doctor
        citasPaciente.removeIf(c -> !c.getDoctorId().equals(doctorId));

        model.addAttribute("paciente", paciente);
        model.addAttribute("citas", citasPaciente);
        return "doctor/detalle-paciente";
    }

    private Long getDoctorId(String email) {
        return memoryUsuarioService.findUserIdByEmail(email)
                .orElse(null);
    }
}
