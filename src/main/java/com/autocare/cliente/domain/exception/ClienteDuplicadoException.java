package com.autocare.cliente.domain.exception;

/** Se lanza cuando el documento o el email ya pertenecen a otro cliente. Se responde 409. */
public class ClienteDuplicadoException extends RuntimeException {

    public ClienteDuplicadoException(String campo, String valor) {
        super("Ya existe un cliente registrado con el " + campo + " " + valor);
    }
}
