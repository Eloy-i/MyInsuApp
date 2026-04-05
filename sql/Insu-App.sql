/* 
Creación de la base de datos

En esta primera fase consiste en 5 tablas.
*/
Create database MyInsuApp;

USE MyInsuApp;

/*
Tabla Usuario
Los datos no pueden ser nulos, tipo_diabetes es ENUM ya que son valores fijos (como pasará más adelante en otras dos tablas).
*/

CREATE TABLE Usuario(
    id_usuario INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    fecha_nacimiento DATE NOT NULL,
    tipo_diabetes ENUM('tipo 1', 'tipo 2', 'gestacional', 'LADA') NOT NULL
);

/*
Tabla para Plumas de insulina
(Relación 1:N con Usuario)
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
La tabla Zona recoge donde se ha realizado el pinchazo... Se relacionará con Inyección
*/

CREATE TABLE Zona(
    id_zona INT AUTO_INCREMENT PRIMARY KEY,
    zona_cuerpo ENUM('triceps derecho', 'triceps izquierdo', 'abdomen derecho', 'abdomen izquierdo', 'gluteo derecho', 'gluteo izquierdo', 'muslo derecho', 'muslo izquierdo') NOT NULL
);

/*
Inyecciones es la tabla principal y se relaciona 1:N con Zona y Pluma y 1:1 con Incidencia

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
    - En este caso por el tipo de relación permito que en caso de que en JAVA permita borrar inyecciones (no creo que lo haga porque no suelen permitirlo las apps de salud), el dato de incidencia se elimine con ese borrado.
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


