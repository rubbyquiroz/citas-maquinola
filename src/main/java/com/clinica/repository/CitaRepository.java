package com.clinica.repository;

import com.clinica.model.Cita;
import com.clinica.model.EstadoCita;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface CitaRepository extends JpaRepository<Cita, Long> {

    List<Cita> findByPacienteIdAndEstadoNot(Long pacienteId, EstadoCita estado);

    List<Cita> findByDoctorIdAndEstadoNot(Long doctorId, EstadoCita estado);

    List<Cita> findByDoctorIdAndEstado(Long doctorId, EstadoCita estado);

    boolean existsByDoctorIdAndFechaAndHoraAndEstadoNot(Long doctorId, LocalDate fecha, LocalTime hora, EstadoCita estado);
}
