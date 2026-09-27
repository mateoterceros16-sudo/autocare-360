package com.autocare.cliente.domain.exception;

/**
 * Se lanza al intentar eliminar un cliente que todavía tiene vehículos.
 * El taller necesita conservar el historial, por eso NO se borran los vehículos en cascada.
 * Se responde 409.
 */
public class ClienteConVehiculosException extends RuntimeException {

    public ClienteConVehiculosException(Long id) {
        super("No se puede eliminar el cliente " + id
                + " porque tiene vehículos registrados. Puedes desactivarlo con PUT (activo = false)");
    }
}
