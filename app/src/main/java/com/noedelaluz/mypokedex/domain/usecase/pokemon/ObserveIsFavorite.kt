package com.noedelaluz.mypokedex.domain.usecase.pokemon

import com.noedelaluz.mypokedex.domain.repositories.PokemonRepository
import kotlinx.coroutines.flow.Flow

class ObserveIsFavorite(
    private val repository: PokemonRepository
) {
    operator fun invoke(name: String): Flow<Boolean> = repository.observeIsFavorite(name)
}
