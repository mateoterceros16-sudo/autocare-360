package com.autocare.vehiculo.domain.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import com.autocare.vehiculo.domain.exception.KilometrajeInvalidoException;

/** Prueba unitaria del dominio: reglas de negocio del vehículo, sin Spring ni base de datos. */
class VehiculoTest {

    @Test
    void normalizaLaPlacaYElVin() {
        Vehiculo v = Vehiculo.nuevo(1L, " 1234-abc ", "", "Toyota", "Hilux", 2020, 1000, TipoCombustible.DIESEL);

        assertEquals("1234ABC", v.getPlaca());
        assertNull(v.getVin());
    }

    @Test
    void elKilometrajeNoPuedeBajar() {
        Vehiculo v = Vehiculo.nuevo(1L, "1234ABC", null, "Toyota", "Hilux", 2020, 50000, TipoCombustible.DIESEL);

        assertThrows(KilometrajeInvalidoException.class, () ->
                v.actualizarDatos("1234ABC", null, "Toyota", "Hilux", 2020, 49000, TipoCombustible.DIESEL));
    }

    @Test
    void elKilometrajePuedeSubir() {
        Vehiculo v = Vehiculo.nuevo(1L, "1234ABC", null, "Toyota", "Hilux", 2020, 50000, TipoCombustible.DIESEL);

        v.actualizarDatos("1234ABC", null, "Toyota", "Hilux", 2020, 52000, TipoCombustible.DIESEL);

        assertEquals(52000, v.getKilometraje());
    }
}
