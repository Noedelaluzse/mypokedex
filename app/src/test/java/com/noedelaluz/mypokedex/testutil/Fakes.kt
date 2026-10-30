package com.noedelaluz.mypokedex.testutil

import com.noedelaluz.mypokedex.domain.connectivity.ConnectivityObserver
import com.noedelaluz.mypokedex.domain.models.Pokemon
import com.noedelaluz.mypokedex.domain.models.PokemonDetail
import com.noedelaluz.mypokedex.domain.repositories.PokemonRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow

class FakePokemonRepository : PokemonRepository {
    val pokemon = MutableStateFlow<List<Pokemon>>(emptyList())
    var refreshResult: Result<Unit> = Result.success(Unit)
    var refreshCalls = 0
        private set

    override fun observePokemon(): Flow<List<Pokemon>> = pokemon
    override fun observeFavorites(): Flow<List<Pokemon>> = MutableStateFlow(emptyList())
    override fun observeIsFavorite(name: String): Flow<Boolean> = MutableStateFlow(false)

    override suspend fun refreshPokemon(): Result<Unit> {
        refreshCalls++
        return refreshResult
    }

    override suspend fun getPokemonDetail(name: String): Result<PokemonDetail> =
        Result.failure(UnsupportedOperationException())

    override suspend fun setFavorite(name: String, isFavorite: Boolean) = Unit
}

class FakeConnectivityObserver : ConnectivityObserver {
    private val state = MutableSharedFlow<Boolean>(replay = 1)
    override val isOnline: Flow<Boolean> = state

    suspend fun emit(isOnline: Boolean) = state.emit(isOnline)
}
