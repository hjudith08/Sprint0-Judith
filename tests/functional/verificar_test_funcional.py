#!/usr/bin/env python3
"""
Verificación reproducible del test funcional del Sprint 0.

No genera datos ficticios ni sustituye la prueba física BLE.
Comprueba evidencias producidas por una ejecución real:
1) Android recibió O3=1234 desde BLE.
2) Android confirmó BLE -> REST con exito=true.
3) El REST devuelve como última medición O3=1234.
4) El usuario confirma que la web muestra O3=1234.
"""

from __future__ import annotations

import argparse
import json
import re
import sys
import urllib.request
import urllib.error
from pathlib import Path

DEFAULT_REST = "https://jcivapo.upv.edu.es/rest/index.php/mediciones/ultima"


def comprobar_logcat(path: Path) -> tuple[bool, list[str]]:
    texto = path.read_text(encoding="utf-8", errors="replace")

    recibe_ble = bool(
        re.search(r"Medici[oó]n BLE recibida.*valor=1234", texto, re.IGNORECASE)
        or re.search(r"minor\s*=\s*(?:04d2|1234).*", texto, re.IGNORECASE)
    )

    rest_ok = bool(
        re.search(
            r"BLE\s*->\s*REST:.*valor=1234.*exito=true",
            texto,
            re.IGNORECASE,
        )
    )

    mensajes = [
        ("Android recibió O3=1234 por BLE", recibe_ble),
        ("Android confirmó el POST REST con exito=true", rest_ok),
    ]
    return all(ok for _, ok in mensajes), [
        ("OK   - " if ok else "FALLO- ") + msg for msg, ok in mensajes
    ]


def comprobar_rest(url: str) -> tuple[bool, str]:
    try:
        with urllib.request.urlopen(url, timeout=15) as respuesta:
            codigo = respuesta.getcode()
            datos = json.loads(respuesta.read().decode("utf-8"))
    except (urllib.error.URLError, json.JSONDecodeError, TimeoutError) as exc:
        return False, f"FALLO- No se pudo consultar el REST: {exc}"

    ok = (
        codigo == 200
        and isinstance(datos, dict)
        and datos.get("tipo") == "O3"
        and float(datos.get("valor", float("nan"))) == 1234.0
    )

    if ok:
        return True, (
            "OK   - REST devuelve la última medición O3=1234"
            f" (id={datos.get('id')}, fecha={datos.get('fecha')})"
        )

    return False, f"FALLO- El REST no devuelve O3=1234 como última medición: {datos}"


def main() -> int:
    parser = argparse.ArgumentParser()
    parser.add_argument(
        "--logcat",
        required=True,
        help="Archivo exportado con adb logcat que contiene la recepción BLE y el POST.",
    )
    parser.add_argument("--rest-url", default=DEFAULT_REST)
    parser.add_argument(
        "--web-confirmada",
        action="store_true",
        help="Usar únicamente después de comprobar visualmente que la web muestra O3=1234.",
    )
    args = parser.parse_args()

    log_ok, mensajes_log = comprobar_logcat(Path(args.logcat))
    for mensaje in mensajes_log:
        print(mensaje)

    rest_ok, mensaje_rest = comprobar_rest(args.rest_url)
    print(mensaje_rest)

    if args.web_confirmada:
        print("OK   - Confirmación manual: la web muestra O3=1234")
    else:
        print("FALLO- Falta confirmar visualmente que la web muestra O3=1234")

    total = log_ok and rest_ok and args.web_confirmada

    print()
    if total:
        print("RESULTADO FINAL: TEST FUNCIONAL COMPLETO SUPERADO")
        return 0

    print("RESULTADO FINAL: TEST FUNCIONAL COMPLETO PENDIENTE/NO SUPERADO")
    return 1


if __name__ == "__main__":
    raise SystemExit(main())
