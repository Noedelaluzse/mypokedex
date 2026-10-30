package com.noedelaluz.mypokedex.presentation.ui.fragments.home

import com.noedelaluz.mypokedex.domain.models.Pokemon

sealed interface HomeUiState {
    data object Loading : HomeUiState
    data class Success(val pokemon: List<Pokemon>) : HomeUiState
    data object Error : HomeUiState
}
