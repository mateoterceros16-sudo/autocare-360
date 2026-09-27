package com.autocare.cliente.infrastructure.adapter.in.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Lo que el cliente HTTP envía para registrar un cliente (POST).
 * Las longitudes coinciden con las columnas de autocare.cliente.
 */
public record CrearClienteRequest(

        @NotBlank(message = "El documento es obligatorio")
        @Size(max = 30, message = "El documento no puede superar 30 caracteres")
        String documento,

        @NotBlank(message = "El nombre completo es obligatorio")
        @Size(max = 180, message = "El nombre no puede superar 180 caracteres")
        String nombreCompleto,

        @NotBlank(message = "El email es obligatorio")
        @Email(message = "El email no tiene un formato válido")
        @Size(max = 180, message = "El email no puede superar 180 caracteres")
        String email,

        @Pattern(regexp = "^[0-9+\\- ]{7,20}$", message = "El teléfono debe tener entre 7 y 20 dígitos (se permiten +, - y espacios)")
        String telefono
) {
}
