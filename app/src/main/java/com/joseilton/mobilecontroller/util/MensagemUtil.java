package com.joseilton.mobilecontroller.util;

import android.content.Context;
import android.widget.Toast;

import java.sql.SQLException;

public class MensagemUtil {
    public static void getMensagem(Context context, String mensagem) {
        Toast.makeText(context, mensagem, Toast.LENGTH_LONG).show();
    }

    public static void getMensagem(Context context, SQLException e) {
        Toast.makeText(context, "Erro: " + e.getMessage(), Toast.LENGTH_LONG).show();
    }
}
