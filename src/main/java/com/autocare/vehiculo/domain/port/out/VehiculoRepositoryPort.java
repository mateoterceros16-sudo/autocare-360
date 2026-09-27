package com.autocare.vehiculo.domain.port.out;

import java.util.List;
import java.util.Optional;

import com.autocare.vehiculo.domain.model.Vehiculo;

/** Port OUT: lo que el núcleo necesita para guardar y leer vehículos. */
public interface VehiculoRepositoryPort {

    Vehiculo guardar(Vehiculo vehiculo);

    Optional<Vehiculo> buscarPorId(Long id);

    List<Vehiculo> listarPorClienteId(Long clienteId);

    boolean existePorPlaca(String placa);

    boolean existePorVin(String vin);

    boolean existeOtroConPlaca(String placa, Long idExcluido);

    boolean existeOtroConVin(String vin, Long idExcluido);

    void eliminarPorId(Long id);
}
