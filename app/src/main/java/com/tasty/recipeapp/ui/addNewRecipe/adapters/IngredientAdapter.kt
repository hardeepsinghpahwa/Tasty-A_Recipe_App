package com.tasty.recipeapp.ui.addNewRecipe.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tasty.recipeapp.R
import com.tasty.recipeapp.databinding.ItemIngredientBinding
import com.tasty.recipeapp.model.response.Ingredient
import javax.inject.Inject


class IngredientAdapter @Inject constructor() :
    RecyclerView.Adapter<IngredientAdapter.MealsItemViewHolder>() {

    private var items = ArrayList<Ingredient>()

    private var selected:Boolean=false

    private lateinit var listener: IngredientClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealsItemViewHolder {
        val layout =
            ItemIngredientBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MealsItemViewHolder(layout)
    }

    fun setClickListener(listener: IngredientClickListener) {
        this.listener = listener
    }

    fun setSelected(value: Boolean){
        selected=value
    }

    fun updateList(data: ArrayList<Ingredient>) {
        items.clear()
        items.addAll(data)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class MealsItemViewHolder(val binding: ItemIngredientBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onBindViewHolder(holder: MealsItemViewHolder, position: Int) {
        with(holder) {
            with(items[position]) {

                binding.name.setTextColor(
                    ContextCompat.getColor(
                        binding.root.context,
                        R.color.black
                    )
                )

                if(!selected){
                    binding.delete.visibility=View.GONE
                }

                if(selected){
                    binding.name.text = this.measure+" "+ this.strIngredient
                }else{
                    binding.name.text = this.strIngredient
                }

                binding.root.setOnClickListener {
                    listener.onIngredientClick(this)
                }

                binding.delete.setOnClickListener {
                    listener.onIngredientDelete(position)
                }
            }

        }
    }
}

interface IngredientClickListener {
    fun onIngredientClick(ingredient: Ingredient)
    fun onIngredientDelete(position: Int)
}


