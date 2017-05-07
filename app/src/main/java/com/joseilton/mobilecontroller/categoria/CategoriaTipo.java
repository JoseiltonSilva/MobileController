package com.joseilton.mobilecontroller.categoria;

public enum CategoriaTipo {
    Debito("Débito"),
    Credito("Crédito");

    private String tipo;

    CategoriaTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getTipo() {
        return tipo;
    }

    @Override
    public String toString() {
        return tipo;
    }
}
