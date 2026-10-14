# Test funcional completo del Sprint 0

## Objetivo

Demostrar de forma reproducible la cadena obligatoria de la rúbrica:

```text
Arduino: O3 ficticio = 1234
        ↓ BLE / iBeacon JU-GTI
Android recibe 1234
        ↓ POST /mediciones
API REST
        ↓
LogicaNegocio
        ↓
Base de datos
        ↓ GET /mediciones/ultima
Web
        ↓
muestra O3 = 1234
```

## Criterio de aceptación

El test se considera **SUPERADO** únicamente cuando, en una misma prueba:

1. La placa ejecuta el programa del Sprint 0 y publica `O3 = 1234`.
2. El teléfono Android detecta `JU-GTI`.
3. Logcat contiene una recepción BLE con `valor=1234`.
4. Logcat contiene `BLE -> REST ... valor=1234 ... exito=true`.
5. `GET /mediciones/ultima` devuelve una medición con `tipo = O3` y `valor = 1234`.
6. Al actualizar la página web se muestran `O3` y `1234`.

No debe marcarse el test como superado si falta cualquiera de los seis puntos.

## Procedimiento

### 1. Preparar la placa

Cargar el programa ubicado en:

```text
src/arduino/HolaMundoIBeacon/
```

Comprobar por puerto serie que se está publicando el valor ficticio `1234`.

### 2. Preparar Android

Instalar la aplicación en un teléfono físico, conceder los permisos Bluetooth/ubicación
necesarios y pulsar **Buscar nuestro dispositivo BTLE**.

Antes de iniciar la prueba puede limpiarse Logcat:

```bash
adb logcat -c
```

### 3. Ejecutar la cadena

Mantener la placa encendida hasta que Android reciba `JU-GTI` y realice el POST.

Exportar la evidencia:

```bash
adb logcat -d > evidencia_logcat.txt
```

### 4. Comprobar REST y web

Actualizar la página web del Sprint 0 y comprobar que aparecen:

```text
Tipo: O3
Valor: 1234
```

### 5. Verificación reproducible

Desde la raíz del repositorio ejecutar:

```bash
python tests/functional/verificar_test_funcional.py --logcat evidencia_logcat.txt --web-confirmada
```

El comando solo devuelve código `0` si:

- Logcat demuestra recepción BLE de `1234`.
- Logcat demuestra que el POST terminó con `exito=true`.
- El REST devuelve `O3=1234` como última medición.
- Se pasa `--web-confirmada` después de verificar visualmente la web.

## Evidencia recomendada para la defensa

Conservar:

- `evidencia_logcat.txt`.
- captura de la web mostrando `O3 = 1234`.
- opcionalmente captura de la fila correspondiente de la tabla `Mediciones`.

Estas evidencias no se marcan como obtenidas hasta realizar la prueba física.
