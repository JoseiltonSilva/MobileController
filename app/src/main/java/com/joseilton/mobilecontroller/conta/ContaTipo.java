package com.joseilton.mobilecontroller.conta;

public enum ContaTipo {
    Dinheiro("Dinheiro"),
    ContaCorrente("Conta Corrente"),
    Poupanca("Poupança"),
    Investimento("Investimento");

    private String tipo;

    ContaTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getTipo() {
        return tipo;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
