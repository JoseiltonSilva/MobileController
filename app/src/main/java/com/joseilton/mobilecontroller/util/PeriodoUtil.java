package com.joseilton.mobilecontroller.util;



import com.joseilton.mobilecontroller.app.Periodo;

import java.util.Date;

public class PeriodoUtil {
    /**
     * Retorna o periodo correspondente ao mes da data informada
     *
     * @param date
     * @return
     */
    public static Periodo thisMonth(Date date) {
        return new Periodo(new Date(date.getYear(), date.getMonth(), 1), DateUtil.addMes(new Date(date.getYear(), date.getMonth(), 1), 1));
    }

    /**
     * Retorna o periodo correspondente ao ano da data informada
     *
     * @param date
     * @return
     */
    public static Periodo thisYear(Date date) {
        return new Periodo(new Date(date.getYear(), 0, 1), new Date(date.getYear(), 11, 31));
    }
}
