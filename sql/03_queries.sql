-- ---------
-- Inicio
-- --------

-- Consulta última inyección para la vista rápida
SELECT i.dosis, i.fecha_hora, z.zona_cuerpo
FROM inyeccion i
INNER JOIN pluma_insulina pl ON i.id_plumaInsulina = pl.id_plumaInsulina
INNER JOIN zona z ON i.id_zona = z.id_zona
WHERE pl.id_usuario = 1
ORDER BY i.fecha_hora DESC
LIMIT 1;

-- Fecha, hora, unidades, zona y incidencia (haya o no)-> Left Join
SELECT
    i.id_inyeccion,
    i.fecha_hora,
    i.dosis,
    z.id_zona,
    z.zona_cuerpo,
    inc.id_incidencia,
    inc.tipo_incidencia
FROM inyeccion i
INNER JOIN pluma_insulina pl ON i.id_plumaInsulina = pl.id_plumaInsulina
INNER JOIN zona z ON i.id_zona = z.id_zona
LEFT JOIN incidencia inc ON i.id_inyeccion = inc.id_inyeccion
WHERE pl.id_usuario = ?
  AND DATE(i.fecha_hora) >= DATE_SUB(CURDATE(), INTERVAL ? DAY)
ORDER BY i.fecha_hora DESC;

-- Borrado de inyección controlando que no hayan pasado mas de 2 horas del registro. A su vez borra incidencia gracias al on delete cascade que pusé en esa tabla
DELETE i FROM inyeccion i
INNER JOIN pluma_insulina pl ON i.id_plumaInsulina = pl.id_plumaInsulina
WHERE i.id_inyeccion = ?
  AND pl.id_usuario = ?
  AND i.fecha_hora >= NOW() - INTERVAL 2 HOUR;





