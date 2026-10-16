# Android BLE

## Diseño de entrada

Consultar `doc/android_design.md` antes de generar o modificar implementación.

## Prompt de trabajo

> Adapta la aplicación Android Java para detectar el iBeacon `JU-GTI`, interpretar `major` y `minor`, reconocer O3 con identificador 11 y enviar el valor recibido en `minor` al REST mediante `PeticionarioREST`. Mantén los permisos necesarios para Android actual, evita envíos duplicados del mismo `major` y conserva los comentarios con diseño lógico. El valor `1234` del test funcional debe proceder de Arduino, no generarse en el flujo BLE real de Android.

## Criterio de aceptación

Un móvil real detecta `JU-GTI`, interpreta el valor `1234` y puede enviarlo al REST.

## Revisión del resultado

Se compara la implementación con `android_design.md`, se revisan los logs BLE y se ejecutan las pruebas unitarias disponibles.
