package com.autocare.cliente.domain.exception;

/** Se lanza cuando se busca un cliente que no existe. Se responde 404. */
public class ClienteNoEncontradoException extends RuntimeException {

    public ClienteNoEncontradoException(Long id) {
        super("No existe un cliente con id " + id);
    }
}
