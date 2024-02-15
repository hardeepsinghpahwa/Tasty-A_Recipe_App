package com.tasty.recipeapp.ui.dashboardScreen.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.tasty.recipeapp.databinding.ItemNewRecipeBinding
import com.tasty.recipeapp.model.response.NewRecipe
import javax.inject.Inject


class NewRecipesAdapter @Inject constructor() :
    RecyclerView.Adapter<NewRecipesAdapter.NewRecipeViewHolder>() {

    private var items = ArrayList<NewRecipe>()

    lateinit var listener: NewRecipeClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewRecipeViewHolder {
        var layout =
            ItemNewRecipeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NewRecipeViewHolder(layout)
    }

    fun setNewRecipeClickListener(listener: NewRecipeClickListener){
        this.listener=listener
    }

    fun updateList(newRecipes: ArrayList<NewRecipe>) {
        items.clear()
        items.addAll(newRecipes)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class NewRecipeViewHolder(val binding: ItemNewRecipeBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onBindViewHolder(holder: NewRecipeViewHolder, position: Int) {
        with(holder) {
            with(items[position]) {
                val circularProgressDrawable = CircularProgressDrawable(binding.root.context)
                circularProgressDrawable.strokeWidth = 5f
                circularProgressDrawable.centerRadius = 30f
                circularProgressDrawable.start()

                binding.title.text = this.name
                binding.chef.text=this.chef
                binding.time.text=this.time

                binding.ratingBar.rating=this.rating.toFloat()

                Glide.with(binding.root.context)
                    .load(this.image)
                    .placeholder(circularProgressDrawable)
                    .into(binding.thumbnail)

                binding.root.setOnClickListener {
                    listener.newRecipeClick(this.id)
                }

            }

        }
    }
}

interface NewRecipeClickListener{
    fun newRecipeClick(mealId:String)
}


