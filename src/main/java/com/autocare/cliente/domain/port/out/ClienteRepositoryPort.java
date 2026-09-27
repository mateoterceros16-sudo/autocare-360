package com.autocare.cliente.domain.port.out;

import java.util.List;
import java.util.Optional;

import com.autocare.cliente.domain.model.Cliente;

/**
 * Port OUT: lo que el núcleo NECESITA para guardar y leer clientes.
 * No sabe nada de JPA ni de PostgreSQL; lo implementa ClientePersistenceAdapter.
 */
public interface ClienteRepositoryPort {

    Cliente guardar(Cliente cliente);

    Optional<Cliente> buscarPorId(Long id);

    List<Cliente> listar();

    List<Cliente> buscarPorNombre(String texto);

    boolean existePorDocumento(String documento);

    boolean existePorEmail(String email);

    boolean existeOtroConDocumento(String documento, Long idExcluido);

    boolean existeOtroConEmail(String email, Long idExcluido);

    void eliminarPorId(Long id);
}
