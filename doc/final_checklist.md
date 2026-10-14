# Checklist final antes de entregar

Este documento reúne las comprobaciones que deben realizarse justo antes de la entrega.

## Estado documental

- [x] Paridad frontend/backend: `obtenerUltimaMedicion()`.

- [x] `author.md` presente.
- [x] `README.md` con descripción, despliegue y ejecución de pruebas.
- [x] Especificaciones de contexto presentes.
- [x] Diseños canónicos sin duplicados.
- [x] Criterios de aceptación documentados.
- [x] Prompts documentados.
- [x] Correspondencia entre diseños e implementación explicada en `doc/README.md`.

## Pruebas automáticas

Ejecutar antes de entregar:

```text
PHP:
php src/logica/test_logica.php
php src/rest/test_rest.php

Web:
node src/web/test_logica_fake.js

Android:
gradlew.bat test
```

Solo marcar como superadas después de ejecutarlas correctamente en el entorno correspondiente.

## Test funcional obligatorio

Debe comprobarse físicamente la cadena completa:

```text
Arduino (O3 = 1234)
        ↓ BLE
Android recibe 1234
        ↓ REST
Lógica de negocio
        ↓
Base de datos
        ↓
Web solicita última medición
        ↓
La web muestra O3 = 1234
```

No debe declararse este test como superado hasta haberlo comprobado de extremo a extremo.

## Git antes de entregar

Cuando todas las comprobaciones anteriores estén superadas:

```text
1. Revisar git status.
2. Hacer commit de los cambios pendientes en develop.
3. Verificar que develop contiene la versión final probada.
4. Integrar develop en master según el flujo indicado por la asignatura.
5. Comprobar que master contiene exactamente la versión entregable.
```
