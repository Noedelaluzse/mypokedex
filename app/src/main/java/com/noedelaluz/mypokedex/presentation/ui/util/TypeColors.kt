package com.noedelaluz.mypokedex.presentation.ui.util

import androidx.annotation.ColorRes
import com.noedelaluz.mypokedex.R

@ColorRes
fun typeColorRes(type: String?): Int = when (type) {
    "fire" -> R.color.type_fire
    "water" -> R.color.type_water
    "electric" -> R.color.type_electric
    "grass" -> R.color.type_grass
    "ice" -> R.color.type_ice
    "fighting" -> R.color.type_fighting
    "poison" -> R.color.type_poison
    "ground" -> R.color.type_ground
    "flying" -> R.color.type_flying
    "psychic" -> R.color.type_psychic
    "bug" -> R.color.type_bug
    "rock" -> R.color.type_rock
    "ghost" -> R.color.type_ghost
    "dragon" -> R.color.type_dragon
    "dark" -> R.color.type_dark
    "steel" -> R.color.type_steel
    "fairy" -> R.color.type_fairy
    else -> R.color.type_normal
}
