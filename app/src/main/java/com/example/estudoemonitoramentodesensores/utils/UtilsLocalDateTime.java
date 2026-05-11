package com.example.estudoemonitoramentodesensores.utils;

import java.time.LocalDateTime;
import java.time.chrono.IsoChronology;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.FormatStyle;
import java.util.Locale;

/**
 * Class: UtilsLocalDateTime
 * Project: Estudo e Monitoramento de Sensores
 * <p>
 * Description:
 *
 * @author Tiago Rodrigues
 * @version 1.0
 * @since 2026-03-18
 */
public final class UtilsLocalDateTime {

    public static DateTimeFormatter formatter;
    private static Locale           localeUsado;
    private UtilsLocalDateTime(){

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

        formatPattern += "      HH:mm:ss";

        formatter = DateTimeFormatter.ofPattern(formatPattern, Locale.getDefault());

    }

    public static String formatLocalDateTime(LocalDateTime dateTime) {

        if (dateTime == null) {
            return null;
        }

        if (localeUsado != Locale.getDefault()){
        inicializaFormatter();
        }

        return dateTime.format(formatter);
    }




}