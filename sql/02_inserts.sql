/*
--------------------------------------------------------
DML -> DATOS DE CONFIGURACIÓN Y PRUEBA
--------------------------------------------------------
*/

-- Inserción del usuario de pruebas... EL primero tiene datos precargados, incluso a futuro. El segundo tiene la base de datos vacía.
-- Para evitar errores voy a fijar los id de los usuarios.
INSERT INTO Usuario (id_usuario, nombre, apellidos, fecha_nacimiento, tipo_diabetes) VALUES
(1, 'Demo', 'Demirez', '2000-01-01', 'LADA'),
(2, 'Carl', 'Asimov', '1990-12-23', 'tipo 1');

-- Inserción del catálogo de zonas. Fijo los IDs para asegurar el correcto mapeo desde Java
INSERT INTO Zona (id_zona, zona_cuerpo) VALUES
(1, 'abdomen derecho'),
(2, 'abdomen izquierdo'),
(3, 'muslo derecho'),
(4, 'muslo izquierdo'),
(5, 'triceps derecho'),
(6, 'triceps izquierdo'),
(7, 'gluteo derecho'),
(8, 'gluteo izquierdo');

/*
Pluma de prueba, la creación de nuevas plumas y desechar viejas se hará desde java, dejo un historial de tres plumas
dos deshechadas con sus inserciones asociadas y una activa hoy tres de mayo..
*/

INSERT INTO Pluma_Insulina (id_usuario, deposito_inicial, activo, fecha_apertura) VALUES
(1, 300, FALSE, '2026-04-10'),
(1, 300, FALSE, '2026-04-21'),
(1, 300, TRUE, '2026-05-03');


/*
----------------------------------------------
INSERTS de prueba Usuario 1 para Inyecciones.
----------------------------------------------
Normalmente nos hacemos de cuatro a ocho inyecciones de insulina al día...
Estos inserts los he generado con IA con instrucciones claras sobre como deben repartirse... De cara a la app van a mostrar
un claro abuso de zonas (en mi caso el triceps se lleva la palma)... Son una cantidad enorme pero porque necesito al menos
treinta ddías de registros y como no sé cuando lo vais a poder corregir, he mandado registros del Usuario 1 a futuro, hasta el día 10 de mayo.
Antes de darlas por cerradas están comprobadas con mi app y algunas modificadas desde la bd para ajustarlas con las plumas.
*/

-- Inserts para la pluma 1 del 10 al 21 de abril
INSERT INTO Inyeccion (id_plumaInsulina, id_zona, dosis, fecha_hora) VALUES

(1, 5, 8.5, '2026-04-10 08:15:00'), (1, 6, 9.5, '2026-04-10 14:00:00'), (1, 1, 3.0, '2026-04-10 21:00:00'), (1, 5, 2.0, '2026-04-10 23:30:00'),
(1, 6, 9.0, '2026-04-11 07:50:00'), (1, 5, 10.0, '2026-04-11 14:15:00'), (1, 2, 2.5, '2026-04-11 20:45:00'), (1, 6, 1.5, '2026-04-11 23:10:00'),
(1, 5, 8.0, '2026-04-12 08:30:00'), (1, 6, 10.5, '2026-04-12 13:50:00'), (1, 1, 3.5, '2026-04-12 21:15:00'), (1, 5, 1.5, '2026-04-12 23:45:00'),

(1, 6, 9.5, '2026-04-13 08:05:00'), (1, 5, 9.0, '2026-04-13 14:30:00'), (1, 3, 2.5, '2026-04-13 20:30:00'), (1, 6, 2.0, '2026-04-13 23:00:00'),
(1, 5, 8.5, '2026-04-14 08:20:00'), (1, 6, 11.0, '2026-04-14 14:10:00'), (1, 1, 3.0, '2026-04-14 21:00:00'), (1, 5, 2.0, '2026-04-14 23:20:00'),
(1, 6, 9.0, '2026-04-15 07:45:00'), (1, 1, 9.5, '2026-04-15 14:00:00'), (1, 2, 2.5, '2026-04-15 21:15:00'), (1, 6, 1.5, '2026-04-15 23:50:00'),

