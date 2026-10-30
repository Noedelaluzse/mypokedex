package com.noedelaluz.mypokedex.di

import android.content.Context
import androidx.room.Room
import com.noedelaluz.mypokedex.config.Constants.DATABASE_NAME
import com.noedelaluz.mypokedex.config.NetworkModule
import com.noedelaluz.mypokedex.data.database.MIGRATION_1_2
import com.noedelaluz.mypokedex.data.database.PokemonDatabase
import com.noedelaluz.mypokedex.data.network.PokeApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/** Dependencias que Hilt no puede construir solo porque vienen de librerías externas. */
@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun providePokeApi(): PokeApi = NetworkModule.providePokeApi()

    @Provides
    @Singleton
    fun providePokemonDatabase(@ApplicationContext context: Context): PokemonDatabase =
        Room.databaseBuilder(context, PokemonDatabase::class.java, DATABASE_NAME)
            .addMigrations(MIGRATION_1_2)
            .build()
}
