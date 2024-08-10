package com.noedelaluz.mypokedex.domain.models


import com.google.gson.annotations.SerializedName

data class PokemonListResponse(
    @SerializedName("results")
    val results: List<PokemonResponse>
)