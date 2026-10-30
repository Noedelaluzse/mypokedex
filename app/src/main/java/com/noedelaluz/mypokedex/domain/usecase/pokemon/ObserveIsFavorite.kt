package com.noedelaluz.mypokedex.domain.usecase.pokemon

import com.noedelaluz.mypokedex.domain.repositories.PokemonRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveIsFavorite @Inject constructor(
    private val repository: PokemonRepository
) {
    operator fun invoke(name: String): Flow<Boolean> = repository.observeIsFavorite(name)
}
