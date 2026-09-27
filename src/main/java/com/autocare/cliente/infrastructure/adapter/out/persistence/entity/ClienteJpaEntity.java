package com.autocare.cliente.infrastructure.adapter.out.persistence.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Entidad JPA: representa la tabla física autocare.cliente.
 * Vive en infraestructura; el dominio (Cliente) no la conoce.
 *
 * La relación con vehiculo es UNIDIRECCIONAL: solo VehiculoJpaEntity tiene @ManyToOne.
 * Aquí no hay @OneToMany porque ningún caso de uso necesita cargar la colección
 * completa de vehículos desde el cliente (se consultan con GET /api/vehiculos/cliente/{id}).
 */
@Entity
@Table(name = "cliente", schema = "autocare")
public class ClienteJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cliente")
    private Long id;

    @Column(name = "documento", nullable = false, unique = true, length = 30)
    private String documento;

    @Column(name = "nombre_completo", nullable = false, length = 180)
    private String nombreCompleto;

    @Column(name = "email", nullable = false, unique = true, length = 180)
    private String email;

    @Column(name = "telefono", length = 20)
    private String telefono;

    @Column(name = "activo", nullable = false)
    private boolean activo;

    @Column(name = "fecha_registro", nullable = false, updatable = false)
    private LocalDateTime fechaRegistro;

    /** Constructor vacío que JPA/Hibernate necesita para crear el objeto al leer de la base. */
    protected ClienteJpaEntity() {
    }

    public ClienteJpaEntity(Long id, String documento, String nombreCompleto, String email,
                            String telefono, boolean activo, LocalDateTime fechaRegistro) {
        this.id = id;
        this.documento = documento;
        this.nombreCompleto = nombreCompleto;
        this.email = email;
        this.telefono = telefono;
        this.activo = activo;
        this.fechaRegistro = fechaRegistro;
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
