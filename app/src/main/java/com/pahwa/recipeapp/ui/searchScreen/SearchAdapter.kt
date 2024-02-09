package com.pahwa.recipeapp.ui.searchScreen

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pahwa.recipeapp.databinding.ItemSearchBinding
import com.pahwa.recipeapp.model.response.MealsItem
import javax.inject.Inject


class SearchAdapter @Inject constructor() :
    RecyclerView.Adapter<SearchAdapter.MealsItemViewHolder>() {

    private var items = ArrayList<MealsItem>()
    private var selectedCategory = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealsItemViewHolder {
        var layout = ItemSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MealsItemViewHolder(layout)
    }

    fun updateList(data: ArrayList<MealsItem>) {
        items.clear()
        items.addAll(data)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class MealsItemViewHolder(val binding: ItemSearchBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onBindViewHolder(holder: MealsItemViewHolder, position: Int) {
        with(holder) {
            with(items[position]) {
                Glide.with(holder.itemView.context)
                    .load(this.strMealThumb)
                    .into(binding.thumbnail)

                binding.title.text = this.strMeal
                binding.subTitle.text = this.strArea + " " + this.strCategory
            }
        }
    }
}


