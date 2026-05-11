package com.example.estudoemonitoramentodesensores.ui.search;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.*;

import com.example.estudoemonitoramentodesensores.repository.AnnotationRepository;
import com.example.estudoemonitoramentodesensores.repository.SensorRepository;
import com.example.estudoemonitoramentodesensores.model.SensorEntity;
import java.util.List;
import java.util.Map;

/**
 * Class: SearchViewModel
 * Project: Estudo e Monitoramento de Sensores
 *
 * Description: ViewModel para busca de sensores. Permite busca por nome,
 *              ID, ou data. Usa Transformations.switchMap para reatividade.
 *
 * @author Tiago Rodrigues
 * @version 1.0
 * @since 2026-04-05
 */
public class SearchViewModel extends AndroidViewModel {
    private final SensorRepository repository;

    private final AnnotationRepository annotationRepository;

    private final LiveData<Map<Long, Integer>> annotationCounts;


    private final MutableLiveData<String> query = new MutableLiveData<>("");
    private final LiveData<List<SensorEntity>> results;

    public SearchViewModel(@NonNull Application app) {
        super(app);
        repository = new SensorRepository(app);
        annotationRepository = new AnnotationRepository(app);
        annotationCounts = annotationRepository.getAllAnnotationCounts();
        results = Transformations.switchMap(query, q -> repository.search(q));
    }


    public LiveData<List<SensorEntity>> getSearchResults() { return results; }

    public LiveData<Map<Long, Integer>> getAnnotationCounts(){
        return annotationCounts;
    }

    // Busca por nome, ID ou data (DAO faz LIKE em múltiplas colunas)
    public void search(String q) { query.setValue(q == null ? "" : q.trim()); }

    public long getSensorIdAt(int pos) {
        List<SensorEntity> list = results.getValue();
        return (list != null && pos < list.size()) ? list.get(pos).getId() : -1;
    }
}