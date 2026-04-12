package com.clinica.repository;

/**
 * Interfaz para acceso a datos de Usuario.
 * Preparada para implementación con Spring Data JPA (MySQL).
 * Por ahora, los datos se manejan en memoria via MemoryUsuarioService.
 */
public interface UsuarioRepository {
    // Métodos para futura implementación JPA
    // Usuario findByEmail(String email);
    // Usuario findById(Long id);
    // void save(Usuario usuario);
}
