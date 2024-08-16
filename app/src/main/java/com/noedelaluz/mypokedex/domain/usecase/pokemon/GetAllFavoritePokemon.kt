package com.noedelaluz.mypokedex.domain.usecase.pokemon

import com.noedelaluz.mypokedex.domain.models.PokemonListResponse
import com.noedelaluz.mypokedex.domain.repositories.PokemonRepository
import kotlinx.coroutines.flow.Flow

interface GetAllFavoritePokemonUseCase {
    fun execute(): Flow<PokemonListResponse>
}
class GetAllFavoritePokemon(
    private val repository: PokemonRepository
): GetAllFavoritePokemonUseCase {
    override fun execute():  Flow<PokemonListResponse> {
        return repository.getFavoritePokemonList()
    }
}