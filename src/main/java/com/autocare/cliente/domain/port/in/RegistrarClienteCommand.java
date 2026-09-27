package com.autocare.cliente.domain.port.in;

/** Datos que necesita el caso de uso para registrar un cliente. */
public record RegistrarClienteCommand(
        String documento,
        String nombreCompleto,
        String email,
        String telefono
) {
}
