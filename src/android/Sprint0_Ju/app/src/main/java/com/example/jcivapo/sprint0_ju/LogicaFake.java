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