### 1. Tipo de sistema donde se ejecuta la aplicación

**Sistema elegido:** PC de usuario (Desktop).

**Descripción de la aplicación:**
MyInsuApp es una aplicación de escritorio desarrollada con JavaFX. Tanto la interfaz gráfica (cliente) como el motor de base de datos (MariaDB) se ejecutan de forma local en la misma máquina del usuario.

Es importante destacar que este proyecto se presenta como un **MVP** y una demo de funcionamiento. El objetivo principal es validar la lógica de negocio, el procesado de datos y la generación de informes estadísticos, sirviendo como base para una futura transición a plataformas móviles.

**Recursos necesarios:**
El equipo debe ser capaz de gestionar dos procesos de forma simultánea:
*   **Servicio de Base de Datos:** MariaDB ejecutándose en segundo plano para la persistencia de datos.
*   **Entorno de ejecución Java (JVM):** Encargado de procesar la lógica de negocio y renderizar la interfaz de usuario.

**Justificación de esta elección:**
Se ha optado por un entorno local en un PC de usuario por los siguientes motivos:
1.  **Privacidad y Seguridad:** Al tratar con datos médicos sensibles, mantener la base de datos en local garantiza que la información no salga del equipo del usuario.
2.  **Rendimiento:** Sin latencia de red, lo que permite que la carga de históricos y la generación de gráficos estadísticos sea inmediata.
3.  **Disponibilidad local:** Al utilizar `localhost`, la aplicación es totalmente funcional sin conexión a internet. El paciente puede registrar sus dosis en cualquier circunstancia, ya que la comunicación entre el programa y los datos es interna del equipo.
4.  **Uso Individual:** La aplicación está diseñada para la gestión personal del tratamiento, por lo que un sistema de escritorio es el entorno más natural y eficiente para el usuario final.
5.  **Naturaleza del proyecto (MVP):** Como demo de funcionamiento, el entorno de escritorio permite centrarnos en lo esencial del proyecto: el registro, seguimiento y procesado de datos, asegurando que la arquitectura sea sólida antes de escalar el sistema.
6.  **Contexto y Limitaciones:** Habiendo centrado el aprendizaje de este primer año en Java, el entorno de escritorio es el ecosistema ideal para aplicar los conocimientos adquiridos. Soy consciente de que, por su naturaleza, el despliegue final óptimo sería en dispositivos Android e iOS; objetivo que me marco para la evolución del proyecto en el segundo curso.



### 2. Requisitos de hardware

Siendo realista con el proyecto (se ejecuta en la maquina virtual de Java y tiene que mantener el motor de MariaDB), sus exigencias son modestas.

**CPU:**
*   **Mínima:** Procesador básico de 2 núcleos (Dual-Core). Es lo justo para levantar el sistema operativo y los dos servicios.
*   **Recomendada:** Procesador de 4 núcleos (tipo Intel Core i5 / Ryzen 5 o equivalentes). Da el margen necesario para que la interfaz gráfica de JavaFX y el dibujado de las gráficas fluyan.

**RAM:**
*   **Mínima:** 4 GB. Según las pruebas de rendimiento realizadas durante la ejecución del proyecto, la Máquina Virtual de Java (JVM) reserva de entrada unos 1300 MB para su entorno, mientras que el proceso de la aplicación en sí (interfaz y lógica JavaFX) consume unos 150 MB. El motor de MariaDB en reposo tiene un consumo residual. Sumando estos ~1.5 GB al consumo base de Windows, 4 GB es el límite inferior.
*   **Recomendada:** 8 GB. Aunque el consumo de la aplicación y la base de datos es bajo, tener 8 GB garantiza que el sistema operativo trabaje totalmente holgado, evitando cualquier tipo de lag al renderizar las pantallas o procesar las consultas SQL del historial o el informe.

**Almacenamiento:**
*   **Mínimo:** 2 GB libres en disco. Esto cubre la instalación de MariaDB, el JDK de Java 21, el código del proyecto y deja hueco para los primeros datos y logs del sistema.
*   **Recomendado:** 5 GB o más y disco SSD para acelerar las lecturas de la base de datos, ademas de asegurar espacio suficiente para las exportaciones XML y copias de segridad del historial. 

