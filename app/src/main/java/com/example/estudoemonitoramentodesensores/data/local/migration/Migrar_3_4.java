//package com.example.estudoemonitoramentodesensores.data.local.migration;
//
//import androidx.annotation.NonNull;
//import androidx.room.migration.Migration;
//import androidx.sqlite.db.SupportSQLiteDatabase;
//
///**
// * Class: Migrar_3_4
// * Project: Estudo e Monitoramento de Sensores
// * <p>
// * Description:
// *
// * @author Tiago Rodrigues
// * @version 1.0
// * @since 2026-03-18
// */
//public class Migrar_3_4 extends Migration {
//
//
//    public Migrar_3_4() {
//        super(3, 4);
//    }
//
//    public void migrate(@NonNull SupportSQLiteDatabase database) {
//
//        database.execSQL( "CREATE TABLE IF NOT EXISTS `Annotation` ("+
//                             "`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"+
//                             " `idSensor` INTEGER NOT NULL,"+
//                             " `createdAt` INTEGER NOT NULL,"+
//                             " `texto` TEXT NOT NULL, "+
//                             "FOREIGN KEY(`idSensor`) REFERENCES `Tabela de sensores`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
//
//        database.execSQL( "CREATE INDEX IF NOT EXISTS `index_Annotation_createdAt` ON `Annotation` (`createdAt`)");
//
//    }
//
//}