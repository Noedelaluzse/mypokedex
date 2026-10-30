package com.noedelaluz.mypokedex.presentation.ui.fragments.datails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import coil.load
import com.google.android.material.chip.Chip
import com.google.android.material.snackbar.Snackbar
import com.noedelaluz.mypokedex.R
import com.noedelaluz.mypokedex.databinding.FragmentDetailBinding
import com.noedelaluz.mypokedex.domain.models.PokemonDetail
import com.noedelaluz.mypokedex.presentation.ui.adapters.AbilitiesAdapter
import com.noedelaluz.mypokedex.presentation.ui.adapters.SpriteAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    private val args: DetailFragmentArgs by navArgs()

    private val viewModel: DetailViewModel by viewModels()

    private val abilitiesAdapter = AbilitiesAdapter()
    private val spritesAdapter = SpriteAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.titlePokemonName.text = args.pokemonName
        binding.backButton.setOnClickListener { findNavController().popBackStack() }
        binding.likeButton.setOnClickListener { viewModel.toggleFavorite() }

        setUpRecyclerViews()
        collectViewModel()
    }

    private fun setUpRecyclerViews() {
        binding.recyclerAbilities.adapter = abilitiesAdapter
        binding.recyclerSprites.adapter = spritesAdapter
        binding.recyclerSprites.layoutManager = GridLayoutManager(requireContext(), 4)
    }

    private fun collectViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch { viewModel.uiState.collect(::render) }
                launch { viewModel.isFavorite.collect(::renderFavorite) }
                launch { viewModel.messages.collect { showSnackBar(getString(it)) } }
            }
        }
    }

    private fun render(state: DetailUiState) {
        when (state) {
            DetailUiState.Loading -> {
                binding.progressBar.visibility = View.VISIBLE
                showEmptyState(false)
            }
            is DetailUiState.Success -> {
                binding.progressBar.visibility = View.GONE
                binding.scrollView2.visibility = View.VISIBLE
                showEmptyState(false)
                bindPokemon(state.pokemon)
            }
            DetailUiState.Error -> {
                binding.progressBar.visibility = View.GONE
                binding.scrollView2.visibility = View.GONE
                showEmptyState(true)
            }
        }
    }

    private fun renderFavorite(isFavorite: Boolean) {
        binding.likeButton.setImageResource(
            if (isFavorite) R.drawable.ic_favorite else R.drawable.ic_dislike
        )
    }

    private fun showEmptyState(show: Boolean) {
        val visibility = if (show) View.VISIBLE else View.GONE
        binding.emptyDatabaseTextView.visibility = visibility
        binding.emptyDatabaseImageView.visibility = visibility
    }

    private fun bindPokemon(pokemon: PokemonDetail) {
        binding.imgPokemonProfile.load(pokemon.artworkUrl) {
            binding.progressPokemonProfile.visibility = View.GONE
            crossfade(true)
            crossfade(600)
            error(R.drawable.ic_error_placeholder)
        }

        binding.idPokemon.text = String.format("#%03d", pokemon.id)
        binding.titlePokemonName.text = pokemon.name.replaceFirstChar { it.uppercase() }
        binding.weightValue.text = String.format("%.1f kg", pokemon.weight / 10.0)
        binding.heightValue.text = String.format("%.1f mts", pokemon.height / 10.0)

        abilitiesAdapter.setData(pokemon.abilities)
        spritesAdapter.setData(pokemon.sprites)

        bindStats(pokemon)
        buildChipsTypes(pokemon.types)
    }

    private fun bindStats(pokemon: PokemonDetail) {
        bindStat(pokemon, "hp", binding.hpNumberValue, binding.hpProgress, R.color.type_ice)
        bindStat(pokemon, "attack", binding.atkNumberValue, binding.atkProgress, R.color.type_fire)
        bindStat(pokemon, "defense", binding.defNumberValue, binding.defProgress, R.color.type_grass)
        bindStat(pokemon, "special-attack", binding.satkNumberValue, binding.satkProgress, R.color.type_water)
        bindStat(pokemon, "special-defense", binding.sdefNumberValue, binding.sdefProgress, R.color.type_electric)
        bindStat(pokemon, "speed", binding.spdNumberValue, binding.spdProgress, R.color.type_flying)
    }

    private fun bindStat(
        pokemon: PokemonDetail,
        statName: String,
        valueView: TextView,
        progressView: ProgressBar,
        @ColorRes colorRes: Int,
    ) {
        val value = pokemon.statValue(statName)
        valueView.text = String.format("%03d", value)
        progressView.progress = value
        progressView.progressTintList = ContextCompat.getColorStateList(requireContext(), colorRes)
    }

    private fun buildChipsTypes(types: List<String>) {
        binding.chipGroupType.removeAllViews()

        types.forEachIndexed { index, typeName ->
            val colorResId = TYPE_COLORS[typeName] ?: R.color.type_normal

            // El primer tipo define el color de fondo del contenedor
            if (index == 0) {
                binding.containerPokemonProfile.setBackgroundColor(
                    ContextCompat.getColor(requireContext(), colorResId)
                )
            }

            val chip = layoutInflater.inflate(R.layout.item_chip_type, binding.chipGroupType, false) as Chip
            chip.text = typeName.replaceFirstChar { it.uppercase() }
            chip.setChipBackgroundColorResource(colorResId)
            chip.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            chip.setChipStrokeColorResource(colorResId)
            binding.chipGroupType.addView(chip)
        }
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT)
            .setAction(getString(R.string.okey)) {}
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.recyclerAbilities.adapter = null
        binding.recyclerSprites.adapter = null
        _binding = null
    }

    private companion object {
        val TYPE_COLORS = mapOf(
            "normal" to R.color.type_normal,
            "fire" to R.color.type_fire,
            "water" to R.color.type_water,
            "electric" to R.color.type_electric,
            "grass" to R.color.type_grass,
            "ice" to R.color.type_ice,
            "fighting" to R.color.type_fighting,
            "poison" to R.color.type_poison,
            "ground" to R.color.type_ground,
            "flying" to R.color.type_flying,
            "psychic" to R.color.type_psychic,
            "bug" to R.color.type_bug,
            "rock" to R.color.type_rock,
            "ghost" to R.color.type_ghost,
            "dragon" to R.color.type_dragon,
            "dark" to R.color.type_dark,
            "steel" to R.color.type_steel,
            "fairy" to R.color.type_fairy,
        )
    }
}
