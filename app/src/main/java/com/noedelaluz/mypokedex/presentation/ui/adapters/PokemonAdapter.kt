package com.noedelaluz.mypokedex.presentation.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.noedelaluz.mypokedex.R
import com.noedelaluz.mypokedex.domain.models.Pokemon
import com.noedelaluz.mypokedex.infrastructure.utils.PokemonDiffUtil
import com.noedelaluz.mypokedex.presentation.ui.fragments.favorite.FavoriteFragmentDirections
import com.noedelaluz.mypokedex.presentation.ui.fragments.home.HomeFragmentDirections

class PokemonAdapter(): RecyclerView.Adapter<PokemonAdapter.MyViewHolder>() {

    private var dataset: List<Pokemon> = emptyList()
    private var tag: String = ""

    class MyViewHolder(val view: View): RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.pokemon_name)
        val number: TextView = view.findViewById(R.id.id_pokemon)
        val image = view.findViewById<ImageView>(R.id.pokemon_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context).inflate(R.layout.pokemon_row_layout, parent, false)
        return MyViewHolder(adapterLayout)
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = dataset[position]
        holder.name.text = item.name.capitalize()
        holder.number.text = "#${item.id}"
        holder.image.load(item.picture) {
            crossfade(true)
            crossfade(600)
            error(R.drawable.ic_error_placeholder)
        }

        var action: Any

        if (tag == "HomeFragment") {
            action = HomeFragmentDirections.actionHomeFragmentToDetailFragment(item.name)
        } else {
            action = FavoriteFragmentDirections.actionFavoriteFragmentToDetailFragment(item.name)
        }

        holder.itemView.setOnClickListener {
            holder.itemView.findNavController().navigate(action)
        }
    }

    fun setData(newData: List<Pokemon>, tag: String = "HomeFragment") {
        val pokemonDiffUtil = PokemonDiffUtil(dataset, newData)
        val diffUtilResult = DiffUtil.calculateDiff(pokemonDiffUtil)
        dataset = newData
        this.tag = tag
        diffUtilResult.dispatchUpdatesTo(this)
        //notifyDataSetChanged()
    }
}