# Sprint0-Judith

Proyecto desarrollado para el Sprint 0 de la asignatura de Proyecto de Aplicaciones de Biometría y Medio Ambiente.

El objetivo del Sprint 0 es comprobar el funcionamiento completo de la arquitectura del sistema, desde la generación de una medición hasta su almacenamiento y posterior visualización en una interfaz web.

## Arquitectura del sistema

El flujo general del proyecto es:

```text
SparkFun Pro nRF52840 Mini
        |
        | BLE / iBeacon
        v
Aplicación Android
        |
        | POST /mediciones
        v
Servidor REST
        |
        v
Lógica de negocio
        |
        v
Base de datos
        |
        | GET /mediciones/ultima
        v
Interfaz web
```

## Funcionamiento del Sprint 0

Durante este Sprint se utiliza una medición de prueba de ozono:

```text
Tipo: O3
Valor: 1234
```

La aplicación Android obtiene esta medición mediante una lógica fake y la envía al servidor REST.

El servidor REST recibe la medición y utiliza la lógica de negocio para validarla y almacenarla en la base de datos.

Posteriormente, la interfaz web solicita la última medición almacenada y muestra:

- fecha;
- tipo;
- valor.

## Tecnologías utilizadas

### Microprocesador

- SparkFun Pro nRF52840 Mini.
- Arduino.
- C++.
- Bluetooth Low Energy.
- iBeacon.

### Aplicación móvil

- Android Studio.
- Java.
- Bluetooth Low Energy.
- `HttpURLConnection`.

### Servidor

- PHP.
- API REST.
- JSON.

### Base de datos

- MySQL / MariaDB.

### Interfaz web

- HTML5.
- CSS.
- JavaScript.
- `fetch()`.

## Estructura del repositorio

```text
.
├── context/
├── doc/
├── prompts/
├── src/
│   ├── android/
│   ├── arduino/
│   ├── bd/
│   ├── logica/
│   ├── rest/
│   └── web/
├── author.md
├── CLAUDE.md
└── README.md
```

### Carpeta `context`

Contiene la documentación y las especificaciones utilizadas como referencia para el desarrollo del proyecto.

### Carpeta `doc`

Contiene los diseños de los distintos componentes del sistema.

Entre ellos se encuentran:

```text
bd_design.md
logica_design.md
rest_design.md
web_design.md
```

### Carpeta `prompts`

Contiene los prompts utilizados durante el desarrollo del proyecto con herramientas de inteligencia artificial.

### Carpeta `src/arduino`

Contiene el código del microprocesador encargado de generar la medición y emitir la información mediante BLE/iBeacon.

### Carpeta `src/android`

Contiene la aplicación Android encargada de trabajar con la información BLE y comunicarse con el servidor REST.

### Carpeta `src/bd`

Contiene los elementos relacionados con la base de datos.

### Carpeta `src/logica`

Contiene la lógica de negocio del sistema.

### Carpeta `src/rest`

Contiene el servidor REST.

### Carpeta `src/web`

Contiene la interfaz web encargada de consultar y mostrar la última medición almacenada.

## Base de datos

La tabla principal utilizada es:

```text
Mediciones
```

Sus campos son:

```text
id
fecha
tipo
valor
```

El campo `id` se genera automáticamente mediante autoincremento.

El campo `fecha` también se genera automáticamente al insertar una nueva medición.

## API REST

Durante el Sprint 0 se utilizan dos operaciones principales.

### Guardar una medición

Ruta:

```text
POST /mediciones
```

Ejemplo de JSON enviado:

```json
{
    "tipo": "O3",
    "valor": 1234
}
```

Esta operación permite almacenar una nueva medición en la base de datos.

### Obtener la última medición

Ruta:

```text
GET /mediciones/ultima
```

Ejemplo de respuesta:

```json
{
    "id": 5,
    "fecha": "2026-10-04 13:01:54",
    "tipo": "O3",
    "valor": 1234
}
```

