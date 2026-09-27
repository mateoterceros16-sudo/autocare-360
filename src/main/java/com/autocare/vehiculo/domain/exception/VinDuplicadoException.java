package com.autocare.vehiculo.domain.exception;

/** RF-15: no se puede duplicar el VIN (número de chasis). Se responde 409. */
public class VinDuplicadoException extends RuntimeException {

    public VinDuplicadoException(String vin) {
        super("Ya existe un vehículo registrado con el VIN " + vin);
    }
}
