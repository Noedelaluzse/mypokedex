package com.noedelaluz.mypokedex.data.datasources

import com.noedelaluz.mypokedex.data.network.PokeApi
import com.noedelaluz.mypokedex.data.network.dto.PokemonDetailDto
import com.noedelaluz.mypokedex.data.network.dto.PokemonListDto
import com.noedelaluz.mypokedex.data.network.dto.TypeDto
import javax.inject.Inject

interface PokemonRemoteDataSource {
    suspend fun fetchPokemonList(): PokemonListDto
    suspend fun fetchPokemonDetail(name: String): PokemonDetailDto
    suspend fun fetchType(type: String): TypeDto
}

class PokemonRemoteDataSourceImpl @Inject constructor(
    private val api: PokeApi
) : PokemonRemoteDataSource {

    override suspend fun fetchPokemonList(): PokemonListDto = api.getPokemonList()

    override suspend fun fetchPokemonDetail(name: String): PokemonDetailDto = api.getPokemon(name)

    override suspend fun fetchType(type: String): TypeDto = api.getType(type)
}
