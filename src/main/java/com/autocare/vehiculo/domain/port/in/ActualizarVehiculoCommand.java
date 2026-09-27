package com.autocare.vehiculo.domain.port.in;

import com.autocare.vehiculo.domain.model.TipoCombustible;

/** Datos que necesita el caso de uso para actualizar un vehículo. El propietario no cambia. */
public record ActualizarVehiculoCommand(
        Long id,
        String placa,
        String vin,
        String marca,
        String modelo,
        int anio,
        int kilometraje,
        TipoCombustible tipoCombustible
) {
}
