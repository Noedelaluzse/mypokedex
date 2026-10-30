package com.noedelaluz.mypokedex.data.datasources

import androidx.room.withTransaction
import com.noedelaluz.mypokedex.data.database.PokemonDatabase
import com.noedelaluz.mypokedex.data.database.entities.PokemonEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface PokemonLocalDataSource {
    fun observePokemon(): Flow<List<PokemonEntity>>
    fun observeFavorites(): Flow<List<PokemonEntity>>
    fun observeIsFavorite(name: String): Flow<Boolean>

    /** Inserta solo los pokemon que aún no existen, para no perder los favoritos ya marcados. */
    suspend fun insertMissing(pokemon: List<PokemonEntity>)
    suspend fun setFavorite(name: String, isFavorite: Boolean)
}

class PokemonLocalDataSourceImpl @Inject constructor(
    private val database: PokemonDatabase
) : PokemonLocalDataSource {

    private val dao = database.pokemonDao()

    override fun observePokemon(): Flow<List<PokemonEntity>> = dao.observeAll()

    override fun observeFavorites(): Flow<List<PokemonEntity>> = dao.observeFavorites()

    override fun observeIsFavorite(name: String): Flow<Boolean> = dao.observeIsFavorite(name)

    override suspend fun insertMissing(pokemon: List<PokemonEntity>) {
        database.withTransaction {
            val existingNames = dao.getAllNames().toSet()
            dao.insertAll(pokemon.filter { it.name !in existingNames })
        }
    }

    override suspend fun setFavorite(name: String, isFavorite: Boolean) {
        dao.updateFavorite(name, if (isFavorite) 1 else 0)
    }
}
