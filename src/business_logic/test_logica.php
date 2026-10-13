<?php

// --------------------------------------------------------------
// Fichero: test_logica.php
// Descripción: Pruebas automáticas de integración de la lógica de negocio con la base de
//              datos.
// Copyright: material académico y modificaciones del autor.
// Fecha de adaptación: 05-10-2026
// Autora de la adaptación: Judith Civico Aporta
// Aportación: validación reproducible de la lógica, persistencia y recuperación de
//             mediciones.
// --------------------------------------------------------------
require_once "LogicaNegocio.php";

$fallos = 0;


// ----------------------------------------------------
// Datos de conexión
// Sustituye estos valores solo en local.
// No subas credenciales reales al repositorio.
// ----------------------------------------------------


$host = "localhost";
$nombreBD = "TU_BASE_DE_DATOS";
$usuario = "TU_USUARIO";
$password = "TU_PASSWORD";


// ----------------------------------------------------
// Crear conexión PDO
// ----------------------------------------------------

try {

    $dsn =
        "mysql:host={$host};" .
        "dbname={$nombreBD};" .
        "charset=utf8mb4";

    $conexion = new PDO(
        $dsn,
        $usuario,
        $password
    );

    $conexion->setAttribute(
        PDO::ATTR_ERRMODE,
        PDO::ERRMODE_EXCEPTION
    );

} catch (PDOException $e) {

    echo "ERROR: no se ha podido conectar con la base de datos\n";

    exit;
}


// ----------------------------------------------------
// Crear lógica de negocio
// ----------------------------------------------------

$logica =
    new LogicaNegocio(
        $conexion
    );


// ----------------------------------------------------
// TEST 1
// Medición O3 válida
// ----------------------------------------------------

echo "TEST 1 - validar medición O3 correcta\n";

$medicionCorrecta =
    new MedicionEntrada(
        "O3",
        1234
    );

$resultado =
    $logica->validarMedicion(
        $medicionCorrecta
    );

if ($resultado) {
    echo "OK\n";
} else {
    echo "ERROR\n";
    $fallos++;
}


// ----------------------------------------------------
// TEST 2
// Tipo de medición incorrecto
// ----------------------------------------------------

echo "\nTEST 2 - validar tipo incorrecto\n";

$medicionIncorrecta =
    new MedicionEntrada(
        "CO2",
        1234
    );

$resultado =
    $logica->validarMedicion(
        $medicionIncorrecta
    );

if (!$resultado) {
    echo "OK\n";
} else {
    echo "ERROR\n";
    $fallos++;
}


// ----------------------------------------------------
// TEST 3
// Guardar medición
// ----------------------------------------------------

echo "\nTEST 3 - guardar medición\n";

$resultadoGuardar =
    $logica->guardarMedicion(
        $medicionCorrecta
    );

if ($resultadoGuardar->exito) {
    echo "OK\n";
} else {
    echo "ERROR: " . $resultadoGuardar->mensaje . "\n";
    $fallos++;
}


// ----------------------------------------------------
// TEST 4
// Obtener última medición
// ----------------------------------------------------

echo "\nTEST 4 - obtener última medición\n";

$mediciones =
    $logica->obtenerUltimaMedicion();

if (count($mediciones) === 1) {

    $ultima =
        $mediciones[0];

    echo "OK\n";

    echo "id = " .
        $ultima->id .
        "\n";

    echo "fecha = " .
        $ultima->fecha .
        "\n";

    echo "tipo = " .
        $ultima->tipo .
        "\n";

    echo "valor = " .
        $ultima->valor .
        "\n";

} else {

    echo "ERROR: no hay mediciones\n";
    $fallos++;
}

exit($fallos === 0 ? 0 : 1);