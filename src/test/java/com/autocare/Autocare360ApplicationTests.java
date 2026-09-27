package com.autocare;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Prueba de integración: levanta TODO el contexto de Spring.
 * Necesita PostgreSQL encendido y el schema autocare creado, porque
 * con ddl-auto=validate Hibernate comprueba que las tablas coincidan con las entidades.
 */
@SpringBootTest
class Autocare360ApplicationTests {

    @Test
    void contextLoads() {
    }
}
