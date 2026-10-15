# Comunicación REST

## Diseño de entrada

Consultar el archivo correspondiente de `doc/` antes de generar implementación.

## Prompt de trabajo

> Implementa el componente REST descrito en `communication_design.md`. Debe exponer `POST /mediciones` y `GET /mediciones/ultima`, validar la forma básica del JSON y delegar las reglas de negocio en `LogicaNegocio`. No accedas directamente a la base de datos salvo para crear/inyectar la conexión. Añade pruebas de integración de ambas rutas.

## Criterio de aceptación

Las rutas llaman a la lógica de negocio y los tests comprueban casos válido, inválido y lectura.

## Revisión del resultado

Se compara la implementación con el diseño, se ejecutan las pruebas disponibles y se corrigen diferencias antes de aceptar el cambio.
