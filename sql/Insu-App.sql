/* 
Base de datos: MyInsuApp fase 1


--------------------------------------------------------------------------
DDL: Consiste en 5 tablas.
--------------------------------------------------------------------------
*/


CREATE DATABASE MyInsuApp;

USE MyInsuApp;

/*
Tabla: Usuario
Datos personales y clínicos.
Tipo_diabetes es ENUM ya que son valores fijos.
*/

CREATE TABLE Usuario(
    id_usuario INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    fecha_nacimiento DATE NOT NULL,
    tipo_diabetes ENUM('tipo 1', 'tipo 2', 'gestacional', 'LADA') NOT NULL
);

/*
Tabla: Pluma_Insulina
Relación: 1:N con Usuario.
Uso On Delete Restrict para proteger el historial y no poder borrar a un usuario con Plumas registradas.
*/

CREATE TABLE Pluma_Insulina(
    id_plumaInsulina INT AUTO_INCREMENT PRIMARY KEY,
    id_usuario INT NOT NULL,
    deposito_inicial INT NOT NULL,
    activo BOOLEAN DEFAULT TRUE NOT NULL,
    fecha_apertura DATE NOT NULL,

    CONSTRAINT fk_usuario_pluma
    FOREIGN KEY (id_usuario) REFERENCES Usuario (id_usuario)
    ON UPDATE CASCADE
    ON DELETE RESTRICT 
);


/*
Tabla: Zona 

Originalmente planteada como un ENUM dentro de Inyección, evoluciona a entidad propia por coherencia actual y escalabilidad futura.

    - Uso: Funciona como tabla catálogo de solo lectura para el usuario (no la va a modificar y sus datos nacen con la app). JavaFX mapeará la lógica visual directamente a través del id_zona.
    - 3FN y Escalabilidad: Al nacer como tabla independiente, permito que en 2º curso se añadan atributos específicos (estado de la zona, absorción, etc.) sin ensuciar el historial de inyecciones. 
    - Finalmente descarto el ENUM en zona_cuerpo para ganar flexibilidad. Con VARCHAR + UNIQUE puedo ampliar mediante INSERTs sin tener que recurrir a un ALTER TABLE. 
*/

CREATE TABLE Zona(
    id_zona INT AUTO_INCREMENT PRIMARY KEY,
    zona_cuerpo VARCHAR(50) NOT NULL UNIQUE
);


/*
Tabla: Inyeccion -> Principal fuente de información y relación entre tablas.
Relación 1:N con Zona y Pluma.
Aunque la lógica de dosis la llevaré de forma estricta en JAVA, añado una capa extra de seguridad con Check que asegura el comportamiento real de la pluma:
    - Dosis superiores a 0 y con un max de 30.
    - En incrementos de 0.5.
*/

CREATE TABLE Inyeccion(
    id_inyeccion INT AUTO_INCREMENT PRIMARY KEY,
    id_plumaInsulina INT NOT NULL,
    id_zona INT NOT NULL,
    dosis DECIMAL(3,1) NOT NULL,
    fecha_hora DATETIME NOT NULL,

    CONSTRAINT chk_dosis_valida CHECK (dosis > 0 AND dosis <= 30 AND DOSIS % 0.5 = 0),

    CONSTRAINT fk_inyeccion_pluma
    FOREIGN KEY (id_plumaInsulina) REFERENCES Pluma_Insulina (id_plumaInsulina)
    ON UPDATE CASCADE
    ON DELETE RESTRICT,

    CONSTRAINT fk_inyeccion_zona
    FOREIGN KEY (id_zona) REFERENCES Zona (id_zona)
    ON UPDATE CASCADE
    ON DELETE RESTRICT   

);


/*
Tabla Incidencia:
Relación 1:1 con Inyección. Cada inyección puede o no tener asociada una incidencia.

    - Unique fuerza que cada inyección tenga un tipo de incidencia como máximo (1:1)
    - En este caso uso On Delete Cascade por si permito borrar la inyección, la incidencia asociada se elimine. 
*/

CREATE TABLE Incidencia(
    id_incidencia INT AUTO_INCREMENT PRIMARY KEY,
    id_inyeccion INT UNIQUE NOT NULL,
    tipo_incidencia ENUM('dolor agudo', 'sangrado', 'bola insulina') NOT NULL,

    CONSTRAINT fk_incidencia_inyeccion
    FOREIGN KEY (id_inyeccion) REFERENCES Inyeccion (id_inyeccion)
    ON UPDATE CASCADE
    ON DELETE CASCADE
    
);


/*
--------------------------------------------------------
DML -> DATOS CON LOS QUE LA APP VA A NACER 
--------------------------------------------------------

Zona y Usuario demo
*/

INSERT INTO Usuario (nombre, apellidos, fecha_nacimiento, tipo_diabetes) VALUES
('Demo', 'Demirez', '2000-01-01', 'LADA');


INSERT INTO Zona (zona_cuerpo) VALUES
('triceps derecho'), ('triceps izquierdo'),
('abdomen derecho'), ('abdomen izquierdo'),
('gluteo derecho'), ('gluteo izquierdo'),
('muslo derecho'), ('muslo izquierdo');



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


/*
Prueba de INSERTS ERRONEOS
*/

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

-- FUNCIONA!! Aunque lo asegure en Java me ha hecho ilusión que funcione mi primer check #4025 - CONSTRAINT `chk_dosis_valida` failed

-- ------------------------------------------------------------
-- 2. PRUEBA UNIQUE -> Tabla Zona
-- ------------------------------------------------------------

-- Valor ya existente:
INSERT INTO Zona (zona_cuerpo) 
VALUES ('abdomen izquierdo');
-- Resultado: #1062 - Entrada duplicada 'abdomen izquierdo' para la clave 'zona_cuerpo'