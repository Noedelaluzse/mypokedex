package com.noedelaluz.mypokedex.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.noedelaluz.mypokedex.data.database.entities.PokemonEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PokemonDao {

    @Query("SELECT * FROM pokemon_table ORDER BY id")
    fun observeAll(): Flow<List<PokemonEntity>>

    @Query("SELECT * FROM pokemon_table WHERE isFavorite = 1 ORDER BY id")
    fun observeFavorites(): Flow<List<PokemonEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM pokemon_table WHERE name = :name AND isFavorite = 1)")
    fun observeIsFavorite(name: String): Flow<Boolean>

    @Query("SELECT name FROM pokemon_table")
    suspend fun getAllNames(): List<String>

    @Query("SELECT COUNT(*) FROM pokemon_table WHERE types = ''")
    suspend fun countWithoutTypes(): Int

    @Query("UPDATE pokemon_table SET types = :types WHERE name = :name")
    suspend fun updateTypes(name: String, types: String)

    @Insert
    suspend fun insertAll(pokemon: List<PokemonEntity>)

    @Query("UPDATE pokemon_table SET isFavorite = :isFavorite WHERE name = :name")
    suspend fun updateFavorite(name: String, isFavorite: Int)
}
