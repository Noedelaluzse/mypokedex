package com.noedelaluz.mypokedex.presentation.ui.fragments.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.noedelaluz.mypokedex.databinding.FragmentFavoriteBinding
import com.noedelaluz.mypokedex.domain.models.Pokemon
import com.noedelaluz.mypokedex.domain.models.PokemonResponse
import com.noedelaluz.mypokedex.presentation.ui.adapters.PokemonAdapter
import com.noedelaluz.mypokedex.presentation.viewmodels.AppViewModel
import kotlinx.coroutines.launch


class FavoriteFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!
    private val TAG = "FavoriteFragment"
    private val favoriteAdapter by lazy { PokemonAdapter() }
    private lateinit var appViewModel: AppViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        appViewModel = ViewModelProvider(requireActivity())[AppViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)

        setupRecyclerView()

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            readDatabase()
        }

        return binding.root
    }

    private fun readDatabase() {
        lifecycleScope.launch {
            appViewModel.readFavoritePokemon.observe(viewLifecycleOwner) { database ->
                if (database.results.isNotEmpty()) {
                    binding.emptyDatabaseImageView.visibility = View.GONE
                    binding.emptyDatabaseTextView.visibility = View.GONE
                    binding.favoriteRecipesRecyclerView.visibility = View.VISIBLE
                    val response = database.results
                    val pokemonList = parseEntityToModelList(response)
                    favoriteAdapter.setData(pokemonList, TAG)
                } else {
                    binding.favoriteRecipesRecyclerView.visibility = View.INVISIBLE
                    binding.emptyDatabaseImageView.visibility = View.VISIBLE
                    binding.emptyDatabaseTextView.visibility = View.VISIBLE
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
        binding.favoriteRecipesRecyclerView.adapter = favoriteAdapter
        binding.favoriteRecipesRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}