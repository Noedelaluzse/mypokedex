package com.noedelaluz.mypokedex.domain.repositories

import com.noedelaluz.mypokedex.domain.models.Pokemon
import com.noedelaluz.mypokedex.domain.models.PokemonDetail
import kotlinx.coroutines.flow.Flow

interface PokemonRepository {

    /** Lista guardada localmente. Es la única fuente de verdad de la pantalla principal. */
    fun observePokemon(): Flow<List<Pokemon>>

    fun observeFavorites(): Flow<List<Pokemon>>

    fun observeIsFavorite(name: String): Flow<Boolean>

    /** Descarga la lista desde la red y la guarda localmente. */
    suspend fun refreshPokemon(): Result<Unit>

    suspend fun getPokemonDetail(name: String): Result<PokemonDetail>

    suspend fun setFavorite(name: String, isFavorite: Boolean)
}
