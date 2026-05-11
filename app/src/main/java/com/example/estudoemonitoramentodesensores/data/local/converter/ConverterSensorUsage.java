package com.example.estudoemonitoramentodesensores.data.local.converter;

import androidx.room.TypeConverter;

import com.example.estudoemonitoramentodesensores.model.SensorUsage;

/**
 * Class: ConverterSensorUsage
 * Project: Estudo e Monitoramento de Sensores
 * <p>
 * Description:
 *
 * @author Tiago Rodrigues
 * @version 1.0
 * @since 2026-03-14
 */
public class ConverterSensorUsage {

    public static SensorUsage[] sensorUsages = SensorUsage.values();
    @TypeConverter
    public static int fromEnumToInt(SensorUsage sensorUsage) {

        if(sensorUsage == null){
            return -1;
        }
        return sensorUsage.ordinal();
    }

    @TypeConverter
    public static SensorUsage fromIntToEnum(int ordinal){
        if(ordinal < 0){
            return null;
        }
        return sensorUsages[ordinal];
    }
}