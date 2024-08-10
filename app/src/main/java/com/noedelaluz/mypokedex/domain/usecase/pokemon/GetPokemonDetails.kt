package com.noedelaluz.mypokedex.domain.usecase.pokemon

import com.noedelaluz.mypokedex.domain.models.PokemonDetail
import com.noedelaluz.mypokedex.domain.repositories.PokemonRepository

interface GetPokemonDetailUseCase {
    suspend fun execute(id: String): PokemonDetail
}

class GetPokemonDetails(
    private val repository: PokemonRepository
): GetPokemonDetailUseCase {
    override suspend fun execute(id: String): PokemonDetail {
        return repository.getPokemonById(id)
    }
}