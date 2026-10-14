# Diseño del componente: bd — database

## 1. Diseño del Componente

Este documento es la especificación canónica de la **base de datos relacional**.
El prefijo `bd` identifica directamente su implementación reproducible en `src/bd/`.

```text
====================================================================================
TABLE: Mediciones

DESCRIPTION: Almacena las mediciones medioambientales recibidas por el sistema.

COLUMNS:

+ id | INT | NOT NULL | Auto-Increment
+ fecha | DATETIME | NOT NULL | CURRENT_TIMESTAMP
+ tipo | VARCHAR(50) | NOT NULL
+ valor | DOUBLE | NOT NULL

PRIMARY KEY: id

FOREIGN KEYS:

CONSTRAINTS:

====================================================================================
```

### Correspondencia con los tipos de dominio

```text
Medicion = ( id: N, fecha: Text, tipo: Text, valor: R )
MedicionEntrada = ( tipo: Text, valor: R )
```

## 2. Aclaraciones del Diseño

- `id` es generado mediante auto-incremento.
- `fecha` se genera mediante `CURRENT_TIMESTAMP`.
- `tipo` y `valor` proceden de `MedicionEntrada`.
- `major` y `minor` son datos de transporte BLE y no se persisten.
- El SQL reproducible del esquema está en `src/bd/crear_tabla.sql`.
- La lógica de negocio que usa esta tabla se especifica en `logica_business_logic_design.md`.

## 3. Reglas Generales

- **Lenguaje de programación:** SQL compatible con MySQL/MariaDB.
- **Encabezados de funciones/métodos:** si se añadieran procedimientos o funciones SQL, incluirían su diseño lógico dentro de un bloque `--------------------`.
- **Legibilidad del código:** mantener nombres de dominio claros y evitar complejidad innecesaria.
- **Pruebas automatizadas:** la persistencia se comprueba desde `src/logica/test_logica.php` y mediante las pruebas de integración REST.
