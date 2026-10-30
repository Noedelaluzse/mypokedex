package com.noedelaluz.mypokedex.presentation.ui.fragments.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.noedelaluz.mypokedex.domain.usecase.pokemon.GetFavoritePokemon
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class FavoriteViewModel(
    getFavoritePokemon: GetFavoritePokemon,
) : ViewModel() {

    val uiState: StateFlow<FavoriteUiState> = getFavoritePokemon()
        .map { pokemon ->
            if (pokemon.isEmpty()) FavoriteUiState.Empty else FavoriteUiState.Success(pokemon)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(STOP_TIMEOUT_MS), FavoriteUiState.Loading)

    private companion object {
        const val STOP_TIMEOUT_MS = 5_000L
    }
}
