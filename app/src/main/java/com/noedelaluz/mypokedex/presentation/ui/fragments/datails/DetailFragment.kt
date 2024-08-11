package com.noedelaluz.mypokedex.presentation.ui.fragments.datails

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import coil.load
import com.google.android.material.chip.Chip
import com.noedelaluz.mypokedex.R
import com.noedelaluz.mypokedex.databinding.FragmentDetailBinding
import com.noedelaluz.mypokedex.domain.models.PokemonDetail
import com.noedelaluz.mypokedex.infrastructure.utils.NetworkListener
import com.noedelaluz.mypokedex.infrastructure.utils.NetworkResult
import com.noedelaluz.mypokedex.infrastructure.utils.observeOnce
import com.noedelaluz.mypokedex.presentation.ui.adapters.AbilitiesAdapter
import com.noedelaluz.mypokedex.presentation.ui.adapters.SpriteAdapter
import com.noedelaluz.mypokedex.presentation.viewmodels.AppViewModel
import kotlinx.coroutines.launch


class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!
    private val TAG = "DetailFragment"
    private lateinit var appviewModel: AppViewModel

    private val abilitiesAdapter by lazy { AbilitiesAdapter() }
    private val spritesAdapter by lazy { SpriteAdapter() }

    private lateinit var networkListener: NetworkListener

    private val args: DetailFragmentArgs by navArgs()

    private var pokemonSaved = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appviewModel = ViewModelProvider(requireActivity())[AppViewModel::class.java]
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)

        checkSavedPokemon()

        binding.likeButton.setOnClickListener {
            if (pokemonSaved) {
                appviewModel.saveFavoritePokemon(args.pokemonName, 0)
                binding.likeButton.setImageResource(R.drawable.ic_dislike)
                pokemonSaved = false
            } else {
                appviewModel.saveFavoritePokemon(args.pokemonName, 1)
                binding.likeButton.setImageResource(R.drawable.ic_favorite)
                pokemonSaved = true
            }
        }

        setUpRecyclerView()

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            networkListener = NetworkListener()
            networkListener.checkNetworkAvailability(requireContext()).collect {
                status ->
                Log.d("NetworkListener", status.toString())
                appviewModel.isFavoritePokemon(args.pokemonName)
                requestApiData()
            }
        }
        return binding.root
    }

    private fun checkSavedPokemon() {
        appviewModel.favoritePokemon.observe(viewLifecycleOwner) { favoritePokemon ->
            if (favoritePokemon == null) {
                binding.likeButton.setImageResource(R.drawable.ic_dislike)
                pokemonSaved = false
            } else {
                binding.likeButton.setImageResource(R.drawable.ic_favorite)
                pokemonSaved = true
            }
        }
    }

    private fun requestApiData() {
        args.let { param ->
            Log.d(TAG, "Pokemon Name: ${param.pokemonName}")
            appviewModel.getPokemonDetails(param.pokemonName)
        }

        appviewModel.pokemonDetailReponse.observe(viewLifecycleOwner) { response ->
            when(response) {
                is NetworkResult.Success -> {
                    response.data?.let { data ->
                        initUI(data)
                    }
                }

                is NetworkResult.Error -> {
                    Log.d(TAG, "Error: ${response.message}")
                    binding.progressBar.visibility = View.GONE
                }

                is NetworkResult.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun initUI(data: PokemonDetail) {
        binding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.imgPokemonProfile.load("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/${data.id}.png") {
            binding.progressPokemonProfile.visibility = View.GONE
            crossfade(true)
            crossfade(600)
            error(R.drawable.ic_error_placeholder)
        }

        binding.idPokemon.text = "#${String.format("%03d", data.id)}"
        binding.titlePokemonName.text = data.name.replaceFirstChar { it.uppercase() }
        binding.weightValue.text = "${data.weight}kg"
        binding.heightValue.text = "${data.height}mts"

        abilitiesAdapter.setData(data.abilities)
        spritesAdapter.setData(data.sprites)
        binding.progressBar.visibility = View.GONE

        // Set STATS
        binding.hpNumberValue.text  = getStat(data.stats[0].baseStat)
        binding.atkNumberValue.text = getStat(data.stats[1].baseStat)
        binding.defNumberValue.text = getStat(data.stats[2].baseStat)
        binding.satkNumberValue.text = getStat(data.stats[3].baseStat)
        binding.sdefNumberValue.text = getStat(data.stats[4].baseStat)
        binding.spdNumberValue.text = getStat(data.stats[5].baseStat)

        // Load progress bar
        binding.hpProgress.progress = data.stats[0].baseStat
        binding.hpProgress.progressTintList = ContextCompat.getColorStateList(requireContext(), R.color.type_ice)

        binding.atkProgress.progress = data.stats[1].baseStat
        binding.atkProgress.progressTintList = ContextCompat.getColorStateList(requireContext(), R.color.type_fire)

        binding.defProgress.progress = data.stats[2].baseStat
        binding.defProgress.progressTintList = ContextCompat.getColorStateList(requireContext(), R.color.type_grass)

        binding.satkProgress.progress = data.stats[3].baseStat
        binding.sdefProgress.progressTintList = ContextCompat.getColorStateList(requireContext(), R.color.type_water)

        binding.sdefProgress.progress = data.stats[4].baseStat
        binding.sdefProgress.progressTintList = ContextCompat.getColorStateList(requireContext(), R.color.type_electric)

        binding.spdProgress.progress = data.stats[5].baseStat
        binding.spdProgress.progressTintList = ContextCompat.getColorStateList(requireContext(), R.color.type_flying)



        buildChipsTypes(data)
    }

    private val getStat: (Int) -> String = { stat ->
        if (stat.toString().length < 3) "0$stat" else stat.toString()
    }

    private fun buildChipsTypes(data: PokemonDetail) {

        val typeColorMap = mapOf(
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
            "fairy" to R.color.type_fairy
        )

        // Set TYPES
        binding.chipGroupType.removeAllViews()

        var wasBacroundChanged = false

        data.types.forEach { type ->
            val typeName = type.type.name
            val colorResId = typeColorMap[typeName] ?: R.color.type_normal

            if (!wasBacroundChanged) {
                // Cambiar el color de fondo del background contenedor
                binding.containerPokemonProfile.setBackgroundColor(ContextCompat.getColor(requireContext(), colorResId))
                wasBacroundChanged = true
            }

            val chip = layoutInflater.inflate(R.layout.item_chip_type, binding.chipGroupType, false) as Chip
            chip.text = typeName.replaceFirstChar { it.uppercase() }
            // Cambiar el color de fondo del Chip
            chip.setChipBackgroundColorResource(colorResId)

            // Cambiar el color del texto del Chip
            val textColor = ContextCompat.getColor(requireContext(), R.color.white)
            chip.setTextColor(textColor)

            // Cambiar el color del borde del Chip
            chip.setChipStrokeColorResource(colorResId)
            binding.chipGroupType.addView(chip)
        }
    }

    private fun setUpRecyclerView() {

        // Setting recycler view for abilities
        binding.recyclerAbilities.adapter = abilitiesAdapter
        binding.recyclerSprites.layoutManager = GridLayoutManager(requireContext(), 2)

        // setting recycler view for sprites
        binding.recyclerSprites.adapter = spritesAdapter
        binding.recyclerSprites.layoutManager = GridLayoutManager(requireContext(), 4)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        args.let { param ->
            binding.titlePokemonName.text = param.pokemonName
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}