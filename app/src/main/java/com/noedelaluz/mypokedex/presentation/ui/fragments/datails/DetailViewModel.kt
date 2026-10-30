package com.noedelaluz.mypokedex.presentation.ui.fragments.datails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.noedelaluz.mypokedex.R
import com.noedelaluz.mypokedex.domain.connectivity.ConnectivityObserver
import com.noedelaluz.mypokedex.domain.usecase.pokemon.GetPokemonDetail
import com.noedelaluz.mypokedex.domain.usecase.pokemon.ObserveIsFavorite
import com.noedelaluz.mypokedex.domain.usecase.pokemon.SetFavoritePokemon
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DetailViewModel(
    private val pokemonName: String,
    private val getPokemonDetail: GetPokemonDetail,
    observeIsFavorite: ObserveIsFavorite,
    private val setFavoritePokemon: SetFavoritePokemon,
    connectivityObserver: ConnectivityObserver,
) : ViewModel() {

    private val _uiState = MutableStateFlow<DetailUiState>(DetailUiState.Loading)
    val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()

    val isFavorite: StateFlow<Boolean> = observeIsFavorite(pokemonName)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(STOP_TIMEOUT_MS), false)

    private val messageChannel = Channel<Int>(Channel.BUFFERED)

    /** Mensajes de un solo uso (id de string) que la vista muestra como Snackbar. */
    val messages = messageChannel.receiveAsFlow()

    private var loadJob: Job? = null

    init {
        loadDetail()
        observeConnectivity(connectivityObserver)
    }

    fun toggleFavorite() {
        viewModelScope.launch {
            val newValue = !isFavorite.value
            setFavoritePokemon(pokemonName, newValue)
            messageChannel.send(
                if (newValue) R.string.pokemon_guardado_en_favoritos
                else R.string.pokemon_eliminado_de_favoritos
            )
        }
    }

    private fun loadDetail() {
        if (loadJob?.isActive == true) return
        loadJob = viewModelScope.launch {
            _uiState.value = DetailUiState.Loading
            getPokemonDetail(pokemonName)
                .onSuccess { _uiState.value = DetailUiState.Success(it) }
                .onFailure { _uiState.value = DetailUiState.Error }
        }
    }

    private fun observeConnectivity(connectivityObserver: ConnectivityObserver) {
        viewModelScope.launch {
            connectivityObserver.isOnline.collect { isOnline ->
                if (!isOnline) {
                    messageChannel.send(R.string.no_internet_connection)
                } else if (_uiState.value is DetailUiState.Error) {
                    loadDetail()
                }
            }
        }
    }

    private companion object {
        const val STOP_TIMEOUT_MS = 5_000L
    }
}
