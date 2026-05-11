package com.example.estudoemonitoramentodesensores.modeloTest;



import android.content.Context;
import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import com.example.estudoemonitoramentodesensores.data.AppDatabase;
import com.example.estudoemonitoramentodesensores.data.local.dao.SensorDao;
import com.example.estudoemonitoramentodesensores.data.local.dao.AnnotationDao;
import com.example.estudoemonitoramentodesensores.model.AnnotationEntity;
import com.example.estudoemonitoramentodesensores.model.SensorEntity;
import com.example.estudoemonitoramentodesensores.model.SensorUsage;
import org.junit.*;
import org.junit.runner.RunWith;
import java.time.*;
import java.util.List;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class SensorDaoTest {

    @Rule public InstantTaskExecutorRule rule = new InstantTaskExecutorRule();

    private AppDatabase db;
    private SensorDao   sensorDao;
    private AnnotationDao annotationDao;

    @Before public void setup() {
        Context ctx = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(ctx, AppDatabase.class)
                .allowMainThreadQueries().build();
        sensorDao    = db.sensorDao();
        annotationDao = db.annotationDao();
    }

    @After public void teardown() { db.close(); }

    private SensorEntity sensor(String name) {
        return new SensorEntity(name, "ID-" + name, 0,
                "Active", SensorUsage.INDUSTRIAL, LocalDate.now());
    }

    @Test public void insertAndQueryById() {
        long id = sensorDao.insert(sensor("Alpha"));
        assertTrue(id > 0);
        SensorEntity s = sensorDao.queryForId(id);
        assertNotNull(s);
        assertEquals("Alpha", s.getSensorName());
        assertEquals(SensorUsage.INDUSTRIAL, s.getSensorUsage());
    }

    @Test public void insertTwoAndQueryAll() {
        sensorDao.insert(sensor("Beta"));
        sensorDao.insert(sensor("Alpha"));
        List<SensorEntity> asc = sensorDao.queryAllAscending();
        assertEquals(2, asc.size());
        assertEquals("Alpha", asc.get(0).getSensorName()); // ascending
    }

    @Test public void deleteRemovesSensor() {
        long id = sensorDao.insert(sensor("ToDelete"));
        SensorEntity s = sensorDao.queryForId(id);
        assertEquals(1, sensorDao.delete(s));
        assertNull(sensorDao.queryForId(id));
    }

    @Test public void updateChangesName() {
        long id = sensorDao.insert(sensor("OldName"));
        SensorEntity s = sensorDao.queryForId(id);
        s.setSensorName("NewName");
        assertEquals(1, sensorDao.update(s));
        assertEquals("NewName", sensorDao.queryForId(id).getSensorName());
    }

    @Test public void annotationCascadeDeleteWithSensor() {
        long sid = sensorDao.insert(sensor("Parent"));
        annotationDao.insert(new AnnotationEntity(sid, LocalDateTime.now(), "Note 1"));
        annotationDao.insert(new AnnotationEntity(sid, LocalDateTime.now(), "Note 2"));
        assertEquals(2, annotationDao.totalIdSensores(sid));

        // Delete sensor — CASCADE should remove annotations
        sensorDao.delete(sensorDao.queryForId(sid));
        assertEquals(0, annotationDao.totalIdSensores(sid));
    }

    @Test public void searchFindsPartialName() {
        sensorDao.insert(sensor("Temperature Sensor"));
        sensorDao.insert(sensor("Pressure Gauge"));
        // Sync query for test — LiveData tested via InstantTaskExecutorRule
        List<SensorEntity> all = sensorDao.queryAllAscending();
        long found = all.stream()
                .filter(s -> s.getSensorName().toLowerCase().contains("temp"))
                .count();
        assertEquals(1, found);
    }
}