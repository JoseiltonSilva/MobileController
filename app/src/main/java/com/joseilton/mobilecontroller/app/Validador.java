package com.joseilton.mobilecontroller.app;

import android.content.Context;

import com.joseilton.mobilecontroller.transacao.Transacao;
import com.joseilton.mobilecontroller.transferencia.Transferencia;
import com.joseilton.mobilecontroller.util.MensagemUtil;

public class Validador {

    public static boolean validarDisponibilidade(Context context, Transacao transacao) {
        if (transacao.getConta().getSaldo()
                .add(transacao.getConta().getLimite())
                .compareTo(transacao.getValor()) >= 0) {
            return true;
        } else {
            MensagemUtil.getMensagem(context, "Valor disponível insuficiente.");
            return false;
        }

    }

    public static boolean validarDisponibilidade(Context context, Transferencia transferencia) {
        if (transferencia.getContaOrigem().getSaldo()
                .add(transferencia.getContaOrigem().getLimite())
                .compareTo(transferencia.getValor()) >= 0) {
            return true;
        } else {
            MensagemUtil.getMensagem(context, "Valor disponível insuficiente.");
            return false;
        }

    }

    public static boolean validarDisponibilidadeEstorno(Context context, Transferencia transferencia) {
        if (transferencia.getContaDestino().getSaldo()
                .add(transferencia.getContaDestino().getLimite())
                .compareTo(transferencia.getValor()) >= 0) {
            return true;
        } else {
            MensagemUtil.getMensagem(context, "Valor disponível insuficiente.");
            return false;
        }

    }

    public static boolean validarEfetivacao(Context context, Transacao transacao) {
        if (!transacao.isEfetivado() || transacao.getId() == null)
            return true;
        else {
            MensagemUtil.getMensagem(context, "Transação já foi efetivada.");
            return false;
        }
    }

    public static boolean validarEstorno(Context context, Transacao transacao) {
        if (transacao.isEfetivado())
            return true;
        else {
            MensagemUtil.getMensagem(context, "Transação não foi efetivada.");
            return false;
        }
    }


}
