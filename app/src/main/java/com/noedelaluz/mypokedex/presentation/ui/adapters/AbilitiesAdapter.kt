package com.noedelaluz.mypokedex.presentation.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.noedelaluz.mypokedex.R

class AbilitiesAdapter : RecyclerView.Adapter<AbilitiesAdapter.MyViewHolder>() {

    private var abilities: List<String> = emptyList()

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val abilityName: TextView = view.findViewById(R.id.abilityName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.ability_card, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.abilityName.text = abilities[position].replaceFirstChar { it.uppercase() }
    }

    override fun getItemCount(): Int = abilities.size

    fun setData(newData: List<String>) {
        abilities = newData
        notifyDataSetChanged()
    }
}
