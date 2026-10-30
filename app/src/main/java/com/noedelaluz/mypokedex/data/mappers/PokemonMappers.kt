package com.noedelaluz.mypokedex.data.mappers

import com.noedelaluz.mypokedex.data.database.entities.PokemonEntity
import com.noedelaluz.mypokedex.data.network.dto.PokemonDetailDto
import com.noedelaluz.mypokedex.data.network.dto.PokemonSummaryDto
import com.noedelaluz.mypokedex.domain.models.Pokemon
import com.noedelaluz.mypokedex.domain.models.PokemonDetail
import com.noedelaluz.mypokedex.domain.models.PokemonStat

private const val SPRITES_BASE_URL =
    "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon"

fun PokemonSummaryDto.toEntity(): PokemonEntity =
    PokemonEntity(id = 0, name = name, url = url)

fun PokemonEntity.toDomain(): Pokemon {
    val pokedexId = url.pokedexId()
    return Pokemon(
        name = name,
        picture = "$SPRITES_BASE_URL/$pokedexId.png",
        id = pokedexId,
    )
}

fun PokemonDetailDto.toDomain(): PokemonDetail = PokemonDetail(
    id = id,
    name = name,
    height = height,
    weight = weight,
    artworkUrl = "$SPRITES_BASE_URL/other/official-artwork/$id.png",
    abilities = abilities.map { it.ability.name },
    types = types.map { it.type.name },
    stats = stats.map { PokemonStat(name = it.stat.name, baseStat = it.baseStat) },
    sprites = listOfNotNull(
        sprites.backDefault,
        sprites.backShiny,
        sprites.frontDefault,
        sprites.frontShiny,
    ),
)

// La url tiene la forma https://pokeapi.co/api/v2/pokemon/25/
private fun String.pokedexId(): Int = trimEnd('/').substringAfterLast('/').toInt()
