package com.example.estudoemonitoramentodesensores.utils;

import com.example.estudoemonitoramentodesensores.model.SensorEntity;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.io.File;
import java.io.FileWriter;
import java.util.List;

public class JsonExporter {

    public static File exportSensors(
            List<SensorEntity> sensors,
            File file
    ) {

        JSONArray array = new JSONArray();

        try {

            for (SensorEntity s : sensors) {

                JSONObject obj = new JSONObject();

                obj.put("id", s.getId());
                obj.put("name", safe(s.getSensorName()));
                obj.put("sensorId", safe(s.getSensorId()));
                obj.put("type", s.getSensorType());
                obj.put("status", safe(s.getSensorStatus()));
                obj.put("usage", s.getSensorUsage() != null ? s.getSensorUsage().toString() : "");
                obj.put("date", s.getFirstMeasureDate() != null ? s.getFirstMeasureDate().toString() : "");

                array.put(obj);
            }

            try (OutputStreamWriter writer =
                         new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8)) {

                writer.write(array.toString(2));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return file;
    }

    private static String safe(String value) {
        return value != null ? value : "";
    }
}