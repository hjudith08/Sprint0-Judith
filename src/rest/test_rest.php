<?php

// ----------------------------------------------------
// Dirección donde esté publicado el servidor REST.
// Cambiar al desplegarlo en Plesk.
// ----------------------------------------------------

$urlBase =
    "https://TU_DOMINIO";


// ----------------------------------------------------
// TEST 1
// POST /mediciones
// ----------------------------------------------------

echo "TEST 1 - POST /mediciones\n";


$datos = [
    "tipo" => "O3",
    "valor" => 1234
];


$curl =
    curl_init(
        $urlBase .
        "/mediciones"
    );


curl_setopt(
    $curl,
    CURLOPT_POST,
    true
);

curl_setopt(
    $curl,
    CURLOPT_POSTFIELDS,
    json_encode($datos)
);

curl_setopt(
    $curl,
    CURLOPT_HTTPHEADER,
    [
        "Content-Type: application/json"
    ]
);

curl_setopt(
    $curl,
    CURLOPT_RETURNTRANSFER,
    true
);


$respuesta =
    curl_exec($curl);


if ($respuesta === false) {

    echo "ERROR\n";

} else {

    $json =
        json_decode(
            $respuesta,
            true
        );


    if (
        isset($json["exito"])
        &&
        $json["exito"] === true
    ) {

        echo "OK\n";

    } else {

        echo "ERROR\n";
    }
}


curl_close($curl);


// ----------------------------------------------------
// TEST 2
// GET /mediciones/ultima
// ----------------------------------------------------

echo "\nTEST 2 - GET /mediciones/ultima\n";


$curl =
    curl_init(
        $urlBase .
        "/mediciones/ultima"
    );


curl_setopt(
    $curl,
    CURLOPT_RETURNTRANSFER,
    true
);


$respuesta =
    curl_exec($curl);


if ($respuesta === false) {

    echo "ERROR\n";

} else {

    $json =
        json_decode(
            $respuesta,
            true
        );


    if (
        isset($json["tipo"])
        &&
        isset($json["valor"])
        &&
        $json["tipo"] === "O3"
    ) {

        echo "OK\n";

        echo "id = " .
            $json["id"] .
            "\n";

        echo "fecha = " .
            $json["fecha"] .
            "\n";

        echo "tipo = " .
            $json["tipo"] .
            "\n";

        echo "valor = " .
            $json["valor"] .
            "\n";

    } else {

        echo "ERROR\n";
    }
}


curl_close($curl);