(1, 5, 10.0, '2026-04-16 08:10:00'), (1, 6, 8.5, '2026-04-16 14:20:00'), (1, 1, 3.5, '2026-04-16 20:45:00'), (1, 5, 2.0, '2026-04-16 23:15:00'),
(1, 6, 8.5, '2026-04-17 08:30:00'), (1, 5, 10.5, '2026-04-17 14:30:00'), (1, 7, 3.0, '2026-04-17 21:00:00'), (1, 6, 1.5, '2026-04-17 23:30:00'),
(1, 5, 9.0, '2026-04-18 07:50:00'), (1, 6, 9.5, '2026-04-18 13:50:00'), (1, 1, 3.5, '2026-04-18 21:15:00'), (1, 5, 2.0, '2026-04-18 23:45:00'),

(1, 6, 9.0, '2026-04-19 08:15:00'), (1, 5, 10.0, '2026-04-19 14:10:00'), (1, 2, 3.0, '2026-04-19 20:30:00'), (1, 6, 1.5, '2026-04-19 23:00:00'),
(1, 5, 8.5, '2026-04-20 08:00:00'), (1, 6, 9.5, '2026-04-20 14:00:00'), (1, 1, 3.5, '2026-04-20 21:00:00'), (1, 5, 2.0, '2026-04-20 23:30:00'),
(1, 6, 8.0, '2026-04-21 07:45:00');

-- Inserts de la pluma 2 hasta el 3 de mayo

INSERT INTO Inyeccion (id_plumaInsulina, id_zona, dosis, fecha_hora) VALUES

(2, 5, 9.5, '2026-04-21 14:15:00'), (2, 2, 2.5, '2026-04-21 20:45:00'), (2, 6, 1.5, '2026-04-21 23:15:00'),

(2, 5, 8.0, '2026-04-22 08:10:00'), (2, 6, 9.5, '2026-04-22 14:30:00'), (2, 1, 2.5, '2026-04-22 21:15:00'), (2, 5, 1.5, '2026-04-22 23:45:00'),
(2, 6, 8.5, '2026-04-23 08:00:00'), (2, 5, 10.0, '2026-04-23 14:00:00'), (2, 4, 3.0, '2026-04-23 20:30:00'), (2, 6, 1.5, '2026-04-23 23:00:00'),
(2, 5, 7.5, '2026-04-24 07:50:00'), (2, 6, 9.0, '2026-04-24 13:50:00'), (2, 1, 3.0, '2026-04-24 21:00:00'), (2, 5, 2.0, '2026-04-24 23:20:00'),

(2, 6, 8.5, '2026-04-25 08:30:00'), (2, 5, 9.5, '2026-04-25 14:10:00'), (2, 2, 2.5, '2026-04-25 21:15:00'), (2, 6, 1.5, '2026-04-25 23:50:00'),
(2, 5, 9.0, '2026-04-26 08:05:00'), (2, 6, 8.0, '2026-04-26 14:20:00'), (2, 1, 3.5, '2026-04-26 21:00:00'), (2, 5, 1.5, '2026-04-26 23:30:00'),
(2, 6, 7.5, '2026-04-27 07:45:00'), (2, 1, 10.0, '2026-04-27 14:00:00'), (2, 8, 2.5, '2026-04-27 21:15:00'), (2, 6, 2.0, '2026-04-27 23:45:00'),

(2, 5, 8.5, '2026-04-28 08:15:00'), (2, 6, 9.0, '2026-04-28 14:30:00'), (2, 1, 3.0, '2026-04-28 20:45:00'), (2, 5, 1.5, '2026-04-28 23:15:00'),
(2, 6, 9.0, '2026-04-29 08:30:00'), (2, 5, 8.5, '2026-04-29 14:10:00'), (2, 2, 2.5, '2026-04-29 21:00:00'), (2, 6, 1.5, '2026-04-29 23:30:00'),
(2, 5, 8.0, '2026-04-30 07:50:00'), (2, 6, 9.5, '2026-04-30 13:50:00'), (2, 1, 3.0, '2026-04-30 21:15:00'), (2, 5, 2.0, '2026-04-30 23:45:00'),

