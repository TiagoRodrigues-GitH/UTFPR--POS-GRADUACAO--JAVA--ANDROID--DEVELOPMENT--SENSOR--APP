package com.example.estudoemonitoramentodesensores.model;

import static androidx.room.ForeignKey.CASCADE;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.time.LocalDateTime;
import java.util.Comparator;

/**
 * Class: AnnotationEntity
 * Project: Estudo e Monitoramento de Sensores
 * <p>
 * Description:
 *
 * @author Tiago Rodrigues
 * @version 1.0
 * @since 2026-03-18
 */

    @Entity(tableName = "Annotation table" ,foreignKeys = {@ForeignKey(entity = SensorEntity.class,
                                  parentColumns ="id",
                                  childColumns ="idSensor",
                                  onDelete =  CASCADE )})
    public class AnnotationEntity {

        @PrimaryKey(autoGenerate = true)
        private long id;

        private long idSensor;

        @NonNull
        @ColumnInfo(index = true)
        private LocalDateTime createdAt;

        @NonNull
        private String text;

    public AnnotationEntity(long idSensor, @NonNull LocalDateTime createdAt, @NonNull String text) {
        this.idSensor  = idSensor;
        this.createdAt = createdAt;
        this.text      = text;
    }

    public static Comparator<AnnotationEntity> orderDESC = new Comparator<AnnotationEntity>() {

        @Override
        public int compare(AnnotationEntity annotationEntity1, AnnotationEntity annotationEntity2) {
            return -1 * annotationEntity1.getCreatedAt().compareTo(annotationEntity2.getCreatedAt());
        }
    };

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getIdSensor() {
        return idSensor;
    }

    public void setIdSensor(long idSensor) {
        this.idSensor = idSensor;
    }

    @NonNull
    public String getText() {
        return text;
    }

    public void setText(@NonNull String text) {
        this.text = text;
    }

    @NonNull
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(@NonNull LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

}