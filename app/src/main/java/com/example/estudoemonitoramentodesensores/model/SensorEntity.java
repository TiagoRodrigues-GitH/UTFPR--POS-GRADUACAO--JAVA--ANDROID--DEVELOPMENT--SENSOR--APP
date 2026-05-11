package com.example.estudoemonitoramentodesensores.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.Objects;



@Entity(tableName = "Sensor table")
public class SensorEntity implements Cloneable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo()
    private long      id;
    @NonNull
    @ColumnInfo(name ="Sensor name", index = true)
    private String    sensorName;
    @ColumnInfo(name ="Sensor id")
    private String    sensorId;
    @ColumnInfo(name ="Sensor type")
    private int       sensorType;
    @ColumnInfo(name ="Sensor state")
    private String    sensorStatus;
    @ColumnInfo(name ="Sensor application")
    private SensorUsage sensorUsage;

    @ColumnInfo(name ="First measure date")
    private LocalDate firstMeasureDate;


    public SensorEntity(@NonNull String sensorName, String sensorId, int sensorType,
                        String sensorStatus, SensorUsage sensorUsage, LocalDate firstMeasureDate) {

        this.sensorName   = sensorName;
        this.sensorId     = sensorId;
        this.sensorType   = sensorType;
        this.sensorStatus = sensorStatus;
        this.sensorUsage = sensorUsage;
        this.firstMeasureDate = firstMeasureDate;
    }

    public static Comparator<SensorEntity> ordenacaoCrescente = (s1, s2) ->
            s1.getSensorName().compareToIgnoreCase(s2.getSensorName());
    public static Comparator<SensorEntity> ordenacaoDecrescente = (s1, s2) ->
            s2.getSensorName().compareToIgnoreCase(s1.getSensorName());

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }


    @NonNull
    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SensorEntity sensor = (SensorEntity) o;

        if(firstMeasureDate == null && sensor.firstMeasureDate !=null){
            return false;
        }
        if(firstMeasureDate != null && !firstMeasureDate.equals(sensor.firstMeasureDate) ){
            return false;
        }



        return sensorType == sensor.sensorType &&
               Objects.equals(sensorName, sensor.sensorName) &&
               Objects.equals(sensorId, sensor.sensorId) &&
               Objects.equals(sensorStatus, sensor.sensorStatus) &&
               sensorUsage == sensor.sensorUsage;
    }

    @Override
    public int hashCode() {
        return Objects.hash(sensorName, sensorId, sensorType, sensorStatus, sensorUsage, firstMeasureDate);
    }

    @NonNull
    public String getSensorName() { return sensorName; }
    public String getSensorId() { return sensorId; }
    public int getSensorType() { return sensorType; }
    public String getSensorStatus() { return sensorStatus; }
    public SensorUsage getSensorUsage() { return sensorUsage; }
    public LocalDate getFirstMeasureDate() {
        return firstMeasureDate;
    }

    public void setSensorName(@NonNull String sensorName) {
        this.sensorName = sensorName;
    }
    public void setSensorId(String sensorId) {
        this.sensorId = sensorId;
    }
    public void setSensorType(int sensorType) {
        this.sensorType = sensorType;
    }
    public void setSensorStatus(String sensorStatus) {
        this.sensorStatus = sensorStatus;
    }
    public void setSensorUsage(SensorUsage sensorUsage) {
        this.sensorUsage = sensorUsage;
    }

    public void setFirstMeasureDate(LocalDate firstMeasureDate) {
        this.firstMeasureDate = firstMeasureDate;
    }


    @Override
    public String toString() {
        return sensorName + "\n" + sensorId + "\n" + sensorType + "\n" + sensorStatus + "\n" + sensorUsage + "\n" + firstMeasureDate ;
    }
}
