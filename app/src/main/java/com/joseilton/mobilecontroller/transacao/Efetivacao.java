package com.joseilton.mobilecontroller.transacao;

import android.content.Context;

public interface Efetivacao {
    public boolean efetivar(Context context, Transacao transacao);

    public boolean estornar(Context context, Transacao transacao);
}
