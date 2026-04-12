package com.clinica.repository;

/**
 * Interfaz para acceso a datos de Citas.
 * Preparada para implementación con Spring Data JPA (MySQL).
 * Por ahora, los datos se manejan en memoria via MemoryCitaService.
 */
public interface CitaRepository {
    // Métodos para futura implementación JPA
    // List<Cita> findByDoctorId(Long doctorId);
    // List<Cita> findByPacienteId(Long pacienteId);
    // Cita findById(Long id);
    // void save(Cita cita);
}
