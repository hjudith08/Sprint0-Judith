# Diseño del componente: web — frontend_business_logic

## 1. Diseño del Componente

Este documento especifica la **lógica de negocio del cliente web** consumida por la GUI.
El prefijo `web` identifica directamente su implementación dentro de `src/web/`.

La GUI no realiza directamente la comunicación HTTP. La clase `LogicaFake`, ubicada en
`src/frontend_business_logic/LogicaFake.js`, encapsula la lectura del backend y ofrece a `app.js` la operación
necesaria para obtener la última medición.

### Tipo de dominio utilizado

```text
Medicion = ( id: N, fecha: Text, tipo: Text, valor: R )
```

### Operación expuesta por la implementación actual

```text
                 -------- LogicaFake ----------------
                 | urlUltimaMedicion: Text
                 |
[ Medicion ] <-- obtenerUltimaMedicion() <--
                 |
                 -------------------------------------
```

### Correspondencia de dominio con el backend

```text
Cliente web                          Backend
---------------------------------------------------------------
obtenerUltimaMedicion()       <->      obtenerUltimaMedicion()
[ Medicion ]                       [ Medicion ]
```

Ambas operaciones representan la misma capacidad de dominio: obtener la última medición
disponible. La lógica cliente mantiene la misma operación, parámetros y tipo lógico de retorno que
la contraparte del backend.

## 2. Aclaraciones del Diseño

- La GUI (`src/web/js/app.js`) no contiene la URL del REST ni ejecuta `fetch()` directamente.
- `src/frontend_business_logic/LogicaFake.js` encapsula `GET /mediciones/ultima`.
- Si el servidor indica que no existen mediciones, la lógica cliente devuelve `[]`.
- Una respuesta HTTP incorrecta produce un error que la GUI puede gestionar.
- La implementación correspondiente está dentro de `src/web/`.
- El diseño de presentación de la GUI está separado en `web_design.md`.

## 3. Reglas Generales

- **Lenguaje de programación:** JavaScript.
- **Encabezados de funciones/métodos:** cada función o método debe incluir su diseño lógico en un bloque delimitado por líneas `--------------------`.
- **Legibilidad del código:** mantener separados acceso al backend y representación de la interfaz.
- **Pruebas automatizadas:** `src/frontend_business_logic/tests/LogicaFakeTest.js` cubre respuesta con medición, ausencia de mediciones y error HTTP.

## Paridad nominal de interfaz

La lógica cliente y la lógica backend exponen la operación con el mismo nombre:

```text
obtenerUltimaMedicion()
```

La implementación web expone la misma firma lógica que el backend: `obtenerUltimaMedicion()`
sin parámetros y con retorno `[ Medicion ]`. La GUI toma el primer elemento cuando existe.
