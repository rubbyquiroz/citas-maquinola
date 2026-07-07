package com.clinica.repository;

import com.clinica.model.Cita;
import com.clinica.model.EstadoCita;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface CitaRepository extends JpaRepository<Cita, Long> {

    List<Cita> findByPacienteId(Long pacienteId);

    List<Cita> findByDoctorId(Long doctorId);

    List<Cita> findByPacienteIdAndEstadoIn(Long pacienteId, List<EstadoCita> estados);

    List<Cita> findByDoctorIdAndEstadoIn(Long doctorId, List<EstadoCita> estados);

    List<Cita> findByDoctorIdAndEstado(Long doctorId, EstadoCita estado);

    boolean existsByDoctorIdAndFechaAndHoraAndEstadoIn(
            Long doctorId, LocalDate fecha, LocalTime hora, List<EstadoCita> estados);
}
