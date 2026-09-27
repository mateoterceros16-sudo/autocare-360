/*
AUTOCARE 360 - DATOS DE PRUEBA (OPCIONAL)
Úsalo si quieres tener datos en la base sin crearlos desde Postman.
Ejecuta primero 01_crear_schema_autocare.sql.
*/
INSERT INTO autocare.cliente (documento, nombre_completo, email, telefono, activo)
VALUES ('4455667', 'Carla Méndez', 'carla.mendez@correo.com', '71234567', TRUE),
       ('9988776', 'Jorge Salinas', 'jorge.salinas@correo.com', NULL, FALSE);

INSERT INTO autocare.vehiculo (id_cliente, placa, vin, marca, modelo, anio, kilometraje, tipo_combustible)
SELECT c.id_cliente, '2345KLM', '1HGCM82633A004352', 'Toyota', 'Corolla', 2018, 85400, 'GASOLINA'
FROM autocare.cliente c WHERE c.documento = '4455667';

INSERT INTO autocare.vehiculo (id_cliente, placa, vin, marca, modelo, anio, kilometraje, tipo_combustible)
SELECT c.id_cliente, '5678XYZ', NULL, 'Nissan', 'Frontier', 2021, 40200, 'DIESEL'
FROM autocare.cliente c WHERE c.documento = '4455667';
