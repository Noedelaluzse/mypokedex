package com.noedelaluz.mypokedex.di

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.room.Room
import com.noedelaluz.mypokedex.MyApplication
import com.noedelaluz.mypokedex.config.Constants.DATABASE_NAME
import com.noedelaluz.mypokedex.config.NetworkModule
import com.noedelaluz.mypokedex.data.connectivity.NetworkConnectivityObserver
import com.noedelaluz.mypokedex.data.database.MIGRATION_1_2
import com.noedelaluz.mypokedex.data.database.PokemonDatabase
import com.noedelaluz.mypokedex.data.datasources.PokemonLocalDataSourceImpl
import com.noedelaluz.mypokedex.data.datasources.PokemonRemoteDataSourceImpl
import com.noedelaluz.mypokedex.data.repositories.PokemonRepositoryImpl
import com.noedelaluz.mypokedex.domain.connectivity.ConnectivityObserver
import com.noedelaluz.mypokedex.domain.repositories.PokemonRepository
import com.noedelaluz.mypokedex.domain.usecase.pokemon.GetFavoritePokemon
import com.noedelaluz.mypokedex.domain.usecase.pokemon.GetPokemonDetail
import com.noedelaluz.mypokedex.domain.usecase.pokemon.GetPokemonList
import com.noedelaluz.mypokedex.domain.usecase.pokemon.ObserveIsFavorite
import com.noedelaluz.mypokedex.domain.usecase.pokemon.RefreshPokemonList
import com.noedelaluz.mypokedex.domain.usecase.pokemon.SetFavoritePokemon
import com.noedelaluz.mypokedex.presentation.ui.fragments.datails.DetailViewModel
import com.noedelaluz.mypokedex.presentation.ui.fragments.favorite.FavoriteViewModel
import com.noedelaluz.mypokedex.presentation.ui.fragments.home.HomeViewModel

/**
 * Raíz de composición de la app: único lugar donde se crean las dependencias.
 * Todo lo demás las recibe por constructor y solo conoce interfaces.
 */
class AppContainer(context: Context) {

    private val appContext = context.applicationContext

    // Data
    private val database: PokemonDatabase by lazy {
        Room.databaseBuilder(appContext, PokemonDatabase::class.java, DATABASE_NAME)
            .addMigrations(MIGRATION_1_2)
            .build()
    }

    private val repository: PokemonRepository by lazy {
        PokemonRepositoryImpl(
            remote = PokemonRemoteDataSourceImpl(NetworkModule.providePokeApi()),
            local = PokemonLocalDataSourceImpl(database),
        )
    }

    private val connectivityObserver: ConnectivityObserver by lazy {
        NetworkConnectivityObserver(appContext)
    }

    // Use cases
    private val getPokemonList get() = GetPokemonList(repository)
    private val getFavoritePokemon get() = GetFavoritePokemon(repository)
    private val observeIsFavorite get() = ObserveIsFavorite(repository)
    private val refreshPokemonList get() = RefreshPokemonList(repository)
    private val getPokemonDetail get() = GetPokemonDetail(repository)
    private val setFavoritePokemon get() = SetFavoritePokemon(repository)

    // ViewModel factories
    val homeViewModelFactory = viewModelFactory {
        initializer { HomeViewModel(getPokemonList, refreshPokemonList, connectivityObserver) }
    }

    val favoriteViewModelFactory = viewModelFactory {
        initializer { FavoriteViewModel(getFavoritePokemon) }
    }

    fun detailViewModelFactory(pokemonName: String) = viewModelFactory {
        initializer {
            DetailViewModel(
                pokemonName = pokemonName,
                getPokemonDetail = getPokemonDetail,
                observeIsFavorite = observeIsFavorite,
                setFavoritePokemon = setFavoritePokemon,
                connectivityObserver = connectivityObserver,
            )
        }
    }
}

fun Fragment.appContainer(): AppContainer =
    (requireActivity().application as MyApplication).container
