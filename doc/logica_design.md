# Diseño del componente: logica

## 1. Diseño del Componente

```text
MedicionEntrada = ( tipo: Text, valor: R )

Medicion = ( id: N, fecha: Text, tipo: Text, valor: R )

ResultadoOperacion = ( exito: B, mensaje: Text )

                           --------- LogicaNegocio ----------------
                           |
                           |
medicion: MedicionEntrada --> validarMedicion() <--
                  valida: B <--
                           |
                           |
medicion: MedicionEntrada --> guardarMedicion() -->
resultado: ResultadoOperacion <--
                           |
                           |
       mediciones: [ Medicion ] <-- obtenerUltimaMedicion() <--
                           |
                           -----------------------------------------
```

## 2. Aclaraciones del Diseño

- `validarMedicion()` devuelve `true` únicamente cuando `tipo` es exactamente `"O3"` y `valor` es un número real válido.
- `guardarMedicion()` valida primero la medición mediante `validarMedicion()`. Si la medición no es válida, devuelve un `ResultadoOperacion` con `exito = false` y un mensaje explicativo, sin almacenar la medición. Si es válida, almacena la medición y devuelve `exito = true`.
- `obtenerUltimaMedicion()` obtiene la medición con el `id` más alto.
- `obtenerUltimaMedicion()` devuelve una colección `[ Medicion ]` que contiene:
  - una medición si existe alguna almacenada;
  - una colección vacía si no existen mediciones.
- No se genera ninguna `Medicion` ficticia para representar que la base de datos está vacía.
- `id` y `fecha` son generados por la base de datos al almacenar una `MedicionEntrada`.
- Los valores `Major` y `Minor` del iBeacon pertenecen a la comunicación BLE y no forman parte de la lógica de negocio.
- Este componente contiene únicamente la lógica de negocio. Las rutas y respuestas HTTP pertenecen al componente `rest`.
- La forma concreta de conexión con la base de datos es un detalle de implementación y no forma parte de este diseño lógico.

## 3. Reglas Generales

- **Lenguaje de programación:** PHP.
- **Encabezados de funciones/métodos:** el encabezado de cada función o método debe incluir su diseño lógico dentro de un bloque de comentarios delimitado por líneas `--------------------`.
- **Legibilidad del código:** el código debe ser lo más claro y autoexplicativo posible, de modo que prácticamente no requiera comentarios adicionales.
- **Pruebas automatizadas:** se deben generar pruebas unitarias o de integración para todas las funciones y métodos clave.