-- --------------------
CONSULTAS PARA LA VISTA INFORME
-- -----------------------------
/*
Objetivo estadistico, informativo y serán parte de la vista Informe y de la exportación xml
Las iba a dejar parametrizadas para java pero como eso lu puedo hacer cuando las copie te las dejo perfectamente
preparadas para copiar y pegar en phpMyadmin. Aunque en mi demo solo habrá un usuario ya he dejado la busqueda adaptada
con Inner jJoin para asegurar que el dato pertenece a un ususario concreto.


-- Dosis total de los últimos (la dejo parametrizada para java) días completos
SELECT SUM(i.dosis) AS total_dosis
FROM inyeccion i
INNER JOIN pluma_insulina pl
ON i.id_plumaInsulina = pl.id_plumaInsulina
WHERE pl.id_usuario = 1 AND DATE(i.fecha_hora) >= DATE_SUB(CURDATE(), INTERVAL 7 DAY);

-- Promedio dosis por día (esta en java la calcularé mediante el total de dosis anterior)
SELECT SUM(i.dosis) / 7 AS promedio_diario
FROM inyeccion i
INNER JOIN pluma_insulina pl
ON i.id_plumaInsulina = pl.id_plumaInsulina
WHERE pl.id_usuario = 1 AND DATE(i.fecha_hora) >= DATE_SUB(curdate(), INTERVAL 7 DAY);

-- Total de inyecciones
SELECT COUNT(i.dosis) AS total_inyecciones
FROM inyeccion i
INNER JOIN pluma_insulina pl
ON i.id_plumaInsulina = pl.id_plumaInsulina
WHERE pl.id_usuario = 1 AND
DATE(i.fecha_hora) >= DATE_SUB(CURDATE(), INTERVAL 7 DAY);

-- Frecuencia media de inyecciones diarias a la semana
SELECT COUNT(i.id_inyeccion) / 7 AS frecuencia_inyecciones_diarias
FROM inyeccion i
INNER JOIN pluma_insulina pl
ON i.id_plumaInsulina = pl.id_plumaInsulina
WHERE pl.id_usuario = 1
AND DATE(i.fecha_hora) >= DATE_SUB(CURDATE(), INTERVAL 7 DAY);

-- Pico max unidades insulina en el rango de 7 días
SELECT MAX(i.dosis) AS pico_max
FROM inyeccion i
INNER JOIN pluma_insulina pl
ON i.id_plumaInsulina = pl.id_plumaInsulina
WHERE pl.id_usuario = 1
AND DATE(i.fecha_hora) >= DATE_SUB(CURDATE(), INTERVAL 7 DAY);

-- Zona mas usada y total de inyecciones el rango
SELECT z.zona_cuerpo AS zona_mas_usada,
COUNT(i.id_inyeccion) AS total_inyecciones
FROM zona z
INNER JOIN inyeccion i ON z.id_zona = i.id_zona
INNER JOIN pluma_insulina pl
ON i.id_plumaInsulina = pl.id_plumaInsulina
WHERE pl.id_usuario = 1
AND DATE(i.fecha_hora) >= DATE_SUB(CURDATE(), INTERVAL 7 DAY)
GROUP BY z.id_zona, z.zona_cuerpo
ORDER BY total_inyecciones DESC
LIMIT 1;

-- Zona con mas incidencias en el rango
SELECT z.zona_cuerpo AS zona_mas_problematica,
COUNT(inc.id_incidencia) AS total_incidencias
FROM zona z
INNER JOIN inyeccion i ON z.id_zona = i.id_zona
INNER JOIN incidencia inc ON inc.id_inyeccion = i.id_inyeccion
INNER JOIN pluma_insulina pl ON i.id_plumaInsulina = pl.id_plumaInsulina
WHERE pl.id_usuario = 1
  AND DATE(i.fecha_hora) >= DATE_SUB(CURDATE(), INTERVAL 7 DAY)
GROUP BY z.id_zona, z.zona_cuerpo
ORDER BY total_incidencias DESC
LIMIT 1;

-- Datos para el graficod e quesitos.

-- Uso de zonas en el rango. En este caso para montarlo si que voy a necesitar ambos datos
SELECT z.zona_cuerpo AS zonas_cuerpo,
COUNT(i.id_inyeccion) AS total_inyecciones
FROM zona z
INNER JOIN inyeccion i ON i.id_zona = z.id_zona
INNER JOIN pluma_insulina pl ON i.id_plumaInsulina = pl.id_plumaInsulina
WHERE pl.id_usuario = 1
AND DATE(i.fecha_hora) >= DATE_SUB(CURDATE(), INTERVAL 7 DAY)
GROUP BY z.id_zona, z.zona_cuerpo
ORDER BY total_inyecciones DESC;

-- Datos para el StackedBarChart

-- Busqueda para el chart de Incidencias por zona y tipo de incidencias en cada zona (semana)
SELECT
    z.zona_cuerpo AS zonas_cuerpo,
    inc.tipo_incidencia,
    COUNT(inc.id_incidencia) AS total_incidencias
FROM zona z
INNER JOIN inyeccion i ON i.id_zona = z.id_zona
INNER JOIN incidencia inc ON inc.id_inyeccion = i.id_inyeccion
INNER JOIN pluma_insulina pl ON i.id_plumaInsulina = pl.id_plumaInsulina
WHERE pl.id_usuario = 1
  AND DATE(i.fecha_hora) >= DATE_SUB(CURDATE(), INTERVAL 7 DAY)
GROUP BY z.id_zona, z.zona_cuerpo, inc.tipo_incidencia
ORDER BY z.zona_cuerpo, total_incidencias DESC;

-- Busqueda para el chart similar a la anterior pero incluyendo Nulls con Left para encontrar inyecciones SIN incidencia... Este es el que me llevo a Java.
SELECT
    z.zona_cuerpo AS zonas_cuerpo,
    inc.tipo_incidencia,
    COUNT(i.id_inyeccion) AS total
FROM zona z
INNER JOIN inyeccion i ON z.id_zona = i.id_zona
INNER JOIN pluma_insulina pl ON i.id_plumaInsulina = pl.id_plumaInsulina
LEFT JOIN incidencia inc ON inc.id_inyeccion = i.id_inyeccion
WHERE pl.id_usuario = 1
  AND DATE(i.fecha_hora) >= DATE_SUB(CURDATE(), INTERVAL 7 DAY)
GROUP BY z.id_zona, z.zona_cuerpo, inc.tipo_incidencia
ORDER BY z.zona_cuerpo, total DESC;

-- El Update se aplica a la tabla pluma... Cuando inicamos una pluma nueva la anterior pasa a estado inactivo.
UPDATE Pluma_Insulina
SET activo = 0
WHERE id_usuario = 1 AND activo = 1;