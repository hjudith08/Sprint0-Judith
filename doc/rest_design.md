# Diseño del componente: rest

## 1. Diseño del Componente

```text
MedicionEntrada = ( tipo: Text, valor: R )

Medicion = ( id: N, fecha: Text, tipo: Text, valor: R )

ResultadoOperacion = ( exito: B, mensaje: Text )


medicion: MedicionEntrada --> POST /mediciones --> resultado: ResultadoOperacion

GET /mediciones/ultima --> medicion: Medicion
```

## 2. Aclaraciones del Diseño

- El componente `rest` expone únicamente las rutas necesarias para el Sprint 0.

- `POST /mediciones` recibe una `MedicionEntrada` formada únicamente por:
  - `tipo`
  - `valor`

- El JSON esperado para `POST /mediciones` tiene esta forma:

```json
{
  "tipo": "O3",
  "valor": 1234
}
```

- `id` y `fecha` no se reciben mediante REST. Son generados posteriormente por la base de datos al almacenar la medición.

- `Major` y `Minor` pertenecen a la comunicación BLE y no se envían al servidor REST ni se almacenan en la base de datos.

- `POST /mediciones` utiliza la lógica de negocio para validar y guardar la medición.

- El resultado de `POST /mediciones` contiene únicamente `exito` y `mensaje`.

Ejemplo de una operación correcta:

```json
{
  "exito": true,
  "mensaje": "Medición guardada correctamente"
}
```

- `GET /mediciones/ultima` utiliza la lógica de negocio para obtener la medición con el `id` más alto.

- Cuando existe una medición, la respuesta contiene:

```json
{
  "id": 1,
  "fecha": "2026-10-03 12:00:00",
  "tipo": "O3",
  "valor": 1234
}
```

- Cuando no existen mediciones almacenadas, no se genera una medición ficticia. La respuesta contiene únicamente un mensaje indicando que no existen mediciones:

```json
{
  "mensaje": "No hay mediciones"
}
```

- El componente `rest` no accede directamente a la base de datos. Las operaciones sobre los datos se realizan mediante `LogicaNegocio`.

- El componente `rest` se encarga de recibir y devolver información mediante HTTP y JSON. La validación y las reglas de negocio pertenecen al componente `logica`.

- Para este Sprint 0 no se añaden otras rutas como:
  - obtener una medición por `id`;
  - obtener todas las mediciones;
  - modificar mediciones;
  - eliminar mediciones.

## 3. Reglas Generales

- **Lenguaje de programación:** PHP.
- **Formato de intercambio de datos:** JSON sobre HTTP.
- **Encabezados de funciones/métodos:** el encabezado de cada función o método debe incluir su diseño lógico dentro de un bloque de comentarios delimitado por líneas `--------------------`.
- **Legibilidad del código:** el código debe ser lo más claro y autoexplicativo posible, de modo que prácticamente no requiera comentarios adicionales.
- **Pruebas automatizadas:** se deben generar pruebas unitarias o de integración para las rutas principales del componente.