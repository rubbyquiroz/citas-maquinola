package com.clinica.service.impl;

import com.clinica.model.Doctor;
import com.clinica.model.Paciente;
import com.clinica.model.Usuario;
import com.clinica.service.UsuarioService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class MemoryUsuarioService implements UsuarioService {

    private final ConcurrentHashMap<Long, Usuario> usuarios = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Long, Doctor> doctores = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Long, Paciente> pacientes = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    private final PasswordEncoder passwordEncoder;

    public MemoryUsuarioService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void initData() {
        // Doctores
        crearDoctor("Dr. García", "dr.garcia@clinica.com", "admin123", "Cardiología");
        crearDoctor("Dr. López", "dr.lopez@clinica.com", "admin123", "Pediatría");

        // Pacientes
        crearPaciente("Juan Pérez", "juan@email.com", "user123", "555-0101");
        crearPaciente("María Gómez", "maria@email.com", "user123", "555-0102");
    }

    private void crearDoctor(String nombre, String email, String password, String especialidad) {
        Long id = idGenerator.getAndIncrement();
        Usuario usuario = new Usuario(id, nombre, email, passwordEncoder.encode(password), "ROLE_DOCTOR");
        usuarios.put(id, usuario);
        Doctor doctor = new Doctor(usuario, especialidad);
        doctores.put(id, doctor);
    }

    private void crearPaciente(String nombre, String email, String password, String telefono) {
        Long id = idGenerator.getAndIncrement();
        Usuario usuario = new Usuario(id, nombre, email, passwordEncoder.encode(password), "ROLE_PACIENTE");
        usuarios.put(id, usuario);
        Paciente paciente = new Paciente(usuario, telefono);
        pacientes.put(id, paciente);
    }

    @Override
    public Optional<Paciente> findPacienteByEmail(String email) {
        return pacientes.values().stream()
                .filter(p -> p.getEmail().equals(email))
                .findFirst();
    }

    @Override
    public Optional<Doctor> findDoctorByEmail(String email) {
        return doctores.values().stream()
                .filter(d -> d.getEmail().equals(email))
                .findFirst();
    }

    @Override
    public List<Doctor> findAllDoctores() {
        return new ArrayList<>(doctores.values());
    }

    @Override
    public Optional<Doctor> findDoctorById(Long id) {
        return Optional.ofNullable(doctores.get(id));
    }

    @Override
    public Optional<Paciente> findPacienteById(Long id) {
        return Optional.ofNullable(pacientes.get(id));
    }

    /**
     * Obtiene el ID del usuario autenticado por su email.
     */
    public Optional<Long> findUserIdByEmail(String email) {
        return usuarios.values().stream()
                .filter(u -> u.getEmail().equals(email))
                .map(Usuario::getId)
                .findFirst();
    }

    /**
     * Obtiene el rol del usuario por su email.
     */
    public Optional<String> findUserRolByEmail(String email) {
        return usuarios.values().stream()
                .filter(u -> u.getEmail().equals(email))
                .map(Usuario::getRol)
                .findFirst();
    }
}
