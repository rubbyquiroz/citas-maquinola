package com.clinica.config;

import com.clinica.model.Doctor;
import com.clinica.model.Paciente;
import com.clinica.model.Usuario;
import com.clinica.repository.DoctorRepository;
import com.clinica.repository.PacienteRepository;
import com.clinica.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Siembra los usuarios de prueba en MySQL la primera vez que se levanta la
 * aplicación (si la tabla usuarios está vacía), para no duplicar datos en
 * cada reinicio.
 */
@Component
public class DataSeeder implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final DoctorRepository doctorRepository;
    private final PacienteRepository pacienteRepository;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(UsuarioRepository usuarioRepository, DoctorRepository doctorRepository,
                       PacienteRepository pacienteRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.doctorRepository = doctorRepository;
        this.pacienteRepository = pacienteRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (usuarioRepository.count() > 0) {
            return;
        }

        crearDoctor("Dr. García", "dr.garcia@clinica.com", "admin123", "Cardiología");
        crearDoctor("Dr. López", "dr.lopez@clinica.com", "admin123", "Pediatría");

        crearPaciente("Juan Pérez", "juan@email.com", "user123", "555-0101");
        crearPaciente("María Gómez", "maria@email.com", "user123", "555-0102");
    }

    private void crearDoctor(String nombre, String email, String password, String especialidad) {
        Usuario usuario = new Usuario(null, nombre, email, passwordEncoder.encode(password), "ROLE_DOCTOR");
        usuario = usuarioRepository.save(usuario);
        doctorRepository.save(new Doctor(usuario, especialidad));
    }

    private void crearPaciente(String nombre, String email, String password, String telefono) {
        Usuario usuario = new Usuario(null, nombre, email, passwordEncoder.encode(password), "ROLE_PACIENTE");
        usuario = usuarioRepository.save(usuario);
        pacienteRepository.save(new Paciente(usuario, telefono));
    }
}
