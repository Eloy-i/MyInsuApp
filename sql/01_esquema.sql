
-- Base de datos: MyInsuApp fase 1


--------------------------------------------------------------------------
-- DDL: Consiste en 5 tablas.
--------------------------------------------------------------------------



CREATE DATABASE MyInsuApp;

USE MyInsuApp;

/*
Tabla: Usuario
Datos personales y clínicos.
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



-- Tabla: Zona

CREATE TABLE Zona(
    id_zona INT AUTO_INCREMENT PRIMARY KEY,
    zona_cuerpo VARCHAR(50) NOT NULL UNIQUE
);



/*
Tabla: Inyeccion -> Principal fuente de información y relación entre tablas.
Relación 1:N con Zona y Pluma.
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
Relación 1:1 opcional con Inyección.
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
