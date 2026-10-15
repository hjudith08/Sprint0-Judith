# Sprint0-Judith

Proyecto desarrollado para el Sprint 0 de la asignatura de Proyecto de Aplicaciones de Biometría y Medio Ambiente.

El objetivo del Sprint 0 es comprobar el funcionamiento completo de la arquitectura mediante una medición ficticia generada en la SparkFun Pro nRF52840 Mini y mostrada finalmente en una interfaz web.

## Flujo del Sprint 0

```text
SparkFun Pro nRF52840 Mini
        |
        | BLE / iBeacon JU-GTI
        v
Aplicación Android (Java)
        |
        | POST /mediciones
        v
Communication (PHP)
        |
        v
Business Logic (PHP)
        |
        v
Base de datos MySQL / MariaDB
        |
        | GET /mediciones/ultima
        v
Frontend Business Logic
        |
        v
Interfaz web
```

La medición ficticia utilizada durante el Sprint 0 es:

```text
Tipo: O3
Valor: 1234
```

El valor nace en el microprocesador, se publica mediante BLE/iBeacon, Android lo recibe y lo envía al servidor. La capa de comunicación delega en la lógica de negocio, la medición se almacena en la base de datos y posteriormente la web recupera y muestra la última medición.

## Estructura del repositorio

```text
Sprint0-Judith/
├── .gitattributes
├── .gitignore
├── README.md
├── author.md
│
├── doc/
│   ├── android_design.md
│   ├── business_logic_design.md
│   ├── communication_design.md
│   ├── database_design.md
│   ├── frontend_business_logic_design.md
│   ├── microprocesador_design.md
│   ├── web_design.md
│   └── prompts/
│
└── src/
    ├── android/
    ├── business_logic/
    ├── communication/
    ├── database/
    ├── frontend_business_logic/
    │   └── LogicaFake.js
    ├── microprocesador/
    └── web/
        ├── index.html
        ├── css/
        ├── js/
        │   └── app.js
        └── tests/
            └── test_logica_fake.js
```

## Correspondencia entre diseño e implementación

```text
doc/android_design.md
        |
        v
src/android/

doc/business_logic_design.md
        |
        v
src/business_logic/

doc/communication_design.md
        |
        v
src/communication/

doc/database_design.md
        |
        v
src/database/

doc/frontend_business_logic_design.md
        |
        v
src/frontend_business_logic/

doc/microprocesador_design.md
        |
        v
src/microprocesador/

doc/web_design.md
        |
        v
src/web/
```

## Componentes

### Microprocesador

La implementación se encuentra en:

```text
src/microprocesador/
```

Se utiliza una SparkFun Pro nRF52840 Mini que publica mediante BLE/iBeacon una medición ficticia de O3.

Durante el Sprint 0 se utiliza:

```text
O3 = 1234
```

El dispositivo anunciado es:

```text
JU-GTI
```

El tipo de medición y el contador se codifican en `major`, mientras que el valor de la medición se transporta en `minor`.

### Android

La aplicación Android se encuentra en:

```text
src/android/
```

Está implementada en Java.

La aplicación:

```text
escanea dispositivos BLE
detecta JU-GTI
interpreta la trama iBeacon
obtiene major y minor
recupera el valor O3 = 1234
evita procesar anuncios repetidos
envía la medición al backend mediante REST
```

### Communication

La capa de comunicación se encuentra en:

```text
src/communication/
```

Está implementada en PHP y gestiona HTTP, JSON, rutas y códigos de estado.

Las operaciones principales son:

```text
POST /mediciones
GET /mediciones/ultima
```

La capa de comunicación no contiene las reglas de negocio. Delega las operaciones en `LogicaNegocio`.

### Business Logic

La lógica de negocio se encuentra en:

```text
src/business_logic/
```

La clase principal es:

```text
LogicaNegocio
```

Las operaciones principales son:

```text
validarMedicion()
guardarMedicion()
obtenerUltimaMedicion()
```

