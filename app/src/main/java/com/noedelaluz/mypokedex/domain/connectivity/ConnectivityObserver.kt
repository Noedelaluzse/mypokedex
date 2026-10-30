package com.noedelaluz.mypokedex.domain.connectivity

import kotlinx.coroutines.flow.Flow

interface ConnectivityObserver {
    /** Emite el estado actual al suscribirse y cada vez que cambia. */
    val isOnline: Flow<Boolean>
}
