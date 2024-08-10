package com.noedelaluz.mypokedex.domain.models


import com.google.gson.annotations.SerializedName

data class StatDetail(
    @SerializedName("name")
    val name: String,
    @SerializedName("url")
    val url: String
)