Esta capa es independiente de HTTP.

### Base de datos

El esquema reproducible se encuentra en:

```text
src/database/crear_tabla.sql
```

La tabla utilizada es:

```text
Mediciones
```

con los campos:

```text
id
fecha
tipo
valor
```

`id` es autoincremental y `fecha` utiliza `CURRENT_TIMESTAMP`.

### Frontend Business Logic

La lógica cliente web se encuentra en:

```text
src/frontend_business_logic/LogicaFake.js
```

La operación principal es:

```text
obtenerUltimaMedicion() --> [ Medicion ]
```

`LogicaFake` encapsula la petición:

```text
GET /mediciones/ultima
```

La interfaz gráfica no realiza directamente la llamada `fetch()`.

### Interfaz web

La interfaz se encuentra en:

```text
src/web/
```

Está formada por HTML, CSS y JavaScript.

`app.js` utiliza `LogicaFake.js` para obtener la última medición y mostrarla en pantalla.

## Arquitectura

El backend mantiene la siguiente separación:

```text
Communication
      |
      v
Business Logic
      |
      v
Database
```

La lógica de negocio no depende de HTTP.

En la parte web:

```text
Web GUI
   |
   v
Frontend Business Logic
   |
   v
Communication
```

La GUI no realiza directamente la comunicación HTTP.

## Pruebas

### Business Logic

Archivo:

```text
src/business_logic/test_logica.php
```

Ejecutar:

```bash
php src/business_logic/test_logica.php
```

### Communication

Archivo:

```text
src/communication/test_rest.php
```

Ejecutar:

```bash
php src/communication/test_rest.php
```

### Frontend Business Logic

Archivo:

```text
src/web/tests/test_logica_fake.js
```

Ejecutar:

```bash
node src/web/tests/test_logica_fake.js
```

Este test comprueba:

```text
recuperación de O3 = 1234
respuesta sin mediciones
errores HTTP
```

### Android

Los tests pueden ejecutarse desde Android Studio o mediante Gradle desde el proyecto Android.

En Windows:

```text
gradlew.bat test
```

## Test funcional completo

El criterio principal del Sprint 0 es comprobar el recorrido completo de una medición ficticia:

```text
Microprocesador
      |
      | O3 = 1234
      v
BLE / iBeacon
      |
      v
Android
      |
      v
POST /mediciones
      |
      v
Communication
      |
      v
Business Logic
      |
      v
Base de datos
      |
      v
GET /mediciones/ultima
      |
      v
Frontend Business Logic
      |
      v
Interfaz web
```

Durante la prueba física se comprobó correctamente que:

```text
la SparkFun publica O3 = 1234
Android detecta JU-GTI
Android recibe el valor 1234
Android envía la medición al REST
el backend valida la medición
la base de datos almacena O3 = 1234
GET /mediciones/ultima devuelve O3 = 1234
la web muestra O3 = 1234
```

Por tanto, la cadena funcional completa del Sprint 0 se ha comprobado correctamente.

## Documentación de diseño

Los diseños se encuentran en:

```text
doc/
```

y corresponden directamente con las implementaciones actuales del repositorio.

Los prompts utilizados durante el desarrollo se encuentran en:

```text
doc/prompts/
```

## Uso de Inteligencia Artificial

Durante el Sprint 0 se ha utilizado Inteligencia Artificial como herramienta de apoyo para revisar, adaptar y documentar el proyecto.

El proceso seguido ha sido:

```text
diseño
   |
   v
prompt
   |
   v
implementación o adaptación
   |
   v
revisión
   |
   v
pruebas
```

La aceptación final del funcionamiento se ha comprobado mediante pruebas del código y mediante la prueba física completa.

## Seguridad

Las credenciales reales de la base de datos no se almacenan en el repositorio público.

El código del repositorio utiliza valores genéricos o configuración externa para las credenciales del servidor.

## Autora

La autora del proyecto se identifica en:

```text
author.md
```
