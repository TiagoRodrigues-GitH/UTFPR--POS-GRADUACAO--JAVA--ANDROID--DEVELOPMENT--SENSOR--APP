package com.example.estudoemonitoramentodesensores.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import com.example.estudoemonitoramentodesensores.model.SensorEntity;
import java.util.List;

/**
 * Class: SensorDao
 * Project: Estudo e Monitoramento de Sensores
 *
 * Description: DAO para operações CRUD de sensores. Oferece queries síncronas
 *              (para background threads) e LiveData (para observação reativa na UI).
 *
 * @author Tiago Rodrigues
 * @version 1.0
 * @since 2026-04-05
 */
@Dao
public interface SensorDao {

    // ---- Leituras síncronas (mantidas para compatibilidade) ----

    @Query("SELECT * FROM `Sensor table` ORDER BY `Sensor name` ASC")
    List<SensorEntity> queryAllAscending();

    @Query("SELECT * FROM `Sensor table` ORDER BY `Sensor name` DESC")
    List<SensorEntity> queryAllDownward();

    @Query("SELECT * FROM `Sensor table` WHERE id = :id")
    SensorEntity queryForId(long id);

    // ---- Leituras reativas para MVVM ----

    @Query("SELECT * FROM `Sensor table` ORDER BY `Sensor name` ASC")
    LiveData<List<SensorEntity>> queryAllAscendingLive();

    @Query("SELECT * FROM `Sensor table` ORDER BY `Sensor name` DESC")
    LiveData<List<SensorEntity>> queryAllDownwardLive();

    @Query("SELECT * FROM `Sensor table` WHERE id = :id")
    LiveData<SensorEntity> queryForIdLive(long id);

    // Busca por nome, ID do sensor, ou data (formato texto LIKE)
    // Permite buscar por qualquer campo textual relevante
    @Query("SELECT * FROM `Sensor table` " +
            "WHERE `Sensor name` LIKE '%' || :query || '%' " +
            "   OR `Sensor id`   LIKE '%' || :query || '%' " +
            "   OR CAST(`First measure date` AS TEXT) LIKE '%' || :query || '%' " +
            "ORDER BY `Sensor name` ASC")
    LiveData<List<SensorEntity>> search(String query);

    // ---- Escritas (executam via ExecutorService no Repository) ----

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(SensorEntity sensor);

    @Update
    int update(SensorEntity sensor);

    @Delete
    int delete(SensorEntity sensor);
}