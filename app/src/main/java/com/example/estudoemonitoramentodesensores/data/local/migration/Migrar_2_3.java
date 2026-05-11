//package com.example.estudoemonitoramentodesensores.data.local.migration;
//
//import androidx.annotation.NonNull;
//import androidx.room.migration.Migration;
//import androidx.sqlite.db.SupportSQLiteDatabase;
//
///**
// * Class: Migrar_2_3
// * Project: Estudo e Monitoramento de Sensores
// * <p>
// * Description:
// *
// * @author Tiago Rodrigues
// * @version 1.0
// * @since 2026-03-16
// */
//public class Migrar_2_3 extends Migration {
//
//    public Migrar_2_3(){
//        super(2,3);
//    }
//
//
//    @Override
//    public void migrate(@NonNull SupportSQLiteDatabase database) {
//
//        database.execSQL("ALTER TABLE 'Tabela de sensores'  ADD COLUMN 'Data da primeira medição' INTEGER");
//
//
//    }
//}
//
//
//
