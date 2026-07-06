package com.clinica.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "doctores")
public class Doctor {

    @Id
    private Long id;

    @OneToOne
    @MapsId
    private Usuario usuario;

    private String especialidad;

    public Doctor() {
    }

    public Doctor(Usuario usuario, String especialidad) {
        this.usuario = usuario;
        this.especialidad = especialidad;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public Long getId() {
        return usuario != null ? usuario.getId() : null;
    }

    public String getNombre() {
        return usuario != null ? usuario.getNombre() : "";
    }

    public String getEmail() {
        return usuario != null ? usuario.getEmail() : "";
    }
}
