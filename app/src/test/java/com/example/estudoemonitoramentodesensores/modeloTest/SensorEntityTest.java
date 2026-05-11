package com.example.estudoemonitoramentodesensores.modeloTest;

/**
 * Class: sensorTest
 * Project: Estudo e Monitoramento de Sensores
 * <p>
 * Description:
 *
 * @author Tiago Rodrigues
 * @version 1.0
 * @since 2026-03-12
 */

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.example.estudoemonitoramentodesensores.model.SensorEntity;
import com.example.estudoemonitoramentodesensores.model.SensorUsage;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;



import com.example.estudoemonitoramentodesensores.model.SensorEntity;
import com.example.estudoemonitoramentodesensores.model.SensorUsage;
import org.junit.Test;
import java.time.LocalDate;
import static org.junit.Assert.*;

    public class SensorEntityTest {

        private SensorEntity makeSensor(String name) {
            return new SensorEntity(name, "ID-001", 0,
                    "Active", SensorUsage.INDUSTRIAL, LocalDate.of(2020, 1, 1));
        }

        @Test public void equalsReturnsTrueForSameSensor() {
            SensorEntity a = makeSensor("Sensor A");
            SensorEntity b = makeSensor("Sensor A");
            assertEquals(a, b);
        }

        @Test public void equalsReturnsFalseForDifferentName() {
            assertNotEquals(makeSensor("Sensor A"), makeSensor("Sensor B"));
        }

        @Test public void comparatorAscendingOrder() {
            SensorEntity a = makeSensor("Alpha");
            SensorEntity b = makeSensor("Beta");
            assertTrue(SensorEntity.ordenacaoCrescente.compare(a, b) < 0);
        }

        @Test public void comparatorDescendingOrder() {
            SensorEntity a = makeSensor("Alpha");
            SensorEntity b = makeSensor("Beta");
            assertTrue(SensorEntity.ordenacaoDecrescente.compare(a, b) > 0);
        }
    }