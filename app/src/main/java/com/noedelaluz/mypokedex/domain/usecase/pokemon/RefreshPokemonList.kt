package com.noedelaluz.mypokedex.domain.usecase.pokemon

import com.noedelaluz.mypokedex.domain.repositories.PokemonRepository

class RefreshPokemonList(
    private val repository: PokemonRepository
) {
    suspend operator fun invoke(): Result<Unit> = repository.refreshPokemon()
}
