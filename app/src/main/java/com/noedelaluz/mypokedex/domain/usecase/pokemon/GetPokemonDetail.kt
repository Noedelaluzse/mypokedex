package com.noedelaluz.mypokedex.domain.usecase.pokemon

import com.noedelaluz.mypokedex.domain.models.PokemonDetail
import com.noedelaluz.mypokedex.domain.repositories.PokemonRepository

class GetPokemonDetail(
    private val repository: PokemonRepository
) {
    suspend operator fun invoke(name: String): Result<PokemonDetail> = repository.getPokemonDetail(name)
}
