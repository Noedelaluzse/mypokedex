package com.noedelaluz.mypokedex.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.noedelaluz.mypokedex.data.database.entities.PokemonEntity

@Database(entities = [PokemonEntity::class], version = 2)
abstract class PokemonDatabase : RoomDatabase() {
    abstract fun pokemonDao(): PokemonDao
}
