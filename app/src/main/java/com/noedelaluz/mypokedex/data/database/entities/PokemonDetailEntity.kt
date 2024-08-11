package com.noedelaluz.mypokedex.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pokemon_detail_table")
data class PokemonDetailEntity(
    @PrimaryKey(autoGenerate = true) var id: Int,

)