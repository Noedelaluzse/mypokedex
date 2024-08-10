package com.noedelaluz.mypokedex.presentation.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.noedelaluz.mypokedex.R
import com.noedelaluz.mypokedex.domain.models.Ability

class AbilitiesAdapter(): RecyclerView.Adapter<AbilitiesAdapter.MyViewHolder>() {

    private var arrAbilities: List<Ability> = emptyList()

    class MyViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val abilityName = view.findViewById<TextView>(R.id.abilityName)
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AbilitiesAdapter.MyViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context).inflate(R.layout.ability_card, parent, false)
        return MyViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: AbilitiesAdapter.MyViewHolder, position: Int) {
        val item = arrAbilities[position]

        holder.abilityName.apply {
            text = item.ability.name.replaceFirstChar { it.uppercase() }
        }
    }

    override fun getItemCount(): Int = arrAbilities.size

    fun setData(newData: List<Ability>) {
        arrAbilities = newData
        notifyDataSetChanged()
    }

}