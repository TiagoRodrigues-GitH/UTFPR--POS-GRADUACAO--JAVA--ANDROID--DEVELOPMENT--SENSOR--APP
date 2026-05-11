package com.example.estudoemonitoramentodesensores.data.local.converter;

import androidx.room.TypeConverter;

import com.example.estudoemonitoramentodesensores.model.SensorEntity;


public class ConverterEstadoSensor {

    public static SensorEntity[] sensor;

    /**
     * Converts int → SensorEntity status string
     * 1 → "Active", 0 → "Inactive"
     */
    @TypeConverter
    public static int fromStatusToInt(String sensorStatus) {
        if (sensorStatus == null) {
            return 0;
        }
        return sensorStatus.equals("Active") ? 1 : 0;
    }

    @TypeConverter
    public static String fromIntToStatus(int value) {
        return value == 1 ? "Active" : "Inactive";
    }
}