**Periféricos:**
*   Teclado, ratón y un monitor con una resolución estándar (al menos 1366x768) para asegurar que las tablas de datos y la interfaz encajan en pantalla sin recortes y se ven a una resolución adecuada.



### 3. Sistema operativo recomendado

**Sistema Operativo Principal:** Microsoft Windows.
**Versión exacta:** Windows 10 o Windows 11 (64 bits).

**Motivo de la elección:**
*   **Compatibilidad requisitos del framework:** Aunque Java es un lenguaje multiplataforma por naturaleza, el proyecto está desarrollado y testeado íntegramente sobre Windows usando Java 21 y JavaFX. Es el entorno donde tengo la certeza de que la JVM renderiza la interfaz gráfica correctamente y donde las rutas del sistema de archivos no darán sorpresas.
*   **Facilidad de instalación:** Al tratarse de una demo (MVP) que otros podrían querer probar, Windows ofrece la ventaja de su alta implementación. Además desde Windows, levantar el servidor MariaDB y el JDK de Java es un proceso rápido guiado por asistentes.
*   **Disponibilidad de paquetes:** Herramientas como XAMPP (que incluye MariaDB) o los ejecutables del propio Java están perfectamente empaquetados y optimizados para Widnows.
*   **Seguridad:** Para esta fase del proyecto, Windows permite gestionar la seguridad de forma directa. El propio Firewall de Windows se encarga de aislar el puerto de la base de datos (3306), asegurando que solo reciba conexiones locales.


### 4. Instalación del entorno

**4.1. Programas necesarios:**
*   **JDK 21** (Java).
*   **XAMPP** (usaremos su módulo de MySQL/MariaDB).
*   **IntelliJ IDEA** (IDE recomendado, es donde se ha desarrollado).

**4.2. Librerías y dependencias:**
No hay que descargar nada a mano. Al abrir el proyecto, Maven (a través del archivo `pom.xml`) se encarga de descargar automáticamente:
*   **JavaFX (21.0.6):** Para la interfaz gráfica.
*   **MySQL Connector/J (9.4.0):** Para conectar con la base de datos.
*   **Jakarta XML Bind (4.0.5):** Para leer y guardar archivos XML.

**4.3. Descarga del proyecto:**
1.  Entrar al enlace de GitHub y descargar el código: https://github.com/Eloy-i/MyInsuApp
2.  Descomprimir el archivo en una carpeta local.

**4.4. Preparación de la Base de Datos:**
1.  Abrir XAMPP y arrancar el servicio de MySQL.
2.  El usuario de la base de datos es `root` con contraseña `root`.
3.  Entrar a phpMyAdmin y ejecutar los scripts de la carpeta `/sql` en este orden:
    *   Primero `01_esquema.sql` (crea la base de datos y las tablas).
    *   Segundo `02_inserts.sql` (mete los datos de prueba).

**4.5. Arranque de la aplicación:**
1.  Abrir la carpeta del proyecto ya descomprimida con IntelliJ IDEA.
2.  Asegurar  que IntelliJ lea el `pom.xml` y descargue las dependencias.
3.  En el árbol de carpetas, ir a: `src/main/java/org/example/myinsuapp/Launcher.java`.
4.  Hacer clic en el botón de "Play" para iniciar la aplicación.

**4.6. Usuarios de prueba:**
La base de datos viene cargada con un "usuario demo" (ID 1) que tiene datos de todo un mes, ideal para probar cómo se ven las gráficas y el historial.
Si se quiere probar la app desde cero con un usuario limpio, hay que ir al archivo:
`src/main/java/org/example/myinsuapp/service/EstadoService.java`
Y cambiar la línea: `private final int ID_USUARIO_DEMO = 1;` por un `2`.


### 5. Usuarios, permisos y estructura de carpetas

**5.1. Usuarios y permisos:**
Al tratarse de una aplicación de escritorio local en fase MVP, el modelo de seguridad es básico y no depende de roles complejos:
*   **Usuarios del sistema:** La aplicación es de uso personal y se ejecuta bajo la cuenta de usuario estándar de Windows que haya iniciado sesión.
*   **Usuario de Base de Datos:** Se emplea el usuario `root` (password `root`) para conectar con MariaDB.
*   **Permisos requeridos:** La ejecución no requiere elevar privilegios ni usar permisos de administrador en Windows. La aplicación solo necesita permisos estándar de lectura y escritura para poder generar los informes en el directorio `/docs/xml`.

