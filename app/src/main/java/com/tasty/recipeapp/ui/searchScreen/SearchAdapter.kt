package com.tasty.recipeapp.ui.searchScreen

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.tasty.recipeapp.databinding.ItemSearchBinding
import com.tasty.recipeapp.model.response.MealsItem
import javax.inject.Inject


class SearchAdapter @Inject constructor() :
    RecyclerView.Adapter<SearchAdapter.MealsItemViewHolder>() {

    private var items = ArrayList<MealsItem>()
    private var selectedCategory = 0

    lateinit var listener: SearchItemClickListener
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealsItemViewHolder {
        var layout = ItemSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MealsItemViewHolder(layout)
    }

    fun setOnClickListener(listener: SearchItemClickListener) {
        this.listener = listener
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
                val circularProgressDrawable = CircularProgressDrawable(binding.root.context)
                circularProgressDrawable.strokeWidth = 5f
                circularProgressDrawable.centerRadius = 30f
                circularProgressDrawable.start()

                Glide.with(holder.itemView.context)
                    .load(this.strMealThumb)
                    .placeholder(circularProgressDrawable)
                    .into(binding.thumbnail)

                binding.title.text = this.strMeal
                binding.subTitle.text = this.strArea + " " + this.strCategory

                binding.root.setOnClickListener {
                    listener.onSearchItemClick(this.idMeal)
                }
            }
        }
    }
}

interface SearchItemClickListener {
    fun onSearchItemClick(mealId: String)
}


