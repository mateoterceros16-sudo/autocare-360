package com.autocare.cliente.domain.port.in;

import com.autocare.cliente.domain.model.Cliente;

/** Port IN: capacidad del sistema de registrar un cliente. */
public interface RegistrarClienteUseCase {

    Cliente registrar(RegistrarClienteCommand command);
}
