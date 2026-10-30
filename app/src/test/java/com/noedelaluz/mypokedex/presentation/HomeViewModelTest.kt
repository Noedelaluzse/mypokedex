package com.noedelaluz.mypokedex.presentation

import com.noedelaluz.mypokedex.R
import com.noedelaluz.mypokedex.domain.models.Pokemon
import com.noedelaluz.mypokedex.domain.usecase.pokemon.GetPokemonList
import com.noedelaluz.mypokedex.domain.usecase.pokemon.RefreshPokemonList
import com.noedelaluz.mypokedex.presentation.ui.fragments.home.HomeUiState
import com.noedelaluz.mypokedex.presentation.ui.fragments.home.HomeViewModel
import com.noedelaluz.mypokedex.testutil.FakeConnectivityObserver
import com.noedelaluz.mypokedex.testutil.FakePokemonRepository
import com.noedelaluz.mypokedex.testutil.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val repository = FakePokemonRepository()
    private val connectivity = FakeConnectivityObserver()
    private val pikachu = Pokemon(name = "pikachu", picture = "url", id = 25)

    private val bulbasaur = Pokemon(
        name = "bulbasaur", picture = "url", id = 1, types = listOf("grass", "poison"),
    )
    private val charmander = Pokemon(
        name = "charmander", picture = "url", id = 4, types = listOf("fire"),
    )

    private fun createViewModel() = HomeViewModel(
        getPokemonList = GetPokemonList(repository),
        refreshPokemonList = RefreshPokemonList(repository),
        connectivityObserver = connectivity,
    )

    @Test
    fun `muestra Success cuando hay pokemon guardados`() = runTest {
        repository.pokemon.value = listOf(pikachu)
        val viewModel = createViewModel()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { viewModel.uiState.collect {} }

        assertEquals(HomeUiState.Success(listOf(pikachu)), viewModel.uiState.value)
    }

    @Test
    fun `muestra Error cuando no hay datos y la descarga falla`() = runTest {
        repository.refreshResult = Result.failure(Exception("sin red"))
        val viewModel = createViewModel()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { viewModel.uiState.collect {} }

        assertEquals(HomeUiState.Error, viewModel.uiState.value)
    }

    @Test
    fun `al volver la conexion avisa y vuelve a descargar`() = runTest {
        val viewModel = createViewModel()
        val messages = mutableListOf<Int>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { viewModel.messages.toList(messages) }

        connectivity.emit(false)
        connectivity.emit(true)

        assertEquals(listOf(R.string.no_internet_connection, R.string.back_online), messages)
        assertEquals(2, repository.refreshCalls)
    }

    @Test
    fun `filtra la lista por nombre`() = runTest {
        repository.pokemon.value = listOf(pikachu, bulbasaur, charmander)
        val viewModel = createViewModel()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { viewModel.uiState.collect {} }

        viewModel.onSearchQueryChange("  CHAR ")

        assertEquals(HomeUiState.Success(listOf(charmander)), viewModel.uiState.value)
    }

    @Test
    fun `filtra la lista por tipo`() = runTest {
        repository.pokemon.value = listOf(pikachu, bulbasaur, charmander)
        val viewModel = createViewModel()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { viewModel.uiState.collect {} }

        viewModel.onTypeSelected("poison")

        assertEquals(HomeUiState.Success(listOf(bulbasaur)), viewModel.uiState.value)
    }

    @Test
    fun `combina busqueda y tipo, y sin coincidencias sigue siendo Success vacio`() = runTest {
        repository.pokemon.value = listOf(pikachu, bulbasaur, charmander)
        val viewModel = createViewModel()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { viewModel.uiState.collect {} }

        viewModel.onTypeSelected("fire")
        viewModel.onSearchQueryChange("bulba")

        assertEquals(HomeUiState.Success(emptyList()), viewModel.uiState.value)
    }
}
