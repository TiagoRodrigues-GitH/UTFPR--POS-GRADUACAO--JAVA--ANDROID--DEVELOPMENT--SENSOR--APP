package com.example.estudoemonitoramentodesensores;

import android.content.Context;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import com.example.estudoemonitoramentodesensores.data.AppDatabase;
import com.example.estudoemonitoramentodesensores.data.local.dao.AnnotationDao;
import com.example.estudoemonitoramentodesensores.data.local.dao.SensorDao;
import com.example.estudoemonitoramentodesensores.model.AnnotationEntity;
import com.example.estudoemonitoramentodesensores.model.SensorEntity;
import com.example.estudoemonitoramentodesensores.model.SensorUsage;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import static org.junit.Assert.*;

/**
 * Class: DatabaseTests
 * Project: Estudo e Monitoramento de Sensores
 *
 * Description: Testes de integração para Room Database. Valida operações CRUD,
 *              TypeConverters (LocalDate, LocalDateTime, enum), cascata de
 *              foreign keys, e queries LiveData.
 *
 * @author Tiago Rodrigues
 * @version 1.0
 * @since 2026-04-05
 */
@RunWith(AndroidJUnit4.class)
public class DatabaseTests {

    private AppDatabase database;
    private SensorDao sensorDao;
    private AnnotationDao annotationDao;

