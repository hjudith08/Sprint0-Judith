// --------------------------------------------------------------
// Fichero: MedicionEntrada.java
// Descripción: Tipo de datos que representa la medición que Android envía al servidor REST.
// Copyright: material académico y modificaciones del autor.
// Fecha de adaptación: 05-10-2026
// Autora de la adaptación: Judith Civico Aporta
// Autor original: Jordi Bataller i Mascarell
// Aportación: definición de los campos tipo y valor usados en la comunicación con el REST.
// --------------------------------------------------------------
package com.example.jcivapo.sprint0_ju;

public class MedicionEntrada {

    private String tipo;
    private double valor;

    // ----------------------------------------------------
    // tipo: Text, valor: R
    //      -->
    // MedicionEntrada()
    //      -->
    // medicion: MedicionEntrada
    // ----------------------------------------------------
    public MedicionEntrada(String tipo, double valor) {
        this.tipo = tipo;
        this.valor = valor;
    }

    // ----------------------------------------------------
    // -->
    // getTipo()
    // -->
    // tipo: Text
    // ----------------------------------------------------
    public String getTipo() {
        return tipo;
    }

    // ----------------------------------------------------
    // -->
    // getValor()
    // -->
    // valor: R
    // ----------------------------------------------------
    public double getValor() {
        return valor;
    }

} // class
