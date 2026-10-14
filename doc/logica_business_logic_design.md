# Diseño del componente: logica — business_logic

## 1. Diseño del Componente

Este documento es la especificación canónica de la **lógica de negocio** del backend.
El prefijo `logica` identifica directamente su implementación en `src/logica/`.

Este componente es independiente de HTTP, rutas, códigos de estado, cabeceras, sockets y
cualquier otro mecanismo de transporte.

### Tipos lógicos

```text
MedicionEntrada = ( tipo: Text, valor: R )
Medicion = ( id: N, fecha: Text, tipo: Text, valor: R )
ResultadoOperacion = ( exito: B, mensaje: Text )
```

### LogicaNegocio

```text
                           --------- LogicaNegocio ----------------
                           |
medicion: MedicionEntrada --> validarMedicion() <--
                  valida: B <--
                           |
medicion: MedicionEntrada --> guardarMedicion() -->
resultado: ResultadoOperacion <--
                           |
       mediciones: [ Medicion ] <-- obtenerUltimaMedicion() <--
                           |
                           -----------------------------------------
```

### Relación con la base de datos

`guardarMedicion()` persiste `tipo` y `valor` en `Mediciones`.
`obtenerUltimaMedicion()` consulta `id`, `fecha`, `tipo` y `valor` de esa tabla.

## 2. Aclaraciones del Diseño

- `validarMedicion()` acepta el tipo permitido por el Sprint y un valor válido.
- `guardarMedicion()` valida antes de insertar.
- `obtenerUltimaMedicion()` recupera la medición con el `id` más alto.
- Si no existen mediciones, devuelve la colección vacía prevista por la implementación.
- La persistencia está alineada con `bd_database_design.md`.
- Los detalles BLE no forman parte de la lógica de negocio.
- No existen dependencias desde `src/logica/` hacia `src/rest/`.
- La implementación correspondiente está en `src/logica/`.

## 3. Reglas Generales

- **Lenguaje de programación:** PHP.
- **Encabezados de funciones/métodos:** cada función o método debe incluir su diseño lógico en un bloque delimitado por líneas `--------------------`.
- **Legibilidad del código:** la lógica debe ser clara, de dominio y desacoplada del transporte.
- **Pruebas automatizadas:** `src/logica/test_logica.php` comprueba validación, persistencia y recuperación.
