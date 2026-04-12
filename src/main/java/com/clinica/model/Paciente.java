package com.clinica.model;

public class Paciente {

    private Usuario usuario;
    private String telefono;

    public Paciente() {
    }

    public Paciente(Usuario usuario, String telefono) {
        this.usuario = usuario;
        this.telefono = telefono;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
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
