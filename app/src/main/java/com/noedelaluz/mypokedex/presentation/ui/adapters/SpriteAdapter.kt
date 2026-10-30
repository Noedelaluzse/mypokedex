package com.noedelaluz.mypokedex.presentation.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.noedelaluz.mypokedex.R

class SpriteAdapter : RecyclerView.Adapter<SpriteAdapter.MyViewHolder>() {

    private var sprites: List<String> = emptyList()

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val sprite: ImageView = view.findViewById(R.id.sprite_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.sprite_card, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.sprite.load(sprites[position]) {
            crossfade(true)
            crossfade(600)
            error(R.drawable.ic_error_placeholder)
        }
    }

    override fun getItemCount(): Int = sprites.size

    fun setData(newData: List<String>) {
        sprites = newData
        notifyDataSetChanged()
    }
}
