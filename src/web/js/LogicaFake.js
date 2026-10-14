// --------------------------------------------------------------
// Fichero: LogicaFake.js
// Descripción: Cliente fake del navegador que obtiene la última medición mediante el API
//              REST.
// Copyright: material académico y modificaciones del autor.
// Fecha de adaptación: 05-10-2026
// Autora de la adaptación: Judith Civico Aporta
// Aportación: comunicación Web -> GET /mediciones/ultima y soporte para pruebas
//             automáticas.
// --------------------------------------------------------------
// src/web/js/LogicaFake.js


class LogicaFake {

    // ----------------------------------------------------
    // --> constructor() --> logica: LogicaFake
    // Inicializa la URL utilizada para recuperar la última medición.
    // ----------------------------------------------------
    constructor() {

        this.urlUltimaMedicion =
            "https://jcivapo.upv.edu.es/rest/index.php/mediciones/ultima";
    }


    /*
    ----------------------------------------------------
    obtenerUltimaMedicion()

    Solicita al servidor REST la última medición almacenada.

    Entrada:
        ninguna

    Salida:
        Medicion | null
    ----------------------------------------------------
    */

    async obtenerUltimaMedicion() {

        const respuesta =
            await fetch(
                this.urlUltimaMedicion
            );


        if (!respuesta.ok) {

            throw new Error(
                "Error HTTP: " + respuesta.status
            );
        }


        const datos =
            await respuesta.json();


        if (datos.mensaje) {

            return [];
        }


        return [datos];
    }
}

// Permite ejecutar pruebas automáticas con Node sin afectar al navegador.
if (typeof module !== "undefined" && module.exports) {
    module.exports = LogicaFake;
}
