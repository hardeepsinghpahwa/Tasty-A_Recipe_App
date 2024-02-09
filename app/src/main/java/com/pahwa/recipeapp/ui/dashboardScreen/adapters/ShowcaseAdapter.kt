package com.pahwa.recipeapp.ui.dashboardScreen.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pahwa.recipeapp.databinding.ItemShowcaseBinding
import com.pahwa.recipeapp.model.response.MealData
import javax.inject.Inject


class ShowcaseAdapter @Inject constructor() :
    RecyclerView.Adapter<ShowcaseAdapter.MealsItemViewHolder>() {

    private var items = ArrayList<MealData>()

    private lateinit var listener: ShowCaseOnClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealsItemViewHolder {
        var layout = ItemShowcaseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MealsItemViewHolder(layout)
    }

    fun setClickListener(listener: ShowCaseOnClickListener) {
        this.listener = listener
    }

    fun updateList(data: ArrayList<MealData>) {
        items.clear()
        items.addAll(data)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class MealsItemViewHolder(val binding: ItemShowcaseBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onBindViewHolder(holder: MealsItemViewHolder, position: Int) {
        with(holder) {
            with(items[position]) {
                binding.title.text = this.strMeal
                Glide.with(binding.root.context)
                    .load(this.strMealThumb)
                    .into(binding.thumbnail)

                binding.root.setOnClickListener{
                    listener.onShowCaseClick(this.idMeal)
                }
            }


    }
}
}

interface ShowCaseOnClickListener {
    fun onShowCaseClick(mealId:String)
}


