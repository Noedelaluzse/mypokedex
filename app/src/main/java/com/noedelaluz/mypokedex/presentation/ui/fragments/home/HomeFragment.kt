package com.noedelaluz.mypokedex.presentation.ui.fragments.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.noedelaluz.mypokedex.databinding.FragmentHomeBinding
import com.noedelaluz.mypokedex.domain.models.Pokemon
import com.noedelaluz.mypokedex.domain.models.PokemonResponse
import com.noedelaluz.mypokedex.infrastructure.utils.NetworkListener
import com.noedelaluz.mypokedex.infrastructure.utils.NetworkResult
import com.noedelaluz.mypokedex.infrastructure.utils.observeOnce
import com.noedelaluz.mypokedex.presentation.ui.adapters.PokemonAdapter
import com.noedelaluz.mypokedex.presentation.viewmodels.AppViewModel
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val TAG = "HomeFragment"

    private var dataRequest = false
    private lateinit var appviewModel: AppViewModel
    private val mAdapter by lazy { PokemonAdapter() }
    private lateinit var networkListener: NetworkListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        appviewModel = ViewModelProvider(requireActivity())[AppViewModel::class.java]

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        setupRecyclerView()

        // TODO: Implement network listener to check network availability and use local data if network is not available
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            networkListener = NetworkListener()
            networkListener.checkNetworkAvailability(requireContext()).collect {
                status ->
                Log.d("NetworkListener", status.toString())
                readDatabase()
            }
        }
        return binding.root
    }

    private fun readDatabase() {
        lifecycleScope.launch {
            appviewModel.readPokemon.observeOnce(viewLifecycleOwner) { database ->

                if (database.results.isNotEmpty()) {
                    hideShimmerEffect()
                    Log.d(TAG, "readDatabase called!")
                    val response = database.results

                    // Crear un List<Pokemon> a partir de la lista de PokemonResponse
                    val pokemonList = parseEntityToModelList(response)
                    mAdapter.setData(pokemonList)


                } else {
                    if (!dataRequest) {
                        Log.d(TAG, "readDatabase called! But database is empty!")
                        requestApiData()
                    }

                }
            }
        }
    }

    private fun requestApiData() {
        Log.d("HomeFragment","requesApiData called" )
        appviewModel.getPokemonList()
        appviewModel.pokemonResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    this.hideShimmerEffect()
                    response.data?.let { data ->
                        val response = data.results
                        // Crear un List<Pokemon> a partir de la lista de PokemonResponse
                        val pokemonList = parseEntityToModelList(response)
                        mAdapter.setData(pokemonList) }
                }
                is NetworkResult.Error -> {
                    this.hideShimmerEffect()
                    loadDataFromCache()
                    Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT).show()

                }
                is NetworkResult.Loading -> {
                    showShimmerEffect()
                    Toast.makeText(requireContext(), "Loading...", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun loadDataFromCache() {
        lifecycleScope.launch {
            appviewModel.readPokemon.observe(viewLifecycleOwner) { database ->
                if (database.results.isNotEmpty()) {
                    val responseList = database.results
                    val pokemonList = parseEntityToModelList(responseList)
                    mAdapter.setData(pokemonList)
                }
            }
        }
    }

    private fun parseEntityToModelList(responseList: List<PokemonResponse>): List<Pokemon> {
        // Crear un List<Pokemon> a partir de la lista de PokemonEntity
        val pokemonLists = responseList.mapIndexed { index, pokemonResponse ->
            val urlParts = pokemonResponse.url.split("/")
            val id = urlParts[urlParts.size - 2].toInt()
            val picture = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/${id}.png"
            Pokemon(
                name = pokemonResponse.name,
                picture = picture,
                id = id
            )
        }

        return pokemonLists
    }

    private fun setupRecyclerView() {
        binding.pokemonRecyclerview.adapter = mAdapter
        binding.pokemonRecyclerview.layoutManager = GridLayoutManager(requireContext(), 2)
    }

    private fun showShimmerEffect() {
        binding.shimmer.visibility = View.VISIBLE
        binding.shimmer.startShimmer()
        binding.pokemonRecyclerview.visibility = View.GONE
    }

    private fun hideShimmerEffect() {
        binding.shimmer.stopShimmer()
        binding.shimmer.visibility = View.GONE
        binding.pokemonRecyclerview.visibility = View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}