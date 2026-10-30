package com.noedelaluz.mypokedex.presentation.ui.fragments.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.noedelaluz.mypokedex.R
import com.noedelaluz.mypokedex.domain.connectivity.ConnectivityObserver
import com.noedelaluz.mypokedex.domain.models.Pokemon
import com.noedelaluz.mypokedex.domain.usecase.pokemon.GetPokemonList
import com.noedelaluz.mypokedex.domain.usecase.pokemon.RefreshPokemonList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    getPokemonList: GetPokemonList,
    private val refreshPokemonList: RefreshPokemonList,
    connectivityObserver: ConnectivityObserver,
) : ViewModel() {

    private val isRefreshing = MutableStateFlow(true)
    private var refreshJob: Job? = null

    private val searchQuery = MutableStateFlow("")
    private val _selectedType = MutableStateFlow<String?>(null)

    /** Tipo seleccionado en los filtros; null = todos. */
    val selectedType: StateFlow<String?> = _selectedType

    private val messageChannel = Channel<Int>(Channel.BUFFERED)

    /** Mensajes de un solo uso (id de string) que la vista muestra como Toast. */
    val messages = messageChannel.receiveAsFlow()

    // Room es la fuente de verdad; la red solo la actualiza.
    val uiState: StateFlow<HomeUiState> = combine(
        getPokemonList(),
        isRefreshing,
        searchQuery,
        _selectedType,
    ) { pokemon, refreshing, query, type ->
        when {
            pokemon.isNotEmpty() -> HomeUiState.Success(pokemon.filter { it.matches(query, type) })
            refreshing -> HomeUiState.Loading
            else -> HomeUiState.Error
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(STOP_TIMEOUT_MS), HomeUiState.Loading)

    init {
        observeConnectivity(connectivityObserver)
        refresh()
    }

    fun onSearchQueryChange(query: String) {
        searchQuery.value = query
    }

    fun onTypeSelected(type: String?) {
        _selectedType.value = type
    }

    fun refresh() {
        if (refreshJob?.isActive == true) return
        refreshJob = viewModelScope.launch {
            isRefreshing.value = true
            refreshPokemonList()
            isRefreshing.value = false
        }
    }

    private fun observeConnectivity(connectivityObserver: ConnectivityObserver) {
        viewModelScope.launch {
            var wasOffline = false
            connectivityObserver.isOnline.collect { isOnline ->
                if (!isOnline) {
                    wasOffline = true
                    messageChannel.send(R.string.no_internet_connection)
                } else if (wasOffline) {
                    wasOffline = false
                    messageChannel.send(R.string.back_online)
                    refresh()
                }
            }
        }
    }

    private fun Pokemon.matches(query: String, type: String?): Boolean {
        val normalizedQuery = query.trim().removePrefix("#")
        val matchesQuery = normalizedQuery.isEmpty() ||
            name.contains(normalizedQuery, ignoreCase = true) ||
            id.toString() == normalizedQuery
        val matchesType = type == null || type in types
        return matchesQuery && matchesType
    }

    private companion object {
        const val STOP_TIMEOUT_MS = 5_000L
    }
}
