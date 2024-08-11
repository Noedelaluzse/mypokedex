package com.noedelaluz.mypokedex.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.noedelaluz.mypokedex.infrastructure.utils.Constants.Companion.POKEMON_TABLE


@Entity(tableName = POKEMON_TABLE)
data class PokemonEntity (
    @PrimaryKey (autoGenerate = true) var id: Int, // autoGenerate = true means that the id will be generated automatically
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "url") var url: String,
    @ColumnInfo(name = "isFavorite") var isFavorite: Int = 0 // 0 means that the pokemon is not favorite
)