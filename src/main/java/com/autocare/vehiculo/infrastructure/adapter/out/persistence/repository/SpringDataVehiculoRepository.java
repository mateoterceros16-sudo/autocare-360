package com.autocare.vehiculo.infrastructure.adapter.out.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.autocare.vehiculo.infrastructure.adapter.out.persistence.entity.VehiculoJpaEntity;

/**
 * Repositorio Spring Data del vehículo.
 * Tiene consultas derivadas (por nombre de método) y una consulta escrita con @Query (JPQL).
 */
public interface SpringDataVehiculoRepository extends JpaRepository<VehiculoJpaEntity, Long> {

    boolean existsByPlaca(String placa);

    boolean existsByVin(String vin);

    boolean existsByPlacaAndIdNot(String placa, Long id);

    boolean existsByVinAndIdNot(String vin, Long id);

    /**
     * JPQL: se escribe sobre entidades y atributos Java (VehiculoJpaEntity, v.cliente.id),
     * no sobre tablas y columnas. Hibernate lo traduce al SQL de PostgreSQL.
     */
    @Query("select v from VehiculoJpaEntity v where v.cliente.id = :clienteId order by v.id")
    List<VehiculoJpaEntity> listarPorCliente(@Param("clienteId") Long clienteId);
}
