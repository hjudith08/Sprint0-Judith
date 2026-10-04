// src/web/js/LogicaFake.js


class LogicaFake {

    constructor() {

        this.urlUltimaMedicion =
            "https://jcivapo.upv.edu.es/rest/index.php/mediciones/ultima";
    }


    /*
    ----------------------------------------------------
    leerUltimaMedicion()

    Solicita al servidor REST la última medición almacenada.

    Entrada:
        ninguna

    Salida:
        Medicion | null
    ----------------------------------------------------
    */

    async leerUltimaMedicion() {

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

            return null;
        }


        return datos;
    }
}