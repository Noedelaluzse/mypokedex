package com.noedelaluz.mypokedex.infrastructure.datasources

import com.noedelaluz.mypokedex.config.NetworkModule
import com.noedelaluz.mypokedex.data.network.PokeApi
import com.noedelaluz.mypokedex.domain.datasources.PokemonDatasource
import com.noedelaluz.mypokedex.domain.models.PokemonDetail
import com.noedelaluz.mypokedex.domain.models.PokemonListResponse

class PokemonDatasourceImpl: PokemonDatasource() {

    private val pokeApi = NetworkModule.getRetrofit().create(PokeApi::class.java)

    override suspend fun getPokemon(): PokemonListResponse {
        var pokemonListResponse: PokemonListResponse? = null
        try {
            val response = pokeApi.getPokemonList()
            if (response.isSuccessful) {
                pokemonListResponse = response.body()!!
            }
            return pokemonListResponse!!
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return pokemonListResponse!!
    }

    override suspend fun getPokemonById(id: String): PokemonDetail {
        var pokemon: PokemonDetail? = null
        try {
            val response = pokeApi.getPokemon(id)
            if (response.isSuccessful) {
                pokemon = response.body()!!
            }
            return pokemon!!
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return pokemon!!
    }


}