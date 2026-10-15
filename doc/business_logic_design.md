# Diseño del componente: business_logic

## 1. Diseño del Componente

Este documento es la especificación canónica de la **lógica de negocio** del backend.

La implementación correspondiente se encuentra en:

```text
src/business_logic/
```

Este componente es independiente de HTTP, rutas, códigos de estado, cabeceras y cualquier otro mecanismo de transporte.

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

`guardarMedicion()` persiste `tipo` y `valor` en la tabla `Mediciones`.

`obtenerUltimaMedicion()` consulta `id`, `fecha`, `tipo` y `valor`.

## 2. Aclaraciones del Diseño

- `validarMedicion()` comprueba que la medición sea válida.
- `guardarMedicion()` valida antes de insertar.
- `obtenerUltimaMedicion()` recupera la medición con el `id` más alto.
- Si no existen mediciones, devuelve una colección vacía.
- La persistencia está alineada con `database_design.md`.
- Los detalles BLE no forman parte de la lógica de negocio.
- No existen dependencias desde `src/business_logic/` hacia `src/communication/`.

## 3. Reglas Generales

- **Lenguaje de programación:** PHP.
- **Encabezados de funciones/métodos:** cada función o método debe incluir su diseño lógico en un bloque delimitado por líneas `--------------------`.
- **Legibilidad del código:** la lógica debe ser clara, de dominio y desacoplada del transporte.
- **Pruebas automatizadas:** `src/business_logic/test_logica.php` comprueba validación, persistencia y recuperación.
