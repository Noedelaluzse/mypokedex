package com.noedelaluz.mypokedex.data.network

import com.noedelaluz.mypokedex.data.network.dto.PokemonDetailDto
import com.noedelaluz.mypokedex.data.network.dto.PokemonListDto
import retrofit2.http.GET
import retrofit2.http.Path

interface PokeApi {

    @GET("pokemon/{pokemonName}")
    suspend fun getPokemon(
        @Path("pokemonName") pokemonName: String
    ): PokemonDetailDto

    // Obtener la lista de pokemones
    @GET("pokemon?limit=151")
    suspend fun getPokemonList(): PokemonListDto
}
