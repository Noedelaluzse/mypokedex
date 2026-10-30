package com.noedelaluz.mypokedex.di

import com.noedelaluz.mypokedex.data.connectivity.NetworkConnectivityObserver
import com.noedelaluz.mypokedex.data.datasources.PokemonLocalDataSource
import com.noedelaluz.mypokedex.data.datasources.PokemonLocalDataSourceImpl
import com.noedelaluz.mypokedex.data.datasources.PokemonRemoteDataSource
import com.noedelaluz.mypokedex.data.datasources.PokemonRemoteDataSourceImpl
import com.noedelaluz.mypokedex.data.repositories.PokemonRepositoryImpl
import com.noedelaluz.mypokedex.domain.connectivity.ConnectivityObserver
import com.noedelaluz.mypokedex.domain.repositories.PokemonRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/** Conecta cada interfaz con su implementación. */
@Module
@InstallIn(SingletonComponent::class)
abstract class BindingsModule {

    @Binds
    @Singleton
    abstract fun bindPokemonRepository(impl: PokemonRepositoryImpl): PokemonRepository

    @Binds
    abstract fun bindRemoteDataSource(impl: PokemonRemoteDataSourceImpl): PokemonRemoteDataSource

    @Binds
    abstract fun bindLocalDataSource(impl: PokemonLocalDataSourceImpl): PokemonLocalDataSource

    @Binds
    abstract fun bindConnectivityObserver(impl: NetworkConnectivityObserver): ConnectivityObserver
}
