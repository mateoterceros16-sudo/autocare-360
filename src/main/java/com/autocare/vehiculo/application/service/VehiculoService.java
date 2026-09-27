package com.autocare.vehiculo.application.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.autocare.cliente.domain.exception.ClienteNoEncontradoException;
import com.autocare.cliente.domain.model.Cliente;
import com.autocare.cliente.domain.port.in.ConsultarClienteUseCase;
import com.autocare.vehiculo.domain.exception.PlacaDuplicadaException;
import com.autocare.vehiculo.domain.exception.PropietarioInactivoException;
import com.autocare.vehiculo.domain.exception.VehiculoNoEncontradoException;
import com.autocare.vehiculo.domain.exception.VinDuplicadoException;
import com.autocare.vehiculo.domain.model.Vehiculo;
import com.autocare.vehiculo.domain.port.in.ActualizarVehiculoCommand;
import com.autocare.vehiculo.domain.port.in.ActualizarVehiculoUseCase;
import com.autocare.vehiculo.domain.port.in.ConsultarVehiculoUseCase;
import com.autocare.vehiculo.domain.port.in.EliminarVehiculoUseCase;
import com.autocare.vehiculo.domain.port.in.RegistrarVehiculoCommand;
import com.autocare.vehiculo.domain.port.in.RegistrarVehiculoUseCase;
import com.autocare.vehiculo.domain.port.out.VehiculoRepositoryPort;

/**
 * Casos de uso del vehículo.
 * Para validar al propietario usa el Port IN del módulo cliente (ConsultarClienteUseCase),
 * NUNCA el repositorio JPA del cliente.
 */
@Service
public class VehiculoService implements RegistrarVehiculoUseCase, ConsultarVehiculoUseCase,
        ActualizarVehiculoUseCase, EliminarVehiculoUseCase {

    private final VehiculoRepositoryPort repositoryPort;
    private final ConsultarClienteUseCase consultarCliente;

    public VehiculoService(VehiculoRepositoryPort repositoryPort, ConsultarClienteUseCase consultarCliente) {
        this.repositoryPort = repositoryPort;
        this.consultarCliente = consultarCliente;
    }

    @Override
    @Transactional
    public Vehiculo registrar(RegistrarVehiculoCommand command) {
        // 1. El propietario debe existir (404) y estar activo (RN-01, 409)
        Cliente propietario = consultarCliente.buscarPorId(command.clienteId())
                .orElseThrow(() -> new ClienteNoEncontradoException(command.clienteId()));
        if (!propietario.isActivo()) {
            throw new PropietarioInactivoException(propietario.getId());
        }

        // 2. Construir el dominio (normaliza placa y VIN)
        Vehiculo nuevo = Vehiculo.nuevo(command.clienteId(), command.placa(), command.vin(),
                command.marca(), command.modelo(), command.anio(), command.kilometraje(),
                command.tipoCombustible());

        // 3. RF-15: no duplicar placa ni VIN
        if (repositoryPort.existePorPlaca(nuevo.getPlaca())) {
            throw new PlacaDuplicadaException(nuevo.getPlaca());
        }
        if (nuevo.getVin() != null && repositoryPort.existePorVin(nuevo.getVin())) {
            throw new VinDuplicadoException(nuevo.getVin());
        }

        // 4. Guardar por el Port OUT
        return repositoryPort.guardar(nuevo);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Vehiculo> buscarPorId(Long id) {
        return repositoryPort.buscarPorId(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Vehiculo> listarPorCliente(Long clienteId) {
        if (consultarCliente.buscarPorId(clienteId).isEmpty()) {
            throw new ClienteNoEncontradoException(clienteId);
        }
        return repositoryPort.listarPorClienteId(clienteId);
    }

    @Override
    @Transactional
    public Vehiculo actualizar(ActualizarVehiculoCommand command) {
        Vehiculo vehiculo = repositoryPort.buscarPorId(command.id())
                .orElseThrow(() -> new VehiculoNoEncontradoException(command.id()));

        // El propio dominio valida que el kilometraje no baje
        vehiculo.actualizarDatos(command.placa(), command.vin(), command.marca(), command.modelo(),
                command.anio(), command.kilometraje(), command.tipoCombustible());

        if (repositoryPort.existeOtroConPlaca(vehiculo.getPlaca(), vehiculo.getId())) {
            throw new PlacaDuplicadaException(vehiculo.getPlaca());
        }
        if (vehiculo.getVin() != null && repositoryPort.existeOtroConVin(vehiculo.getVin(), vehiculo.getId())) {
            throw new VinDuplicadoException(vehiculo.getVin());
        }
        return repositoryPort.guardar(vehiculo);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        if (repositoryPort.buscarPorId(id).isEmpty()) {
            throw new VehiculoNoEncontradoException(id);
        }
        repositoryPort.eliminarPorId(id);
    }
}
