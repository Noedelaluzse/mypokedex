package com.noedelaluz.mypokedex.domain.models


import com.google.gson.annotations.SerializedName

data class Move(
    @SerializedName("move")
    val move: MoveDetail
)