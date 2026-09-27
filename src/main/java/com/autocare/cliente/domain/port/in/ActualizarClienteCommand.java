package com.autocare.cliente.domain.port.in;

/** Datos que necesita el caso de uso para actualizar un cliente existente. */
public record ActualizarClienteCommand(
        Long id,
        String documento,
        String nombreCompleto,
        String email,
        String telefono,
        boolean activo
) {
}
