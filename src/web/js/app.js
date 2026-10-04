// src/web/js/app.js


const logicaFake =
    new LogicaFake();


/*
----------------------------------------------------
mostrarMedicion(medicion)

Muestra una medición en la interfaz.

Entrada:
    medicion

Salida:
    ninguna
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
mostrarError(mensaje)

Muestra un mensaje de error en la interfaz.

Entrada:
    mensaje

Salida:
    ninguna
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
limpiarDatos()

Limpia los datos mostrados en la interfaz.

Entrada:
    ninguna

Salida:
    ninguna
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
cargarUltimaMedicion()

Obtiene la última medición desde LogicaFake
y actualiza la interfaz.

Entrada:
    ninguna

Salida:
    ninguna
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

        const medicion =
            await logicaFake
                .leerUltimaMedicion();


        if (medicion === null) {

            limpiarDatos();

            mostrarError(
                "No existen mediciones almacenadas."
            );

            return;
        }


        mostrarMedicion(
            medicion
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
inicializarInterfaz()

Inicializa la página al terminar de cargar el documento.

Entrada:
    ninguna

Salida:
    ninguna
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