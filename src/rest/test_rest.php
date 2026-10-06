<?php

// --------------------------------------------------------------
// Fichero: test_rest.php
// Descripción: Pruebas automáticas de integración de las rutas REST del Sprint 0.
// Copyright: material académico y modificaciones del autor.
// Fecha de adaptación: 05-10-2026
// Autora de la adaptación: Judith Civico Aporta
// Aportación: comprobación de POST válido, rechazo de datos inválidos y consulta GET de la
//             última medición.
// --------------------------------------------------------------
$urlBase = "https://jcivapo.upv.edu.es/rest/index.php";
$fallos = 0;

/*
------------------------------------------------------------
datos: array --> hacerPeticionPOST() --> respuesta: array

Envía JSON mediante POST /mediciones y devuelve la respuesta.
------------------------------------------------------------
*/
function hacerPeticionPOST(string $urlBase, array $datos): array
{
    $curl = curl_init($urlBase . "/mediciones");

    curl_setopt_array($curl, [
        CURLOPT_POST => true,
        CURLOPT_POSTFIELDS => json_encode($datos),
        CURLOPT_HTTPHEADER => ["Content-Type: application/json"],
        CURLOPT_RETURNTRANSFER => true
    ]);

    $respuesta = curl_exec($curl);
    $codigoHTTP = curl_getinfo($curl, CURLINFO_HTTP_CODE);
    curl_close($curl);

    if ($respuesta === false) {
        return ["codigo" => 0, "json" => null];
    }

    return [
        "codigo" => $codigoHTTP,
        "json" => json_decode($respuesta, true)
    ];
}

/*
------------------------------------------------------------
urlBase: Text --> hacerPeticionGET() --> respuesta: array

Obtiene GET /mediciones/ultima y devuelve la respuesta.
------------------------------------------------------------
*/
function hacerPeticionGET(string $urlBase): array
{
    $curl = curl_init($urlBase . "/mediciones/ultima");
    curl_setopt($curl, CURLOPT_RETURNTRANSFER, true);

    $respuesta = curl_exec($curl);
    $codigoHTTP = curl_getinfo($curl, CURLINFO_HTTP_CODE);
    curl_close($curl);

    if ($respuesta === false) {
        return ["codigo" => 0, "json" => null];
    }

    return [
        "codigo" => $codigoHTTP,
        "json" => json_decode($respuesta, true)
    ];
}

// TEST 1: POST válido.
echo "TEST 1 - POST /mediciones válido\n";
$r = hacerPeticionPOST($urlBase, ["tipo" => "O3", "valor" => 1234]);
if ($r["codigo"] === 201 && ($r["json"]["exito"] ?? false) === true) {
    echo "OK\n";
} else {
    echo "ERROR\n";
    $fallos++;
}

// TEST 2: valor no numérico debe rechazarse.
echo "\nTEST 2 - POST con valor no numérico\n";
$r = hacerPeticionPOST($urlBase, ["tipo" => "O3", "valor" => "abc"]);
if ($r["codigo"] === 400 && ($r["json"]["exito"] ?? true) === false) {
    echo "OK\n";
} else {
    echo "ERROR\n";
    $fallos++;
}

// TEST 3: GET de la última medición.
echo "\nTEST 3 - GET /mediciones/ultima\n";
$r = hacerPeticionGET($urlBase);
$j = $r["json"];
if (
    $r["codigo"] === 200
    && is_array($j)
    && isset($j["id"], $j["fecha"], $j["tipo"], $j["valor"])
    && $j["tipo"] === "O3"
) {
    echo "OK\n";
    echo "id = {$j['id']}\n";
    echo "fecha = {$j['fecha']}\n";
    echo "tipo = {$j['tipo']}\n";
    echo "valor = {$j['valor']}\n";
} else {
    echo "ERROR\n";
    $fallos++;
}

exit($fallos === 0 ? 0 : 1);
