package com.noedelaluz.mypokedex.domain.repositories

import com.noedelaluz.mypokedex.data.database.entities.PokemonEntity
import com.noedelaluz.mypokedex.domain.models.PokemonDetail
import com.noedelaluz.mypokedex.domain.models.PokemonResponse
import com.noedelaluz.mypokedex.domain.models.PokemonListResponse
import kotlinx.coroutines.flow.Flow

abstract class PokemonRepository {
    abstract suspend fun getPokemon(): PokemonListResponse
    abstract suspend fun getPokemonById(id: String): PokemonDetail

    abstract suspend fun savePokemon(pokemonList: List<PokemonResponse>): Boolean
    abstract suspend fun savePokemonDetail(): Boolean
    abstract suspend fun isFavoritePokemon(name: String): PokemonEntity?
    abstract suspend fun saveFavoritePokemon(name: String, isFavorite: Int): Int
    abstract fun getPokemonList(): Flow<PokemonListResponse>
}