package com.noedelaluz.mypokedex.presentation.ui.fragments.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.chip.Chip
import com.noedelaluz.mypokedex.R
import com.noedelaluz.mypokedex.databinding.FragmentHomeBinding
import com.noedelaluz.mypokedex.di.appContainer
import com.noedelaluz.mypokedex.domain.models.Pokemon
import com.noedelaluz.mypokedex.domain.models.PokemonTypes
import com.noedelaluz.mypokedex.presentation.ui.adapters.PokemonAdapter
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels { appContainer().homeViewModelFactory }

    private val pokemonAdapter = PokemonAdapter(onPokemonClick = ::openDetail)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupFilters()
        collectViewModel()
    }

    private fun setupRecyclerView() {
        binding.pokemonRecyclerview.adapter = pokemonAdapter
        binding.pokemonRecyclerview.layoutManager = GridLayoutManager(requireContext(), 2)
    }

    private fun setupFilters() {
        binding.searchInput.doOnTextChanged { text, _, _, _ ->
            viewModel.onSearchQueryChange(text?.toString().orEmpty())
        }

        val selectedType = viewModel.selectedType.value
        addFilterChip(type = null, label = getString(R.string.filter_all), checked = selectedType == null)
        PokemonTypes.ALL.forEach { type ->
            addFilterChip(type, type.replaceFirstChar { it.uppercase() }, checked = type == selectedType)
        }
        binding.typeChipGroup.setOnCheckedStateChangeListener { group, checkedIds ->
            val chip = checkedIds.firstOrNull()?.let { group.findViewById<Chip>(it) }
            viewModel.onTypeSelected(chip?.tag as? String)
        }
    }

    private fun addFilterChip(type: String?, label: String, checked: Boolean) {
        val chip = layoutInflater.inflate(R.layout.item_filter_chip, binding.typeChipGroup, false) as Chip
        chip.id = View.generateViewId()
        chip.text = label
        chip.tag = type
        // El estado lo restaura el ViewModel; los ids generados no coinciden tras recrear la vista
        chip.isSaveEnabled = false
        chip.isChecked = checked
        binding.typeChipGroup.addView(chip)
    }

    private fun collectViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch { viewModel.uiState.collect(::render) }
                launch {
                    viewModel.messages.collect { messageRes ->
                        Toast.makeText(requireContext(), messageRes, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun render(state: HomeUiState) {
        when (state) {
            HomeUiState.Loading -> {
                binding.noResultsTextView.visibility = View.GONE
                showEmptyState(false)
                showShimmerEffect()
            }
            is HomeUiState.Success -> {
                hideShimmerEffect()
                showEmptyState(false)
                binding.noResultsTextView.visibility =
                    if (state.pokemon.isEmpty()) View.VISIBLE else View.GONE
                pokemonAdapter.submitList(state.pokemon)
            }
            HomeUiState.Error -> {
                binding.noResultsTextView.visibility = View.GONE
                hideShimmerEffect()
                binding.pokemonRecyclerview.visibility = View.GONE
                showEmptyState(true)
            }
        }
    }

    private fun showEmptyState(show: Boolean) {
        val visibility = if (show) View.VISIBLE else View.GONE
        binding.emptyDatabaseImageView.visibility = visibility
        binding.emptyDatabaseTextView.visibility = visibility
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

    private fun openDetail(pokemon: Pokemon) {
        findNavController().navigate(
            HomeFragmentDirections.actionHomeFragmentToDetailFragment(pokemon.name)
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.pokemonRecyclerview.adapter = null
        _binding = null
    }
}
