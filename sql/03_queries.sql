/*
Este programa se centra en el registro diarío de inyecciones de insulina que se aplica un paciente diabetico. Su objetivo, además de llevar un control de las inyecciones y dosis que uno se aplica, es poder llevar un registro de incidencias y punciones para poder prevenir el desarrollo de lipodistrófias. La base de datos la he montado de forma que permita llevar un control exahistivo de ello. 

En ese aspecto, el diseño de la BD y las consultas han sido el motor de esta app. Estas queries son una de las partes del programa de las que me siento más orgulloso ya que me he centrado en no simplemente mostrar datos en crudo, si no que los he procesado para aportar utilidad real, que es lo verdaderamente potente de una base de datos relacional. Salvo las que pertenecen al historial, que si son un filtro en crudo para que el propio usuario analice, la mayoría de consultas cruzan, suman y filtran información para obtener conclusiones de valor. 

Las voy a ordenar por temática y el módulo del programa dónde las voy a usar. Las he dejado parametrizadas para JAVA, auqnue aquuí te he fijado al usuario 1. El intervalo te lo dejo con ? para que pongas el que te apetezca.  

PD: Hoy 3 de Mayo a las 15:17 me he visto con un problema de última hora horrible. Al introducir datos a futuro para las pruebas (cosa que no sucedería con el uso natural de la app), algunas queries se fastidiaron... He podido arreglarlo todo añadiendo AND i.fecha_hora BETWEEN DATE_SUB(NOW(), INTERVAL 1 DAY) AND NOW(). Ese now último pone una barrera en el presente. Micro-infarto de última hora desactivado.
*/


-- ---------
-- Inicio
-- --------

-- Consulta última inyección para la vista rápida
SELECT i.dosis, i.fecha_hora, z.zona_cuerpo
FROM inyeccion i
INNER JOIN pluma_insulina pl ON i.id_plumaInsulina = pl.id_plumaInsulina
INNER JOIN zona z ON i.id_zona = z.id_zona
WHERE pl.id_usuario = 1 AND i.fecha_hora <= NOW()
ORDER BY i.fecha_hora DESC
LIMIT 1;

-- Consulta que nos da el total consumido de la pluma a modo orientativo (solo inyecciones, purgas no se pueden contabilizar)
SELECT SUM(dosis) FROM inyeccion i
WHERE id_plumaInsulina = 1
AND i.fecha_hora <= NOW()


----------------------------------
-- DELETE y Update
-- ------------------------------

-- Borrado de inyección controlando que no hayan pasado mas de 2 horas del registro. A su vez borra incidencia gracias al on delete cascade que pusé en esa tabla
DELETE i FROM inyeccion i
INNER JOIN pluma_insulina pl ON i.id_plumaInsulina = pl.id_plumaInsulina
WHERE i.id_inyeccion = ?
AND pl.id_usuario = ? AND i.fecha_hora >= NOW() - INTERVAL 2 HOUR;

-- Update lo aplico al campo "activo" de la pluma, para desactivar todas las plumas. En JAVA lo he planteado como parte de una transacción que primero desactivan y luego se hace el insert de la nueva. 
UPDATE Pluma_Insulina
SET activo = 0
WHERE id_usuario = 1 AND activo = 1;



---------------------------------------------------------------
-- BUSQUEDAS PARA HISTORIAL
---------------------------------------------------

-- Inyecciones en rango haya o no incidencia con Left Join para obtener los null de la tabla Incidencia
SELECT
  i.id_inyeccion, i.fecha_hora, i.dosis,
  z.id_zona, z.zona_cuerpo,
  inc.id_incidencia, inc.tipo_incidencia
FROM pluma_insulina pl
INNER JOIN inyeccion i ON i.id_plumaInsulina = pl.id_plumaInsulina
INNER JOIN zona z ON i.id_zona = z.id_zona
LEFT JOIN incidencia inc ON i.id_inyeccion = inc.id_inyeccion
WHERE pl.id_usuario = 1
AND i.fecha_hora BETWEEN DATE_SUB(NOW(), INTERVAL ? DAY) AND NOW()
ORDER BY i.fecha_hora DESC;

