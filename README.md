# AutoCare 360 · Backend

Taller automotriz multimarca (proyecto PA-02, Programación Aplicada 2026-2).
Spring Boot 4 · Java 21 · Monolito hexagonal · JPA/Hibernate · PostgreSQL · API REST.

Este backend implementa la relación **Cliente (1) → Vehículo (N)** con CRUD completo,
validaciones, reglas de negocio, manejo global de errores y transacciones
(capítulos 03 a 08 de las guías del curso).

---

## 1. Cómo arrancarlo (paso a paso)

### 1.1 Base de datos (una sola vez)
1. PostgreSQL encendido. La base `autocare360` y el usuario `autocare_admin` ya existen (guía de DataGrip).
2. En DataGrip, conectado a `autocare360` con `autocare_admin`, abre y ejecuta completo
   `database/01_crear_schema_autocare.sql`.
   - Crea el schema `autocare` y las tablas `cliente` y `vehiculo`.
   - Borra las tablas viejas `public.clientes` y `public.vehiculos` que había creado `ddl-auto=update`.
3. (Opcional) `database/02_datos_de_prueba.sql` carga 2 clientes y 2 vehículos de ejemplo.

### 1.2 Contraseña de la base
La contraseña **no** está en `application.yml`. Se lee del archivo `.env` de la raíz del proyecto:

```
DB_PASSWORD=la_contraseña_de_autocare_admin
```

- `.env` está en `.gitignore`: **nunca se sube a GitHub**.
- `.env.example` es la plantilla que sí se sube, para que cada integrante cree su propio `.env`.

### 1.3 Abrir y ejecutar en IntelliJ
1. **File → Open** → carpeta `autocare-360` (la que tiene `pom.xml`) → **Trust Project**.
2. Espera a que Maven descargue las dependencias (barra inferior derecha).
3. Si pide JDK: **Setup SDK → Download JDK → 21**.
4. Abre `src/main/java/com/autocare/Autocare360Application.java` y pulsa el ▶ verde.
5. En la consola debe aparecer `Tomcat started on port 9090`.

Si falla al arrancar:

| Mensaje | Causa | Solución |
|---|---|---|
| `Could not resolve placeholder 'DB_PASSWORD'` | Falta el archivo `.env` | Créalo en la raíz del proyecto (paso 1.2) |
| `password authentication failed` | La contraseña del `.env` no coincide | Corrige el `.env` |
| `Schema-validation: missing table [autocare.cliente]` | No se ejecutó el script SQL | Paso 1.1 |
| `Connection to localhost:5432 refused` | PostgreSQL apagado | Enciéndelo (Servicios de Windows) |

### 1.4 Probar la API con Postman
1. Postman → **Import** → `docs/AutoCare360_API_completa.postman_collection.json`.
2. Ejecuta las carpetas **en orden** (1, 2, 3, 4). Las primeras guardan los ids que usan las siguientes.
3. O pulsa los tres puntos de la colección → **Run** para ejecutar las 30 pruebas de una vez.
   La carpeta 4 limpia los datos para poder volver a correr todo.

---

## 2. Endpoints

| Método | Ruta | Qué hace | Éxito | Errores posibles |
|---|---|---|---|---|
| GET | `/api/health` | Comprueba que el backend está vivo | 200 | — |
| POST | `/api/clientes` | Registra un cliente | 201 | 400, 409 (documento/email repetido) |
| GET | `/api/clientes` | Lista clientes (`?nombre=` opcional) | 200 | — |
| GET | `/api/clientes/{id}` | Busca un cliente | 200 | 404 |
| PUT | `/api/clientes/{id}` | Actualiza un cliente (incluye `activo`) | 200 | 400, 404, 409 |
| DELETE | `/api/clientes/{id}` | Elimina un cliente sin vehículos | 204 | 404, 409 (tiene vehículos) |
| POST | `/api/vehiculos` | Registra un vehículo | 201 | 400, 404 (cliente), 409 (RN-01, placa/VIN) |
| GET | `/api/vehiculos/{id}` | Busca un vehículo | 200 | 404 |
| GET | `/api/vehiculos/cliente/{clienteId}` | Vehículos de un cliente | 200 | 404 |
| PUT | `/api/vehiculos/{id}` | Actualiza un vehículo | 200 | 400, 404, 409 (placa/VIN, km que baja) |
| DELETE | `/api/vehiculos/{id}` | Elimina un vehículo | 204 | 404 |

Todos los errores responden con el mismo formato (`ApiError`):

