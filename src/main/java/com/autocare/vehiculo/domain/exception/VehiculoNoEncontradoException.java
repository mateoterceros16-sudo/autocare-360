package com.autocare.vehiculo.domain.exception;

/** Se lanza cuando se busca un vehículo que no existe. Se responde 404. */
public class VehiculoNoEncontradoException extends RuntimeException {

    public VehiculoNoEncontradoException(Long id) {
        super("No existe un vehículo con id " + id);
    }
}
