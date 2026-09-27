package com.autocare.shared.web;

import java.time.Instant;
import java.util.Map;

/**
 * Formato único de error de toda la API.
 * Todos los errores (400, 404, 405, 409, 415, 500) responden con esta misma forma.
 *
 * @param timestamp   momento del error
 * @param status      código HTTP (por ejemplo 404)
 * @param error       nombre del código HTTP (por ejemplo "Not Found")
 * @param message     explicación para quien usa la API
 * @param path        ruta que se llamó
 * @param fieldErrors errores por campo (solo en validaciones; si no hay, va vacío)
 */
public record ApiError(
        Instant timestamp,
        int status,
        String error,
        String message,
        String path,
        Map<String, String> fieldErrors
) {
}
