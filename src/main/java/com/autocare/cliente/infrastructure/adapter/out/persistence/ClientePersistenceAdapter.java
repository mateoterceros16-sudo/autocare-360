package com.autocare.cliente.infrastructure.adapter.out.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import com.autocare.cliente.domain.exception.ClienteConVehiculosException;
import com.autocare.cliente.domain.model.Cliente;
import com.autocare.cliente.domain.port.out.ClienteRepositoryPort;
import com.autocare.cliente.infrastructure.adapter.out.persistence.mapper.ClientePersistenceMapper;
import com.autocare.cliente.infrastructure.adapter.out.persistence.repository.SpringDataClienteRepository;

/**
 * Adaptador de salida: implementa el Port OUT usando Spring Data JPA.
 * Es el único lugar del módulo cliente que sabe que existe JPA.
 */
@Component
public class ClientePersistenceAdapter implements ClienteRepositoryPort {

    private final SpringDataClienteRepository repository;

    public ClientePersistenceAdapter(SpringDataClienteRepository repository) {
        this.repository = repository;
    }

    @Override
    public Cliente guardar(Cliente cliente) {
        var entity = ClientePersistenceMapper.toEntity(cliente);
        var guardado = repository.save(entity);
        return ClientePersistenceMapper.toDomain(guardado);
    }

    @Override
    public Optional<Cliente> buscarPorId(Long id) {
        return repository.findById(id).map(ClientePersistenceMapper::toDomain);
    }

    @Override
    public List<Cliente> listar() {
        return repository.findAllByOrderByIdAsc().stream()
                .map(ClientePersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public List<Cliente> buscarPorNombre(String texto) {
        return repository.findByNombreCompletoContainingIgnoreCaseOrderByIdAsc(texto).stream()
                .map(ClientePersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public boolean existePorDocumento(String documento) {
        return repository.existsByDocumento(documento);
    }

    @Override
    public boolean existePorEmail(String email) {
        return repository.existsByEmail(email);
    }

    @Override
    public boolean existeOtroConDocumento(String documento, Long idExcluido) {
        return repository.existsByDocumentoAndIdNot(documento, idExcluido);
    }

    @Override
    public boolean existeOtroConEmail(String email, Long idExcluido) {
        return repository.existsByEmailAndIdNot(email, idExcluido);
    }

    @Override
    public void eliminarPorId(Long id) {
        try {
            repository.deleteById(id);
            // flush() ejecuta el DELETE en este momento para detectar aquí la violación de la FK
            repository.flush();
        } catch (DataIntegrityViolationException e) {
            // La FK fk_vehiculo_cliente impide borrar un cliente con vehículos
            throw new ClienteConVehiculosException(id);
        }
    }
}
