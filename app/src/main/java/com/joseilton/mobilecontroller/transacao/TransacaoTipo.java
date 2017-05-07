package com.joseilton.mobilecontroller.transacao;

public enum TransacaoTipo {
    Despesa {
        @Override
        public Efetivacao getEfetivacao() {
            return new Despesa();
        }

    },
    Receita {
        @Override
        public Efetivacao getEfetivacao() {
            return new Receita();
        }

    };

    public abstract Efetivacao getEfetivacao();

}
