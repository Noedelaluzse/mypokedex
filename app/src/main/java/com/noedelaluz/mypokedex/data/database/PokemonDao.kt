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

    @Query("SELECT * FROM pokemon_table WHERE name = :name AND isFavorite = 1 LIMIT 1")
    fun getFavoritePokemon(name: String): PokemonEntity?

    @Query("UPDATE pokemon_table SET isFavorite = :isFavorite WHERE name = :name")
    fun updateFavoritePokemon(name: String, isFavorite: Int): Int

}