-- Igual que el anterior pero únicamente me traigo inyecciones con incidencia.
SELECT
  i.id_inyeccion, i.fecha_hora, i.dosis,
  z.id_zona, z.zona_cuerpo,
  inc.id_incidencia, inc.tipo_incidencia
FROM pluma_insulina pl
INNER JOIN inyeccion i ON i.id_plumaInsulina = pl.id_plumaInsulina
INNER JOIN zona z ON i.id_zona = z.id_zona
INNER JOIN incidencia inc ON i.id_inyeccion = inc.id_inyeccion
WHERE pl.id_usuario = 1
AND i.fecha_hora BETWEEN DATE_SUB(NOW(), INTERVAL ? DAY) AND NOW()
ORDER BY i.fecha_hora DESC;




-- --------------------
-- CONSULTAS PARA LA VISTA INFORME
-- -----------------------------

-- Objetivo estadistico, informativo y que serán parte de la vista Informe-view y de la exportación xml. Estas van todas también en intervalo. 


-- Dosis total de los últimos (la dejo parametrizada para java) días.
SELECT SUM(i.dosis) AS total_dosis
FROM inyeccion i
INNER JOIN pluma_insulina pl
ON i.id_plumaInsulina = pl.id_plumaInsulina
WHERE pl.id_usuario = 1 
AND i.fecha_hora BETWEEN DATE_SUB(NOW(), INTERVAL ? DAY) AND NOW();

-- Promedio dosis por día (esta en java la calcularé mediante el total de dosis anterior)
SELECT SUM(i.dosis) / ? AS promedio_diario
FROM inyeccion i
INNER JOIN pluma_insulina pl
ON i.id_plumaInsulina = pl.id_plumaInsulina
WHERE pl.id_usuario = 1
AND i.fecha_hora BETWEEN DATE_SUB(NOW(), INTERVAL ? DAY) AND NOW();


-- Total de inyecciones en el periodo
SELECT COUNT(i.dosis) AS total_inyecciones
FROM inyeccion i
INNER JOIN pluma_insulina pl
ON i.id_plumaInsulina = pl.id_plumaInsulina
WHERE pl.id_usuario = 1
AND i.fecha_hora BETWEEN DATE_SUB(NOW(), INTERVAL ? DAY) AND NOW();

-- Total de incidencias en el periodo
SELECT COUNT(inc.id_incidencia) AS total_incidencias
FROM incidencia inc
INNER JOIN inyeccion i ON i.id_inyeccion = inc.id_inyeccion
INNER JOIN Pluma_Insulina pl 
ON i.id_plumaInsulina = pl.id_plumaInsulina                                
WHERE pl.id_usuario = 1 
AND i.fecha_hora BETWEEN DATE_SUB(NOW(), INTERVAL ? DAY) AND NOW();

-- Frecuencia media de inyecciones diarias a la semana (Esta también la voy a calcular en Java)
SELECT COUNT(i.id_inyeccion) / ? AS frecuencia_inyecciones_diarias
FROM inyeccion i
INNER JOIN pluma_insulina pl
ON i.id_plumaInsulina = pl.id_plumaInsulina
WHERE pl.id_usuario = 1
AND i.fecha_hora BETWEEN DATE_SUB(NOW(), INTERVAL ? DAY) AND NOW()

-- Pico max unidades (La dosis mayor de una vez, interesante de obtener por si afecta a la lipodistrófia) insulina en el rango de 7 días
SELECT MAX(i.dosis) AS dosis_mayor
FROM inyeccion i
INNER JOIN pluma_insulina pl
ON i.id_plumaInsulina = pl.id_plumaInsulina
WHERE pl.id_usuario = 1
AND i.fecha_hora BETWEEN DATE_SUB(NOW(), INTERVAL ? DAY) AND NOW();


