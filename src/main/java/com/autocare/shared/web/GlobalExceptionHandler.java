package com.autocare.shared.web;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import com.autocare.cliente.domain.exception.ClienteConVehiculosException;
import com.autocare.cliente.domain.exception.ClienteDuplicadoException;
import com.autocare.cliente.domain.exception.ClienteNoEncontradoException;
import com.autocare.vehiculo.domain.exception.KilometrajeInvalidoException;
import com.autocare.vehiculo.domain.exception.PlacaDuplicadaException;
import com.autocare.vehiculo.domain.exception.PropietarioInactivoException;
import com.autocare.vehiculo.domain.exception.VehiculoNoEncontradoException;
import com.autocare.vehiculo.domain.exception.VinDuplicadoException;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Manejo centralizado de errores.
 * Los controllers y services NO tienen try/catch: lanzan excepciones con significado
 * de negocio y esta clase las traduce al status HTTP correcto con el formato ApiError.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // ---------------------------------------------------------------- 404
    @ExceptionHandler({ClienteNoEncontradoException.class, VehiculoNoEncontradoException.class})
    public ResponseEntity<ApiError> manejarNoEncontrado(RuntimeException ex, HttpServletRequest request) {
        return construir(HttpStatus.NOT_FOUND, ex.getMessage(), request, Map.of());
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ApiError> manejarRutaInexistente(NoResourceFoundException ex, HttpServletRequest request) {
        return construir(HttpStatus.NOT_FOUND, "No existe la ruta " + request.getRequestURI(), request, Map.of());
    }

    // ---------------------------------------------------------------- 409
    @ExceptionHandler({
            ClienteDuplicadoException.class,
            ClienteConVehiculosException.class,
            PlacaDuplicadaException.class,
            VinDuplicadoException.class,
            PropietarioInactivoException.class,
            KilometrajeInvalidoException.class
    })
    public ResponseEntity<ApiError> manejarConflicto(RuntimeException ex, HttpServletRequest request) {
        return construir(HttpStatus.CONFLICT, ex.getMessage(), request, Map.of());
    }

    /**
     * Última barrera: PostgreSQL rechazó la operación por una restricción (UNIQUE, FK, CHECK).
     * Pasa, por ejemplo, si dos peticiones simultáneas registran la misma placa.
     * No devolvemos el mensaje interno de PostgreSQL al cliente: solo lo registramos en el log.
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiError> manejarIntegridad(DataIntegrityViolationException ex, HttpServletRequest request) {
        log.warn("Violación de integridad en {}: {}", request.getRequestURI(), ex.getMostSpecificCause().getMessage());
        return construir(HttpStatus.CONFLICT,
                "La operación no se puede realizar porque viola una restricción de la base de datos",
                request, Map.of());
    }

    // ---------------------------------------------------------------- 400
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> manejarValidacion(MethodArgumentNotValidException ex, HttpServletRequest request) {
        Map<String, String> errores = new LinkedHashMap<>();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errores.putIfAbsent(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return construir(HttpStatus.BAD_REQUEST, "La solicitud tiene datos inválidos", request, errores);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> manejarJsonInvalido(HttpMessageNotReadableException ex, HttpServletRequest request) {
        return construir(HttpStatus.BAD_REQUEST,
                "El cuerpo no es un JSON válido o algún valor tiene un formato incorrecto",
                request, Map.of());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiError> manejarTipoIncorrecto(MethodArgumentTypeMismatchException ex, HttpServletRequest request) {
        String mensaje = "El valor '" + ex.getValue() + "' no es válido para el parámetro '" + ex.getName() + "'";
        return construir(HttpStatus.BAD_REQUEST, mensaje, request, Map.of());
    }

    // ---------------------------------------------------------------- 405 y 415
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiError> manejarMetodoNoPermitido(HttpRequestMethodNotSupportedException ex, HttpServletRequest request) {
        return construir(HttpStatus.METHOD_NOT_ALLOWED,
                "El método " + ex.getMethod() + " no está permitido en esta ruta", request, Map.of());
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ApiError> manejarTipoContenido(HttpMediaTypeNotSupportedException ex, HttpServletRequest request) {
        return construir(HttpStatus.UNSUPPORTED_MEDIA_TYPE,
                "Envía el cuerpo en formato JSON (Content-Type: application/json)", request, Map.of());
    }

    // ---------------------------------------------------------------- 500
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> manejarErrorInesperado(Exception ex, HttpServletRequest request) {
        log.error("Error inesperado en {}", request.getRequestURI(), ex);
        return construir(HttpStatus.INTERNAL_SERVER_ERROR,
                "Ocurrió un error interno en el servidor", request, Map.of());
    }

    private ResponseEntity<ApiError> construir(HttpStatus status, String mensaje,
                                               HttpServletRequest request, Map<String, String> fieldErrors) {
        ApiError cuerpo = new ApiError(
                Instant.now(),
                status.value(),
                status.getReasonPhrase(),
                mensaje,
                request.getRequestURI(),
                fieldErrors);
        return ResponseEntity.status(status).body(cuerpo);
    }
}
