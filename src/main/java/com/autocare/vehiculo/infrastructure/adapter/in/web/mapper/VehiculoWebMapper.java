package com.autocare.vehiculo.infrastructure.adapter.in.web.mapper;

import com.autocare.vehiculo.domain.model.Vehiculo;
import com.autocare.vehiculo.domain.port.in.ActualizarVehiculoCommand;
import com.autocare.vehiculo.domain.port.in.RegistrarVehiculoCommand;
import com.autocare.vehiculo.infrastructure.adapter.in.web.dto.ActualizarVehiculoRequest;
import com.autocare.vehiculo.infrastructure.adapter.in.web.dto.CrearVehiculoRequest;
import com.autocare.vehiculo.infrastructure.adapter.in.web.dto.VehiculoResponse;

/** Traduce entre DTOs HTTP y el núcleo del módulo vehículo. */
public final class VehiculoWebMapper {

    private VehiculoWebMapper() {
    }

    public static RegistrarVehiculoCommand toCommand(CrearVehiculoRequest request) {
        return new RegistrarVehiculoCommand(
                request.clienteId(),
                request.placa(),
                request.vin(),
                request.marca(),
                request.modelo(),
                request.anio(),
                request.kilometraje(),
                request.tipoCombustible());
    }

    public static ActualizarVehiculoCommand toCommand(Long id, ActualizarVehiculoRequest request) {
        return new ActualizarVehiculoCommand(
                id,
                request.placa(),
                request.vin(),
                request.marca(),
                request.modelo(),
                request.anio(),
                request.kilometraje(),
                request.tipoCombustible());
    }

    public static VehiculoResponse toResponse(Vehiculo vehiculo) {
        return new VehiculoResponse(
                vehiculo.getId(),
                vehiculo.getClienteId(),
                vehiculo.getPlaca(),
                vehiculo.getVin(),
                vehiculo.getMarca(),
                vehiculo.getModelo(),
                vehiculo.getAnio(),
                vehiculo.getKilometraje(),
                vehiculo.getTipoCombustible(),
                vehiculo.getFechaRegistro());
    }
}
