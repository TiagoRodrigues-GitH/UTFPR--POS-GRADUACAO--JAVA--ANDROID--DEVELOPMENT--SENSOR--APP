package com.example.estudoemonitoramentodesensores.data.local.converter;

import androidx.room.TypeConverter;

import java.time.LocalDate;

/**
 * Class: ConverterLocalDate
 * Project: Estudo e Monitoramento de Sensores
 * <p>
 * Description:
 *
 * @author Tiago Rodrigues
 * @version 1.0
 * @since 2026-03-16
 */

public class ConverterLocalDate {


    @TypeConverter
    public static Long fromLocalDateToLong(LocalDate date) {
        if (date == null) {
            return null;
        }
        return date.toEpochDay();
    }

    @TypeConverter
    public static LocalDate fromLongToLocalDate(Long epochDay){
        if(epochDay == null){
            return null;
        }
        return LocalDate.ofEpochDay(epochDay);


    }
}