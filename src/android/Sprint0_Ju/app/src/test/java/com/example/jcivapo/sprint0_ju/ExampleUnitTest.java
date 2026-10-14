// --------------------------------------------------------------
// Fichero: ExampleUnitTest.java
// Descripción: Pruebas unitarias de los tipos de datos y la lógica fake
//              utilizados por la aplicación Android del Sprint 0.
// Copyright: material académico y modificaciones del autor.
// Fecha de adaptación: 14-10-2026
// Autora de la adaptación: Judith Civico Aporta
// Aportación: sustitución del test de ejemplo por pruebas reales del proyecto.
// --------------------------------------------------------------

package com.example.jcivapo.sprint0_ju;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ExampleUnitTest {

    // ------------------------------------------------------------
    // probarMedicionEntrada()
// --> probarMedicionEntrada() --> resultadoTest: B
//
    // Comprueba que MedicionEntrada conserva correctamente
    // el tipo y el valor recibidos en el constructor.
    // ------------------------------------------------------------
    @Test
    public void probarMedicionEntrada() {

        MedicionEntrada medicion =
                new MedicionEntrada(
                        "O3",
                        1234
                );

        assertEquals(
                "O3",
                medicion.getTipo()
        );

        assertEquals(
                1234.0,
                medicion.getValor(),
                0.001
        );
    }


    // ------------------------------------------------------------
    // probarMedicionFake()
// --> probarMedicionFake() --> resultadoTest: B
//
    // Comprueba que la lógica fake utilizada en pruebas aisladas
    // genera la medición controlada O3 = 1234.
    // ------------------------------------------------------------
    @Test
    public void probarMedicionFake() {

        LogicaFake logicaFake =
                new LogicaFake();

        MedicionEntrada medicion =
                logicaFake.obtenerMedicionFake();

        assertEquals(
                "O3",
                medicion.getTipo()
        );

        assertEquals(
                1234.0,
                medicion.getValor(),
                0.001
        );
    }
}
