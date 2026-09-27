package com.autocare.vehiculo.domain.port.in;

import com.autocare.vehiculo.domain.model.Vehiculo;

/** Port IN: registrar un vehículo para un cliente existente y activo. */
public interface RegistrarVehiculoUseCase {

    Vehiculo registrar(RegistrarVehiculoCommand command);
}
