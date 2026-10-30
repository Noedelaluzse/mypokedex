package com.noedelaluz.mypokedex.domain.models

data class PokemonDetail(
    val id: Int,
    val name: String,
    val height: Int,
    val weight: Int,
    val artworkUrl: String,
    val abilities: List<String>,
    val types: List<String>,
    val stats: List<PokemonStat>,
    val sprites: List<String>,
) {
    fun statValue(statName: String): Int =
        stats.firstOrNull { it.name == statName }?.baseStat ?: 0
}
