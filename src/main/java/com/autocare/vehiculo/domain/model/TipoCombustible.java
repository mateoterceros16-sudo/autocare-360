package com.autocare.vehiculo.domain.model;

/**
 * Tipos de combustible que atiende el taller.
 * Los valores coinciden con el CHECK ck_vehiculo_tipo_combustible de PostgreSQL.
 */
public enum TipoCombustible {
    GASOLINA,
    DIESEL,
    GAS,
    ELECTRICO,
    HIBRIDO
}
