# Diseño del componente: web

## 1. Diseño del Componente

El componente `web` permite visualizar la última medición almacenada en el sistema.

La interfaz web no accede directamente a la base de datos ni contiene lógica de negocio.

La GUI utiliza el componente `frontend_business_logic` para obtener la última medición.

El flujo general del componente es:

```text
Interfaz Web
     |
     v
Frontend Business Logic
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

El campo `id` forma parte de la medición recibida, pero no se muestra en la interfaz durante el Sprint 0.

### Frontend Business Logic

La implementación de la lógica cliente se encuentra en:

```text
src/frontend_business_logic/LogicaFake.js
```

La operación utilizada por la GUI es:

```text
obtenerUltimaMedicion() --> [ Medicion ]
```

Responsabilidad:

La clase `LogicaFake` representa en el navegador la operación necesaria para obtener la última medición.

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
[ Medicion ]
```

Si no existen mediciones, la lógica cliente devuelve:

```text
[]
```

Si ocurre un error de comunicación, el error se propaga para que la interfaz pueda mostrar un mensaje.

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
--> cargarUltimaMedicion() -->
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
[ Medicion ]
        |
        v
mostrarMedicion(mediciones[0])
```

Si la colección está vacía:

```text
[]
   |
   v
mostrar mensaje de que no existen mediciones
```

Si ocurre un error durante la petición:

```text
cargarUltimaMedicion()
        |
        v
mostrar mensaje de error
```

Esta operación se realiza al cargar la página y al pulsar el botón `Actualizar`.

### Estructura de la interfaz

La pantalla muestra una tarjeta sencilla con la última medición:

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

### Archivos del componente

La estructura de la GUI es:

```text
src/web/
|
├── index.html
|
├── css/
|   └── style.css
|
├── js/
|   └── app.js
|
└── tests/
    └── test_logica_fake.js
```

La lógica cliente utilizada por la GUI se encuentra separada en:

```text
src/frontend_business_logic/LogicaFake.js
```

`index.html` define la estructura de la interfaz.

`style.css` define la presentación visual.

`app.js` controla la interfaz, representa los datos y gestiona el botón `Actualizar`.

`test_logica_fake.js` prueba automáticamente la lógica cliente.

### Comunicación con REST

La interfaz no realiza directamente las peticiones HTTP.

El flujo correcto es:

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

La web no realiza operaciones `POST`, `PUT` ni `DELETE` durante este Sprint.

## 2. Aclaraciones del Diseño

- La página principal es `index.html`.
- La web utiliza HTML, CSS y JavaScript.
- La lógica cliente está en `src/frontend_business_logic/LogicaFake.js`.
- `app.js` no realiza directamente peticiones HTTP.
- La lógica cliente proporciona `obtenerUltimaMedicion()`.
- La web utiliza `GET /mediciones/ultima`.
- La web no accede directamente a la base de datos.
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
- **Legibilidad del código:** el código debe ser lo más claro y autoexplicativo posible.
- **Separación de responsabilidades:** HTML define la estructura, CSS la presentación, `LogicaFake.js` encapsula la comunicación REST y `app.js` controla la interfaz.
- **Acceso a datos:** la interfaz web nunca accede directamente a la base de datos.
- **Simplicidad:** no se utilizan frameworks ni librerías externas innecesarias.
- **Correspondencia diseño-código:** la implementación debe respetar este diseño.
- **Pruebas automatizadas:** `src/web/tests/test_logica_fake.js` prueba la obtención de la última medición, la colección vacía y los errores HTTP.
