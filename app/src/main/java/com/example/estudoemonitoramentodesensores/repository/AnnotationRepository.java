package com.example.estudoemonitoramentodesensores.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.estudoemonitoramentodesensores.data.AppDatabase;
import com.example.estudoemonitoramentodesensores.data.local.dao.AnnotationDao;
import com.example.estudoemonitoramentodesensores.model.AnnotationEntity;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Class: AnnotationRepository
 * Project: Estudo e Monitoramento de Sensores
 * <p>
 * Description:
 *
 * @author Tiago Rodrigues
 * @version 1.0
 * @since 2026-04-03
 */
public class AnnotationRepository {

    private final AnnotationDao annotationDao;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public AnnotationRepository(Application application) {

        annotationDao = AppDatabase.getInstance(application).annotationDao();
    }

    public LiveData<List<AnnotationEntity>> getForSensor(long sensorId) {
        return annotationDao.queryForIdSensorLive(sensorId);
    }

    public LiveData<Integer> getCountForSensor(long sensorId) {
        return annotationDao.totalIdSensoresLive(sensorId);
    }

    public LiveData<Map<Long, Integer>> getAllAnnotationCounts() {
        return annotationDao.getAllAnnotationCounts();
    }

    public LiveData<Map<Long, List<AnnotationEntity>>> getAllAnnotationsGrouped() {
        return annotationDao.getAllAnnotationsGrouped();
    }

    public void insert(AnnotationEntity annotation, SensorRepository.OnResultCallback<Long> cb) {
        executor.execute(() -> {
            long newId = annotationDao.insert(annotation);
            if (cb != null) cb.onResult(newId);
        });
    }

    public void update(AnnotationEntity annotation, SensorRepository.OnResultCallback<Long> cb) {
        executor.execute(() -> {
            long rows = annotationDao.update(annotation);
            if (cb != null) cb.onResult(rows);
        });
    }

    public void delete(AnnotationEntity annotation, SensorRepository.OnResultCallback<Integer> cb) {
        executor.execute(() -> {
            int rows = annotationDao.delete(annotation);
            if (cb != null) cb.onResult(rows);
        });
    }
}