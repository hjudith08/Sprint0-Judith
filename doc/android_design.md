# Diseño del componente: android

## 1. Diseño del Componente

El componente `android` recibe el iBeacon `JU-GTI`, extrae la medición ficticia enviada por Arduino y la transmite al servidor REST.

El flujo funcional es:

```text
iBeacon JU-GTI
     |
     v
MainActivity / ScanCallback
     |
     v
TramaIBeacon
     |
     +--> major -> tipo + contador
     |
     +--> minor -> valor O3
     |
     v
MedicionEntrada("O3", valor)
     |
     v
PeticionarioREST.enviarMedicion()
     |
     v
POST /mediciones
```

### Tipos utilizados

```text
N = número natural
R = número real
B = booleano
Text = texto

MedicionEntrada = ( tipo: Text, valor: R )
ResultadoREST = ( exito: B, mensaje: Text )
```

### MedicionEntrada

```text
                 ------ MedicionEntrada -------
                 |
                 | tipo: Text
                 | valor: R
                 |
                 |
tipo: Text,   --> MedicionEntrada() -->
valor: R         |
                 |
        Text <-- getTipo() <--
                 |
                 |
           R <-- getValor() <--
                 |
                 -------------------------------
```

### LogicaFake

Se mantiene para pruebas aisladas del cliente Android. No forma parte del flujo funcional completo Arduino -> Android -> REST.

```text
                 -------- LogicaFake --------
                 |
MedicionEntrada <-- obtenerMedicionFake() <--
                 |
                 -----------------------------
```

Devuelve `MedicionEntrada("O3", 1234)`.

### PeticionarioREST

```text
                 ------- PeticionarioREST --------
                 |
medicion:        |
MedicionEntrada --> enviarMedicion() -->
                 |
                 ----------------------------------
```

Responsabilidad:

- convertir la medición a JSON;
- ejecutar `POST /mediciones` mediante `HttpURLConnection`;
- devolver el resultado mediante callback;
- ejecutar la comunicación de red fuera del hilo principal.

### MainActivity

Responsabilidades principales:

```text
preparar permisos y escáner BLE
        |
        v
buscar JU-GTI
        |
        v
parsear TramaIBeacon
        |
        v
comprobar tipo O3 y contador
        |
        v
evitar duplicados del mismo major
        |
        v
enviar minor al REST
```

El identificador O3 se obtiene del byte alto de `major`. El `minor` se convierte en el valor de `MedicionEntrada`.

`TramaIBeacon` y `Utilidades` son clases de apoyo procedentes de la base docente y se reutilizan para interpretar la trama.

## 2. Aclaraciones del Diseño

- El beacon esperado se llama `JU-GTI`.
- El identificador de O3 es `11`.
- Android no genera el valor utilizado en el test funcional completo; lo recibe desde Arduino mediante `minor`.
- `LogicaFake` se conserva únicamente para pruebas unitarias y pruebas aisladas de Android.
- El `major` completo se utiliza para evitar subir repetidamente el mismo anuncio BLE.
- La URL de escritura es `https://jcivapo.upv.edu.es/rest/index.php/mediciones`.
- Android no accede directamente a la base de datos.

## 3. Reglas Generales

- **Lenguaje de programación:** Java para Android.
- **Encabezados de funciones/métodos:** los métodos propios del Sprint incluyen su diseño lógico dentro de comentarios delimitados por líneas `--------------------`.
- **Legibilidad del código:** se mantiene el código claro, con responsabilidades separadas entre recepción BLE, representación de datos y comunicación REST.
- **Pruebas automatizadas:** se prueban automáticamente `MedicionEntrada` y `LogicaFake`; la comunicación de red y BLE se verifica además mediante pruebas de integración del Sprint.
