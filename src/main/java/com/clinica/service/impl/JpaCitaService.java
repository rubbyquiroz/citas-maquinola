package com.clinica.service.impl;

import com.clinica.model.Cita;
import com.clinica.model.EstadoCita;
import com.clinica.repository.CitaRepository;
import com.clinica.service.CitaService;
import com.clinica.service.UsuarioService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class JpaCitaService implements CitaService {

    private final CitaRepository citaRepository;
    private final UsuarioService usuarioService;

    public JpaCitaService(CitaRepository citaRepository, UsuarioService usuarioService) {
        this.citaRepository = citaRepository;
        this.usuarioService = usuarioService;
    }

    @Override
    public List<Cita> findCitasByPacienteId(Long pacienteId) {
        List<Cita> result = citaRepository.findByPacienteIdAndEstadoNot(pacienteId, EstadoCita.COMPLETADA);
        enrichWithNames(result);
        return result;
    }

    @Override
    public List<Cita> findCitasByDoctorId(Long doctorId) {
        List<Cita> result = citaRepository.findByDoctorIdAndEstadoNot(doctorId, EstadoCita.COMPLETADA);
        enrichWithNames(result);
        return result;
    }

    @Override
    public List<Cita> findCitasByDoctorIdAndEstado(Long doctorId, EstadoCita estado) {
        List<Cita> result = citaRepository.findByDoctorIdAndEstado(doctorId, estado);
        enrichWithNames(result);
        return result;
    }

    @Override
    public Optional<Cita> findCitaById(Long id) {
        Optional<Cita> cita = citaRepository.findById(id);
        cita.ifPresent(this::enrichNames);
        return cita;
    }

    @Override
    public Cita crearCita(Long doctorId, Long pacienteId, LocalDate fecha, LocalTime hora) {
        Cita cita = new Cita(null, doctorId, pacienteId, fecha, hora, EstadoCita.PENDIENTE, null);
        return citaRepository.save(cita);
    }

    @Override
    public boolean cancelarCita(Long citaId, Long pacienteId) {
        Optional<Cita> citaOpt = citaRepository.findById(citaId);
        if (citaOpt.isPresent()) {
            Cita cita = citaOpt.get();
            if (cita.getPacienteId().equals(pacienteId) && cita.getEstado() == EstadoCita.PENDIENTE) {
                cita.setEstado(EstadoCita.CANCELADA);
                citaRepository.save(cita);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean confirmarCita(Long citaId, Long doctorId) {
        Optional<Cita> citaOpt = citaRepository.findById(citaId);
        if (citaOpt.isPresent()) {
            Cita cita = citaOpt.get();
            if (cita.getDoctorId().equals(doctorId) && cita.getEstado() == EstadoCita.PENDIENTE) {
                cita.setEstado(EstadoCita.CONFIRMADA);
                citaRepository.save(cita);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean cancelarCitaByDoctor(Long citaId, Long doctorId) {
        Optional<Cita> citaOpt = citaRepository.findById(citaId);
        if (citaOpt.isPresent()) {
            Cita cita = citaOpt.get();
            if (cita.getDoctorId().equals(doctorId)
                    && (cita.getEstado() == EstadoCita.PENDIENTE || cita.getEstado() == EstadoCita.CONFIRMADA)) {
                cita.setEstado(EstadoCita.CANCELADA);
                citaRepository.save(cita);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isSlotDisponible(Long doctorId, LocalDate fecha, LocalTime hora) {
        return !citaRepository.existsByDoctorIdAndFechaAndHoraAndEstadoNot(doctorId, fecha, hora, EstadoCita.CANCELADA);
    }

    @Override
    public List<LocalTime> getHorariosDisponibles() {
        List<LocalTime> horarios = new ArrayList<>();
        LocalTime inicio = LocalTime.of(9, 0);
        LocalTime fin = LocalTime.of(17, 0);

        LocalTime actual = inicio;
        while (actual.isBefore(fin)) {
            horarios.add(actual);
            actual = actual.plusMinutes(30);
        }
        return horarios;
    }

    private void enrichWithNames(List<Cita> citas) {
        citas.forEach(this::enrichNames);
    }

    private void enrichNames(Cita cita) {
        usuarioService.findDoctorById(cita.getDoctorId())
                .ifPresent(d -> cita.setNombreDoctor(d.getNombre()));

        usuarioService.findPacienteById(cita.getPacienteId())
                .ifPresent(p -> cita.setNombrePaciente(p.getNombre()));
    }
}
