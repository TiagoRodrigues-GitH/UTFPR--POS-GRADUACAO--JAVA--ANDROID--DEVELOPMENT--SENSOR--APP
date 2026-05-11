package com.example.estudoemonitoramentodesensores.ui.annotation;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.estudoemonitoramentodesensores.model.AnnotationEntity;
import com.example.estudoemonitoramentodesensores.repository.AnnotationRepository;

import java.util.List;

/**
 * Class: AnnotationViewModel
 * Project: Estudo e Monitoramento de Sensores
 * <p>
 * Description:
 *
 * @author Tiago Rodrigues
 * @version 1.0
 * @since 2026-04-03
 */
public class AnnotationViewModel extends AndroidViewModel {

    private final AnnotationRepository repository;

    private LiveData<List<AnnotationEntity>> annotations;


    private final MutableLiveData<Long> insertResult    = new MutableLiveData<>();

    private final MutableLiveData<Integer> operationResult = new MutableLiveData<>();

    public AnnotationViewModel(@NonNull Application application) {
        super(application);
        repository = new AnnotationRepository(application);
    }

    public void loadForSensor(long sensorId) {
        annotations = repository.getForSensor(sensorId);
    }

    public LiveData<List<AnnotationEntity>> getAnnotations()     { return annotations; }
    public LiveData<Long> getInsertResult()    { return insertResult; }
    public LiveData<Integer>               getOperationResult() { return operationResult; }

    public void insert(AnnotationEntity annotation) {
        repository.insert(annotation, newId -> insertResult.postValue(newId));
    }

    public void update(AnnotationEntity annotation) {
        repository.update(annotation, rows -> operationResult.postValue(rows.intValue()));
    }

    public void delete(AnnotationEntity annotation) {
        repository.delete(annotation, rows -> operationResult.postValue(rows));
    }
}