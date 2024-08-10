package com.noedelaluz.mypokedex.domain.usecase.pokemon

import com.noedelaluz.mypokedex.domain.models.PokemonListResponse
import com.noedelaluz.mypokedex.domain.repositories.PokemonRepository

interface GetAllPokemonUseCase  {
    suspend fun execute(): PokemonListResponse
}

class GetAllPokemon(
    private val repository: PokemonRepository
) : GetAllPokemonUseCase {

    override suspend fun execute(): PokemonListResponse {
        return repository.getPokemon()
    }
}