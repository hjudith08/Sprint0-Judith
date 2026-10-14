# Índice de diseños del Sprint 0

Este directorio contiene una única especificación canónica por responsabilidad.

El agente revisor v2 permite usar nombres prefijados como
`<nombre>_communication_design.md`, `<nombre>_business_logic_design.md`,
`<nombre>_database_design.md` y `<nombre>_frontend_business_logic_design.md`.
Se utiliza esa variante para que el prefijo coincida con las carpetas reales de `src/`
sin mover ni modificar código funcional ya validado.

| Diseño canónico | Implementación correspondiente | Responsabilidad |
| --- | --- | --- |
| `arduino_design.md` | `src/arduino/` | Generación y publicación BLE |
| `android_design.md` | `src/android/` | Cliente Android BLE → REST |
| `rest_communication_design.md` | `src/rest/` | Comunicación HTTP/JSON del backend |
| `logica_business_logic_design.md` | `src/logica/` | Lógica de negocio del backend |
| `bd_database_design.md` | `src/bd/` | Persistencia relacional |
| `web_frontend_business_logic_design.md` | `src/web/js/LogicaFake.js` | Proxy/lógica cliente web |
| `web_design.md` | `src/web/` | GUI web |

`acceptance_criteria.md` recoge los criterios reproducibles de aceptación y el test funcional
de extremo a extremo exigido por la rúbrica.

## Flujo de dependencias

```text
Arduino -> BLE -> Android -> REST
                         |
                         v
                 src/rest/ (communication)
                         |
                         v
                 src/logica/ (business_logic)
                         |
                         v
                 src/bd/ (database)

Web GUI -> LogicaFake -> REST
```

La lógica de negocio del backend permanece independiente de HTTP y transporte.

## Comprobación final

Antes de entregar, seguir `final_checklist.md`.

## Test funcional

- `functional_test.md`: procedimiento completo y reproducible.
- `functional_test_evidence.md`: plantilla para registrar la evidencia real.
