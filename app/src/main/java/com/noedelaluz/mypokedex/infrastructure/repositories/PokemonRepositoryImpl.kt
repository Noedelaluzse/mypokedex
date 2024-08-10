package com.noedelaluz.mypokedex.infrastructure.repositories

import com.noedelaluz.mypokedex.domain.datasources.PokemonDatasource
import com.noedelaluz.mypokedex.domain.models.PokemonDetail
import com.noedelaluz.mypokedex.domain.models.PokemonListResponse
import com.noedelaluz.mypokedex.domain.models.PokemonResponse
import com.noedelaluz.mypokedex.domain.repositories.PokemonRepository
import com.noedelaluz.mypokedex.infrastructure.datasources.PokemonLocalDatasource
import kotlinx.coroutines.flow.Flow

class PokemonRepositoryImpl(
    private val remote: PokemonDatasource,
    private val local: PokemonLocalDatasource,
): PokemonRepository() {

    override suspend fun getPokemon(): PokemonListResponse {
        return this.remote.getPokemon()
    }

    override suspend fun getPokemonById(id: String): PokemonDetail {
        return this.remote.getPokemonById(id)
    }

    override suspend fun savePokemon(pokemonList: List<PokemonResponse> ): Boolean {
        return this.local.savePokemon(pokemonList)
    }

    override suspend fun savePokemonDetail(): Boolean {
        TODO("Not yet implemented")
        return false
    }

    override fun getPokemonList(): Flow<PokemonListResponse> {
        val pokemonList: Flow<PokemonListResponse> = this.local.getPokemon()

        return pokemonList
    }
}