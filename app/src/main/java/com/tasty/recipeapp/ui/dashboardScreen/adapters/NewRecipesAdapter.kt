package com.tasty.recipeapp.ui.dashboardScreen.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.tasty.recipeapp.databinding.ItemNewRecipeBinding
import com.tasty.recipeapp.model.response.MealsItem
import javax.inject.Inject


class NewRecipesAdapter @Inject constructor() :
    RecyclerView.Adapter<NewRecipesAdapter.MealsItemViewHolder>() {

    private var items = ArrayList<MealsItem>()

    lateinit var listener: NewRecipeClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealsItemViewHolder {
        var layout =
            ItemNewRecipeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MealsItemViewHolder(layout)
    }

    fun setNewRecipeClickListener(listener: NewRecipeClickListener) {
        this.listener = listener
    }

    fun updateList(MealsItems: ArrayList<MealsItem>) {
        items.clear()
        items.addAll(MealsItems)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class MealsItemViewHolder(val binding: ItemNewRecipeBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onBindViewHolder(holder: MealsItemViewHolder, position: Int) {
        with(holder) {
            with(items[position]) {
                val circularProgressDrawable = CircularProgressDrawable(binding.root.context)
                circularProgressDrawable.strokeWidth = 5f
                circularProgressDrawable.centerRadius = 30f
                circularProgressDrawable.start()

                binding.title.text = this.strMeal
                binding.chef.text = this.addedBy
                binding.time.text = this.time

                if (this.rating != null) {
                    binding.ratingBar.rating = this.rating!!.toFloat()
                }

                Glide.with(binding.root.context)
                    .load(this.strMealThumb)
                    .placeholder(circularProgressDrawable)
                    .into(binding.thumbnail)

                binding.root.setOnClickListener {
                    listener.newRecipeClick(this.idMeal)
                }

            }

        }
    }
}

interface NewRecipeClickListener {
    fun newRecipeClick(mealId: String)
}


