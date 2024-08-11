package com.noedelaluz.mypokedex.presentation.viewmodels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.noedelaluz.mypokedex.data.database.entities.PokemonEntity
import com.noedelaluz.mypokedex.domain.models.PokemonDetail
import com.noedelaluz.mypokedex.domain.models.PokemonListResponse
import com.noedelaluz.mypokedex.domain.models.PokemonResponse
import com.noedelaluz.mypokedex.domain.usecase.pokemon.GetAllPokemon
import com.noedelaluz.mypokedex.domain.usecase.pokemon.GetPokemonDetails
import com.noedelaluz.mypokedex.domain.usecase.pokemon.GetPokemonFromCache
import com.noedelaluz.mypokedex.domain.usecase.pokemon.IsFavoritePokemon
import com.noedelaluz.mypokedex.domain.usecase.pokemon.SaveFavoritePokemon
import com.noedelaluz.mypokedex.domain.usecase.pokemon.SavePokemonDB
import com.noedelaluz.mypokedex.infrastructure.datasources.PokemonDatasourceImpl
import com.noedelaluz.mypokedex.infrastructure.datasources.PokemonLocalDatasource
import com.noedelaluz.mypokedex.infrastructure.repositories.PokemonRepositoryImpl
import com.noedelaluz.mypokedex.infrastructure.utils.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AppViewModel(application: Application) : AndroidViewModel(application) {

    private val remoteDatasource = PokemonDatasourceImpl()
    private val localDatasource = PokemonLocalDatasource(application)

    private val repository = PokemonRepositoryImpl(remoteDatasource, localDatasource)

    // Use Cases Remote
    private val getPokemonListUseCase = GetAllPokemon(repository)
    private val getPokemonDetailsUseCase = GetPokemonDetails(repository)

    // Use Cases Local
    private val insertPokemonUseCase = SavePokemonDB(repository)
    private val getPokemonFromCache = GetPokemonFromCache(repository)
    private val isFavoritePokemon = IsFavoritePokemon(repository)
    private val updateFavoritePokemon = SaveFavoritePokemon(repository)

    /*** ROOM DATABASE **/
    val readPokemon: LiveData<PokemonListResponse> = getPokemonFromCache.execute().asLiveData()
    var favoritePokemon: MutableLiveData<PokemonEntity?> = MutableLiveData()
    /** RETROFIT **/
    var pokemonResponse: MutableLiveData<NetworkResult<PokemonListResponse>> = MutableLiveData()
    var pokemonDetailReponse: MutableLiveData<NetworkResult<PokemonDetail>> = MutableLiveData()


    fun getPokemonList() = viewModelScope.launch {
        getPokemonListSafeCall()
    }

    fun getPokemonDetails(id: String) = viewModelScope.launch {
        getPokemonDetailsSafeCall(id)
    }

    fun isFavoritePokemon(name: String) = viewModelScope.launch(Dispatchers.IO) {
        favoritePokemon.postValue(isFavoritePokemon.execute(name))
    }


    fun saveFavoritePokemon(name: String, isFavorite: Int) = viewModelScope.launch {
        updatePokemon(name, isFavorite)
    }

    private fun updatePokemon(name: String, isFavorite: Int) = viewModelScope.launch(Dispatchers.IO) {
        updateFavoritePokemon.execute(name, isFavorite)
    }

    private suspend fun getPokemonDetailsSafeCall(id: String) {
        pokemonDetailReponse.value = NetworkResult.Loading()

        if (hasInternetConnection()) {
            try {
                val response = getPokemonDetailsUseCase.execute(id)
                pokemonDetailReponse.value = NetworkResult.Success(response)
            } catch(e: Exception) {
                pokemonDetailReponse.value = NetworkResult.Error("Pokemon not found.")
            }
        }
    }

    private suspend fun getPokemonListSafeCall() {
        pokemonResponse.value = NetworkResult.Loading()

        if (hasInternetConnection()) {
            try {
                val response = getPokemonListUseCase.execute()
                pokemonResponse.value = NetworkResult.Success(response)

                //TODO: Save to Room Database
                val pokemonList = pokemonResponse.value!!.data
                if (pokemonList != null) {
                    insertPokemonToDatabase(pokemonList.results)
                }
            } catch (e: Exception) {
                pokemonResponse.value = NetworkResult.Error("Pokemon not found.")
            }
        }
    }

    private fun insertPokemonToDatabase(results: List<PokemonResponse>) {
        viewModelScope.launch(Dispatchers.IO) {
            insertPokemonUseCase.execute(results)
        }
    }

    private fun hasInternetConnection(): Boolean {

        val connectivityManager = getApplication<Application>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager

        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false

        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }

    }

}