package com.autocare.cliente.infrastructure.adapter.in.web.mapper;

import com.autocare.cliente.domain.model.Cliente;
import com.autocare.cliente.domain.port.in.ActualizarClienteCommand;
import com.autocare.cliente.domain.port.in.RegistrarClienteCommand;
import com.autocare.cliente.infrastructure.adapter.in.web.dto.ActualizarClienteRequest;
import com.autocare.cliente.infrastructure.adapter.in.web.dto.ClienteResponse;
import com.autocare.cliente.infrastructure.adapter.in.web.dto.CrearClienteRequest;

/**
 * Traduce entre el mundo HTTP (DTOs) y el núcleo (commands y dominio).
 * Así el controller no se llena de conversiones.
 */
public final class ClienteWebMapper {

    private ClienteWebMapper() {
    }

    public static RegistrarClienteCommand toCommand(CrearClienteRequest request) {
        return new RegistrarClienteCommand(
                request.documento(),
                request.nombreCompleto(),
                request.email(),
                request.telefono());
    }

    public static ActualizarClienteCommand toCommand(Long id, ActualizarClienteRequest request) {
        return new ActualizarClienteCommand(
                id,
                request.documento(),
                request.nombreCompleto(),
                request.email(),
                request.telefono(),
                request.activo());
    }

    public static ClienteResponse toResponse(Cliente cliente) {
        return new ClienteResponse(
                cliente.getId(),
                cliente.getDocumento(),
                cliente.getNombreCompleto(),
                cliente.getEmail(),
                cliente.getTelefono(),
                cliente.isActivo(),
                cliente.getFechaRegistro());
    }
}
