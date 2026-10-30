package com.noedelaluz.mypokedex.data

import com.noedelaluz.mypokedex.data.database.entities.PokemonEntity
import com.noedelaluz.mypokedex.data.mappers.toDomain
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
}
