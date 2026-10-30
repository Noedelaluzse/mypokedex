package com.noedelaluz.mypokedex.presentation.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.google.android.material.card.MaterialCardView
import com.noedelaluz.mypokedex.R
import com.noedelaluz.mypokedex.domain.models.Pokemon
import com.noedelaluz.mypokedex.presentation.ui.util.typeColorRes

class PokemonAdapter(
    @LayoutRes private val layoutRes: Int = R.layout.pokemon_row_layout,
    // true cuando el nombre y el id se dibujan encima del color del tipo
    private val textOverTypeColor: Boolean = true,
    private val onPokemonClick: (Pokemon) -> Unit,
) : ListAdapter<Pokemon, PokemonAdapter.MyViewHolder>(PokemonDiffCallback) {

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val card: MaterialCardView = view.findViewById(R.id.materialCardView)
        val name: TextView = view.findViewById(R.id.pokemon_name)
        val number: TextView = view.findViewById(R.id.id_pokemon)
        val image: ImageView = view.findViewById(R.id.pokemon_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(layoutRes, parent, false)
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
        bindTypeColor(holder, item)
        holder.itemView.setOnClickListener { onPokemonClick(item) }
    }

    private fun bindTypeColor(holder: MyViewHolder, item: Pokemon) {
        val context = holder.itemView.context
        val backgroundColor = ContextCompat.getColor(context, typeColorRes(item.types.firstOrNull()))
        holder.card.setCardBackgroundColor(backgroundColor)

        if (textOverTypeColor) {
            // Texto oscuro sobre colores claros (eléctrico, hielo...) para mantener el contraste
            val isLight = ColorUtils.calculateLuminance(backgroundColor) > LIGHT_BACKGROUND_THRESHOLD
            val textColor = ContextCompat.getColor(context, if (isLight) R.color.darker else R.color.white)
            holder.name.setTextColor(textColor)
            holder.number.setTextColor(textColor)
        }
    }

    private object PokemonDiffCallback : DiffUtil.ItemCallback<Pokemon>() {
        override fun areItemsTheSame(oldItem: Pokemon, newItem: Pokemon) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Pokemon, newItem: Pokemon) = oldItem == newItem
    }

    private companion object {
        const val LIGHT_BACKGROUND_THRESHOLD = 0.5
    }
}
