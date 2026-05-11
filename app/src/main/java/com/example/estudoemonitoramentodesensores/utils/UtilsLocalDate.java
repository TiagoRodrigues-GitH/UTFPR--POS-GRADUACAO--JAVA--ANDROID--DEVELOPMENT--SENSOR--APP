package com.example.estudoemonitoramentodesensores.utils;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.time.chrono.IsoChronology;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.FormatStyle;
import java.util.Locale;

/**
 * Class: UtilsLocalDate
 * Project: Estudo e Monitoramento de Sensores
 * <p>
 * Description:
 *
 * @author Tiago Rodrigues
 * @version 1.0
 * @since 2026-03-16
 */
public final class UtilsLocalDate {

    public static DateTimeFormatter formatter;
    private static Locale localeUsado;

    private UtilsLocalDate() {

    }

    public static void inicializaFormatter(){

        localeUsado = Locale.getDefault();

        String formatPattern = DateTimeFormatterBuilder.getLocalizedDateTimePattern(FormatStyle.SHORT,
                                                                                        null,
                                                                                                 IsoChronology.INSTANCE,
                                                                                                 localeUsado);

        formatPattern = formatPattern.replaceAll("\\byy\\b", "yyyy");

        formatPattern = formatPattern.replaceAll("\\bM\\b", "MM");

        formatPattern = formatPattern.replaceAll("\\bd\\b", "dd");

        formatter = DateTimeFormatter.ofPattern(formatPattern, Locale.getDefault());

    }

    public static String formatLocalDate(LocalDate date) {

        if (date == null) {
            return null;
        }

        if(localeUsado !=  Locale.getDefault()){
            inicializaFormatter();
        }

        return date.format(formatter);
    }

    public static long toMillissegundos(LocalDate date) {

        if (date == null) {
            return 0;
        }

        return date.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }


    public static int diferencaEmAnosParaHoje(LocalDate date) {

        return diferencaEmAnos(date, LocalDate.now());

    }


    public static int diferencaEmAnos(LocalDate date1, LocalDate date2) {

        if (date1 == null || date2 == null) {

            return 0;
        }
        Period period = Period.between(date1, date2);

        return period.getYears();
    }



}