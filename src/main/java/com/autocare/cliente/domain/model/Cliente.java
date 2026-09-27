package com.autocare.cliente.domain.model;

import java.time.LocalDateTime;

/**
 * Modelo de dominio: el cliente propietario de vehículos del taller.
 * Es Java puro: NO tiene anotaciones de JPA ni de Spring.
 */
public class Cliente {

    private final Long id;
    private String documento;
    private String nombreCompleto;
    private String email;
    private String telefono;
    private boolean activo;
    private final LocalDateTime fechaRegistro;

    public Cliente(Long id, String documento, String nombreCompleto, String email,
                   String telefono, boolean activo, LocalDateTime fechaRegistro) {
        this.id = id;
        this.documento = documento;
        this.nombreCompleto = nombreCompleto;
        this.email = email;
        this.telefono = telefono;
        this.activo = activo;
        this.fechaRegistro = fechaRegistro;
    }

    /**
     * Crea un cliente nuevo: todavía sin id (lo genera PostgreSQL),
     * activo por defecto y con la fecha de registro del momento.
     */
    public static Cliente nuevo(String documento, String nombreCompleto, String email, String telefono) {
        return new Cliente(null, limpiar(documento), limpiar(nombreCompleto), normalizarEmail(email),
                limpiarOpcional(telefono), true, LocalDateTime.now());
    }

    /**
     * Cambia los datos editables. El id y la fecha de registro NO cambian nunca.
     */
    public void actualizarDatos(String documento, String nombreCompleto, String email,
                                String telefono, boolean activo) {
        this.documento = limpiar(documento);
        this.nombreCompleto = limpiar(nombreCompleto);
        this.email = normalizarEmail(email);
        this.telefono = limpiarOpcional(telefono);
        this.activo = activo;
    }

    private static String limpiar(String valor) {
        return valor == null ? null : valor.trim();
    }

    private static String limpiarOpcional(String valor) {
        return (valor == null || valor.isBlank()) ? null : valor.trim();
    }

    private static String normalizarEmail(String valor) {
        return valor == null ? null : valor.trim().toLowerCase();
    }

    public Long getId() {
        return id;
    }

    public String getDocumento() {
        return documento;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public String getEmail() {
        return email;
    }

    public String getTelefono() {
        return telefono;
    }

    public boolean isActivo() {
        return activo;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }
}
