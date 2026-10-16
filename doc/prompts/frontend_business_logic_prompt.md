# Lógica cliente web

## Diseño de entrada

Consultar `doc/frontend_business_logic_design.md` y `doc/web_design.md`.

## Prompt de trabajo

> Implementa una lógica cliente JavaScript llamada `LogicaFake` en `src/frontend_business_logic/LogicaFake.js` que encapsule la petición `GET /mediciones/ultima`. Debe ofrecer `obtenerUltimaMedicion()` a la interfaz y mantener la misma firma lógica que la operación equivalente del backend. Debe devolver una colección `[ Medicion ]` cuando exista una medición, devolver `[]` cuando el servidor indique que no existen mediciones y lanzar un error en respuestas HTTP incorrectas. La GUI no debe ejecutar `fetch()` directamente ni acceder a la base de datos.

## Criterio de aceptación

`src/web/js/app.js` obtiene los datos exclusivamente a través de `LogicaFake`.

Las pruebas deben cubrir:

```text
respuesta con una medición
respuesta sin mediciones
error HTTP
```

## Revisión del resultado

Se compara la implementación con `frontend_business_logic_design.md` y `business_logic_design.md` para comprobar la paridad de la operación `obtenerUltimaMedicion()`.

Después se ejecuta:

```bash
node src/web/tests/test_logica_fake.js
```
