package com.autocare.cliente.infrastructure.adapter.in.web;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.autocare.cliente.domain.exception.ClienteNoEncontradoException;
import com.autocare.cliente.domain.model.Cliente;
import com.autocare.cliente.domain.port.in.ActualizarClienteUseCase;
import com.autocare.cliente.domain.port.in.ConsultarClienteUseCase;
import com.autocare.cliente.domain.port.in.EliminarClienteUseCase;
import com.autocare.cliente.domain.port.in.RegistrarClienteUseCase;
import com.autocare.cliente.infrastructure.adapter.in.web.dto.ActualizarClienteRequest;
import com.autocare.cliente.infrastructure.adapter.in.web.dto.ClienteResponse;
import com.autocare.cliente.infrastructure.adapter.in.web.dto.CrearClienteRequest;
import com.autocare.cliente.infrastructure.adapter.in.web.mapper.ClienteWebMapper;

import jakarta.validation.Valid;

/**
 * Adaptador de entrada REST del módulo cliente.
 * Solo conoce los Port IN (interfaces), no el service concreto ni JPA.
 */
@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    private final RegistrarClienteUseCase registrarCliente;
    private final ConsultarClienteUseCase consultarCliente;
    private final ActualizarClienteUseCase actualizarCliente;
    private final EliminarClienteUseCase eliminarCliente;

    public ClienteController(RegistrarClienteUseCase registrarCliente,
                             ConsultarClienteUseCase consultarCliente,
                             ActualizarClienteUseCase actualizarCliente,
                             EliminarClienteUseCase eliminarCliente) {
        this.registrarCliente = registrarCliente;
        this.consultarCliente = consultarCliente;
        this.actualizarCliente = actualizarCliente;
        this.eliminarCliente = eliminarCliente;
    }

    @PostMapping
    public ResponseEntity<ClienteResponse> crear(@Valid @RequestBody CrearClienteRequest request) {
        Cliente creado = registrarCliente.registrar(ClienteWebMapper.toCommand(request));
        return ResponseEntity
                .created(URI.create("/api/clientes/" + creado.getId()))
                .body(ClienteWebMapper.toResponse(creado));
    }

    @GetMapping
    public List<ClienteResponse> listar(@RequestParam(required = false) String nombre) {
        return consultarCliente.listar(nombre).stream()
                .map(ClienteWebMapper::toResponse)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponse> buscarPorId(@PathVariable Long id) {
        Cliente cliente = consultarCliente.buscarPorId(id)
                .orElseThrow(() -> new ClienteNoEncontradoException(id));
        return ResponseEntity.ok(ClienteWebMapper.toResponse(cliente));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponse> actualizar(@PathVariable Long id,
                                                      @Valid @RequestBody ActualizarClienteRequest request) {
        Cliente actualizado = actualizarCliente.actualizar(ClienteWebMapper.toCommand(id, request));
        return ResponseEntity.ok(ClienteWebMapper.toResponse(actualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        eliminarCliente.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
