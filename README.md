# Índice de diseños del Sprint 0

Este directorio contiene una única especificación canónica por responsabilidad.

El agente revisor v2 permite usar nombres prefijados como:

```text
<nombre>_communication_design.md
<nombre>_business_logic_design.md
<nombre>_database_design.md
<nombre>_frontend_business_logic_design.md
```

Se utiliza esa variante para mantener una correspondencia clara entre cada diseño y su implementación dentro de `src/`.

| Diseño canónico | Implementación correspondiente | Responsabilidad |
| --- | --- | --- |
| `arduino_design.md` | `src/arduino/` | Generación y publicación BLE |
| `android_design.md` | `src/android/` | Cliente Android BLE → REST |
| `rest_communication_design.md` | `src/communication/` | Comunicación HTTP/JSON del backend |
| `logica_business_logic_design.md` | `src/business_logic/` | Lógica de negocio del backend |
| `bd_database_design.md` | `src/database/` | Persistencia relacional |
| `web_frontend_business_logic_design.md` | `src/web/js/LogicaFake.js` | Lógica cliente/proxy web |
| `web_design.md` | `src/web/` | Interfaz gráfica web |

`acceptance_criteria.md` recoge los criterios reproducibles de aceptación y el test funcional de extremo a extremo exigido por la rúbrica.

## Flujo de dependencias

```text
Arduino
   |
   | BLE / iBeacon
   v
Android
   |
   | REST
   v
src/communication/
   |
   v
src/business_logic/
   |
   v
src/database/


Web GUI
   |
   v
LogicaFake
   |
   | REST
   v
src/communication/
```

La capa de comunicación conoce HTTP, JSON y las rutas REST.

La lógica de negocio permanece independiente de HTTP y del mecanismo de transporte.

La persistencia se representa mediante la tabla `Mediciones`, cuyo esquema reproducible se encuentra en:

```text
src/database/crear_tabla.sql
```

## Diseños disponibles

### Arduino

```text
arduino_design.md
```

Describe la generación de la medición ficticia:

```text
O3 = 1234
```

y su publicación mediante BLE/iBeacon.

Implementación:

```text
src/arduino/
```

### Android

```text
android_design.md
```

Describe la recepción BLE, interpretación de la trama iBeacon y envío de la medición al servidor REST.

Implementación:

```text
src/android/
```

### Comunicación REST

```text
rest_communication_design.md
```

Describe las operaciones HTTP del backend:

```text
POST /mediciones
GET /mediciones/ultima
```

La capa de comunicación delega las operaciones de dominio en la lógica de negocio.

Implementación:

```text
src/communication/
```

### Lógica de negocio

```text
logica_business_logic_design.md
```

Describe las operaciones principales:

```text
validarMedicion()
guardarMedicion()
obtenerUltimaMedicion()
```

Esta capa es independiente de HTTP.

Implementación:

```text
src/business_logic/
```

### Base de datos

```text
bd_database_design.md
```

Describe la tabla:

```text
Mediciones
```

con los campos:

```text
id
fecha
tipo
valor
```

Implementación reproducible:

```text
src/database/crear_tabla.sql
```

### Lógica cliente web

```text
web_frontend_business_logic_design.md
```

Describe la lógica utilizada por la interfaz web para acceder al backend.

La operación expuesta mantiene paridad lógica con el backend:

```text
obtenerUltimaMedicion() --> [ Medicion ]
```

Implementación:

```text
src/web/js/LogicaFake.js
```

### Interfaz web

```text
web_design.md
```

Describe la presentación de los datos y la interacción de la interfaz.

Implementación:

```text
src/web/
```

## Criterios de aceptación

Los criterios de aceptación del Sprint 0 se encuentran en:

```text
acceptance_criteria.md
```

Incluyen comprobaciones para:

```text
Arduino / BLE
Android
Lógica de negocio
Comunicación REST
Lógica cliente web
Test funcional completo
```

## Test funcional

El procedimiento completo está documentado en:

```text
functional_test.md
```

La evidencia de la prueba realizada se encuentra en:

```text
functional_test_evidence.md
```

La cadena validada es:

```text
Arduino
   ↓
Android
   ↓
REST
   ↓
Lógica de negocio
   ↓
Base de datos
   ↓
Web
```

Durante la prueba se comprobó correctamente la medición ficticia:

```text
O3 = 1234
```

## Comprobación final

Antes de entregar, revisar:

```text
final_checklist.md
```

El proyecto debe mantener correspondencia entre:

```text
diseño
implementación
tests
criterios de aceptación
```

y conservar separadas las responsabilidades de comunicación, lógica de negocio, persistencia e interfaz.
