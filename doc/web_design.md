# Diseño del componente: web

## 1. Diseño del Componente

El componente `web` permite visualizar la última medición almacenada en el sistema.

La interfaz web no accede directamente a la base de datos ni contiene lógica de negocio.

La comunicación con el servidor REST se realiza mediante una lógica fake en JavaScript que representa la operación de lectura de la última medición.

El flujo general del componente es:

```text
Interfaz Web
     |
     v
LogicaFake
     |
     | GET /mediciones/ultima
     v
Servidor REST
     |
     v
LogicaNegocio
     |
     v
Base de datos
```

### Tipos utilizados

```text
N     = número natural
R     = número real
Text  = texto
```

La medición recibida desde el servidor REST se representa como:

```text
Medicion = (
    id: N,
    fecha: Text,
    tipo: Text,
    valor: R
)
```

El campo `id` forma parte de la medición recibida, pero no se mostrará en la interfaz durante el Sprint 0.

### LogicaFake

```text
---------------- LogicaFake ----------------
|
| urlUltimaMedicion: Text
|
obtenerUltimaMedicion() --> Medicion | null
|
---------------------------------------------
```

Responsabilidad:

La lógica fake representa en el navegador la operación necesaria para obtener la última medición.

`obtenerUltimaMedicion()` realiza una petición al servidor REST:

```text
GET /mediciones/ultima
```

El flujo es:

```text
obtenerUltimaMedicion()
        |
        v
GET /mediciones/ultima
        |
        v
Servidor REST
        |
        v
Medicion | null
```

La lógica fake no accede directamente a la base de datos.

### obtenerUltimaMedicion

```text
obtenerUltimaMedicion() --> Medicion | null
```

Responsabilidad:

Solicita al servidor REST la última medición almacenada.

Durante el Sprint 0 se utilizará la URL:

```text
https://jcivapo.upv.edu.es/rest/index.php/mediciones/ultima
```

Si la petición es correcta, se obtiene una respuesta con una estructura similar a:

```json
{
    "id": 5,
    "fecha": "2026-10-04 12:54:17",
    "tipo": "O3",
    "valor": 1234
}
```

Si no existen mediciones, se devuelve el mensaje proporcionado por el servidor REST.

Si ocurre un error de comunicación, el error será gestionado posteriormente por la interfaz.

### mostrarMedicion

```text
medicion: Medicion --> mostrarMedicion()
```

Responsabilidad:

Muestra en la interfaz los datos de la última medición obtenida.

Los campos mostrados son:

```text
fecha
tipo
valor
```

El campo:

```text
id
```

no se muestra al usuario.

### cargarUltimaMedicion

```text
cargarUltimaMedicion()
```

Responsabilidad:

Controla el proceso de actualización de la información mostrada en pantalla.

El flujo es:

```text
cargarUltimaMedicion()
        |
        v
LogicaFake.obtenerUltimaMedicion()
        |
        v
mostrarMedicion()
```

Si ocurre un error durante la petición:

```text
cargarUltimaMedicion()
        |
        v
mostrar mensaje de error
```

Esta operación se realizará:

```text
al cargar la página

y

al pulsar el botón Actualizar
```

### Estructura de la interfaz

La pantalla mostrará una tarjeta sencilla con la última medición:

```text
+------------------------------------------------------+
|                    Última medición                   |
+------------------------------------------------------+
|                                                      |
|  Fecha:  <fecha>                                     |
|                                                      |
|  Tipo:   <tipo>                                      |
|                                                      |
|  Valor:  <valor>                                     |
|                                                      |
|                 [ Actualizar ]                       |
|                                                      |
+------------------------------------------------------+
```

El botón:

```text
Actualizar
```

ejecutará:

```text
cargarUltimaMedicion()
```

y solicitará nuevamente al servidor REST la última medición disponible.

### Diseño visual

La interfaz tendrá un diseño sencillo para facilitar la demostración del Sprint 0.

Se utilizará:

```text
fondo claro

tarjeta central

bordes ligeramente redondeados

separación entre los datos

botón Actualizar
```

No se añadirán elementos visuales ni funcionalidades que no sean necesarias para demostrar el funcionamiento de la arquitectura.

### Archivos del componente

La estructura será:

```text
src/web/
|
├── index.html
|
├── css/
|   └── style.css
|
└── js/
    ├── LogicaFake.js
    └── app.js
```

`index.html`:

```text
estructura de la interfaz
```

`style.css`:

```text
presentación visual de la página
```

`LogicaFake.js`:

```text
comunicación con el servidor REST
+
obtención de la última medición
```

`app.js`:

```text
control de la interfaz
+
representación de los datos
+
gestión del botón Actualizar
```

### Comunicación con REST

La interfaz utiliza únicamente:

```text
GET /mediciones/ultima
```

La petición HTTP no se realiza directamente desde `app.js`.

El flujo correcto será:

```text
app.js
   |
   v
LogicaFake.obtenerUltimaMedicion()
   |
   v
GET /mediciones/ultima
   |
   v
Servidor REST
```

La web no realiza operaciones de:

```text
POST
PUT
DELETE
```

durante este Sprint.

## 2. Aclaraciones del Diseño

- La página principal será `index.html`.
- La web utiliza HTML, CSS y JavaScript.
- La comunicación con REST se realiza mediante `LogicaFake.js`.
- `app.js` no realiza directamente las peticiones HTTP.
- La lógica fake proporciona `obtenerUltimaMedicion()`.
- La web utiliza `GET /mediciones/ultima`.
- La web no accede directamente a la base de datos.
- La web no contiene lógica de negocio real.
- Al cargar la página se solicita automáticamente la última medición.
- El botón `Actualizar` vuelve a solicitar la última medición.
- La interfaz muestra únicamente `fecha`, `tipo` y `valor`.
- El campo `id` no se muestra al usuario.
- Si no existen mediciones o se produce un error, se muestra un mensaje sencillo.
- No se implementan listados, filtros, modificación ni eliminación de mediciones.
- La interfaz se mantiene sencilla para facilitar la demostración del Sprint 0.

## 3. Reglas Generales

- **Lenguaje de programación:** HTML5, CSS y JavaScript.
- **Encabezados de funciones/métodos:** cada función o método incluye su diseño lógico en un bloque de comentarios delimitado por líneas `--------------------`.
- **Legibilidad del código:** el código debe ser lo más claro y autoexplicativo posible, sin apenas comentarios adicionales.
- **Separación de responsabilidades:** HTML define la estructura, CSS la presentación, `LogicaFake.js` la comunicación REST y `app.js` controla la interfaz.
- **Comunicación REST:** las peticiones se realizarán mediante `fetch()`.
- **Acceso a datos:** la interfaz web nunca accede directamente a la base de datos.
- **Simplicidad:** no se utilizarán frameworks ni librerías externas innecesarias.
- **Correspondencia diseño-código:** la implementación debe respetar este diseño.
- **Pruebas automatizadas:** se generan pruebas unitarias o de integración para las funciones principales.
- **Pruebas mínimas:** se comprobará la obtención de la última medición, su representación en pantalla, el funcionamiento del botón `Actualizar` y la gestión de errores.
