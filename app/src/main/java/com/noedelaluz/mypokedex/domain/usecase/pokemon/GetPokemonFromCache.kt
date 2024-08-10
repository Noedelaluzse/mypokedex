package com.noedelaluz.mypokedex.domain.usecase.pokemon

import com.noedelaluz.mypokedex.domain.models.PokemonListResponse
import com.noedelaluz.mypokedex.domain.repositories.PokemonRepository
import kotlinx.coroutines.flow.Flow

interface GetPokemonFromCacheUseCase {
    fun execute(): Flow<PokemonListResponse>
}

class GetPokemonFromCache(private val repository: PokemonRepository): GetPokemonFromCacheUseCase {
    override fun execute(): Flow<PokemonListResponse> {
        return this.repository.getPokemonList()
    }
}