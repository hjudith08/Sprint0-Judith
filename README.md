# Sprint0-Judith

Proyecto desarrollado para el Sprint 0 de la asignatura de Proyecto de Aplicaciones de Biometría y Medio Ambiente.

El objetivo es comprobar la cadena completa desde una medición ficticia generada en Arduino hasta su visualización en una página web.

## Flujo del Sprint 0

```text
SparkFun Pro nRF52840 Mini
        |
        | BLE / iBeacon JU-GTI
        v
Aplicación Android (Java)
        |
        | POST /mediciones
        v
Servidor REST (PHP)
        |
        v
Lógica de negocio (PHP)
        |
        v
Base de datos MySQL / MariaDB
        |
        | GET /mediciones/ultima
        v
Interfaz web
```

La medición ficticia utilizada es:

```text
Tipo: O3
Valor: 1234
```

El valor nace en Arduino, se codifica en la trama iBeacon, Android lo recibe y lo envía al servidor REST. La lógica de negocio valida y almacena la medición. La web consulta después la última medición almacenada.

## Estructura del repositorio

```text
.
├── context/
│   ├── Software_Engineering_Spec.md
│   └── Database_Design_Spec.md
├── doc/
│   ├── README.md
│   ├── acceptance_criteria.md
│   ├── android_design.md
│   ├── arduino_design.md
│   ├── rest_communication_design.md
│   ├── logica_business_logic_design.md
│   ├── bd_database_design.md
│   ├── web_frontend_business_logic_design.md
│   └── web_design.md
├── prompts/
├── src/
│   ├── android/
│   ├── arduino/
│   ├── bd/
│   ├── logica/
│   ├── rest/
│   └── web/
├── author.md
└── README.md
```

### Correspondencia exacta entre diseño e implementación

Se usa la variante de nombres prefijados admitida por el agente v2 para conservar la
implementación funcional sin mover código:

```text
rest_communication_design.md          -> src/rest/
logica_business_logic_design.md       -> src/logica/
bd_database_design.md                 -> src/bd/
web_frontend_business_logic_design.md -> src/web/js/LogicaFake.js
web_design.md                         -> src/web/
android_design.md                     -> src/android/
arduino_design.md                     -> src/arduino/
```

De esta forma no existen diseños duplicados o ambiguos y cada documento indica de forma
explícita qué implementación revisa.

## Componentes

### Arduino

`src/arduino/HolaMundoIBeacon/` contiene la implementación para SparkFun Pro nRF52840 Mini. Durante el Sprint 0 publica la medición ficticia de O3 mediante un iBeacon llamado `JU-GTI`.

### Android

`src/android/Sprint0_Ju/` contiene la aplicación Java que escanea BLE, interpreta la trama iBeacon y envía la medición al REST mediante `HttpURLConnection`.

`LogicaFake` se conserva únicamente para pruebas aisladas; el valor del test funcional completo procede de Arduino.

### REST

`src/rest/` contiene la capa de comunicación HTTP. Las rutas principales son:

```text
POST /mediciones
GET /mediciones/ultima
```

La capa REST delega las reglas de negocio en `LogicaNegocio`.

### Lógica de negocio

`src/logica/` contiene `LogicaNegocio.php`. Esta capa valida, guarda y recupera mediciones y no implementa rutas HTTP.

### Base de datos

`src/bd/crear_tabla.sql` contiene el esquema reproducible de la tabla:

```text
Mediciones(id, fecha, tipo, valor)
```

### Web

`src/web/` contiene la interfaz. `app.js` se ocupa de la presentación y utiliza `LogicaFake.js` para obtener la última medición mediante REST.


### Paridad frontend/backend

La lógica cliente web y la lógica de negocio backend exponen la misma operación lógica:

```text
obtenerUltimaMedicion() --> [ Medicion ]
```

El frontend devuelve `[]` cuando no hay datos y `[Medicion]` cuando existe una última
medición; `app.js` consume el primer elemento para mostrarlo.

## Despliegue

### Base de datos

1. Crear una base de datos MySQL/MariaDB.
2. Ejecutar `src/bd/crear_tabla.sql`.
3. Configurar en el servidor las credenciales reales sin subirlas al repositorio.

### REST

Desplegar el contenido necesario de `src/rest/` junto con `src/logica/` en el servidor PHP manteniendo las rutas relativas que utiliza la implementación existente.

Durante el Sprint se utiliza el servidor de la UPV y las rutas públicas configuradas en el código del proyecto.

### Web

Desplegar `src/web/` en un servidor web y asegurar que puede acceder al endpoint REST configurado en `LogicaFake.js`.

### Android

Abrir `src/android/Sprint0_Ju/` con Android Studio, compilar e instalar en un móvil real con Bluetooth Low Energy.

### Arduino

Abrir `src/arduino/HolaMundoIBeacon/HolaMundoIBeacon.ino`, seleccionar la placa correspondiente y cargar el programa.

## Cómo ejecutar los tests

### Lógica de negocio

```bash
php src/logica/test_logica.php
```

Requiere una base de datos de pruebas correctamente configurada.

### REST

```bash
php src/rest/test_rest.php
```

Requiere que el endpoint REST y la base de datos de pruebas estén disponibles.

### Web

```bash
node src/web/test_logica_fake.js
```

Este test simula las respuestas HTTP y no necesita modificar `LogicaFake.js`.

### Android

Desde `src/android/Sprint0_Ju/` en Windows:

```text
gradlew.bat test
```

También pueden ejecutarse los tests desde Android Studio.

## Criterios de aceptación

Los pasos reproducibles de cada componente y el test funcional obligatorio están descritos en:

```text
doc/acceptance_criteria.md
```

## Uso de IA

La carpeta `prompts/` recoge versiones consolidadas de los prompts utilizados durante el desarrollo y su relación con diseños, criterios de aceptación y revisión del resultado.

## Evidencias para la revisión

- `author.md` identifica a la autora.
- `doc/README.md` relaciona cada diseño con su implementación.
- `doc/acceptance_criteria.md` define criterios de aceptación reproducibles.
- `prompts/` documenta el uso de IA, el diseño de entrada, el criterio de aceptación y la revisión.
- Los diseños separan comunicación, lógica de negocio, persistencia y lógica cliente.
- La lógica de negocio del backend se mantiene independiente de HTTP.
- Los tests disponibles están documentados en este README.


## Test funcional obligatorio

El procedimiento reproducible para validar la cadena completa
**Arduino → Android → REST → lógica de negocio → BD → web** está en
`doc/functional_test.md`.

Después de realizar la prueba física y exportar Logcat:

```bash
python tests/functional/verificar_test_funcional.py --logcat evidencia_logcat.txt --web-confirmada
```

El test no se considera superado hasta que la ejecución real cumpla todos los criterios
indicados en `doc/functional_test.md`.

## Seguridad

Las credenciales reales de la base de datos no deben almacenarse en Git. Los archivos del repositorio deben utilizar valores de ejemplo o configuración externa.

## Estado de validación

Se ha comprobado la emisión BLE de `JU-GTI` y la detección desde un dispositivo Android físico. También existen pruebas para lógica de negocio, REST, web y Android.

Antes de cerrar el Sprint debe ejecutarse y documentarse una última prueba completa:

```text
Arduino -> Android -> REST -> lógica -> BD -> web
```

y comprobar que el valor mostrado al final sigue siendo `O3 = 1234`.
