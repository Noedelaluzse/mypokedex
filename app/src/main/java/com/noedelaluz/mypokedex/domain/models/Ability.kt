package com.noedelaluz.mypokedex.domain.models


import com.google.gson.annotations.SerializedName

data class Ability(
    @SerializedName("ability")
    val ability: AbilityDetail
)