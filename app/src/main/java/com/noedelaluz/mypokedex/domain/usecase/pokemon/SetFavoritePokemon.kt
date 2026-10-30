package com.noedelaluz.mypokedex.domain.usecase.pokemon

import com.noedelaluz.mypokedex.domain.repositories.PokemonRepository
import javax.inject.Inject

class SetFavoritePokemon @Inject constructor(
    private val repository: PokemonRepository
) {
    suspend operator fun invoke(name: String, isFavorite: Boolean) = repository.setFavorite(name, isFavorite)
}
