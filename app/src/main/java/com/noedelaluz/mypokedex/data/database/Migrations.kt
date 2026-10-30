package com.noedelaluz.mypokedex.data.database

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.noedelaluz.mypokedex.config.Constants.POKEMON_TABLE

/** Añade la columna de tipos conservando los favoritos ya guardados. */
val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE $POKEMON_TABLE ADD COLUMN types TEXT NOT NULL DEFAULT ''")
    }
}