(2, 6, 8.5, '2026-05-01 08:15:00'), (2, 5, 9.0, '2026-05-01 14:20:00'), (2, 1, 2.5, '2026-05-01 20:30:00'), (2, 6, 1.5, '2026-05-01 23:00:00'),
(2, 5, 7.5, '2026-05-02 08:00:00'), (2, 6, 8.5, '2026-05-02 14:10:00'), (2, 2, 3.0, '2026-05-02 21:00:00'), (2, 5, 1.5, '2026-05-02 23:20:00'),
(2, 6, 8.0, '2026-05-03 08:30:00');

-- Pluma 3 la activa actualmente... Tienen datos del futuro pero desde la app en un uso real de registro evidentemente esto no permito que suceda.

INSERT INTO Inyeccion (id_plumaInsulina, id_zona, dosis, fecha_hora) VALUES

(3, 5, 6.0, '2026-05-03 14:15:00'), (3, 1, 2.0, '2026-05-03 21:00:00'), (3, 6, 1.5, '2026-05-03 23:30:00'),

(3, 5, 5.0, '2026-05-04 08:00:00'), (3, 6, 6.5, '2026-05-04 14:00:00'), (3, 1, 2.0, '2026-05-04 21:15:00'),
(3, 6, 5.5, '2026-05-05 08:10:00'), (3, 5, 6.0, '2026-05-05 14:30:00'), (3, 2, 2.5, '2026-05-05 20:45:00'),
(3, 5, 5.0, '2026-05-06 07:50:00'), (3, 6, 6.5, '2026-05-06 13:50:00'), (3, 1, 2.0, '2026-05-06 21:00:00'),

(3, 6, 4.5, '2026-05-07 08:30:00'), (3, 5, 6.0, '2026-05-07 14:10:00'), (3, 7, 2.0, '2026-05-07 21:15:00'),
(3, 5, 5.5, '2026-05-08 08:15:00'), (3, 6, 5.5, '2026-05-08 14:20:00'), (3, 1, 2.5, '2026-05-08 20:30:00'),
(3, 6, 5.0, '2026-05-09 08:00:00'), (3, 5, 6.5, '2026-05-09 14:00:00'), (3, 2, 2.0, '2026-05-09 21:00:00'),
(3, 5, 4.5, '2026-05-10 08:20:00'), (3, 6, 6.0, '2026-05-10 14:30:00'), (3, 1, 2.5, '2026-05-10 20:45:00');


/*
REGISTRO DE INCIDENCIAS para pruebas

El patron  para ajustarlo a la realidad es el siguiente

Sangrado en Abdomen D (ID 1).
Bolas y Dolor por masacre en Tríceps (IDs 5, 6).
Dolor en Muslos al estar estar la piel de la zona poco acostumbrada.
*/

-- Incidencias

INSERT INTO Incidencia (id_inyeccion, tipo_incidencia) VALUES
(1, 'bola insulina'),
(3, 'sangrado'),
(6, 'dolor agudo'),
(11, 'sangrado'),
(15, 'sangrado'),
(18, 'bola insulina'),
(22, 'dolor agudo'),
(26, 'sangrado'),
(32, 'bola insulina'),
(38, 'bola insulina'),
(42, 'sangrado'),
(50, 'bola insulina'),
(54, 'bola insulina'),
(58, 'sangrado'),
(63, 'sangrado'),
(67, 'sangrado'),
(71, 'dolor agudo'),
(76, 'sangrado'),
(78, 'sangrado'),
(84, 'sangrado'),
(89, 'sangrado'),
(92, 'bola insulina'),
(97, 'sangrado'),
(103, 'dolor agudo'),
(110, 'sangrado'),
(115, 'dolor agudo');





-- NO PARTE DE LOS INSERTS DE INICIO

-- Prueba de INSERTS ERRONEOS -> Solo para LUZ, para comprobar restricciones.
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
