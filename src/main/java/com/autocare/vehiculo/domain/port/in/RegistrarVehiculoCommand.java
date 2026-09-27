package com.autocare.vehiculo.domain.port.in;

import com.autocare.vehiculo.domain.model.TipoCombustible;

/** Datos que necesita el caso de uso para registrar un vehículo. */
public record RegistrarVehiculoCommand(
        Long clienteId,
        String placa,
        String vin,
        String marca,
        String modelo,
        int anio,
        int kilometraje,
        TipoCombustible tipoCombustible
) {
}
