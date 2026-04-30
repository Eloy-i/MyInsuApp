/*
--------------------------------------------------------
DML -> DATOS DE CONFIGURACIÓN Y PRUEBA
--------------------------------------------------------
*/

-- Inserción del usuario de pruebas
INSERT INTO Usuario (nombre, apellidos, fecha_nacimiento, tipo_diabetes) VALUES
('Demo', 'Demirez', '2000-01-01', 'LADA');

-- Inserción del catálogo de zonas. Fijo los IDs para asegurar el correcto mapeo desde Java
INSERT INTO Zona (id_zona, zona_cuerpo) VALUES
(1, 'triceps derecho'),
(2, 'triceps izquierdo'),
(3, 'abdomen derecho'),
(4, 'abdomen izquierdo'),
(5, 'gluteo derecho'),
(6, 'gluteo izquierdo'),
(7, 'muslo derecho'),
(8, 'muslo izquierdo');

/*
-----------------------
DML -> DATOS DE PRUEBA
-----------------------
*/

/*
Pluma de prueba, la creación de nuevas plumas y desechar viejas se hará desde java
*/

INSERT INTO Pluma_Insulina (id_usuario, deposito_inicial, activo, fecha_apertura)
VALUES (1, 300, TRUE, CURDATE());

/*
INSERTS de prueba en Inyecciones: Basado en 1 semana de mi actividad
Pluma: 1 y Usuario: 1
*/

INSERT INTO Inyeccion (id_plumaInsulina, id_zona, dosis, fecha_hora) VALUES
-- DÍA 1 (7 Inyecciones)
(1, 4, 9.5, '2026-04-07 08:15:00'),
(1, 2, 2.5, '2026-04-07 11:30:00'),
(1, 4, 8.0, '2026-04-07 14:20:00'),
(1, 7, 2.0, '2026-04-07 17:00:00'),
(1, 2, 4.0, '2026-04-07 20:45:00'),
(1, 1, 2.0, '2026-04-07 22:30:00'),
(1, 4, 2.5, '2026-04-07 23:50:00'),

-- DÍA 2 (5 Inyecciones)
(1, 2, 7.0, '2026-04-08 08:45:00'),
(1, 4, 4.0, '2026-04-08 14:10:00'),
(1, 3, 2.5, '2026-04-08 18:00:00'),
(1, 2, 6.5, '2026-04-08 20:30:00'),
(1, 2, 1.0, '2026-04-08 23:15:00'),

-- DÍA 3 (6 Inyecciones)
(1, 4, 8.5, '2026-04-09 08:05:00'),
(1, 8, 3.0, '2026-04-09 11:00:00'),
(1, 4, 5.5, '2026-04-09 14:45:00'),
(1, 4, 2.0, '2026-04-09 16:30:00'),
(1, 2, 4.0, '2026-04-09 20:15:00'),
(1, 5, 2.5, '2026-04-09 23:55:00'),

-- DÍA 4 (5 Inyecciones)
(1, 2, 8.0, '2026-04-10 08:30:00'),
(1, 4, 7.0, '2026-04-10 13:50:00'),
(1, 2, 4.0, '2026-04-10 17:15:00'),
(1, 2, 2.5, '2026-04-10 20:55:00'),
(1, 4, 3.0, '2026-04-10 22:40:00'),

-- DÍA 5 (4 Inyecciones)
(1, 4, 6.5, '2026-04-11 08:10:00'),
(1, 4, 10.0, '2026-04-11 14:05:00'),
(1, 2, 6.5, '2026-04-11 19:00:00'),
(1, 4, 2.0, '2026-04-11 23:20:00'),

-- DÍA 6 (5 Inyecciones)
(1, 2, 6.0, '2026-04-12 08:50:00'),
(1, 4, 3.5, '2026-04-12 14:30:00'),
(1, 1, 2.0, '2026-04-12 17:45:00'),
(1, 2, 4.0, '2026-04-12 21:00:00'),
(1, 2, 1.5, '2026-04-12 23:50:00'),

-- DÍA 7 (5 Inyecciones)
(1, 4, 8.5, '2026-04-13 08:20:00'),
(1, 2, 11.0, '2026-04-13 14:15:00'),
(1, 4, 2.5, '2026-04-13 16:00:00'),
(1, 2, 1.5, '2026-04-13 20:40:00'),
(1, 4, 4.0, '2026-04-13 22:15:00');


/*
REGISTRO DE INCIDENCIAS para pruebas
Basado en los pinchazos de esta semana reales que me he ido realizando.
PD: Pensar si añadir más cantidad de registros para hacer busquedas, aunque creo que es suficiente.
*/


INSERT INTO Incidencia (id_inyeccion, tipo_incidencia) VALUES
-- 1. Sangrado en Abdomen Izquierdo (Inyección ID 3)
(3, 'sangrado'),

-- 2. OTRO Sangrado en Abdomen Derecho
(10, 'sangrado'),

-- 3. Sangrado en Glúteo Derecho (día 3)
(18, 'sangrado'),

-- 4. Bola de insulina en Triceps Izquierdo (día 6)
(28, 'bola insulina'),

-- 5. Bola de insulina en Triceps Derecho (día 6)
(30, 'sangrado');



-- Prueba de INSERTS ERRONEOS
-- ------------------------------------------------
-- 1. PRUEBAS CHECK -> Tabla Inyección
-- ------------------------------------------------

-- Dosis no multiplo de 0.5
INSERT INTO Inyeccion (id_plumaInsulina, id_zona, dosis, fecha_hora)
VALUES (1, 4, 3.3, '2026-04-14 10:00:00');

-- Dosis encima de 30
INSERT INTO Inyeccion (id_plumaInsulina, id_zona, dosis, fecha_hora)
VALUES (1, 4, 38.0, '2026-04-14 10:00:00');

-- Dosis igual a 0
INSERT INTO Inyeccion (id_plumaInsulina, id_zona, dosis, fecha_hora)
VALUES (1, 4, 0, '2026-04-14 10:00:00');

-- ------------------------------------------------------------
-- 2. PRUEBA UNIQUE -> Tabla Zona
-- ------------------------------------------------------------

-- Valor ya existente:
INSERT INTO Zona (zona_cuerpo)
VALUES ('abdomen izquierdo');
-- Resultado: #1062 - Entrada duplicada 'abdomen izquierdo' para la clave 'zona_cuerpo'
