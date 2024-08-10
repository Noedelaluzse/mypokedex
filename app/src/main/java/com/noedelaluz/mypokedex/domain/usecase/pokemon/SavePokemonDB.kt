package com.noedelaluz.mypokedex.domain.usecase.pokemon

import com.noedelaluz.mypokedex.domain.models.PokemonResponse
import com.noedelaluz.mypokedex.domain.repositories.PokemonRepository

interface SavePokemonDbUseCase {
    suspend fun execute(pokemonList: List<PokemonResponse>): Boolean
}
class SavePokemonDB(
    private val repository: PokemonRepository
): SavePokemonDbUseCase {
    override suspend fun execute(pokemonList: List<PokemonResponse>): Boolean {
        return repository.savePokemon(pokemonList)
    }
}