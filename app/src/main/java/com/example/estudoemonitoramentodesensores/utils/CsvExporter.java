package com.example.estudoemonitoramentodesensores.utils;

import com.example.estudoemonitoramentodesensores.model.SensorEntity;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CsvExporter {

    public static File exportSensors(
            List<SensorEntity> sensors,
            File file
    ) {

        try (OutputStreamWriter writer =
                     new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8)) {

            writer.append("id,name,sensorId,type,status,usage,date\n");

            for (SensorEntity s : sensors) {

                writer.append(String.valueOf(s.getId())).append(",");
                writer.append(safe(s.getSensorName())).append(",");
                writer.append(safe(s.getSensorId())).append(",");
                writer.append(String.valueOf(s.getSensorType())).append(",");
                writer.append(safe(s.getSensorStatus())).append(",");
                writer.append(s.getSensorUsage() != null ? s.getSensorUsage().toString() : "").append(",");
                writer.append(s.getFirstMeasureDate() != null ? s.getFirstMeasureDate().toString() : "");
                writer.append("\n");
            }
        }catch (IOException e) {
            e.printStackTrace();
        }

        return file;
    }

    private static String safe(String value) {
        return value != null ? value : "";
    }
}