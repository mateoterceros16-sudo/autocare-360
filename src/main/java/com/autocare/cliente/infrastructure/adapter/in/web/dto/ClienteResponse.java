package com.autocare.cliente.infrastructure.adapter.in.web.dto;

import java.time.LocalDateTime;

/** Lo que la API devuelve sobre un cliente. */
public record ClienteResponse(
        Long id,
        String documento,
        String nombreCompleto,
        String email,
        String telefono,
        boolean activo,
        LocalDateTime fechaRegistro
) {
}
