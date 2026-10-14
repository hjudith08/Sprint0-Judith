# Diseño del componente: rest — communication

## 1. Diseño del Componente

Este documento es la especificación canónica de la **capa de comunicación** del backend.
El prefijo `rest` identifica directamente su implementación en `src/rest/`.

La responsabilidad de este componente es recibir peticiones HTTP/JSON, transformar la entrada
al tipo de dominio correspondiente e invocar la lógica de negocio. No contiene reglas de dominio
ni realiza consultas SQL de negocio por su cuenta.

### Tipos lógicos usados

```text
MedicionEntrada = ( tipo: Text, valor: R )
Medicion = ( id: N, fecha: Text, tipo: Text, valor: R )
ResultadoOperacion = ( exito: B, mensaje: Text )
```

### Interfaces externas

```text
medicion: MedicionEntrada --> POST /mediciones --> resultado: ResultadoOperacion

GET /mediciones/ultima --> mediciones: [ Medicion ]
```

### Dependencias

```text
HTTP / JSON
    |
    v
rest / communication       [src/rest/]
    |
    v
logica / business_logic    [src/logica/]
    |
    v
bd / database              [tabla Mediciones]
```

## 2. Aclaraciones del Diseño

- `POST /mediciones` recibe únicamente `tipo` y `valor`.
- `POST /mediciones` delega en `LogicaNegocio.guardarMedicion()`.
- `GET /mediciones/ultima` delega en `LogicaNegocio.obtenerUltimaMedicion()`.
- `id` y `fecha` los genera la base de datos.
- `major`, `minor`, RSSI y demás detalles BLE no pertenecen a esta capa.
- La capa de comunicación puede conocer HTTP, JSON, rutas y códigos de estado.
- La lógica de negocio no depende de esta capa.
- La implementación correspondiente está en `src/rest/`.

## 3. Reglas Generales

- **Lenguaje de programación:** PHP.
- **Encabezados de funciones/métodos:** cada función o método debe incluir su diseño lógico en un bloque delimitado por líneas `--------------------`.
- **Legibilidad del código:** separar recepción HTTP/JSON, delegación en negocio y construcción de la respuesta.
- **Pruebas automatizadas:** `src/rest/test_rest.php` cubre las operaciones REST principales del Sprint 0.
