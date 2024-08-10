package com.noedelaluz.mypokedex.domain.models


import com.google.gson.annotations.SerializedName

data class MoveDetail(
    @SerializedName("name")
    val name: String
)