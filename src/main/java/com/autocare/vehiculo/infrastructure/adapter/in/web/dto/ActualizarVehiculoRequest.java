package com.autocare.vehiculo.infrastructure.adapter.in.web.dto;

import com.autocare.vehiculo.domain.model.TipoCombustible;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

/**
 * Lo que se envía en un PUT de vehículo.
 * No incluye id (va en la URL) ni clienteId (el propietario no se cambia por aquí).
 */
public record ActualizarVehiculoRequest(

        @NotBlank(message = "La placa es obligatoria")
        @Pattern(regexp = "^[A-Za-z0-9 -]{5,10}$", message = "La placa debe tener entre 5 y 10 letras o números")
        String placa,

        @Pattern(regexp = "^[A-HJ-NPR-Za-hj-npr-z0-9]{17}$",
                message = "El VIN debe tener 17 caracteres (letras y números, sin I, O ni Q)")
        String vin,

        @NotBlank(message = "La marca es obligatoria")
        @Size(max = 60, message = "La marca no puede superar 60 caracteres")
        String marca,

        @NotBlank(message = "El modelo es obligatorio")
        @Size(max = 60, message = "El modelo no puede superar 60 caracteres")
        String modelo,

        @NotNull(message = "El año es obligatorio")
        @Min(value = 1950, message = "El año no puede ser menor a 1950")
        @Max(value = 2100, message = "El año no puede ser mayor a 2100")
        Integer anio,

        @NotNull(message = "El kilometraje es obligatorio")
        @PositiveOrZero(message = "El kilometraje no puede ser negativo")
        Integer kilometraje,

        @NotNull(message = "El tipo de combustible es obligatorio (GASOLINA, DIESEL, GAS, ELECTRICO o HIBRIDO)")
        TipoCombustible tipoCombustible
) {
}
