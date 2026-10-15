# Evidencia del test funcional

Fecha: 14/10/2026
Commit probado: pendiente de commit final

- [x] Arduino publica O3 = 1234.
- [x] Android detecta JU-GTI.
- [x] Android registra valor = 1234.
- [x] Android registra BLE -> REST con exito=true.
- [x] GET /mediciones/ultima devuelve O3 = 1234.
- [x] La web muestra O3 = 1234.

Resultado:

- [x] SUPERADO
- [ ] NO SUPERADO

Observaciones:

La cadena completa Arduino -> Android -> REST -> lógica de negocio -> BD -> web
se ha comprobado correctamente con la medición ficticia O3 = 1234.