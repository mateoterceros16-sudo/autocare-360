package com.autocare;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Clase principal de AutoCare 360.
 * Está en el paquete base com.autocare para que el escaneo de componentes
 * encuentre todos los módulos: cliente, vehiculo y shared.
 */
@SpringBootApplication
public class Autocare360Application {

    public static void main(String[] args) {
        SpringApplication.run(Autocare360Application.class, args);
    }
}
