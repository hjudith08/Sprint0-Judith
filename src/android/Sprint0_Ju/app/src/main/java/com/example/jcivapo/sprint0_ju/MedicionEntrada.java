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