package com.noedelaluz.mypokedex.presentation.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.noedelaluz.mypokedex.R
import com.noedelaluz.mypokedex.domain.models.Pokemon

class PokemonAdapter(
    private val onPokemonClick: (Pokemon) -> Unit,
) : ListAdapter<Pokemon, PokemonAdapter.MyViewHolder>(PokemonDiffCallback) {

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.pokemon_name)
        val number: TextView = view.findViewById(R.id.id_pokemon)
        val image: ImageView = view.findViewById(R.id.pokemon_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.pokemon_row_layout, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = getItem(position)
        holder.name.text = item.name.replaceFirstChar { it.uppercase() }
        holder.number.text = "#${item.id}"
        holder.image.load(item.picture) {
            crossfade(true)
            crossfade(600)
            error(R.drawable.ic_error_placeholder)
        }
        holder.itemView.setOnClickListener { onPokemonClick(item) }
    }

    private object PokemonDiffCallback : DiffUtil.ItemCallback<Pokemon>() {
        override fun areItemsTheSame(oldItem: Pokemon, newItem: Pokemon) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Pokemon, newItem: Pokemon) = oldItem == newItem
    }
}
