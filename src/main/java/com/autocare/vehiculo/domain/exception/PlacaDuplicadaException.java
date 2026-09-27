package com.autocare.vehiculo.domain.exception;

/** RF-15: no se puede duplicar la placa. Se responde 409. */
public class PlacaDuplicadaException extends RuntimeException {

    public PlacaDuplicadaException(String placa) {
        super("Ya existe un vehículo registrado con la placa " + placa);
    }
}
