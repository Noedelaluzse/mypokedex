package com.noedelaluz.mypokedex.data.network

import com.noedelaluz.mypokedex.domain.models.PokemonDetail
import com.noedelaluz.mypokedex.domain.models.PokemonListResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface PokeApi {

    @GET("pokemon/{pokemonName}")
    suspend fun getPokemon(
        @Path("pokemonName") pokemonName: String
    ): Response<PokemonDetail>

    // Obtener la lista de pokemones
    @GET("pokemon?limit=151")
    suspend fun getPokemonList(): Response<PokemonListResponse>

}