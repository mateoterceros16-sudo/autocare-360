package com.autocare.cliente.infrastructure.adapter.out.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.autocare.cliente.infrastructure.adapter.out.persistence.entity.ClienteJpaEntity;

/**
 * Repositorio de Spring Data: Spring genera la implementación al arrancar.
 * Las consultas se derivan del nombre del método (consultas derivadas).
 * Es un detalle de infraestructura: solo lo usa el adapter, nunca el service.
 */
public interface SpringDataClienteRepository extends JpaRepository<ClienteJpaEntity, Long> {

    boolean existsByDocumento(String documento);

    boolean existsByEmail(String email);

    boolean existsByDocumentoAndIdNot(String documento, Long id);

    boolean existsByEmailAndIdNot(String email, Long id);

    List<ClienteJpaEntity> findAllByOrderByIdAsc();

    List<ClienteJpaEntity> findByNombreCompletoContainingIgnoreCaseOrderByIdAsc(String texto);
}
