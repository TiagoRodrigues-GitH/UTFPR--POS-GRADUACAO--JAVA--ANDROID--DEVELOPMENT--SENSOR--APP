package com.example.estudoemonitoramentodesensores.data.local.dao;
import com.example.estudoemonitoramentodesensores.model.AnnotationEntity;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import androidx.room.MapInfo;

import java.util.List;
import java.util.Map;

@Dao
public interface AnnotationDao {

    @Query("SELECT * FROM `Annotation table` WHERE idSensor = :sensorId ORDER BY createdAt DESC")
    List<AnnotationEntity> queryForIdSensor(long sensorId);

    @Query("SELECT COUNT(*) FROM `Annotation table` WHERE idSensor = :sensorId")
    int totalIdSensores(long sensorId);

    @Query("SELECT * FROM `Annotation table` WHERE idSensor = :sensorId ORDER BY createdAt DESC")
    LiveData<List<AnnotationEntity>> queryForIdSensorLive(long sensorId);

    @Query("SELECT COUNT(*) FROM `Annotation table` WHERE idSensor = :sensorId")
    LiveData<Integer> totalIdSensoresLive(long sensorId);

    @MapInfo(keyColumn = "idSensor")
    @Query("SELECT * FROM `Annotation table` ORDER BY createdAt DESC")
    LiveData<Map<Long, List<AnnotationEntity>>> getAllAnnotationsGrouped();

    @MapInfo(keyColumn = "idSensor", valueColumn = "count")
    @Query("SELECT idSensor, COUNT(*) as count FROM `Annotation table` GROUP BY idSensor")
    LiveData<Map<Long, Integer>> getAllAnnotationCounts();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(AnnotationEntity annotation);

    @Update
    int update(AnnotationEntity annotation);

    @Delete
    int delete(AnnotationEntity annotation);
}