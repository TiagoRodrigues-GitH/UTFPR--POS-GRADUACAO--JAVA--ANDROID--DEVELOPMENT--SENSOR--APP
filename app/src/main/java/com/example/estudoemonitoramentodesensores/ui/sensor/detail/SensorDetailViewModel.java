package com.example.estudoemonitoramentodesensores.ui.sensor.detail;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.estudoemonitoramentodesensores.R;
import com.example.estudoemonitoramentodesensores.model.SensorEntity;
import com.example.estudoemonitoramentodesensores.repository.AnnotationRepository;
import com.example.estudoemonitoramentodesensores.repository.SensorRepository;

/**
 * Class: SensorDetailViewModel
 * Project: Estudo e Monitoramento de Sensores
 * <p>
 * Description:
 *
 * @author Tiago Rodrigues
 * @version 1.0
 * @since 2026-04-03
 */
public class SensorDetailViewModel extends AndroidViewModel {

    private final SensorRepository sensorRepository;
    private final AnnotationRepository annotationRepository;

    // Quando não-null, a Activity termina com RESULT_OK
    private final MutableLiveData<Long> savedSensorId = new MutableLiveData<>();
    // Quando não-null, Activity mostra o erro (R.string.xxx)
    private final MutableLiveData<Integer> saveError     = new MutableLiveData<>();

    private LiveData<SensorEntity> sensor;
    private LiveData<Integer>      annotationCount;

    public SensorDetailViewModel(@NonNull Application application) {
        super(application);
        sensorRepository     = new SensorRepository(application);
        annotationRepository = new AnnotationRepository(application);
    }

    public void loadSensor(long id) {
        sensor = sensorRepository.getById(id);
    }

    public void loadAnnotationCount(long sensorId) {
        annotationCount = annotationRepository.getCountForSensor(sensorId);
    }

    public LiveData<SensorEntity> getSensor()          { return sensor; }
    public LiveData<Integer>       getAnnotationCount() { return annotationCount; }
    public LiveData<Long>          getSavedSensorId()   { return savedSensorId; }
    public LiveData<Integer>       getSaveError()       { return saveError; }

    public void saveNew(SensorEntity sensor) {
        sensorRepository.insert(sensor, newId -> {
            if (newId > 0) {
                sensor.setId(newId);
                savedSensorId.postValue(newId);
            } else {
                saveError.postValue(R.string.error_ao_tentar_inserir);
            }
        });
    }

    public void saveEdit(SensorEntity sensor) {
        sensorRepository.update(sensor, rows -> {
            if (rows == 1) {
                savedSensorId.postValue(sensor.getId());
            } else {
                saveError.postValue(R.string.erro_ao_tentar_alterar);
            }
        });
    }
}