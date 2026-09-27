package com.autocare.vehiculo.domain.port.in;

import java.util.List;
import java.util.Optional;

import com.autocare.vehiculo.domain.model.Vehiculo;

/** Port IN: consultar un vehículo o los vehículos de un cliente. */
public interface ConsultarVehiculoUseCase {

    Optional<Vehiculo> buscarPorId(Long id);

    List<Vehiculo> listarPorCliente(Long clienteId);
}
