# Lógica cliente web

## Diseño de entrada

Consultar `doc/web_frontend_business_logic_design.md` y `doc/web_design.md`.

## Prompt de trabajo

> Implementa una lógica cliente JavaScript llamada `LogicaFake` que encapsule la petición `GET /mediciones/ultima`. Debe ofrecer `obtenerUltimaMedicion()` a la interfaz, devolver la medición recibida o `null` si el servidor indica que no existen mediciones, y lanzar un error en respuestas HTTP incorrectas. La GUI no debe acceder directamente a la base de datos.

## Criterio de aceptación

`app.js` obtiene los datos a través de `LogicaFake`, y las pruebas cubren respuesta con medición, respuesta vacía y error HTTP.

## Revisión del resultado

Se compara la implementación con el diseño y se ejecuta `node src/web/test_logica_fake.js`.
