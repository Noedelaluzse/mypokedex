package com.noedelaluz.mypokedex.data.network.dto

import com.google.gson.annotations.SerializedName

data class PokemonListDto(
    @SerializedName("results") val results: List<PokemonSummaryDto>,
)

data class PokemonSummaryDto(
    @SerializedName("name") val name: String,
    @SerializedName("url") val url: String,
)

data class PokemonDetailDto(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("height") val height: Int,
    @SerializedName("weight") val weight: Int,
    @SerializedName("abilities") val abilities: List<AbilitySlotDto>,
    @SerializedName("stats") val stats: List<StatSlotDto>,
    @SerializedName("types") val types: List<TypeSlotDto>,
    @SerializedName("sprites") val sprites: SpritesDto,
)

data class TypeDto(
    @SerializedName("pokemon") val pokemon: List<TypePokemonSlotDto>,
)

data class TypePokemonSlotDto(
    @SerializedName("slot") val slot: Int,
    @SerializedName("pokemon") val pokemon: NamedResourceDto,
)

data class NamedResourceDto(
    @SerializedName("name") val name: String,
)

data class AbilitySlotDto(
    @SerializedName("ability") val ability: NamedResourceDto,
)

data class StatSlotDto(
    @SerializedName("base_stat") val baseStat: Int,
    @SerializedName("stat") val stat: NamedResourceDto,
)

data class TypeSlotDto(
    @SerializedName("type") val type: NamedResourceDto,
)

data class SpritesDto(
    @SerializedName("back_default") val backDefault: String?,
    @SerializedName("back_shiny") val backShiny: String?,
    @SerializedName("front_default") val frontDefault: String?,
    @SerializedName("front_shiny") val frontShiny: String?,
)
