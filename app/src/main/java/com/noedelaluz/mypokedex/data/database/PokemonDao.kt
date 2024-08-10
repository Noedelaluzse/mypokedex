package com.noedelaluz.mypokedex.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.noedelaluz.mypokedex.data.database.entities.PokemonEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PokemonDao {

    @Query("SELECT * FROM pokemon_table")
    fun getAllPokemon(): Flow<List<PokemonEntity>>

    @Insert
    fun insertAll(pokemon: List<PokemonEntity>): List<Long>
}