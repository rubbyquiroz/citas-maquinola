package com.clinica.service.impl;

import com.clinica.model.Doctor;
import com.clinica.model.Paciente;
import com.clinica.repository.DoctorRepository;
import com.clinica.repository.PacienteRepository;
import com.clinica.service.UsuarioService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class JpaUsuarioService implements UsuarioService {

    private final DoctorRepository doctorRepository;
    private final PacienteRepository pacienteRepository;

    public JpaUsuarioService(DoctorRepository doctorRepository, PacienteRepository pacienteRepository) {
        this.doctorRepository = doctorRepository;
        this.pacienteRepository = pacienteRepository;
    }

    @Override
    public Optional<Paciente> findPacienteByEmail(String email) {
        return pacienteRepository.findByUsuarioEmail(email);
    }

    @Override
    public Optional<Doctor> findDoctorByEmail(String email) {
        return doctorRepository.findByUsuarioEmail(email);
    }

    @Override
    public List<Doctor> findAllDoctores() {
        return doctorRepository.findAll();
    }

    @Override
    public Optional<Doctor> findDoctorById(Long id) {
        return doctorRepository.findById(id);
    }

    @Override
    public Optional<Paciente> findPacienteById(Long id) {
        return pacienteRepository.findById(id);
    }
}
