package com.autocare.vehiculo.infrastructure.adapter.in.web.dto;

import java.time.LocalDateTime;

import com.autocare.vehiculo.domain.model.TipoCombustible;

/**
 * Lo que la API devuelve sobre un vehículo.
 * Del propietario solo va su id: no se anida el cliente completo (evita ciclos y datos de más).
 */
public record VehiculoResponse(
        Long id,
        Long clienteId,
        String placa,
        String vin,
        String marca,
        String modelo,
        int anio,
        int kilometraje,
        TipoCombustible tipoCombustible,
        LocalDateTime fechaRegistro
) {
}
