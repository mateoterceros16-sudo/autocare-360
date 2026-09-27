package com.autocare.cliente.infrastructure.adapter.out.persistence.mapper;

import com.autocare.cliente.domain.model.Cliente;
import com.autocare.cliente.infrastructure.adapter.out.persistence.entity.ClienteJpaEntity;

/** Convierte entre el dominio (Cliente) y la entidad JPA (ClienteJpaEntity). */
public final class ClientePersistenceMapper {

    private ClientePersistenceMapper() {
    }

    public static ClienteJpaEntity toEntity(Cliente cliente) {
        return new ClienteJpaEntity(
                cliente.getId(),
                cliente.getDocumento(),
                cliente.getNombreCompleto(),
                cliente.getEmail(),
                cliente.getTelefono(),
                cliente.isActivo(),
                cliente.getFechaRegistro());
    }

    public static Cliente toDomain(ClienteJpaEntity entity) {
        return new Cliente(
                entity.getId(),
                entity.getDocumento(),
                entity.getNombreCompleto(),
                entity.getEmail(),
                entity.getTelefono(),
                entity.isActivo(),
                entity.getFechaRegistro());
    }
}
