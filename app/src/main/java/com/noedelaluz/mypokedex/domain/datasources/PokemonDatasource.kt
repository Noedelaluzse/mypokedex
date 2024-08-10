package com.noedelaluz.mypokedex.domain.datasources

import com.noedelaluz.mypokedex.domain.models.PokemonDetail
import com.noedelaluz.mypokedex.domain.models.PokemonListResponse

abstract class PokemonDatasource {
    abstract suspend fun getPokemon(): PokemonListResponse
    abstract suspend fun getPokemonById(id: String): PokemonDetail
}
