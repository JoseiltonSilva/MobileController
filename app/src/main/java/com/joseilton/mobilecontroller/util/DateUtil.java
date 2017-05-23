package com.joseilton.mobilecontroller.util;

import com.joseilton.mobilecontroller.app.Periodo;

import java.util.Calendar;
import java.util.Date;

public class DateUtil {

    /**
     * Adiciona quantidade de dias na data.
     *
     * @param data
     * @param qtd
     * @return
     */
    public static Date addDia(Date data, int qtd) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(data);
        cal.add(Calendar.DAY_OF_MONTH, qtd);
        return cal.getTime();
    }

    /**
     * Adiciona quantidade de meses na data.
     *
     * @param data
     * @param qtd
     * @return
     */
    public static Date addMes(Date data, int qtd) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(data);
        cal.add(Calendar.MONTH, qtd);
        return cal.getTime();
    }

    /**
     * Adiciona quantidade de anos na data.
     *
     * @param data
     * @param qtd
     * @return
     */
    public static Date addAno(Date data, int qtd) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(data);
        cal.add(Calendar.YEAR, qtd);
        return cal.getTime();
    }

    /**
     * Retorma uma String com a data formatada dd/MM/yyyy.
     *
     * @param data
     * @return
     */
    public static String getFormatData(Date data) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(data);
        return cal.get(Calendar.DAY_OF_MONTH) + "/" + (cal.get(Calendar.MONTH) + 1) + "/" + cal.get(Calendar.YEAR);
    }

    /**
     * Retorma uma String com a data formatada mmm/yyyy.
     *
     * @param data
     * @return
     */
    public static String getMonthAndYear(Date data) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(data);

        switch (cal.get(Calendar.MONTH)) {
            case 0:
                return "janeiro" + "/" + cal.get(Calendar.YEAR);

            case 1:
                return "fevereiro" + "/" + cal.get(Calendar.YEAR);

            case 2:
                return "março" + "/" + cal.get(Calendar.YEAR);

            case 3:
                return "abril" + "/" + cal.get(Calendar.YEAR);

            case 4:
                return "maio" + "/" + cal.get(Calendar.YEAR);
            case 5:
                return "junho" + "/" + cal.get(Calendar.YEAR);

            case 6:
                return "julho" + "/" + cal.get(Calendar.YEAR);

            case 7:
                return "agosto" + "/" + cal.get(Calendar.YEAR);

            case 8:
                return "setembro" + "/" + cal.get(Calendar.YEAR);

            case 9:
                return "outubro" + "/" + cal.get(Calendar.YEAR);

            case 10:
                return "novembro" + "/" + cal.get(Calendar.YEAR);


            case 11:
                return "dezembro" + "/" + cal.get(Calendar.YEAR);

            default:
                return "";

        }
    }

    /**
     * Retorma uma String com a data formatada mmm/yyyy.
     *
     * @param data
     * @return
     */
    public static String getMonth(Date data) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(data);

        switch (cal.get(Calendar.MONTH)) {
            case 0:
                return "Janeiro";

            case 1:
                return "Fevereiro";

            case 2:
                return "Março";

            case 3:
                return "Abril";

            case 4:
                return "Maio";
            case 5:
                return "Junho";

            case 6:
                return "Julho";

            case 7:
                return "Agosto";

            case 8:
                return "Setembro";

            case 9:
                return "Outubro";

            case 10:
                return "Novembro";


            case 11:
                return "Dezembro";

            default:
                return "";

        }
    }

    /**
     * Retorma uma String com o dia no formato DDD.
     *
     * @param data
     * @return
     */
    public static String getDay(Date data) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(data);

        switch (cal.get(Calendar.DAY_OF_WEEK)) {

            case 1:
                return "Domingo";

            case 2:
                return "Segunda";

            case 3:
                return "Terça";

            case 4:
                return "Quarta";

            case 5:
                return "Quinta";

            case 6:
                return "Sexta";

            case 7:
                return "Sábado";

            default:
                return "";

        }
    }

    public static Periodo thisMonth(Date date) {
        return new Periodo(new Date(date.getYear(), date.getMonth(), 1), DateUtil.addMes(new Date(date.getYear(), date.getMonth(), 1), 1));
    }

    public static Periodo thisYear(Date date) {
        return new Periodo(new Date(date.getYear(), 0, 1), new Date(date.getYear(), 11, 31));
    }
}
