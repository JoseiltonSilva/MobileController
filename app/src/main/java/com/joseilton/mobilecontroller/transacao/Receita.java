package com.joseilton.mobilecontroller.transacao;

import android.content.Context;

import com.joseilton.mobilecontroller.app.Validador;


public class Receita implements Efetivacao {
    @Override
    public boolean efetivar(Context context, Transacao transacao) {
        if (Validador.validarEfetivacao(context, transacao)) {
            transacao.getConta().setSaldo(transacao.getConta().getSaldo()
                    .add(transacao.getValor()));

            transacao.setEfetivado(true);
            return true;
        } else
            return false;


    }

    @Override
    public boolean estornar(Context context, Transacao transacao) {
        if (Validador.validarDisponibilidade(context, transacao)
                && Validador.validarEstorno(context, transacao)) {
            transacao.getConta().setSaldo(transacao.getConta().getSaldo()
                    .subtract(transacao.getValor()));

            transacao.setEfetivado(false);
            return true;
        } else
            return false;

    }

}
