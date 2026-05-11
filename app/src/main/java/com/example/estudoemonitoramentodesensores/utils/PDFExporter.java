package com.example.estudoemonitoramentodesensores.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;

import androidx.core.content.res.ResourcesCompat;

import com.example.estudoemonitoramentodesensores.R;
import com.example.estudoemonitoramentodesensores.model.SensorEntity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class PDFExporter {

    public static File exportSensors(
            Context context,
            List<SensorEntity> sensors
    ) {

        PdfDocument document = new PdfDocument();

        // ✅ CREATE paint FIRST
        Paint paint = new Paint();
        paint.setTextSize(12);
        paint.setAntiAlias(true);
        paint.setSubpixelText(true);

        // ✅ Load fonts
        Typeface latinFont = ResourcesCompat.getFont(context, R.font.notosans_regular);
        Typeface chineseFont = ResourcesCompat.getFont(context, R.font.notosanssc_regular);

        // ✅ Get language
        String lang = LocaleUtils.getAppLanguage();

        // ✅ Apply font AFTER paint exists
        if ("zh-CN".equals(lang)) {
            paint.setTypeface(chineseFont);
        } else {
            paint.setTypeface(latinFont);
        }

        int pageNumber = 1;
        int y = 40;

        PdfDocument.PageInfo pageInfo =
                new PdfDocument.PageInfo.Builder(595, 842, pageNumber).create();

        PdfDocument.Page page = document.startPage(pageInfo);
        Canvas canvas = page.getCanvas();

        for (SensorEntity s : sensors) {

            canvas.drawText("ID: " + s.getId(), 40, y, paint);
            y += 20;

            canvas.drawText("Name: " + safe(s.getSensorName()), 40, y, paint);
            y += 20;

            canvas.drawText("Sensor ID: " + safe(s.getSensorId()), 40, y, paint);
            y += 20;

            canvas.drawText("Type: " + s.getSensorType(), 40, y, paint);
            y += 20;

            canvas.drawText("Status: " + safe(s.getSensorStatus()), 40, y, paint);
            y += 20;

            canvas.drawText("Usage: " +
                            (s.getSensorUsage() != null ? s.getSensorUsage().toString() : ""),
                    40, y, paint);
            y += 20;

            canvas.drawText("Date: " +
                            (s.getFirstMeasureDate() != null ? s.getFirstMeasureDate().toString() : ""),
                    40, y, paint);
            y += 30;

            // New page
            if (y > 800) {
                document.finishPage(page);
                pageNumber++;

                pageInfo = new PdfDocument.PageInfo.Builder(595, 842, pageNumber).create();
                page = document.startPage(pageInfo);
                canvas = page.getCanvas();

                y = 40;
            }
        }

        document.finishPage(page);

        File file = new File(
                context.getExternalFilesDir(null),
                "sensors.pdf"
        );

        try {
            document.writeTo(new FileOutputStream(file));
        } catch (IOException e) {
            e.printStackTrace();
        }

        document.close();

        return file;
    }

    private static String safe(String value) {
        return value != null ? value : "";
    }
}