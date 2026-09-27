package com.autocare.vehiculo.infrastructure.adapter.out.persistence.entity;

import java.time.LocalDateTime;

import com.autocare.cliente.infrastructure.adapter.out.persistence.entity.ClienteJpaEntity;
import com.autocare.vehiculo.domain.model.TipoCombustible;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * Entidad JPA de la tabla autocare.vehiculo (el lado N de la relación).
 *
 * @ManyToOne: muchos vehículos pertenecen a un mismo cliente.
 * Este es el LADO PROPIETARIO de la relación: aquí está la FK id_cliente.
 * LAZY: el cliente no se carga de la base hasta que alguien lo necesite.
 * Sin cascade: guardar o borrar un vehículo NUNCA debe guardar o borrar a su propietario.
 */
@Entity
@Table(name = "vehiculo", schema = "autocare")
public class VehiculoJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_vehiculo")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_cliente", nullable = false)
    private ClienteJpaEntity cliente;

    @Column(name = "placa", nullable = false, unique = true, length = 10)
    private String placa;

    @Column(name = "vin", unique = true, length = 17)
    private String vin;

    @Column(name = "marca", nullable = false, length = 60)
    private String marca;

    @Column(name = "modelo", nullable = false, length = 60)
    private String modelo;

    @Column(name = "anio", nullable = false)
    private Integer anio;

    @Column(name = "kilometraje", nullable = false)
    private Integer kilometraje;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_combustible", nullable = false, length = 20)
    private TipoCombustible tipoCombustible;

    @Column(name = "fecha_registro", nullable = false, updatable = false)
    private LocalDateTime fechaRegistro;

    /** Constructor vacío que JPA/Hibernate necesita. */
    protected VehiculoJpaEntity() {
    }

    public VehiculoJpaEntity(Long id, ClienteJpaEntity cliente, String placa, String vin, String marca,
                             String modelo, Integer anio, Integer kilometraje,
                             TipoCombustible tipoCombustible, LocalDateTime fechaRegistro) {
        this.id = id;
        this.cliente = cliente;
        this.placa = placa;
        this.vin = vin;
        this.marca = marca;
        this.modelo = modelo;
        this.anio = anio;
        this.kilometraje = kilometraje;
        this.tipoCombustible = tipoCombustible;
        this.fechaRegistro = fechaRegistro;
    }

    public Long getId() {
        return id;
    }

    public ClienteJpaEntity getCliente() {
        return cliente;
    }

    public String getPlaca() {
        return placa;
    }

    public String getVin() {
        return vin;
    }

    public String getMarca() {
        return marca;
    }

    public String getModelo() {
        return modelo;
    }

    public Integer getAnio() {
        return anio;
    }

    public Integer getKilometraje() {
        return kilometraje;
    }

    public TipoCombustible getTipoCombustible() {
        return tipoCombustible;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }
}
