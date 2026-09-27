package com.autocare.vehiculo.infrastructure.adapter.in.web;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.autocare.vehiculo.domain.exception.VehiculoNoEncontradoException;
import com.autocare.vehiculo.domain.model.Vehiculo;
import com.autocare.vehiculo.domain.port.in.ActualizarVehiculoUseCase;
import com.autocare.vehiculo.domain.port.in.ConsultarVehiculoUseCase;
import com.autocare.vehiculo.domain.port.in.EliminarVehiculoUseCase;
import com.autocare.vehiculo.domain.port.in.RegistrarVehiculoUseCase;
import com.autocare.vehiculo.infrastructure.adapter.in.web.dto.ActualizarVehiculoRequest;
import com.autocare.vehiculo.infrastructure.adapter.in.web.dto.CrearVehiculoRequest;
import com.autocare.vehiculo.infrastructure.adapter.in.web.dto.VehiculoResponse;
import com.autocare.vehiculo.infrastructure.adapter.in.web.mapper.VehiculoWebMapper;

import jakarta.validation.Valid;

/** Adaptador de entrada REST del módulo vehículo. */
@RestController
@RequestMapping("/api/vehiculos")
public class VehiculoController {

    private final RegistrarVehiculoUseCase registrarVehiculo;
    private final ConsultarVehiculoUseCase consultarVehiculo;
    private final ActualizarVehiculoUseCase actualizarVehiculo;
    private final EliminarVehiculoUseCase eliminarVehiculo;

    public VehiculoController(RegistrarVehiculoUseCase registrarVehiculo,
                              ConsultarVehiculoUseCase consultarVehiculo,
                              ActualizarVehiculoUseCase actualizarVehiculo,
                              EliminarVehiculoUseCase eliminarVehiculo) {
        this.registrarVehiculo = registrarVehiculo;
        this.consultarVehiculo = consultarVehiculo;
        this.actualizarVehiculo = actualizarVehiculo;
        this.eliminarVehiculo = eliminarVehiculo;
    }

    @PostMapping
    public ResponseEntity<VehiculoResponse> registrar(@Valid @RequestBody CrearVehiculoRequest request) {
        Vehiculo creado = registrarVehiculo.registrar(VehiculoWebMapper.toCommand(request));
        return ResponseEntity
                .created(URI.create("/api/vehiculos/" + creado.getId()))
                .body(VehiculoWebMapper.toResponse(creado));
    }

    @GetMapping("/{id}")
    public ResponseEntity<VehiculoResponse> buscarPorId(@PathVariable Long id) {
        Vehiculo vehiculo = consultarVehiculo.buscarPorId(id)
                .orElseThrow(() -> new VehiculoNoEncontradoException(id));
        return ResponseEntity.ok(VehiculoWebMapper.toResponse(vehiculo));
    }

    @GetMapping("/cliente/{clienteId}")
    public List<VehiculoResponse> listarPorCliente(@PathVariable Long clienteId) {
        return consultarVehiculo.listarPorCliente(clienteId).stream()
                .map(VehiculoWebMapper::toResponse)
                .toList();
    }

    @PutMapping("/{id}")
    public ResponseEntity<VehiculoResponse> actualizar(@PathVariable Long id,
                                                       @Valid @RequestBody ActualizarVehiculoRequest request) {
        Vehiculo actualizado = actualizarVehiculo.actualizar(VehiculoWebMapper.toCommand(id, request));
        return ResponseEntity.ok(VehiculoWebMapper.toResponse(actualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        eliminarVehiculo.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
