package com.tasty.recipeapp.ui.loginScreen

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.tasty.recipeapp.R
import com.tasty.recipeapp.databinding.ImageItemBinding

class PagerAdapter(private val imageUrlList: List<String>) :
    RecyclerView.Adapter<PagerAdapter.ViewPagerViewHolder>() {

    inner class ViewPagerViewHolder(val binding: ImageItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun setData(imageUrl: String) {

            Glide.with(binding.root.context)
                .load(imageUrl)
                .error(R.drawable.ic_launcher_foreground)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(binding.ivImage)
        }

    }

    override fun getItemCount(): Int = imageUrlList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerViewHolder {

        val binding = ImageItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return ViewPagerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewPagerViewHolder, position: Int) {

        holder.setData(imageUrlList[position])
    }

}