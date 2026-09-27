package com.autocare.cliente.domain.port.in;

import com.autocare.cliente.domain.model.Cliente;

/** Port IN: capacidad de actualizar los datos de un cliente. */
public interface ActualizarClienteUseCase {

    Cliente actualizar(ActualizarClienteCommand command);
}
