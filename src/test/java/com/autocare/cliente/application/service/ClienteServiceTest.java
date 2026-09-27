package com.autocare.cliente.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.autocare.cliente.domain.exception.ClienteDuplicadoException;
import com.autocare.cliente.domain.exception.ClienteNoEncontradoException;
import com.autocare.cliente.domain.model.Cliente;
import com.autocare.cliente.domain.port.in.ActualizarClienteCommand;
import com.autocare.cliente.domain.port.in.RegistrarClienteCommand;
import com.autocare.cliente.domain.port.out.ClienteRepositoryPort;

/**
 * Prueba UNITARIA del service: no levanta Spring ni PostgreSQL.
 * Gracias al Port OUT, le pasamos un repositorio falso en memoria.
 */
class ClienteServiceTest {

    private ClienteService service;

    @BeforeEach
    void preparar() {
        service = new ClienteService(new ClienteRepositoryFalso());
    }

    @Test
    void registraUnClienteNuevoActivoYConId() {
        Cliente creado = service.registrar(new RegistrarClienteCommand(
                "7894561", "Ana Rojas", "Ana.Rojas@Correo.com", "70012345"));

        assertNotNull(creado.getId());
        assertTrue(creado.isActivo());
        assertEquals("ana.rojas@correo.com", creado.getEmail());
    }

    @Test
    void noPermiteDocumentoRepetido() {
        service.registrar(new RegistrarClienteCommand("7894561", "Ana Rojas", "ana@correo.com", null));

        assertThrows(ClienteDuplicadoException.class, () -> service.registrar(
                new RegistrarClienteCommand("7894561", "Otra Persona", "otra@correo.com", null)));
    }

    @Test
    void actualizarUnIdInexistenteLanzaNoEncontrado() {
        assertThrows(ClienteNoEncontradoException.class, () -> service.actualizar(
                new ActualizarClienteCommand(99L, "1", "X", "x@correo.com", null, true)));
    }

    /** Implementación falsa del Port OUT: guarda en una lista. */
    private static class ClienteRepositoryFalso implements ClienteRepositoryPort {

        private final List<Cliente> datos = new ArrayList<>();
        private final AtomicLong secuencia = new AtomicLong();

        @Override
        public Cliente guardar(Cliente cliente) {
            Long id = cliente.getId() != null ? cliente.getId() : secuencia.incrementAndGet();
            Cliente guardado = new Cliente(id, cliente.getDocumento(), cliente.getNombreCompleto(),
                    cliente.getEmail(), cliente.getTelefono(), cliente.isActivo(), cliente.getFechaRegistro());
            datos.removeIf(c -> c.getId().equals(id));
            datos.add(guardado);
            return guardado;
        }

        @Override
        public Optional<Cliente> buscarPorId(Long id) {
            return datos.stream().filter(c -> c.getId().equals(id)).findFirst();
        }

        @Override
        public List<Cliente> listar() {
            return List.copyOf(datos);
        }

        @Override
        public List<Cliente> buscarPorNombre(String texto) {
            return datos.stream()
                    .filter(c -> c.getNombreCompleto().toLowerCase().contains(texto.toLowerCase()))
                    .toList();
        }

        @Override
        public boolean existePorDocumento(String documento) {
            return datos.stream().anyMatch(c -> c.getDocumento().equals(documento));
        }

        @Override
        public boolean existePorEmail(String email) {
            return datos.stream().anyMatch(c -> c.getEmail().equals(email));
        }

        @Override
        public boolean existeOtroConDocumento(String documento, Long idExcluido) {
            return datos.stream().anyMatch(c -> c.getDocumento().equals(documento) && !c.getId().equals(idExcluido));
        }

        @Override
        public boolean existeOtroConEmail(String email, Long idExcluido) {
            return datos.stream().anyMatch(c -> c.getEmail().equals(email) && !c.getId().equals(idExcluido));
        }

        @Override
        public void eliminarPorId(Long id) {
            datos.removeIf(c -> c.getId().equals(id));
        }
    }
}