-- Zona mas usada y total de inyecciones el rango
SELECT z.zona_cuerpo AS zona_mas_usada,
COUNT(i.id_inyeccion) AS total_inyecciones
FROM zona z
INNER JOIN inyeccion i ON z.id_zona = i.id_zona
INNER JOIN pluma_insulina pl
ON i.id_plumaInsulina = pl.id_plumaInsulina
WHERE pl.id_usuario = 1
AND i.fecha_hora BETWEEN DATE_SUB(NOW(), INTERVAL ? DAY) AND NOW()
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
AND i.fecha_hora BETWEEN DATE_SUB(NOW(), INTERVAL ? DAY) AND NOW()
GROUP BY z.id_zona, z.zona_cuerpo
ORDER BY total_incidencias DESC
LIMIT 1;

-- Estos datos los saco con el objetivo de montar un gráfico de quesitos y otro de barras en JavaFX

-- Uso de zonas con total de inyecciones en cada una en el rango. 
SELECT z.zona_cuerpo AS zonas_cuerpo,
COUNT(i.id_inyeccion) AS total_inyecciones
FROM zona z
INNER JOIN inyeccion i ON i.id_zona = z.id_zona
INNER JOIN pluma_insulina pl ON i.id_plumaInsulina = pl.id_plumaInsulina
WHERE pl.id_usuario = 1
AND i.fecha_hora BETWEEN DATE_SUB(NOW(), INTERVAL ? DAY) AND NOW()
GROUP BY z.id_zona, z.zona_cuerpo
ORDER BY total_inyecciones DESC;

-- Datos para el StackedBarChart, el grçafico de barras. Me dio unos buenos dolores de cabeza montar eso en JAVAFX, yo pensaba que el front era mas sencillo. 

-- Busqueda y contabilidad de Incidencias por zona y tipo de incidencias en cada zona (semana) Este no lo usaré en java
SELECT
    z.zona_cuerpo AS zonas_cuerpo,
    inc.tipo_incidencia,
    COUNT(inc.id_incidencia) AS total_incidencias
FROM zona z
INNER JOIN inyeccion i ON i.id_zona = z.id_zona
INNER JOIN incidencia inc ON inc.id_inyeccion = i.id_inyeccion
INNER JOIN pluma_insulina pl ON i.id_plumaInsulina = pl.id_plumaInsulina
WHERE pl.id_usuario = 1
AND i.fecha_hora BETWEEN DATE_SUB(NOW(), INTERVAL ? DAY) AND NOW()
GROUP BY z.id_zona, z.zona_cuerpo, inc.tipo_incidencia
ORDER BY zonas_cuerpo, total_incidencias DESC;

-- Busqueda para el chart similar a la anterior pero incluyendo Nulls con Left para encontrar inyecciones SIN incidencia... Este es el que me llevo a Java, para ello el count es desde id_inyeccion y la necesidad de nulls para procesarlos allí. 
SELECT
 z.zona_cuerpo AS zonas_cuerpo,
    inc.tipo_incidencia,
  COUNT(i.id_inyeccion) AS total
FROM zona z
INNER JOIN inyeccion i ON i.id_zona = z.id_zona
LEFT JOIN incidencia inc ON inc.id_inyeccion = i.id_inyeccion
INNER JOIN pluma_insulina pl ON i.id_plumaInsulina = pl.id_plumaInsulina
WHERE pl.id_usuario = 1
AND i.fecha_hora BETWEEN DATE_SUB(NOW(), INTERVAL ? DAY) AND NOW()
GROUP BY z.id_zona, z.zona_cuerpo, inc.tipo_incidencia
ORDER BY zonas_cuerpo ASC;



/* Me ha parecido interesante buscar una validación que me de la antiguiendad de la primera inyección registrada por el usuario... Ideal para que las consultas de intevalos no fracasen al obtener medias y validar desde java que se pueden pedir. Buscando info he encontrado esto: 
https://www.datacamp.com/es/tutorial/datediff-sql-function */

-- Antiguiedad de datos de inyección con Datediff

SELECT DATEDIFF(CURDATE(), MIN(i.fecha_hora)) AS dias_uso
FROM inyeccion i 
INNER JOIN pluma_insulina pl ON i.id_plumaInsulina = pl.id_plumaInsulina
WHERE pl.id_usuario = 1;


-- Ahora las siguientes 250 líneas... No, es broma, ya no hay más :).
