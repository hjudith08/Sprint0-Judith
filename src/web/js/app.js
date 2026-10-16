// --------------------------------------------------------------
// Fichero: app.js
// Descripción: Controlador de la interfaz web que carga, representa y actualiza la última
//              medición.
// Copyright: material académico y modificaciones del autor.
// Fecha de adaptación: 05-10-2026
// Autora de la adaptación: Judith Civico Aporta
// Aportación: carga inicial, actualización manual y gestión visual de errores.
// --------------------------------------------------------------
// src/web/js/app.js


const logicaFake =
    new LogicaFake();


/*
----------------------------------------------------
medicion: Medicion --> mostrarMedicion() -->

Muestra una medición en la interfaz.
----------------------------------------------------
*/

function mostrarMedicion(
    medicion
) {

    document.getElementById(
        "fecha"
    ).textContent =
        medicion.fecha;


    document.getElementById(
        "tipo"
    ).textContent =
        medicion.tipo;


    document.getElementById(
        "valor"
    ).textContent =
        medicion.valor;
}


/*
----------------------------------------------------
mensaje: Text --> mostrarError() -->

Muestra un mensaje de error en la interfaz.
----------------------------------------------------
*/

function mostrarError(
    mensaje
) {

    document.getElementById(
        "mensaje"
    ).textContent =
        mensaje;
}


/*
----------------------------------------------------
--> limpiarDatos() -->

Limpia los datos mostrados en la interfaz.
----------------------------------------------------
*/

function limpiarDatos() {

    document.getElementById(
        "fecha"
    ).textContent =
        "-";


    document.getElementById(
        "tipo"
    ).textContent =
        "-";


    document.getElementById(
        "valor"
    ).textContent =
        "-";
}


/*
----------------------------------------------------
--> cargarUltimaMedicion() -->

Obtiene la última medición desde LogicaFake y actualiza la interfaz.
----------------------------------------------------
*/

async function cargarUltimaMedicion() {

    const boton =
        document.getElementById(
            "botonActualizar"
        );


    boton.disabled =
        true;


    mostrarError(
        "Actualizando..."
    );


    try {

        const mediciones =
            await logicaFake
                .obtenerUltimaMedicion();


        if (mediciones.length === 0) {

            limpiarDatos();

            mostrarError(
                "No existen mediciones almacenadas."
            );

            return;
        }


        mostrarMedicion(
            mediciones[0]
        );


        mostrarError(
            ""
        );


    } catch (error) {

        limpiarDatos();


        mostrarError(
            "No se pudo recuperar la medición."
        );


        console.error(
            error
        );


    } finally {

        boton.disabled =
            false;
    }
}


/*
----------------------------------------------------
--> inicializarInterfaz() -->

Inicializa la página y registra el evento del botón de actualización.
----------------------------------------------------
*/

function inicializarInterfaz() {

    const boton =
        document.getElementById(
            "botonActualizar"
        );


    boton.addEventListener(
        "click",
        cargarUltimaMedicion
    );


    cargarUltimaMedicion();
}


document.addEventListener(
    "DOMContentLoaded",
    inicializarInterfaz
);