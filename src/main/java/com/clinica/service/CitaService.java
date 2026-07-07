package com.clinica.service;

import com.clinica.model.Cita;
import com.clinica.model.EstadoCita;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface CitaService {

    List<Cita> findCitasActivasByPacienteId(Long pacienteId);

    List<Cita> findHistorialCitasByPacienteId(Long pacienteId);

    List<Cita> findCitasActivasByDoctorId(Long doctorId);

    List<Cita> findHistorialCitasByDoctorId(Long doctorId);

    List<Cita> findCitasByDoctorIdAndEstado(Long doctorId, EstadoCita estado);

    Optional<Cita> findCitaById(Long id);

    Cita crearCita(Long doctorId, Long pacienteId, LocalDate fecha, LocalTime hora);

    boolean cancelarCita(Long citaId, Long pacienteId);

    boolean confirmarCita(Long citaId, Long doctorId);

    boolean completarCita(Long citaId, Long doctorId);

    boolean cancelarCitaByDoctor(Long citaId, Long doctorId);

    boolean isSlotDisponible(Long doctorId, LocalDate fecha, LocalTime hora);

    boolean isHorarioAtencionValido(LocalDate fecha, LocalTime hora);

    List<LocalTime> getHorariosDisponibles();
}
