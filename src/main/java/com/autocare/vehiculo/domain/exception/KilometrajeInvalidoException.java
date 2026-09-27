package com.autocare.vehiculo.domain.exception;

/** Regla del taller: el kilometraje de un vehículo no puede disminuir. Se responde 409. */
public class KilometrajeInvalidoException extends RuntimeException {

    public KilometrajeInvalidoException(int kilometrajeActual, int kilometrajeNuevo) {
        super("El kilometraje no puede bajar: el vehículo ya registra " + kilometrajeActual
                + " km y se intentó guardar " + kilometrajeNuevo + " km");
    }
}
