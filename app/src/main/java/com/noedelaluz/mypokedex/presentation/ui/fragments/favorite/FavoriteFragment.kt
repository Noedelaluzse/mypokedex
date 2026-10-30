package com.noedelaluz.mypokedex.presentation.ui.fragments.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.noedelaluz.mypokedex.R
import com.noedelaluz.mypokedex.databinding.FragmentFavoriteBinding
import com.noedelaluz.mypokedex.di.appContainer
import com.noedelaluz.mypokedex.domain.models.Pokemon
import com.noedelaluz.mypokedex.presentation.ui.adapters.PokemonAdapter
import kotlinx.coroutines.launch

class FavoriteFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FavoriteViewModel by viewModels { appContainer().favoriteViewModelFactory }

    private val favoriteAdapter = PokemonAdapter(
        layoutRes = R.layout.favorite_row_layout,
        textOverTypeColor = false,
        onPokemonClick = ::openDetail,
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.favoriteRecipesRecyclerView.adapter = favoriteAdapter
        binding.favoriteRecipesRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect(::render)
            }
        }
    }

    private fun render(state: FavoriteUiState) {
        when (state) {
            FavoriteUiState.Loading -> {
                binding.favoriteRecipesRecyclerView.visibility = View.INVISIBLE
                showEmptyState(false)
            }
            FavoriteUiState.Empty -> {
                favoriteAdapter.submitList(emptyList())
                binding.favoriteRecipesRecyclerView.visibility = View.INVISIBLE
                showEmptyState(true)
            }
            is FavoriteUiState.Success -> {
                binding.favoriteRecipesRecyclerView.visibility = View.VISIBLE
                showEmptyState(false)
                favoriteAdapter.submitList(state.pokemon)
            }
        }
    }

    private fun showEmptyState(show: Boolean) {
        val visibility = if (show) View.VISIBLE else View.GONE
        binding.emptyDatabaseImageView.visibility = visibility
        binding.emptyDatabaseTextView.visibility = visibility
    }

    private fun openDetail(pokemon: Pokemon) {
        findNavController().navigate(
            FavoriteFragmentDirections.actionFavoriteFragmentToDetailFragment(pokemon.name)
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.favoriteRecipesRecyclerView.adapter = null
        _binding = null
    }
}
