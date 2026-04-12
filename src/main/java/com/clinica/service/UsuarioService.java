package com.clinica.service;

import com.clinica.model.Doctor;
import com.clinica.model.Paciente;

import java.util.List;
import java.util.Optional;

public interface UsuarioService {

    Optional<Paciente> findPacienteByEmail(String email);

    Optional<Doctor> findDoctorByEmail(String email);

    List<Doctor> findAllDoctores();

    Optional<Doctor> findDoctorById(Long id);

    Optional<Paciente> findPacienteById(Long id);
}
