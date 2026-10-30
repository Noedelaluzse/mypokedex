package com.noedelaluz.mypokedex.presentation.ui.fragments.datails

import com.noedelaluz.mypokedex.domain.models.PokemonDetail

sealed interface DetailUiState {
    data object Loading : DetailUiState
    data class Success(val pokemon: PokemonDetail) : DetailUiState
    data object Error : DetailUiState
}
