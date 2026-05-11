package com.example.estudoemonitoramentodesensores.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.estudoemonitoramentodesensores.data.AppDatabase;
import com.example.estudoemonitoramentodesensores.data.local.dao.SensorDao;
import com.example.estudoemonitoramentodesensores.model.SensorEntity;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Class: SensorRepository
 * Project: Estudo e Monitoramento de Sensores
 * <p>
 * Description:
 *
 * @author Tiago Rodrigues
 * @version 1.0
 * @since 2026-04-03
 */
public class SensorRepository {

    private final SensorDao sensorDao;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public SensorRepository(Application application) {

        sensorDao = AppDatabase.getInstance(application).sensorDao();
    }

    public LiveData<List<SensorEntity>> getAllAscending() {
        return sensorDao.queryAllAscendingLive();
    }

    public LiveData<List<SensorEntity>> getAllDescending() {
        return sensorDao.queryAllDownwardLive();
    }

    public LiveData<SensorEntity> getById(long id) {
        return sensorDao.queryForIdLive(id);
    }

    public void insert(SensorEntity sensor, OnResultCallback<Long> callback) {
        executor.execute(() -> {
            long newId = sensorDao.insert(sensor);
            if (callback != null) callback.onResult(newId);
        });
    }

    public void update(SensorEntity sensor, OnResultCallback<Integer> callback) {
        executor.execute(() -> {
            int rows = sensorDao.update(sensor);
            if (callback != null) callback.onResult(rows);
        });
    }

    public void delete(SensorEntity sensor, OnResultCallback<Integer> callback) {
        executor.execute(() -> {
            int rows = sensorDao.delete(sensor);
            if (callback != null) callback.onResult(rows);
        });
    }
    public LiveData<List<SensorEntity>> search(String query) {
        return sensorDao.search(query == null ? "" : query);
    }

    public interface OnResultCallback<T> {
        void onResult(T result);
    }
}