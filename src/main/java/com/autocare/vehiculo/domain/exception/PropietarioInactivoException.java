package com.autocare.vehiculo.domain.exception;

/** RN-01: cada vehículo debe asociarse a un propietario activo. Se responde 409. */
public class PropietarioInactivoException extends RuntimeException {

    public PropietarioInactivoException(Long clienteId) {
        super("El cliente " + clienteId + " está inactivo: no se le pueden registrar vehículos (RN-01)");
    }
}
