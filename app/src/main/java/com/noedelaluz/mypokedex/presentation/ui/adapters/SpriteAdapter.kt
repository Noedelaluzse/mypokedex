package com.noedelaluz.mypokedex.presentation.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.noedelaluz.mypokedex.R
import com.noedelaluz.mypokedex.domain.models.Sprites

class SpriteAdapter(): RecyclerView.Adapter<SpriteAdapter.MyViewHolder>() {

    private var dataset: List<String> = emptyList()

    class MyViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val sprite = view.findViewById<ImageView>(R.id.sprite_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpriteAdapter.MyViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context).inflate(R.layout.sprite_card, parent, false)
        return MyViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: SpriteAdapter.MyViewHolder, position: Int) {
        val item = dataset[position]

        holder.sprite.load(item) {
            crossfade(true)
            crossfade(600)
            error(R.drawable.ic_error_placeholder)
        }
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    fun setData(newData: Sprites) {
        val list = mutableListOf<String>()

        list.add(newData.backDefault)
        list.add(newData.backShiny)
        list.add(newData.frontDefault)
        list.add(newData.frontShiny)

        dataset = list

        notifyDataSetChanged()
    }

}