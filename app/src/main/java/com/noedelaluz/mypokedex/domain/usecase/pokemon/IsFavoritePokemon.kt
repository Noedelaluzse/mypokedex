package com.noedelaluz.mypokedex.domain.usecase.pokemon

import com.noedelaluz.mypokedex.data.database.entities.PokemonEntity
import com.noedelaluz.mypokedex.domain.repositories.PokemonRepository
import kotlinx.coroutines.flow.Flow

interface IsFavoritePokemonUseCase {
    suspend fun  execute(name: String): PokemonEntity?
}

class IsFavoritePokemon(
    private val repository: PokemonRepository
): IsFavoritePokemonUseCase {

    override suspend fun execute(name: String): PokemonEntity?  {
        return this.repository.isFavoritePokemon(name)
    }
}