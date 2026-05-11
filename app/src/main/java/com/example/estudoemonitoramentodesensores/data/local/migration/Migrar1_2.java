//package com.example.estudoemonitoramentodesensores.data.local.migration;
//
//import androidx.annotation.NonNull;
//import androidx.room.migration.Migration;
//import androidx.sqlite.db.SupportSQLiteDatabase;
//
//public class Migrar1_2 extends Migration {
//
//    public Migrar1_2() {
//        super(1, 2); // from version 1 → version 2
//    }
//
//    @Override
//    public void migrate(@NonNull SupportSQLiteDatabase database) {
//
//        // ─── Step 1: Create new table (exact schema from version 2 JSON) ─────
//        database.execSQL("CREATE TABLE IF NOT EXISTS `Tabela de sensores` " +
//                "(" +
//                "`id`               INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
//                "`Nome do sensor`   TEXT    NOT NULL, " +
//                "`Id do sensor`     TEXT, " +               // nullable — no NOT NULL
//                "`Tipo do sensor`   INTEGER NOT NULL, " +
//                "`Estado do sensor` TEXT, " +               // stays TEXT, nullable
//                "`Uso do sensor`    INTEGER" +              // was TEXT, now INTEGER, nullable
//                ")");
//
//        // ─── Step 2: Create index (from version 2 JSON) ──────────────────────
//        database.execSQL("CREATE INDEX IF NOT EXISTS " +
//                "`index_Tabela de sensores_Nome do sensor` " +
//                "ON `Tabela de sensores` (`Nome do sensor`)");
//
//        // ─── Step 3: Copy data, converting `Uso do sensor` TEXT → INTEGER ────
//        database.execSQL("INSERT INTO `Tabela de sensores` " +
//                "(`id`, `Nome do sensor`, `Id do sensor`, `Tipo do sensor`, `Estado do sensor`, `Uso do sensor`) " +
//                "SELECT " +
//                "`id`, " +
//                "`Nome do sensor`, " +
//                "`Id do sensor`, " +
//                "`Tipo do sensor`, " +
//                "`Estado do sensor`, " +   // TEXT → TEXT, no conversion needed
//
//                // ── Uso do sensor: TEXT → INTEGER ──────────────────────────────
//                "CASE `Uso do sensor` " +
//                "WHEN 'Residencia'          THEN 0 " +
//                "WHEN 'Comercial'           THEN 1 " +
//                "WHEN 'Automotivo'          THEN 2 " +
//                "WHEN 'CidadesInteligentes' THEN 3 " +
//                "WHEN 'SetorEnergico'       THEN 4 " +
//                "WHEN 'Varejo'              THEN 5 " +
//                "WHEN 'Agricultura'         THEN 6 " +
//                "WHEN 'Outros'              THEN 7 " +
//                "ELSE NULL " +
//                "END " +
//
//                "FROM `Lista de sensores`");
//
//        // ─── Step 4: Drop the old table ───────────────────────────────────────
//        database.execSQL("DROP TABLE IF EXISTS `Lista de sensores`");
//    }
//}