Esta operación permite recuperar la última medición almacenada.

## Servidor utilizado

Durante el Sprint 0 el servidor REST se encuentra desplegado en Plesk.

### Ruta utilizada por Android

```text
https://jcivapo.upv.edu.es/rest/index.php/mediciones
```

Esta ruta se utiliza para enviar mediciones mediante `POST`.

### Ruta utilizada por la web

```text
https://jcivapo.upv.edu.es/rest/index.php/mediciones/ultima
```

Esta ruta se utiliza para obtener la última medición mediante `GET`.

## Aplicación Android

La aplicación Android incluye una lógica fake utilizada durante el Sprint 0.

La medición de prueba utilizada es:

```text
Tipo: O3
Valor: 1234
```

El flujo utilizado es:

```text
LogicaFake
     |
     v
MedicionEntrada
     |
     v
PeticionarioREST
     |
     v
POST /mediciones
```

`PeticionarioREST` utiliza `HttpURLConnection` para enviar la medición al servidor REST.

## Interfaz web

La interfaz web permite visualizar la última medición almacenada.

El flujo utilizado es:

```text
app.js
   |
   v
LogicaFake.js
   |
   v
GET /mediciones/ultima
   |
   v
Servidor REST
```

La interfaz muestra:

```text
Fecha
Tipo
Valor
```

También dispone de un botón:

```text
Actualizar
```

que vuelve a solicitar la última medición disponible.

## Pruebas realizadas

### Pruebas de la lógica de negocio

Se ha comprobado:

- validación de una medición correcta;
- rechazo de un tipo incorrecto;
- almacenamiento de una medición;
- recuperación de la última medición.

### Pruebas del servidor REST

Se ha comprobado:

- funcionamiento de `POST /mediciones`;
- funcionamiento de `GET /mediciones/ultima`;
- comunicación entre REST y lógica de negocio;
- almacenamiento real en la base de datos.

### Pruebas de Android

Se ha comprobado el envío de una medición fake:

```text
O3 = 1234
```

desde Android hasta el servidor REST.

Se ha verificado que la medición queda almacenada correctamente en la base de datos.

### Pruebas de la interfaz web

Se ha comprobado:

- obtención de la última medición mediante REST;
- representación de la fecha;
- representación del tipo;
- representación del valor;
- funcionamiento del botón `Actualizar`.

## Flujo validado

Durante el Sprint 0 se ha validado correctamente el siguiente flujo:

```text
Android
   |
   v
REST
   |
   v
Lógica de negocio
   |
   v
Base de datos
   |
   v
REST
   |
   v
Web
```

Esto permite comprobar la comunicación completa entre los principales componentes del sistema.

## Estado actual del Sprint 0

Actualmente se encuentra implementado y probado:

- emisión BLE del microprocesador;
- aplicación Android;
- lógica fake en Android;
- comunicación Android → REST;
- servidor REST;
- lógica de negocio;
- base de datos;
- almacenamiento de mediciones;
- consulta de la última medición;
- interfaz web;
- comunicación REST → Web.

## Limitaciones del Sprint 0

- La medición utilizada para probar la comunicación Android → REST es una medición fake con valor `1234`.
- La lectura BLE completa no se ha podido validar en todas las pruebas mediante un dispositivo Android físico.
- La interfaz web muestra únicamente la última medición.
- No se implementan operaciones de modificación de mediciones.
- No se implementan operaciones de eliminación de mediciones.
- No se implementan filtros ni históricos de mediciones.

## Seguridad

Las credenciales reales de acceso a la base de datos no se almacenan en el repositorio.

Los archivos incluidos en GitHub deben utilizar valores de ejemplo como:

```text
TU_BASE_DE_DATOS
TU_USUARIO
TU_PASSWORD
```

Las credenciales reales solo deben configurarse en el servidor utilizado para el despliegue.
