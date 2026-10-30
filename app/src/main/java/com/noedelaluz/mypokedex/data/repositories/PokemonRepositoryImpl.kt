package com.noedelaluz.mypokedex.data.repositories

import com.noedelaluz.mypokedex.data.datasources.PokemonLocalDataSource
import com.noedelaluz.mypokedex.data.datasources.PokemonRemoteDataSource
import com.noedelaluz.mypokedex.data.mappers.toDomain
import com.noedelaluz.mypokedex.data.mappers.toEntity
import com.noedelaluz.mypokedex.data.mappers.toTypesByPokemon
import com.noedelaluz.mypokedex.data.util.suspendRunCatching
import com.noedelaluz.mypokedex.domain.models.Pokemon
import com.noedelaluz.mypokedex.domain.models.PokemonDetail
import com.noedelaluz.mypokedex.domain.models.PokemonTypes
import com.noedelaluz.mypokedex.domain.repositories.PokemonRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PokemonRepositoryImpl(
    private val remote: PokemonRemoteDataSource,
    private val local: PokemonLocalDataSource,
) : PokemonRepository {

    override fun observePokemon(): Flow<List<Pokemon>> =
        local.observePokemon().map { entities -> entities.map { it.toDomain() } }

    override fun observeFavorites(): Flow<List<Pokemon>> =
        local.observeFavorites().map { entities -> entities.map { it.toDomain() } }

    override fun observeIsFavorite(name: String): Flow<Boolean> = local.observeIsFavorite(name)

    override suspend fun refreshPokemon(): Result<Unit> = suspendRunCatching {
        val summaries = remote.fetchPokemonList().results
        local.insertMissing(summaries.map { it.toEntity() })
        if (local.hasMissingTypes()) refreshTypes()
    }

    // La lista de PokeAPI no trae tipos: se obtienen de los 18 endpoints de tipo, en paralelo.
    private suspend fun refreshTypes() {
        val typeResponses = coroutineScope {
            PokemonTypes.ALL
                .map { type -> async { type to remote.fetchType(type) } }
                .awaitAll()
        }
        local.updateTypes(typeResponses.toTypesByPokemon())
    }

    override suspend fun getPokemonDetail(name: String): Result<PokemonDetail> = suspendRunCatching {
        remote.fetchPokemonDetail(name).toDomain()
    }

    override suspend fun setFavorite(name: String, isFavorite: Boolean) {
        local.setFavorite(name, isFavorite)
    }
}
