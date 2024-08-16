package com.noedelaluz.mypokedex.infrastructure.datasources

import android.content.Context
import androidx.room.Room
import com.noedelaluz.mypokedex.data.database.entities.PokemonEntity
import com.noedelaluz.mypokedex.domain.datasources.PokemonDatabase
import com.noedelaluz.mypokedex.domain.models.PokemonDetail
import com.noedelaluz.mypokedex.domain.models.PokemonListResponse
import com.noedelaluz.mypokedex.domain.models.PokemonResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PokemonLocalDatasource( applicationContext: Context) {

    private val db = Room.databaseBuilder(
        applicationContext,
        PokemonDatabase::class.java, "pokemon_database"
    ).build()

    fun getPokemon(): Flow<PokemonListResponse> {
        return db.pokemonDao().getAllPokemon().map { pokemonList ->
            val pokemonResponseList = pokemonList.map { pokemonEntity ->
                PokemonResponse(
                    name = pokemonEntity.name,
                    url = pokemonEntity.url
                )
            }
            PokemonListResponse(results = pokemonResponseList)
        }
    }

    fun getFavoritePokemon(name: String): PokemonEntity?{
        return db.pokemonDao().getFavoritePokemon(name)
    }

    fun updateFavoritePokemon(name: String, isFavorite: Int): Int {
        return db.pokemonDao().updateFavoritePokemon(name, isFavorite)
    }

    suspend fun savePokemon(pokemonList: List<PokemonResponse>): Boolean {
        val pokemonEntityList: List<PokemonEntity> = pokemonList.map { pokemonResponse ->
            PokemonEntity(
                id = 0,
                name = pokemonResponse.name,
                url = pokemonResponse.url)
        }

        db.pokemonDao().insertAll(pokemonEntityList)

        return true
    }

    fun getAllFavoritePokemon(): Flow<PokemonListResponse> {
        return db.pokemonDao().getAllFavoritePokemon().map { pokemonList ->
            val pokemonResponseList = pokemonList.map { pokemonEntity ->
                PokemonResponse(
                    name = pokemonEntity.name,
                    url = pokemonEntity.url
                )
            }
            PokemonListResponse(results = pokemonResponseList)
        }
    }


}