package com.example.estudoemonitoramentodesensores.data;

import android.content.Context;

import androidx.annotation.VisibleForTesting;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.estudoemonitoramentodesensores.data.local.converter.ConverterEstadoSensor;
import com.example.estudoemonitoramentodesensores.data.local.converter.ConverterLocalDate;
import com.example.estudoemonitoramentodesensores.data.local.converter.ConverterLocalDateTime;
import com.example.estudoemonitoramentodesensores.data.local.converter.ConverterSensorUsage;
import com.example.estudoemonitoramentodesensores.data.local.dao.AnnotationDao;
import com.example.estudoemonitoramentodesensores.data.local.dao.SensorDao;

import com.example.estudoemonitoramentodesensores.model.AnnotationEntity;
import com.example.estudoemonitoramentodesensores.model.SensorEntity;

/**
 * Class: AppDatabase
 * Project: Estudo e Monitoramento de Sensores
 * <p>
 * Description:
 *
 * @author Tiago Rodrigues
 * @version 1.0
 * @since 2026-03-10
 */


@Database(entities = {SensorEntity.class,
                      AnnotationEntity.class},
                      version = 1)
@TypeConverters({ConverterSensorUsage.class,
                 ConverterEstadoSensor.class,
                 ConverterLocalDate.class,
                 ConverterLocalDateTime.class})
public abstract class AppDatabase extends RoomDatabase {

    private static volatile AppDatabase INSTANCE;

    public abstract SensorDao sensorDao();

    public abstract AnnotationDao annotationDao();


    public static AppDatabase getInstance(final Context context) {

       if (INSTANCE == null) {
            synchronized (AppDatabase.class) {

            if (INSTANCE == null) {

                INSTANCE = Room.databaseBuilder(
                        context.getApplicationContext(),
                        AppDatabase.class,
                        "sensores.db")
                        .fallbackToDestructiveMigration()
                        .build();
                }
            }
       }
       return INSTANCE;
    }

    @VisibleForTesting
    public static void resetInstance() {
        if (INSTANCE != null && INSTANCE.isOpen()) {
            INSTANCE.close();
        }
        INSTANCE = null;
    }
}