    @Before
    public void criarDatabase() {
        // Database em memória (apagada após testes)
        Context context = ApplicationProvider.getApplicationContext();
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase.class)
                .allowMainThreadQueries() // Apenas para testes
                .build();
        sensorDao = database.sensorDao();
        annotationDao = database.annotationDao();
    }

    @After
    public void fecharDatabase() {
        database.close();
    }

    // ==================== TESTES SENSOR ====================

    @Test
    public void testInserirSensor() {
        // Arrange: criar sensor
        SensorEntity sensor = new SensorEntity(
                "Sensor Temperatura",
                "TEMP-001",
                2, // Tipo 2
                "Active",
                SensorUsage.INDUSTRIAL,
                LocalDate.of(2024, 1, 15)
        );

        // Act: inserir
        long id = sensorDao.insert(sensor);

        // Assert: ID válido
        assertTrue("ID deve ser maior que 0", id > 0);

        // Assert: recuperar e comparar
        SensorEntity recuperado = sensorDao.queryForId(id);
        assertNotNull("Sensor não pode ser null", recuperado);
        assertEquals("Nome incorreto", "Sensor Temperatura", recuperado.getSensorName());
        assertEquals("Sensor ID incorreto", "TEMP-001", recuperado.getSensorId());
        assertEquals("Status incorreto", "Active", recuperado.getSensorStatus());
        assertEquals("Uso incorreto", SensorUsage.INDUSTRIAL, recuperado.getSensorUsage());
        assertEquals("Data incorreta", LocalDate.of(2024, 1, 15), recuperado.getFirstMeasureDate());
    }

    @Test
    public void testAtualizarSensor() {
        // Arrange: inserir sensor
        SensorEntity sensor = criarSensorPadrao();
        long id = sensorDao.insert(sensor);
        sensor.setId(id);

        // Act: alterar nome e status
        sensor.setSensorName("Sensor Atualizado");
        sensor.setSensorStatus("Inactive");
        int rows = sensorDao.update(sensor);

        // Assert: uma linha alterada
        assertEquals("Deve alterar 1 linha", 1, rows);

        // Assert: verificar alterações
        SensorEntity atualizado = sensorDao.queryForId(id);
        assertEquals("Nome não atualizado", "Sensor Atualizado", atualizado.getSensorName());
        assertEquals("Status não atualizado", "Inactive", atualizado.getSensorStatus());
    }

    @Test
    public void testDeletarSensor() {
        // Arrange
        SensorEntity sensor = criarSensorPadrao();
        long id = sensorDao.insert(sensor);
        sensor.setId(id);

        // Act: deletar
        int rows = sensorDao.delete(sensor);

        // Assert
        assertEquals("Deve deletar 1 linha", 1, rows);
        assertNull("Sensor deve ser null após delete", sensorDao.queryForId(id));
    }

    @Test
    public void testListarSensoresOrdenados() {
        // Arrange: inserir 3 sensores
        SensorEntity s1 = criarSensor("Zebra Sensor", "Z001");
        SensorEntity s2 = criarSensor("Alpha Sensor", "A001");
        SensorEntity s3 = criarSensor("Beta Sensor", "B001");
        sensorDao.insert(s1);
        sensorDao.insert(s2);
        sensorDao.insert(s3);

        // Act: listar ascendente
        List<SensorEntity> asc = sensorDao.queryAllAscending();

        // Assert: ordem alfabética
        assertEquals("Deve ter 3 sensores", 3, asc.size());
        assertEquals("Primeiro deve ser Alpha", "Alpha Sensor", asc.get(0).getSensorName());
        assertEquals("Segundo deve ser Beta", "Beta Sensor", asc.get(1).getSensorName());
        assertEquals("Terceiro deve ser Zebra", "Zebra Sensor", asc.get(2).getSensorName());

        // Act: listar descendente
        List<SensorEntity> desc = sensorDao.queryAllDownward();

        // Assert: ordem reversa
        assertEquals("Primeiro deve ser Zebra", "Zebra Sensor", desc.get(0).getSensorName());
        assertEquals("Último deve ser Alpha", "Alpha Sensor", desc.get(2).getSensorName());
    }

    // ==================== TESTES ANNOTATION ====================

    @Test
    public void testInserirAnotacao() {
        // Arrange: criar sensor primeiro (FK)
        SensorEntity sensor = criarSensorPadrao();
        long sensorId = sensorDao.insert(sensor);

        AnnotationEntity anotacao = new AnnotationEntity(
                sensorId,
                LocalDateTime.of(2024, 3, 20, 14, 30, 0),
                "Calibração realizada"
        );

        // Act
        long id = annotationDao.insert(anotacao);

        // Assert
        assertTrue("ID deve ser > 0", id > 0);

        List<AnnotationEntity> lista = annotationDao.queryForIdSensor(sensorId);
        assertEquals("Deve ter 1 anotação", 1, lista.size());
        assertEquals("Texto incorreto", "Calibração realizada", lista.get(0).getText());
    }

    @Test
    public void testCascataDelete() {
        // Arrange: sensor com 2 anotações
        SensorEntity sensor = criarSensorPadrao();
        long sensorId = sensorDao.insert(sensor);

        AnnotationEntity a1 = new AnnotationEntity(sensorId, LocalDateTime.now(), "Nota 1");
        AnnotationEntity a2 = new AnnotationEntity(sensorId, LocalDateTime.now(), "Nota 2");
        annotationDao.insert(a1);
        annotationDao.insert(a2);

        // Assert: 2 anotações existem
        assertEquals("Deve ter 2 anotações", 2, annotationDao.totalIdSensores(sensorId));

        // Act: deletar sensor (cascata deve apagar anotações)
        sensor.setId(sensorId);
        sensorDao.delete(sensor);

        // Assert: anotações foram apagadas
        assertEquals("Anotações devem ser apagadas", 0, annotationDao.totalIdSensores(sensorId));
    }

    @Test
    public void testOrdenacaoAnotacoesPorData() throws InterruptedException {
        // Arrange: sensor com 3 anotações em datas diferentes
        SensorEntity sensor = criarSensorPadrao();
        long sensorId = sensorDao.insert(sensor);

        AnnotationEntity a1 = new AnnotationEntity(sensorId, LocalDateTime.of(2024, 1, 10, 10, 0), "Mais antiga");
        AnnotationEntity a2 = new AnnotationEntity(sensorId, LocalDateTime.of(2024, 3, 15, 14, 0), "Mais recente");
        AnnotationEntity a3 = new AnnotationEntity(sensorId, LocalDateTime.of(2024, 2, 20, 12, 0), "Intermediária");

        annotationDao.insert(a1);
        Thread.sleep(10); // Evitar conflito de timestamp
        annotationDao.insert(a2);
        Thread.sleep(10);
        annotationDao.insert(a3);

        // Act: query ordena DESC por createdAt
        List<AnnotationEntity> lista = annotationDao.queryForIdSensor(sensorId);

        // Assert: ordem decrescente (mais recente primeiro)
        assertEquals("Deve ter 3 anotações", 3, lista.size());
        assertEquals("Primeira deve ser mais recente", "Mais recente", lista.get(0).getText());
        assertEquals("Segunda deve ser intermediária", "Intermediária", lista.get(1).getText());
        assertEquals("Terceira deve ser mais antiga", "Mais antiga", lista.get(2).getText());
    }

    // ==================== TESTES LIVEDATA ====================

    @Test
    public void testLiveDataSensores() throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);
        final List<SensorEntity>[] resultado = new List[1];

        // Observar LiveData
        LiveData<List<SensorEntity>> liveData = sensorDao.queryAllAscendingLive();
        liveData.observeForever(new Observer<List<SensorEntity>>() {
            @Override
            public void onChanged(List<SensorEntity> sensors) {
                resultado[0] = sensors;
                latch.countDown();
            }
        });

        // Inserir sensor
        SensorEntity sensor = criarSensorPadrao();
        sensorDao.insert(sensor);

        // Aguardar LiveData atualizar (timeout 2s)
        assertTrue("LiveData deve emitir em 2s", latch.await(2, TimeUnit.SECONDS));
        assertNotNull("Lista não pode ser null", resultado[0]);
        assertEquals("Deve ter 1 sensor", 1, resultado[0].size());
    }

    @Test
    public void testBuscaPorTexto() throws InterruptedException {
        // Arrange: inserir sensores variados
        sensorDao.insert(criarSensor("Sensor Temperatura", "TEMP-001"));
        sensorDao.insert(criarSensor("Sensor Pressão", "PRES-002"));
        sensorDao.insert(criarSensor("Medidor Umidade", "UMID-003"));

        final CountDownLatch latch = new CountDownLatch(1);
        final List<SensorEntity>[] resultado = new List[1];

        // Act: buscar por "Sensor"
        LiveData<List<SensorEntity>> liveData = sensorDao.search("Sensor");
        liveData.observeForever(new Observer<List<SensorEntity>>() {
            @Override
            public void onChanged(List<SensorEntity> sensors) {
                resultado[0] = sensors;
                latch.countDown();
            }
        });

        assertTrue("Busca deve retornar em 2s", latch.await(2, TimeUnit.SECONDS));

        // Assert: apenas sensores com "Sensor" no nome
        assertEquals("Deve encontrar 2 sensores", 2, resultado[0].size());
        assertTrue("Deve conter Temperatura",
                resultado[0].stream().anyMatch(s -> s.getSensorName().contains("Temperatura")));
        assertTrue("Deve conter Pressão",
                resultado[0].stream().anyMatch(s -> s.getSensorName().contains("Pressão")));
    }

    // ==================== HELPERS ====================

    private SensorEntity criarSensorPadrao() {
        return new SensorEntity(
                "Sensor Teste",
                "TEST-001",
                1,
                "Active",
                SensorUsage.RESIDENTIAL,
                LocalDate.of(2024, 1, 1)
        );
    }

    private SensorEntity criarSensor(String nome, String id) {
        return new SensorEntity(
                nome,
                id,
                0,
                "Active",
                SensorUsage.OTHERS,
                LocalDate.now()
        );
    }
}