```json
{
  "timestamp": "2026-09-27T15:04:05.123Z",
  "status": 409,
  "error": "Conflict",
  "message": "Ya existe un vehículo registrado con la placa 2345KLM",
  "path": "/api/vehiculos",
  "fieldErrors": {}
}
```

**Cómo provocar un 500 para la defensa:** con la app corriendo, detén el servicio de PostgreSQL
y haz `GET /api/clientes`. El backend responde 500 con el mismo formato de error.

---

## 3. Reglas de negocio

| Regla | Dónde se valida | Respuesta |
|---|---|---|
| Campos obligatorios, longitudes, formato de email, placa, VIN | Request DTO (`@Valid`) | 400 |
| Documento y email únicos | `ClienteService` + UNIQUE en PostgreSQL | 409 |
| **RN-01**: el vehículo debe pertenecer a un propietario **activo** | `VehiculoService` | 409 |
| **RF-15**: no duplicar placa ni VIN | `VehiculoService` + UNIQUE en PostgreSQL | 409 |
| El kilometraje no puede bajar | Dominio `Vehiculo.actualizarDatos` | 409 |
| No borrar un cliente con vehículos (se conserva el historial) | FK de PostgreSQL → adapter | 409 |
| Año entre 1950 y 2100, kilometraje ≥ 0, combustible válido | DTO + CHECK en PostgreSQL | 400 |

---

## 4. Arquitectura (monolito hexagonal)

```
com.autocare
├── Autocare360Application          ← clase principal (paquete base)
├── shared/web                      ← ApiError, GlobalExceptionHandler, HealthController
├── cliente                         ← módulo padre
│   ├── domain
│   │   ├── model                   ← Cliente (Java puro, sin JPA)
│   │   ├── exception               ← ClienteNoEncontrado, ClienteDuplicado, ClienteConVehiculos
│   │   └── port
│   │       ├── in                  ← casos de uso (Registrar, Consultar, Actualizar, Eliminar) + commands
│   │       └── out                 ← ClienteRepositoryPort
│   ├── application/service         ← ClienteService (reglas + @Transactional)
│   └── infrastructure/adapter
│       ├── in/web                  ← ClienteController, dto, mapper
│       └── out/persistence         ← ClienteJpaEntity, SpringDataClienteRepository, mapper, adapter
└── vehiculo                        ← módulo dependiente (misma estructura)
    └── ... VehiculoJpaEntity tiene @ManyToOne(LAZY) + @JoinColumn(id_cliente)
```

Dirección de dependencias: `infrastructure → application → domain`.
El módulo `vehiculo` usa del módulo `cliente` solo su Port IN `ConsultarClienteUseCase`
(y, dentro de infraestructura, la referencia JPA para la FK). El módulo `cliente` no conoce a `vehiculo`.

---

## 5. Pruebas automáticas

| Prueba | Tipo | Necesita PostgreSQL |
|---|---|---|
| `ClienteServiceTest` | Unitaria (repositorio falso en memoria) | No |
| `VehiculoTest` | Unitaria del dominio (regla del kilometraje) | No |
| `Autocare360ApplicationTests` | Integración (arranca todo Spring) | Sí |

En IntelliJ: clic derecho en `src/test/java` → **Run 'All Tests'**.

---

## 6. Git: subir el proyecto por partes

Este proyecto no trae historial Git. Crea el repositorio y haz commits pequeños;
lo ideal es que **cada integrante haga uno o más** para que la rúbrica vea su aporte.

```bash
git init
git add .gitignore .env.example pom.xml mvnw mvnw.cmd .mvn .gitattributes README.md
git commit -m "chore: configurar proyecto Maven de AutoCare 360"

git add database src/main/resources
git commit -m "feat: schema autocare con tablas cliente y vehiculo; ddl-auto validate"

git add src/main/java/com/autocare/Autocare360Application.java src/main/java/com/autocare/shared
git commit -m "feat: manejo global de errores con ApiError y endpoint de salud"

git add src/main/java/com/autocare/cliente src/test/java/com/autocare/cliente
git commit -m "feat: modulo cliente hexagonal con JPA, CRUD y reglas de negocio"

git add src/main/java/com/autocare/vehiculo src/test/java/com/autocare/vehiculo
git commit -m "feat: modulo vehiculo con relacion 1:N, RN-01 y RF-15"

git add src/test/java/com/autocare/Autocare360ApplicationTests.java docs
git commit -m "test: prueba de contexto y coleccion Postman completa"

git branch -M main
git remote add origin https://github.com/USUARIO/autocare-360.git
git push -u origin main
```
