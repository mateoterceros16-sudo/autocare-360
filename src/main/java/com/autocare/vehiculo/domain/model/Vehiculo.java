package com.autocare.vehiculo.domain.model;

import java.time.LocalDateTime;

import com.autocare.vehiculo.domain.exception.KilometrajeInvalidoException;

/**
 * Modelo de dominio del vehículo. Java puro: sin @Entity ni @ManyToOne.
 * Del propietario solo guarda su id (clienteId): la relación con el objeto
 * ClienteJpaEntity existe únicamente en la capa de persistencia.
 */
public class Vehiculo {

    private final Long id;
    private final Long clienteId;
    private String placa;
    private String vin;
    private String marca;
    private String modelo;
    private int anio;
    private int kilometraje;
    private TipoCombustible tipoCombustible;
    private final LocalDateTime fechaRegistro;

    public Vehiculo(Long id, Long clienteId, String placa, String vin, String marca, String modelo,
                    int anio, int kilometraje, TipoCombustible tipoCombustible, LocalDateTime fechaRegistro) {
        this.id = id;
        this.clienteId = clienteId;
        this.placa = placa;
        this.vin = vin;
        this.marca = marca;
        this.modelo = modelo;
        this.anio = anio;
        this.kilometraje = kilometraje;
        this.tipoCombustible = tipoCombustible;
        this.fechaRegistro = fechaRegistro;
    }

    /** Crea un vehículo nuevo (sin id todavía) con placa y VIN normalizados. */
    public static Vehiculo nuevo(Long clienteId, String placa, String vin, String marca, String modelo,
                                 int anio, int kilometraje, TipoCombustible tipoCombustible) {
        return new Vehiculo(null, clienteId, normalizarPlaca(placa), normalizarVin(vin),
                marca.trim(), modelo.trim(), anio, kilometraje, tipoCombustible, LocalDateTime.now());
    }

    /**
     * Actualiza los datos del vehículo.
     * Regla del taller: el kilometraje nunca puede bajar (el odómetro solo avanza).
     * El propietario (clienteId) no se cambia por aquí.
     */
    public void actualizarDatos(String placa, String vin, String marca, String modelo,
                                int anio, int nuevoKilometraje, TipoCombustible tipoCombustible) {
        if (nuevoKilometraje < this.kilometraje) {
            throw new KilometrajeInvalidoException(this.kilometraje, nuevoKilometraje);
        }
        this.placa = normalizarPlaca(placa);
        this.vin = normalizarVin(vin);
        this.marca = marca.trim();
        this.modelo = modelo.trim();
        this.anio = anio;
        this.kilometraje = nuevoKilometraje;
        this.tipoCombustible = tipoCombustible;
    }

    /** "1234-abc" y "1234 ABC" se guardan igual: "1234ABC". */
    private static String normalizarPlaca(String placa) {
        return placa.trim().toUpperCase().replace(" ", "").replace("-", "");
    }

    private static String normalizarVin(String vin) {
        return (vin == null || vin.isBlank()) ? null : vin.trim().toUpperCase();
    }

    public Long getId() {
        return id;
    }

    public Long getClienteId() {
        return clienteId;
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

    public int getAnio() {
        return anio;
    }

    public int getKilometraje() {
        return kilometraje;
    }

    public TipoCombustible getTipoCombustible() {
        return tipoCombustible;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }
}
