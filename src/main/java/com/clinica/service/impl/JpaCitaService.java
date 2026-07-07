package com.clinica.service.impl;

import com.clinica.model.Cita;
import com.clinica.model.EstadoCita;
import com.clinica.repository.CitaRepository;
import com.clinica.service.CitaService;
import com.clinica.service.UsuarioService;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class JpaCitaService implements CitaService {

    private static final LocalTime HORA_INICIO = LocalTime.of(9, 0);
    private static final LocalTime HORA_FIN = LocalTime.of(17, 0);
    private static final int DURACION_TURNO_MINUTOS = 30;
    private static final List<EstadoCita> ESTADOS_ACTIVOS =
            Arrays.asList(EstadoCita.PENDIENTE, EstadoCita.CONFIRMADA);

    private final CitaRepository citaRepository;
    private final UsuarioService usuarioService;

    public JpaCitaService(CitaRepository citaRepository, UsuarioService usuarioService) {
        this.citaRepository = citaRepository;
        this.usuarioService = usuarioService;
    }

    @Override
    public List<Cita> findCitasActivasByPacienteId(Long pacienteId) {
        List<Cita> result = citaRepository.findByPacienteIdAndEstadoIn(pacienteId, ESTADOS_ACTIVOS);
        enrichWithNames(result);
        return result;
    }

    @Override
    public List<Cita> findHistorialCitasByPacienteId(Long pacienteId) {
        List<Cita> result = citaRepository.findByPacienteId(pacienteId);
        enrichWithNames(result);
        return result;
    }

    @Override
    public List<Cita> findCitasActivasByDoctorId(Long doctorId) {
        List<Cita> result = citaRepository.findByDoctorIdAndEstadoIn(doctorId, ESTADOS_ACTIVOS);
        enrichWithNames(result);
        return result;
    }

    @Override
    public List<Cita> findHistorialCitasByDoctorId(Long doctorId) {
        List<Cita> result = citaRepository.findByDoctorId(doctorId);
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
        if (!isHorarioAtencionValido(fecha, hora)) {
            throw new IllegalArgumentException("La fecha u hora esta fuera del horario de atencion.");
        }
        if (!isSlotDisponible(doctorId, fecha, hora)) {
            throw new IllegalArgumentException("El horario seleccionado ya esta ocupado.");
        }

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
    public boolean completarCita(Long citaId, Long doctorId) {
        Optional<Cita> citaOpt = citaRepository.findById(citaId);
        if (citaOpt.isPresent()) {
            Cita cita = citaOpt.get();
            if (cita.getDoctorId().equals(doctorId) && cita.getEstado() == EstadoCita.CONFIRMADA) {
                cita.setEstado(EstadoCita.COMPLETADA);
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
        return !citaRepository.existsByDoctorIdAndFechaAndHoraAndEstadoIn(doctorId, fecha, hora, ESTADOS_ACTIVOS);
    }

    @Override
    public boolean isHorarioAtencionValido(LocalDate fecha, LocalTime hora) {
        if (fecha == null || hora == null || fecha.isBefore(LocalDate.now())) {
            return false;
        }

        DayOfWeek dia = fecha.getDayOfWeek();
        if (dia == DayOfWeek.SATURDAY || dia == DayOfWeek.SUNDAY) {
            return false;
        }

        if (hora.isBefore(HORA_INICIO) || !hora.isBefore(HORA_FIN)) {
            return false;
        }

        return hora.getMinute() % DURACION_TURNO_MINUTOS == 0 && hora.getSecond() == 0 && hora.getNano() == 0;
    }

    @Override
    public List<LocalTime> getHorariosDisponibles() {
        List<LocalTime> horarios = new ArrayList<>();

        LocalTime actual = HORA_INICIO;
        while (actual.isBefore(HORA_FIN)) {
            horarios.add(actual);
            actual = actual.plusMinutes(DURACION_TURNO_MINUTOS);
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
