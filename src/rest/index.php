<?php

require_once __DIR__ . '/../logica/LogicaNegocio.php';

header(
    'Content-Type: application/json; charset=utf-8'
);


// ------------------------------------------------------------
// crearConexion() --> PDO
//
// Crea y devuelve una conexión PDO con la base de datos.
//
// En el repositorio público se utilizan valores genéricos. 
// En el servidor Plesk estos valores se cambiaran por las credenciales reales de la base de datos.
// ------------------------------------------------------------
function crearConexion(): PDO
{
    $host =
        'localhost';

    $nombreBD =
        'TU_BASE_DE_DATOS';

    $usuario =
        'TU_USUARIO';

    $password =
        'TU_PASSWORD';


    $dsn =
        "mysql:host={$host};"
        . "dbname={$nombreBD};"
        . "charset=utf8mb4";


    return new PDO(
        $dsn,
        $usuario,
        $password,
        [
            PDO::ATTR_ERRMODE
                => PDO::ERRMODE_EXCEPTION,

            PDO::ATTR_DEFAULT_FETCH_MODE
                => PDO::FETCH_ASSOC
        ]
    );

} // crearConexion()


// ------------------------------------------------------------
// datos, codigoHTTP: N --> responderJSON()
//
// Envía una respuesta JSON utilizando el código HTTP indicado
// y finaliza la ejecución de la petición.
// ------------------------------------------------------------
function responderJSON(
    $datos,
    int $codigoHTTP = 200
): void {

    http_response_code(
        $codigoHTTP
    );


    echo json_encode(
        $datos,
        JSON_UNESCAPED_UNICODE
    );


    exit;

} // responderJSON()


// ------------------------------------------------------------
// obtenerRuta() --> Text
//
// Obtiene la ruta solicitada a partir de la petición HTTP.
// ------------------------------------------------------------
function obtenerRuta(): string
{
    $ruta =
        parse_url(
            $_SERVER['REQUEST_URI'],
            PHP_URL_PATH
        );


    return rtrim(
        $ruta,
        '/'
    );

} // obtenerRuta()


try {

    $conexion =
        crearConexion();


    $logica =
        new LogicaNegocio(
            $conexion
        );


    $metodo =
        $_SERVER['REQUEST_METHOD'];


    $ruta =
        obtenerRuta();


    // ----------------------------------------------------------
    // medicion: MedicionEntrada
    //      -->
    // POST /mediciones
    //      -->
    // resultado: ResultadoOperacion
    // ----------------------------------------------------------
    if (
        $metodo === 'POST'
        &&
        str_ends_with(
            $ruta,
            '/mediciones'
        )
    ) {

        $contenido =
            file_get_contents(
                'php://input'
            );


        $datos =
            json_decode(
                $contenido,
                true
            );


        if (!is_array($datos)) {

            responderJSON(
                [
                    'exito' => false,
                    'mensaje'
                        => 'El cuerpo debe contener JSON válido'
                ],
                400
            );
        }


        if (!array_key_exists(
            'tipo',
            $datos
        )) {

            responderJSON(
                [
                    'exito' => false,
                    'mensaje'
                        => 'Falta el campo tipo'
                ],
                400
            );
        }


        if (!array_key_exists(
            'valor',
            $datos
        )) {

            responderJSON(
                [
                    'exito' => false,
                    'mensaje'
                        => 'Falta el campo valor'
                ],
                400
            );
        }


        $medicion =
            new MedicionEntrada(
                $datos['tipo'],
                (float) $datos['valor']
            );


        $resultado =
            $logica->guardarMedicion(
                $medicion
            );


        if (!$resultado->exito) {

            responderJSON(
                [
                    'exito'
                        => false,

                    'mensaje'
                        => $resultado->mensaje
                ],
                400
            );
        }


        responderJSON(
            [
                'exito'
                    => true,

                'mensaje'
                    => $resultado->mensaje
            ],
            201
        );
    }


    // ----------------------------------------------------------
    // GET /mediciones/ultima
    //
    // Solicita a la lógica de negocio la última medición
    // almacenada y la devuelve en formato JSON.
    // ----------------------------------------------------------
    if (
        $metodo === 'GET'
        &&
        str_ends_with(
            $ruta,
            '/mediciones/ultima'
        )
    ) {

        $mediciones =
            $logica->obtenerUltimaMedicion();


        if (count($mediciones) === 0) {

            responderJSON(
                [
                    'mensaje'
                        => 'No hay mediciones'
                ],
                200
            );
        }


        $medicion =
            $mediciones[0];


        responderJSON(
            [
                'id'
                    => $medicion->id,

                'fecha'
                    => $medicion->fecha,

                'tipo'
                    => $medicion->tipo,

                'valor'
                    => $medicion->valor
            ],
            200
        );
    }


    // ----------------------------------------------------------
    // RUTA NO ENCONTRADA
    // ----------------------------------------------------------

    responderJSON(
        [
            'mensaje'
                => 'Ruta no encontrada'
        ],
        404
    );


} catch (Throwable $e) {

    // ----------------------------------------------------------
    // ERROR INTERNO
    // ----------------------------------------------------------

    responderJSON(
        [
            'mensaje'
                => 'Error interno del servidor'
        ],
        500
    );
}