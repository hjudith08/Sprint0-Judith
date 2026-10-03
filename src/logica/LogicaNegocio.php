<?php

// ----------------------------------------------------
// MedicionEntrada = ( tipo: Text, valor: R )
// ----------------------------------------------------
class MedicionEntrada
{
    public string $tipo;
    public float $valor;

    public function __construct(
        string $tipo,
        float $valor
    ) {
        $this->tipo = $tipo;
        $this->valor = $valor;
    }
}


// ----------------------------------------------------
// Medicion = ( id: N, fecha: Text, tipo: Text, valor: R )
// ----------------------------------------------------
class Medicion
{
    public int $id;
    public string $fecha;
    public string $tipo;
    public float $valor;

    public function __construct(
        int $id,
        string $fecha,
        string $tipo,
        float $valor
    ) {
        $this->id = $id;
        $this->fecha = $fecha;
        $this->tipo = $tipo;
        $this->valor = $valor;
    }
}


// ----------------------------------------------------
// ResultadoOperacion = ( exito: B, mensaje: Text )
// ----------------------------------------------------
class ResultadoOperacion
{
    public bool $exito;
    public string $mensaje;

    public function __construct(
        bool $exito,
        string $mensaje
    ) {
        $this->exito = $exito;
        $this->mensaje = $mensaje;
    }
}


class LogicaNegocio
{
    private PDO $conexion;


    public function __construct(PDO $conexion)
    {
        $this->conexion = $conexion;
    }


    /*
    ------------------------------------------------------------
    medicion: MedicionEntrada
        -->
    validarMedicion()
        -->
    valida: B
    ------------------------------------------------------------
    */
    public function validarMedicion(
        MedicionEntrada $medicion
    ): bool {

        if ($medicion->tipo !== "O3") {
            return false;
        }

        if (!is_numeric($medicion->valor)) {
            return false;
        }

        return true;
    }


    /*
    ------------------------------------------------------------
    medicion: MedicionEntrada
        -->
    guardarMedicion()
        -->
    resultado: ResultadoOperacion
    ------------------------------------------------------------
    */
    public function guardarMedicion(
        MedicionEntrada $medicion
    ): ResultadoOperacion {

        if (!$this->validarMedicion($medicion)) {

            return new ResultadoOperacion(
                false,
                "Medición no válida"
            );
        }

        try {

            $sql = "
                INSERT INTO Mediciones (
                    tipo,
                    valor
                )
                VALUES (
                    :tipo,
                    :valor
                )
            ";

            $sentencia =
                $this->conexion->prepare($sql);

            $sentencia->execute([
                ':tipo' => $medicion->tipo,
                ':valor' => $medicion->valor
            ]);

            return new ResultadoOperacion(
                true,
                "Medición guardada correctamente"
            );

        } catch (PDOException $e) {

            return new ResultadoOperacion(
                false,
                "Error al guardar la medición"
            );
        }
    }


    /*
    ------------------------------------------------------------
    obtenerUltimaMedicion()
        -->
    mediciones: [ Medicion ]
    ------------------------------------------------------------
    */
    public function obtenerUltimaMedicion(): array
    {
        try {

            $sql = "
                SELECT
                    id,
                    fecha,
                    tipo,
                    valor
                FROM Mediciones
                ORDER BY id DESC
                LIMIT 1
            ";

            $sentencia =
                $this->conexion->query($sql);

            $fila =
                $sentencia->fetch(
                    PDO::FETCH_ASSOC
                );

            if ($fila === false) {
                return [];
            }

            $medicion =
                new Medicion(
                    (int) $fila['id'],
                    $fila['fecha'],
                    $fila['tipo'],
                    (float) $fila['valor']
                );

            return [$medicion];

        } catch (PDOException $e) {

            return [];
        }
    }
}