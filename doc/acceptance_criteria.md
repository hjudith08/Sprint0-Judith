# Criterios de aceptación del Sprint 0

Los siguientes criterios permiten reproducir las comprobaciones sin modificar la implementación funcional ya validada.

## Arduino / BLE

- Cargar `src/arduino/HolaMundoIBeacon/HolaMundoIBeacon.ino` en la SparkFun Pro nRF52840 Mini.
- Verificar con un escáner BLE que aparece `JU-GTI`.
- Comprobar que la trama contiene el tipo O3 en `major` y el valor ficticio `1234` en `minor`.
- Comprobar que las publicaciones se producen aproximadamente cada 10 segundos.

## Android

- Conceder los permisos Bluetooth y ubicación solicitados por la aplicación.
- Pulsar **Buscar nuestro dispositivo** y comprobar que se detecta `JU-GTI`.
- Verificar en Logcat que se interpreta `minor = 1234`.
- Verificar que el envío REST termina correctamente.
- Ejecutar los tests unitarios desde Android Studio o mediante `gradlew.bat test` en Windows.

## Lógica de negocio

- Configurar una base de datos de pruebas con el esquema de `src/bd/crear_tabla.sql`.
- Configurar las credenciales de pruebas sin subir credenciales reales al repositorio.
- Ejecutar `php src/logica/test_logica.php`.
- El proceso debe terminar sin fallos.

## Comunicación REST

- Con el REST desplegado, ejecutar `php src/rest/test_rest.php`.
- Deben comprobarse POST válido, rechazo de datos inválidos y recuperación de la última medición.

## Lógica cliente web

- Tener Node.js disponible.
- Ejecutar `node src/web/test_logica_fake.js`.
- Las pruebas deben mostrar `OK` y terminar con código `0`.

## Test funcional obligatorio

1. Arduino fija la medición ficticia `O3 = 1234`.
2. La SparkFun emite el iBeacon `JU-GTI`.
3. Android recibe el valor `1234` desde `minor`.
4. Android realiza `POST /mediciones` mediante el código funcional existente.
5. El backend valida y guarda la medición en la tabla `Mediciones`.
6. La web solicita la última medición mediante `LogicaFake.js`.
7. La pantalla muestra `O3` y `1234`.

El test completo queda validado cuando los siete pasos se observan en una misma prueba de extremo a extremo.
