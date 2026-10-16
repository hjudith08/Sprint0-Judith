// --------------------------------------------------------------
// Fichero: ExampleInstrumentedTest.java
// Descripción: Prueba instrumentada que verifica el contexto y el paquete de la
//              aplicación Android del Sprint 0.
// Copyright: material académico y modificaciones del autor.
// Fecha de adaptación: 14-10-2026
// Autora de la adaptación: Judith Civico Aporta
// Aportación: documentación de la prueba instrumentada incluida en el proyecto.
// --------------------------------------------------------------
package com.example.jcivapo.sprint0_ju;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

// ------------------------------------------------------------
// ExampleInstrumentedTest
//
// Agrupa las pruebas que requieren ejecutarse sobre Android.
// ------------------------------------------------------------
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    // ------------------------------------------------------------
    // --> useAppContext() --> resultadoTest: B
    // Comprueba que el contexto instrumentado pertenece al paquete del proyecto.
    // ------------------------------------------------------------
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.example.jcivapo.sprint0_ju", appContext.getPackageName());
    }
}