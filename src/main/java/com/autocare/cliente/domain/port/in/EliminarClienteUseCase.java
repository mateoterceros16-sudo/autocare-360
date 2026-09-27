package com.autocare.cliente.domain.port.in;

/** Port IN: capacidad de eliminar un cliente sin vehículos. */
public interface EliminarClienteUseCase {

    void eliminar(Long id);
}
