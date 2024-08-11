package com.noedelaluz.mypokedex.domain.usecase.pokemon

import com.noedelaluz.mypokedex.domain.repositories.PokemonRepository

interface SaveFavoritePokemonUseCase {
    suspend fun execute(name: String, isFavorite: Int): Int
}

class SaveFavoritePokemon(
    private val repository: PokemonRepository
): SaveFavoritePokemonUseCase {
    override suspend fun execute(name: String, isFavorite: Int): Int {
        return this.repository.saveFavoritePokemon(name, isFavorite)
    }
}