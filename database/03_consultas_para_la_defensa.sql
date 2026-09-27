/*
AUTOCARE 360 - CONSULTAS ÚTILES PARA LA DEFENSA
Ejecuta cada consulta por separado (cursor encima + Ctrl + Enter).
*/

-- 1. ¿Estoy en la base y usuario correctos?
SELECT current_database() AS base, current_user AS usuario;

-- 2. Ver los clientes (demuestra que un POST llegó a PostgreSQL)
SELECT * FROM autocare.cliente ORDER BY id_cliente;

-- 3. Ver los vehículos con la FK id_cliente
SELECT * FROM autocare.vehiculo ORDER BY id_vehiculo;

-- 4. JOIN padre-hijo: cada vehículo apunta a su propietario (relación 1:N)
SELECT v.id_vehiculo, v.placa, v.marca, v.modelo,
       v.id_cliente AS fk_id_cliente,
       c.id_cliente AS pk_id_cliente, c.nombre_completo
FROM autocare.vehiculo v
JOIN autocare.cliente c ON c.id_cliente = v.id_cliente
ORDER BY v.id_vehiculo;

-- 5. ¿Cuántos vehículos tiene cada cliente? (muchos hijos -> un padre)
SELECT c.id_cliente, c.nombre_completo, COUNT(v.id_vehiculo) AS cantidad_vehiculos
FROM autocare.cliente c
LEFT JOIN autocare.vehiculo v ON v.id_cliente = c.id_cliente
GROUP BY c.id_cliente, c.nombre_completo
ORDER BY c.id_cliente;

-- 6. Todas las restricciones (PK, FK, UNIQUE, CHECK) de mis tablas
SELECT tc.table_name, tc.constraint_name, tc.constraint_type
FROM information_schema.table_constraints tc
WHERE tc.table_schema = 'autocare'
ORDER BY tc.table_name, tc.constraint_type;

-- 7. Columnas reales de la tabla vehiculo (tipo y si acepta NULL)
SELECT column_name, data_type, is_nullable
FROM information_schema.columns
WHERE table_schema = 'autocare' AND table_name = 'vehiculo'
ORDER BY ordinal_position;