**5.2. Estructura de carpetas:**

*   **Código e Instalación:**
    *   `/src/main/java/`: Contiene el código fuente y los controladores.
    *   `/src/main/resources/`: Almacena las vistas .FXML.
*   **Configuración de BD:**
    *   `/sql/`: Directorio que contiene los scripts necesarios para replicar la estructura de la base de datos y sus inserciones iniciales.
*   **Datos y Copias de seguridad:**
    *   Los registros principales residen en el motor de MariaDB (almacenados en el directorio interno de XAMPP).
    *   `/docs/xml/`: Directorio utilizado por la aplicación para guardar las copias de seguridad de los informes exportados en formato XML.


### 6. Mantenimiento básico

**Actualizaciones:**
*   Como es un proyecto cerrado (MVP) que funciona en local, lo ideal es no tocar las versiones de XAMPP ni del JDK mientras todo funcione bien.
*   Las dependencias del `pom.xml` (JavaFX, MySQL Connector) se tocarían si en el futuro dan un error grave y definitivamente cuanto se vaya a ampliar el proyecto en el segundo curso.

**Revisiones periódicas:**
*   **Espacio en disco:** Lo único importante es asegurar que el disco duro no se llene. Si se queda sin espacio, MariaDB fallará al guardar nuevas inyecciones y la app no podrá generar los informes en `/docs/xml/`.

**Protocolo de actuación ante fallos:**
*   **No conecta a la base de datos:** Abrir XAMPP y comprobar que MySQL está arrancado. Si se ha quedado pillado, pararlo y volver a darle a Start. También desde `org/example/myinsuapp/database/DBConnection.java` se puede modificar la configuarción de acceso a la base de datos.
*   **Errores de la aplicación:** Si la app falla o se cierra de golpe, hay que ir a la consola de IntelliJ y leer el error exacto y ver en qué línea del código ha fallado.
*   **Datos corruptos (Restaurar copia):** Si haciendo pruebas la base de datos se rompe, lo más rápido es entrar a phpMyAdmin, borrar las tablas y volver a ejecutar los dos archivos `.sql` de la carpeta `/sql/` para dejarla limpia como al principio.



### 7. Evidencias del funcionamiento

Para validar el despliegue local y el manejo de excepciones, se adjunta en la carpeta `/docs/sistemas/` un vídeo demostrativo de un minuto que evidencia:

*   **Escenario 1 (Flujo normal):** Se muestra el servicio MySQL levantado en XAMPP, seguido del arranque exitoso de la aplicación y la carga correcta de la interfaz gráfica y los datos.
*   **Escenario 2 (Tolerancia a fallos):** Se apaga intencionadamente el módulo MySQL en XAMPP para simular una caída del servidor. Al volver a arrancar la aplicación, se comprueba que el sistema captura la excepción de conexión y levanta correctamente la vista de error controlada, evitando un cierre inesperado.

Adicionalmente adjunto capturas

```markdown
![Pantalla principal](docs/empleabilidad/captura_inicio.png)
![Registro de inyecciones](docs/empleabilidad/captura_registro.png)
![Historial](docs/empleabilidad/captura_historial.png)
![Informe](docs/empleabilidad/captura_informe.png)
![Grafico Uso](docs/empleabilidad/captura_grafico_1.png)
![Gráfico de Estado](docs/empleabilidad/captura_grafico2.png)

```

### 8. Esquema de funcionamiento

```mermaid
graph TD
A[Usuario] -->|Interactúa| B(Interfaz JavaFX)
B -->|Controla eventos| C[Controladores]
C -->|Lógica de Negocio| D[Services]
D -->|Gestión de CRUD| E[Modelos de Dominio]
D -->|Acceso a Persistencia| F[DAOs]
F -->|Consultas SQL| G[(MariaDB)]

    subgraph Generación de Informes
    F -->|Consultas Cruzadas| H[DTOs - Objetos de Transferencia]
    H -->|Vista Previa| B
    H -->|Persistencia Física| I[Exportador XML - /docs/xml]
    end