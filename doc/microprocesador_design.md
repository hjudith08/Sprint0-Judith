# Diseño del componente: microprocesador

## 1. Diseño del Componente

El componente `microprocesador` genera la medición ficticia requerida por el Sprint 0 y la publica mediante un iBeacon BLE desde la SparkFun Pro nRF52840 Mini.

La implementación correspondiente se encuentra en:

```text
src/microprocesador/
```

El flujo funcional es:

```text
valor O3 ficticio = 1234
        |
        v
Publicador.publicarO3()
        |
        | major = (O3 << 8) | contador
        | minor = 1234
        v
EmisoraBLE.emitirAnuncioIBeacon()
        |
        v
iBeacon JU-GTI
```

### Tipos utilizados

```text
N = número natural
Z = número entero
B = booleano
Text = texto

MedicionesID = { O3, TEMPERATURA, RUIDO }
```

### Publicador

```text
                 -------- Publicador ----------------
                 |
                 | beaconUUID: [ N ]_16
                 | RSSI: Z
                 |
            encenderEmisora() -->
                 |
valorO3: Z,      |
contador: N,  --> publicarO3() -->
tiempoEspera: N  |
                 |
                 -------------------------------------
```

Responsabilidad:

- codificar el identificador de O3 y el contador en `major`;
- colocar el valor de la medición en `minor`;
- ordenar a `EmisoraBLE` la publicación del iBeacon.

Para O3:

```text
major = (11 << 8) | contador
minor = valorO3
```

### Programa principal

```text
setup()
  |
  v
encender emisora BLE
  |
  v
loop()
  |
  +--> incrementar contador
  |
  +--> valorO3 = 1234
  |
  +--> publicarO3(valorO3, contador, intervalo)
  |
  v
repetir
```

La medida ficticia nace en el programa del microprocesador incluido dentro de `src/microprocesador/`.

### Infraestructura reutilizada

`EmisoraBLE`, `LED`, `PuertoSerie` y el resto de clases docentes se reutilizan como infraestructura.

`Medidor` queda preparado para una futura lectura analógica del sensor, pero la medición física no forma parte del criterio funcional del Sprint 0.

## 2. Aclaraciones del Diseño

- El nombre anunciado por BLE es `JU-GTI`.
- El UUID utilizado es `EPSG-GTI-PROY-3A` codificado en 16 bytes.
- El identificador lógico de O3 es `11`.
- `major` transporta el tipo de medición en el byte alto y el contador en el byte bajo.
- `minor` transporta el valor ficticio `1234`.
- El contador permite al cliente Android distinguir una medición nueva de anuncios repetidos.
- El ciclo de publicación del Sprint 0 está ajustado aproximadamente a 10 segundos.
- La lectura analógica real del sensor no se utiliza para el criterio de aceptación del Sprint 0.

## 3. Reglas Generales

- **Lenguaje de programación:** C++ para Arduino.
- **Encabezados de funciones/métodos:** cada función o método propio del Sprint incluye su diseño lógico en un bloque de comentarios delimitado por líneas `--------------------`.
- **Legibilidad del código:** se conserva el código docente y se realizan únicamente las adaptaciones necesarias para el Sprint 0.
- **Pruebas:** las partes puramente hardware/BLE se validan mediante la prueba funcional de integración; la cadena posterior dispone de tests en Android, REST, lógica y web.
