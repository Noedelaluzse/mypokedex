package com.noedelaluz.mypokedex.data.network

import com.noedelaluz.mypokedex.data.network.dto.PokemonDetailDto
import com.noedelaluz.mypokedex.data.network.dto.PokemonListDto
import com.noedelaluz.mypokedex.data.network.dto.TypeDto
import retrofit2.http.GET
import retrofit2.http.Path

interface PokeApi {

    @GET("pokemon/{pokemonName}")
    suspend fun getPokemon(
        @Path("pokemonName") pokemonName: String
    ): PokemonDetailDto

    // Pokemon que pertenecen a un tipo (con su slot: 1 = tipo principal)
    @GET("type/{typeName}")
    suspend fun getType(
        @Path("typeName") typeName: String
    ): TypeDto

    // Obtener la lista de pokemones
    @GET("pokemon?limit=151")
    suspend fun getPokemonList(): PokemonListDto
}
