package com.noedelaluz.mypokedex.data.util

import kotlin.coroutines.cancellation.CancellationException

/**
 * Igual que [runCatching], pero deja pasar la [CancellationException] para no romper
 * la cancelación cooperativa de las corrutinas.
 */
inline fun <T> suspendRunCatching(block: () -> T): Result<T> =
    try {
        Result.success(block())
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        Result.failure(e)
    }
