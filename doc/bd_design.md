# Diseño del componente: bd

## 1. Diseño del Componente

```text
====================================================================================
TABLE: Mediciones

DESCRIPTION: Almacena las mediciones medioambientales recibidas por el sistema.

COLUMNS:

+ id | INT(11) | NOT NULL | Auto-Increment
+ fecha | DATETIME | NOT NULL | CURRENT_TIMESTAMP
+ tipo | VARCHAR(50) | NOT NULL
+ valor | DOUBLE | NOT NULL

PRIMARY KEY: id

FOREIGN KEYS:

CONSTRAINTS:

====================================================================================
```

## 2. Aclaraciones del Diseño

- Cada fila de `Mediciones` se corresponde con el tipo lógico `Medicion = ( id: N, fecha: Text, tipo: Text, valor: R )`: `id` es `INT(11)`, `fecha` es `DATETIME`, `tipo` es `VARCHAR(50)` y `valor` es `DOUBLE`.
- Una `MedicionEntrada = ( tipo: Text, valor: R )` solo aporta `tipo` y `valor`. `id` y `fecha` los genera la propia base de datos (`Auto-Increment` y `CURRENT_TIMESTAMP`).
- Los valores `Major` y `Minor` del iBeacon pertenecen a la parte BLE y no se almacenan en la base de datos.

## 3. Reglas Generales

- **Lenguaje de programación:** SQL (MySQL/MariaDB).
- **Encabezados de funciones/métodos:** cada función o método incluye su diseño lógico en un bloque de comentarios delimitado por líneas `--------------------`.
- **Legibilidad del código:** el código debe ser lo más claro y autoexplicativo posible, sin apenas comentarios adicionales.
- **Pruebas automatizadas:** se generan pruebas unitarias o de integración para todas las funciones y métodos clave.
