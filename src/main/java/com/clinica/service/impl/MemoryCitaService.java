package com.clinica.service.impl;

import com.clinica.model.Cita;
import com.clinica.model.EstadoCita;
import com.clinica.service.CitaService;
import com.clinica.service.UsuarioService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
public class MemoryCitaService implements CitaService {

    private final ConcurrentHashMap<Long, Cita> citas = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    private final MemoryUsuarioService usuarioService;

    public MemoryCitaService(MemoryUsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @Override
    public List<Cita> findCitasByPacienteId(Long pacienteId) {
        List<Cita> result = citas.values().stream()
                .filter(c -> c.getPacienteId().equals(pacienteId))
                .filter(c -> c.getEstado() != EstadoCita.COMPLETADA)
                .collect(Collectors.toList());
        enrichWithNames(result);
        return result;
    }

    @Override
    public List<Cita> findCitasByDoctorId(Long doctorId) {
        List<Cita> result = citas.values().stream()
                .filter(c -> c.getDoctorId().equals(doctorId))
                .filter(c -> c.getEstado() != EstadoCita.COMPLETADA)
                .collect(Collectors.toList());
        enrichWithNames(result);
        return result;
    }

    @Override
    public List<Cita> findCitasByDoctorIdAndEstado(Long doctorId, EstadoCita estado) {
        List<Cita> result = citas.values().stream()
                .filter(c -> c.getDoctorId().equals(doctorId))
                .filter(c -> c.getEstado() == estado)
                .collect(Collectors.toList());
        enrichWithNames(result);
        return result;
    }

    @Override
    public Optional<Cita> findCitaById(Long id) {
        Optional<Cita> cita = Optional.ofNullable(citas.get(id));
        cita.ifPresent(this::enrichNames);
        return cita;
    }

    @Override
    public Cita crearCita(Long doctorId, Long pacienteId, LocalDate fecha, LocalTime hora) {
        Long id = idGenerator.getAndIncrement();
        Cita cita = new Cita(id, doctorId, pacienteId, fecha, hora, EstadoCita.PENDIENTE, null);
        citas.put(id, cita);
        return cita;
    }

    @Override
    public boolean cancelarCita(Long citaId, Long pacienteId) {
        Cita cita = citas.get(citaId);
        if (cita != null && cita.getPacienteId().equals(pacienteId)
                && cita.getEstado() == EstadoCita.PENDIENTE) {
            cita.setEstado(EstadoCita.CANCELADA);
            return true;
        }
        return false;
    }

    @Override
    public boolean confirmarCita(Long citaId, Long doctorId) {
        Cita cita = citas.get(citaId);
        if (cita != null && cita.getDoctorId().equals(doctorId)
                && cita.getEstado() == EstadoCita.PENDIENTE) {
            cita.setEstado(EstadoCita.CONFIRMADA);
            return true;
        }
        return false;
    }

    @Override
    public boolean cancelarCitaByDoctor(Long citaId, Long doctorId) {
        Cita cita = citas.get(citaId);
        if (cita != null && cita.getDoctorId().equals(doctorId)
                && (cita.getEstado() == EstadoCita.PENDIENTE || cita.getEstado() == EstadoCita.CONFIRMADA)) {
            cita.setEstado(EstadoCita.CANCELADA);
            return true;
        }
        return false;
    }

    @Override
    public boolean isSlotDisponible(Long doctorId, LocalDate fecha, LocalTime hora) {
        return citas.values().stream()
                .noneMatch(c -> c.getDoctorId().equals(doctorId)
                        && c.getFecha().equals(fecha)
                        && c.getHora().equals(hora)
                        && c.getEstado() != EstadoCita.CANCELADA);
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
