package com.joseilton.mobilecontroller.app;

import java.math.BigDecimal;
import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.ParsePosition;

/**
 * Created by Joseilton on 17/06/2017.
 */

public class Formatador {

    public static String formatarValorMonetario(BigDecimal valor) {

        NumberFormat nf = NumberFormat.getCurrencyInstance();
        String formatado = nf.format (valor);

        return formatado;
    }
}
