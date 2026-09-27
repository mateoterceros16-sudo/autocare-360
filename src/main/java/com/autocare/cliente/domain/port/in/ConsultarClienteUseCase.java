package com.autocare.cliente.domain.port.in;

import java.util.List;
import java.util.Optional;

import com.autocare.cliente.domain.model.Cliente;

/**
 * Port IN: capacidad de consultar clientes.
 * También lo usa el módulo vehiculo para comprobar que el propietario exista y esté activo.
 */
public interface ConsultarClienteUseCase {

    Optional<Cliente> buscarPorId(Long id);

    List<Cliente> listar(String nombre);
}
