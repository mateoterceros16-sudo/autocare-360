package com.autocare.cliente.infrastructure.adapter.in.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Lo que se envía en un PUT. No incluye el id (va en la URL)
 * ni la fecha de registro (la controla el servidor).
 */
public record ActualizarClienteRequest(

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
        String telefono,

        @NotNull(message = "Debes indicar si el cliente está activo (true o false)")
        Boolean activo
) {
}
