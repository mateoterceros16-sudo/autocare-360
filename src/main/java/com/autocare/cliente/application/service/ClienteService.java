package com.autocare.cliente.application.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.autocare.cliente.domain.exception.ClienteDuplicadoException;
import com.autocare.cliente.domain.exception.ClienteNoEncontradoException;
import com.autocare.cliente.domain.model.Cliente;
import com.autocare.cliente.domain.port.in.ActualizarClienteCommand;
import com.autocare.cliente.domain.port.in.ActualizarClienteUseCase;
import com.autocare.cliente.domain.port.in.ConsultarClienteUseCase;
import com.autocare.cliente.domain.port.in.EliminarClienteUseCase;
import com.autocare.cliente.domain.port.in.RegistrarClienteCommand;
import com.autocare.cliente.domain.port.in.RegistrarClienteUseCase;
import com.autocare.cliente.domain.port.out.ClienteRepositoryPort;

/**
 * Casos de uso del cliente. Aquí viven las reglas de negocio y el límite de la transacción.
 * Depende del Port OUT (una interfaz), nunca del repositorio de Spring Data.
 */
@Service
public class ClienteService implements RegistrarClienteUseCase, ConsultarClienteUseCase,
        ActualizarClienteUseCase, EliminarClienteUseCase {

    private final ClienteRepositoryPort repositoryPort;

    public ClienteService(ClienteRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    @Transactional
    public Cliente registrar(RegistrarClienteCommand command) {
        Cliente nuevo = Cliente.nuevo(command.documento(), command.nombreCompleto(),
                command.email(), command.telefono());

        // Regla de negocio: no se repite documento ni email
        if (repositoryPort.existePorDocumento(nuevo.getDocumento())) {
            throw new ClienteDuplicadoException("documento", nuevo.getDocumento());
        }
        if (repositoryPort.existePorEmail(nuevo.getEmail())) {
            throw new ClienteDuplicadoException("email", nuevo.getEmail());
        }
        return repositoryPort.guardar(nuevo);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Cliente> buscarPorId(Long id) {
        return repositoryPort.buscarPorId(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Cliente> listar(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            return repositoryPort.listar();
        }
        return repositoryPort.buscarPorNombre(nombre.trim());
    }

    @Override
    @Transactional
    public Cliente actualizar(ActualizarClienteCommand command) {
        // PUT sobre un id que no existe: 404, nunca se crea un cliente "por accidente"
        Cliente cliente = repositoryPort.buscarPorId(command.id())
                .orElseThrow(() -> new ClienteNoEncontradoException(command.id()));

        cliente.actualizarDatos(command.documento(), command.nombreCompleto(),
                command.email(), command.telefono(), command.activo());

        if (repositoryPort.existeOtroConDocumento(cliente.getDocumento(), cliente.getId())) {
            throw new ClienteDuplicadoException("documento", cliente.getDocumento());
        }
        if (repositoryPort.existeOtroConEmail(cliente.getEmail(), cliente.getId())) {
            throw new ClienteDuplicadoException("email", cliente.getEmail());
        }
        return repositoryPort.guardar(cliente);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        if (repositoryPort.buscarPorId(id).isEmpty()) {
            throw new ClienteNoEncontradoException(id);
        }
        // Si tiene vehículos, la FK de PostgreSQL lo impide y el adapter lanza ClienteConVehiculosException
        repositoryPort.eliminarPorId(id);
    }
}
