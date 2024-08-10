package com.noedelaluz.mypokedex.domain.models


import com.google.gson.annotations.SerializedName

data class Species(
    @SerializedName("name")
    val name: String
)