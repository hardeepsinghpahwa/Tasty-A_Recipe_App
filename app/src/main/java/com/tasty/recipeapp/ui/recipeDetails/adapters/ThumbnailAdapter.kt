package com.tasty.recipeapp.ui.recipeDetails.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tasty.recipeapp.R
import com.tasty.recipeapp.databinding.ThumbnailItemBinding
import javax.inject.Inject


class ThumbnailAdapter @Inject constructor() :
    RecyclerView.Adapter<ThumbnailAdapter.MealsItemViewHolder>() {

    private var items = ArrayList<String>()

    private lateinit var listener: ThumbnailClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealsItemViewHolder {
        val layout =
            ThumbnailItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MealsItemViewHolder(layout)
    }

    fun setClickListener(listener: ThumbnailClickListener) {
        this.listener = listener
    }

    fun updateList(data: ArrayList<String>) {
        items.clear()
        items.addAll(data)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class MealsItemViewHolder(val binding: ThumbnailItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onBindViewHolder(holder: MealsItemViewHolder, position: Int) {
        with(holder) {
            with(items[position]) {
                Glide.with(binding.root.context)
                    .load("http://www.themealdb.com/images/ingredients/${this}-Small.png")
                    .placeholder(R.drawable.placeholder)
                    .into(binding.thumbnail)

                binding.root.setOnClickListener {
                    listener.onThumbnailClick("http://www.themealdb.com/images/ingredients/${this}.png",this)
                }
            }

        }
    }
}

interface ThumbnailClickListener {
    fun onThumbnailClick(image: String,name:String)
}


