// --------------------------------------------------------------
// Fichero: LogicaFake.java
// Descripción: Lógica fake del cliente Android para generar una medición de O3 controlada
//              durante las pruebas.
// Copyright: material académico y modificaciones del autor.
// Fecha de adaptación: 05-10-2026
// Autora de la adaptación: Judith Civico Aporta
// Aportación: implementación de la lógica fake requerida para las pruebas del Sprint 0.
// --------------------------------------------------------------
package com.example.jcivapo.sprint0_ju;

// ----------------------------------------------------
// Lógica fake para realizar pruebas sin depender
// todavía de la recepción real mediante Bluetooth.
// ----------------------------------------------------
public class LogicaFake {

    // ----------------------------------------------------
    // -->
    // obtenerMedicionFake()
    // -->
    // medicion: MedicionEntrada
    // ----------------------------------------------------
    public MedicionEntrada obtenerMedicionFake() {

        return new MedicionEntrada(
                "O3",
                1234
        );

    } // ()

} // class