package com.noedelaluz.mypokedex.domain.usecase.pokemon

import com.noedelaluz.mypokedex.domain.models.Pokemon
import com.noedelaluz.mypokedex.domain.repositories.PokemonRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPokemonList @Inject constructor(
    private val repository: PokemonRepository
) {
    operator fun invoke(): Flow<List<Pokemon>> = repository.observePokemon()
}
