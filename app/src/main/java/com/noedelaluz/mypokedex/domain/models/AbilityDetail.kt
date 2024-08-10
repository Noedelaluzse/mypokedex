package com.noedelaluz.mypokedex.domain.models


import com.google.gson.annotations.SerializedName

data class AbilityDetail(
    @SerializedName("name")
    val name: String
)