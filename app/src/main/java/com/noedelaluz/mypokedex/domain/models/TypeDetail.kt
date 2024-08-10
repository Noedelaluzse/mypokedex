package com.noedelaluz.mypokedex.domain.models


import com.google.gson.annotations.SerializedName

data class TypeDetail(
    @SerializedName("name")
    val name: String,
    @SerializedName("url")
    val url: String
)