package org.example.myinsuapp.database;

public interface DBSchem {

    // Esquema para tabla Usuario
    String TAB_USUARIO = "Usuario";
    String COL_USER_ID = "id_usuario";
    String COL_USER_NOMBRE = "nombre";
    String COL_USER_APELL = "apellidos";
    String COL_USER_NACIMIENTO = "fecha_nacimiento";
    String COL_USER_DT = "tipo_diabetes";

    // Esquema tabla Pluma_Insulina
    String TAB_PLUMA = "Pluma_Insulina";
    String COL_PLUMA_ID = "id_plumaInsulina";
    String COL_PLUMA_USER = "id_usuario";
    String COL_PLUMA_DEPOSITO = "deposito_inicial";
    String COL_PLUMA_ACTIVO = "activo";
    String COL_PLUMA_FECHA = "fecha_apertura";

    // Esquema tabla Zona
    String TAB_ZONA = "Zona";
    String COL_ZONA_ID = "id_zona";
    String COL_ZONA_NOMBRE = "zona_cuerpo";

    // Esquema tabla Inyeccion
    String TAB_INYECCION = "Inyeccion";
    String COL_INY_ID = "id_inyeccion";
    String COL_INY_PLUMA = "id_plumaInsulina";
    String COL_INY_ZONA = "id_zona";
    String COL_INY_DOSIS = "dosis";
    String COL_INY_FECHA = "fecha_hora";

    // Esquema tabla Incidencia
    String TAB_INCIDENCIA = "Incidencia";
    String COL_INC_ID = "id_incidencia";
    String COL_INC_INY = "id_inyeccion";
    String COL_INC_TIPO = "tipo_incidencia";




}
