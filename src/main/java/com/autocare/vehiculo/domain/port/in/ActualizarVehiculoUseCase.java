package com.autocare.vehiculo.domain.port.in;

import com.autocare.vehiculo.domain.model.Vehiculo;

/** Port IN: actualizar los datos de un vehículo. */
public interface ActualizarVehiculoUseCase {

    Vehiculo actualizar(ActualizarVehiculoCommand command);
}
