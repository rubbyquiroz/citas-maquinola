package com.clinica.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Cita {

    private Long id;
    private Long doctorId;
    private Long pacienteId;
    private LocalDate fecha;
    private LocalTime hora;
    private EstadoCita estado;
    private String notas;

    // Campos transient para vista
    private String nombreDoctor;
    private String nombrePaciente;

    public Cita() {
    }

    public Cita(Long id, Long doctorId, Long pacienteId, LocalDate fecha,
                LocalTime hora, EstadoCita estado, String notas) {
        this.id = id;
        this.doctorId = doctorId;
        this.pacienteId = pacienteId;
        this.fecha = fecha;
        this.hora = hora;
        this.estado = estado;
        this.notas = notas;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
    }

    public Long getPacienteId() {
        return pacienteId;
    }

    public void setPacienteId(Long pacienteId) {
        this.pacienteId = pacienteId;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public LocalTime getHora() {
        return hora;
    }

    public void setHora(LocalTime hora) {
        this.hora = hora;
    }

    public EstadoCita getEstado() {
        return estado;
    }

    public void setEstado(EstadoCita estado) {
        this.estado = estado;
    }

    public String getNotas() {
        return notas;
    }

    public void setNotas(String notas) {
        this.notas = notas;
    }

    public String getNombreDoctor() {
        return nombreDoctor;
    }

    public void setNombreDoctor(String nombreDoctor) {
        this.nombreDoctor = nombreDoctor;
    }

    public String getNombrePaciente() {
        return nombrePaciente;
    }

    public void setNombrePaciente(String nombrePaciente) {
        this.nombrePaciente = nombrePaciente;
    }
}
