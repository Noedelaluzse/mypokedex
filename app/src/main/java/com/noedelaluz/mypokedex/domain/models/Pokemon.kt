package com.noedelaluz.mypokedex.domain.models

data class Pokemon (
    val name: String,
    val picture: String,
    val id: Int,
    // El primero es el tipo principal. Vacío si aún no se han descargado.
    val types: List<String> = emptyList()
)
