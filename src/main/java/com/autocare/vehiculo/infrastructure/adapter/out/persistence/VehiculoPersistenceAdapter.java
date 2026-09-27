package com.autocare.vehiculo.infrastructure.adapter.out.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.autocare.cliente.infrastructure.adapter.out.persistence.repository.SpringDataClienteRepository;
import com.autocare.vehiculo.domain.model.Vehiculo;
import com.autocare.vehiculo.domain.port.out.VehiculoRepositoryPort;
import com.autocare.vehiculo.infrastructure.adapter.out.persistence.mapper.VehiculoPersistenceMapper;
import com.autocare.vehiculo.infrastructure.adapter.out.persistence.repository.SpringDataVehiculoRepository;

/**
 * Adaptador de salida del vehículo: implementa el Port OUT con Spring Data JPA.
 * Usa el repositorio del cliente solo para obtener una REFERENCIA (getReferenceById)
 * y así llenar la FK id_cliente sin hacer un SELECT extra. Esto queda dentro de
 * infraestructura: el service no lo ve.
 */
@Component
public class VehiculoPersistenceAdapter implements VehiculoRepositoryPort {

    private final SpringDataVehiculoRepository repository;
    private final SpringDataClienteRepository clienteRepository;

    public VehiculoPersistenceAdapter(SpringDataVehiculoRepository repository,
                                      SpringDataClienteRepository clienteRepository) {
        this.repository = repository;
        this.clienteRepository = clienteRepository;
    }

    @Override
    public Vehiculo guardar(Vehiculo vehiculo) {
        var clienteRef = clienteRepository.getReferenceById(vehiculo.getClienteId());
        var entity = VehiculoPersistenceMapper.toEntity(vehiculo, clienteRef);
        var guardado = repository.save(entity);
        return VehiculoPersistenceMapper.toDomain(guardado);
    }

    @Override
    public Optional<Vehiculo> buscarPorId(Long id) {
        return repository.findById(id).map(VehiculoPersistenceMapper::toDomain);
    }

    @Override
    public List<Vehiculo> listarPorClienteId(Long clienteId) {
        return repository.listarPorCliente(clienteId).stream()
                .map(VehiculoPersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public boolean existePorPlaca(String placa) {
        return repository.existsByPlaca(placa);
    }

    @Override
    public boolean existePorVin(String vin) {
        return repository.existsByVin(vin);
    }

    @Override
    public boolean existeOtroConPlaca(String placa, Long idExcluido) {
        return repository.existsByPlacaAndIdNot(placa, idExcluido);
    }

    @Override
    public boolean existeOtroConVin(String vin, Long idExcluido) {
        return repository.existsByVinAndIdNot(vin, idExcluido);
    }

    @Override
    public void eliminarPorId(Long id) {
        repository.deleteById(id);
    }
}
