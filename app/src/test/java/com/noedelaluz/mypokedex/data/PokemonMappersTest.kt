package com.noedelaluz.mypokedex.data

import com.noedelaluz.mypokedex.data.database.entities.PokemonEntity
import com.noedelaluz.mypokedex.data.mappers.toDomain
import com.noedelaluz.mypokedex.data.mappers.toTypesByPokemon
import com.noedelaluz.mypokedex.data.network.dto.NamedResourceDto
import com.noedelaluz.mypokedex.data.network.dto.TypeDto
import com.noedelaluz.mypokedex.data.network.dto.TypePokemonSlotDto
import org.junit.Assert.assertEquals
import org.junit.Test

class PokemonMappersTest {

    @Test
    fun `la entidad se convierte a modelo de dominio usando el id de la url`() {
        val entity = PokemonEntity(id = 1, name = "pikachu", url = "https://pokeapi.co/api/v2/pokemon/25/")

        val pokemon = entity.toDomain()

        assertEquals(25, pokemon.id)
        assertEquals("pikachu", pokemon.name)
        assertEquals(
            "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/25.png",
            pokemon.picture,
        )
    }

    @Test
    fun `la entidad convierte los tipos guardados a una lista con el principal primero`() {
        val entity = PokemonEntity(
            id = 1, name = "bulbasaur", url = "https://pokeapi.co/api/v2/pokemon/1/", types = "grass,poison",
        )

        assertEquals(listOf("grass", "poison"), entity.toDomain().types)
    }

    @Test
    fun `una entidad sin tipos descargados da una lista vacia`() {
        val entity = PokemonEntity(id = 1, name = "bulbasaur", url = "https://pokeapi.co/api/v2/pokemon/1/")

        assertEquals(emptyList<String>(), entity.toDomain().types)
    }

    @Test
    fun `las respuestas por tipo se agrupan por pokemon ordenadas por slot`() {
        fun slot(slot: Int, name: String) = TypePokemonSlotDto(slot, NamedResourceDto(name))
        val responses = listOf(
            "poison" to TypeDto(listOf(slot(2, "bulbasaur"))),
            "grass" to TypeDto(listOf(slot(1, "bulbasaur"), slot(1, "oddish"))),
        )

        val result = responses.toTypesByPokemon()

        assertEquals("grass,poison", result["bulbasaur"])
        assertEquals("grass", result["oddish"])
    }
}
