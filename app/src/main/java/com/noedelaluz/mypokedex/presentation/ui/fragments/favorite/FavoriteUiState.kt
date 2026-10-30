package com.noedelaluz.mypokedex.presentation.ui.fragments.favorite

import com.noedelaluz.mypokedex.domain.models.Pokemon

sealed interface FavoriteUiState {
    data object Loading : FavoriteUiState
    data object Empty : FavoriteUiState
    data class Success(val pokemon: List<Pokemon>) : FavoriteUiState
}
