package com.autocare.vehiculo.infrastructure.adapter.out.persistence.mapper;

import com.autocare.cliente.infrastructure.adapter.out.persistence.entity.ClienteJpaEntity;
import com.autocare.vehiculo.domain.model.Vehiculo;
import com.autocare.vehiculo.infrastructure.adapter.out.persistence.entity.VehiculoJpaEntity;

/**
 * Convierte entre dominio y JPA.
 * La entidad JPA tiene el OBJETO cliente; el dominio solo tiene el clienteId.
 */
public final class VehiculoPersistenceMapper {

    private VehiculoPersistenceMapper() {
    }

    public static VehiculoJpaEntity toEntity(Vehiculo vehiculo, ClienteJpaEntity cliente) {
        return new VehiculoJpaEntity(
                vehiculo.getId(),
                cliente,
                vehiculo.getPlaca(),
                vehiculo.getVin(),
                vehiculo.getMarca(),
                vehiculo.getModelo(),
                vehiculo.getAnio(),
                vehiculo.getKilometraje(),
                vehiculo.getTipoCombustible(),
                vehiculo.getFechaRegistro());
    }

    public static Vehiculo toDomain(VehiculoJpaEntity entity) {
        return new Vehiculo(
                entity.getId(),
                entity.getCliente().getId(),
                entity.getPlaca(),
                entity.getVin(),
                entity.getMarca(),
                entity.getModelo(),
                entity.getAnio(),
                entity.getKilometraje(),
                entity.getTipoCombustible(),
                entity.getFechaRegistro());
    }
}
