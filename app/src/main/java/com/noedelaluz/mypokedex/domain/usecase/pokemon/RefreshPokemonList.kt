package com.noedelaluz.mypokedex.domain.usecase.pokemon

import com.noedelaluz.mypokedex.domain.repositories.PokemonRepository
import javax.inject.Inject

class RefreshPokemonList @Inject constructor(
    private val repository: PokemonRepository
) {
    suspend operator fun invoke(): Result<Unit> = repository.refreshPokemon()
}
