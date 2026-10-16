// --------------------------------------------------------------
// Fichero: test_logica_fake.js
// Descripción: Pruebas automáticas de la lógica cliente web del Sprint 0.
// Copyright: material académico y modificaciones del autor.
// Fecha de adaptación: 14-10-2026
// Autora de la adaptación: Judith Civico Aporta
// Aportación: pruebas de medición válida, ausencia de datos y error HTTP.
// --------------------------------------------------------------

const LogicaFake = require("../../frontend_business_logic/LogicaFake.js");

let fallos = 0;

// ------------------------------------------------------------
// nombre: Text, condicion: B --> comprobar() -->
// Registra como OK o FALLO el resultado de una comprobación.
// ------------------------------------------------------------
function comprobar(nombre, condicion) {
    if (condicion) {
        console.log("OK - " + nombre);
    } else {
        console.error("FALLO - " + nombre);
        fallos++;
    }
}

// ------------------------------------------------------------
// --> ejecutar() --> resultadoTest: B
// Ejecuta los casos de prueba de LogicaFake y finaliza con código 0/1.
// ------------------------------------------------------------
async function ejecutar() {
    const originalFetch = global.fetch;

    try {
        global.fetch = async () => ({
            ok: true,
            status: 200,
            json: async () => ({
                id: 1,
                fecha: "2026-10-06 12:00:00",
                tipo: "O3",
                valor: 1234
            })
        });

        const logicaConDato = new LogicaFake();
        const mediciones = await logicaConDato.obtenerUltimaMedicion();

        comprobar(
            "obtenerUltimaMedicion devuelve O3 = 1234",
            Array.isArray(mediciones) && mediciones.length === 1 && mediciones[0].tipo === "O3" && mediciones[0].valor === 1234
        );

        global.fetch = async () => ({
            ok: true,
            status: 200,
            json: async () => ({ mensaje: "No hay mediciones" })
        });

        const logicaVacia = new LogicaFake();
        const vacia = await logicaVacia.obtenerUltimaMedicion();

        comprobar(
            "obtenerUltimaMedicion devuelve colección vacía cuando no hay mediciones",
            Array.isArray(vacia) && vacia.length === 0
        );

        global.fetch = async () => ({
            ok: false,
            status: 500,
            json: async () => ({})
        });

        const logicaError = new LogicaFake();
        let errorDetectado = false;

        try {
            await logicaError.obtenerUltimaMedicion();
        } catch (error) {
            errorDetectado = error.message.includes("500");
        }

        comprobar(
            "obtenerUltimaMedicion informa de error HTTP",
            errorDetectado
        );

    } finally {
        global.fetch = originalFetch;
    }

    process.exit(fallos === 0 ? 0 : 1);
}

ejecutar();
