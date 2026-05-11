package com.example.estudoemonitoramentodesensores.ui.sensor.list;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.example.estudoemonitoramentodesensores.model.AnnotationEntity;
import com.example.estudoemonitoramentodesensores.model.SensorEntity;
import com.example.estudoemonitoramentodesensores.repository.AnnotationRepository;
import com.example.estudoemonitoramentodesensores.repository.SensorRepository;

import java.util.List;
import java.util.Map;

/**
 * Class: SensorListViewModel
 * Project: Estudo e Monitoramento de Sensores
 * <p>
 * Description:
 *
 * @author Tiago Rodrigues
 * @version 1.0
 * @since 2026-04-03
 */

public class SensorListViewModel extends AndroidViewModel {

    private final SensorRepository repository;
    private final AnnotationRepository annotationRepository;

    private final MutableLiveData<Boolean> sortAscending = new MutableLiveData<>(true);

    private final LiveData<List<SensorEntity>> sensors;
    private final LiveData<Map<Long, Integer>> annotationCounts;
    private final LiveData<Map<Long, List<AnnotationEntity>>> annotationsGrouped;

    public SensorListViewModel(@NonNull Application application) {
        super(application);
        repository = new SensorRepository(application);
        annotationRepository = new AnnotationRepository(application);
        
        annotationCounts = annotationRepository.getAllAnnotationCounts();
        annotationsGrouped = annotationRepository.getAllAnnotationsGrouped();
        
        sensors = Transformations.switchMap(sortAscending, ascending ->
                    Boolean.TRUE.equals(ascending)
                            ? repository.getAllAscending()
                            : repository.getAllDescending()
            );
    }

    public LiveData<Map<Long, Integer>> getAnnotationCounts() {
        return annotationCounts;
    }

    public LiveData<Map<Long, List<AnnotationEntity>>> getAnnotationsGrouped() {
        return annotationsGrouped;
    }

    public LiveData<List<SensorEntity>> getSensors() {
        return sensors;
    }

    public void setSortAscending(boolean ascending) {
        sortAscending.setValue(ascending);
    }

    public boolean isSortAscending() {
        Boolean val = sortAscending.getValue();
        return val != null && val;
    }

    public void insert(SensorEntity sensor, SensorRepository.OnResultCallback<Long> callback) {
        repository.insert(sensor, callback);
    }

    public void update(SensorEntity sensor, SensorRepository.OnResultCallback<Integer> callback) {
        repository.update(sensor, callback);
    }

    public void delete(SensorEntity sensor, SensorRepository.OnResultCallback<Integer> callback) {
        repository.delete(sensor, callback);
    }
}