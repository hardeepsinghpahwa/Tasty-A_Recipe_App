package com.pahwa.recipeapp.ui.dashboardScreen.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pahwa.recipeapp.databinding.ItemNewRecipeBinding
import com.pahwa.recipeapp.model.response.Categories
import javax.inject.Inject


class NewrecipesAdapter @Inject constructor() :
    RecyclerView.Adapter<NewrecipesAdapter.CategoriesViewHolder>() {

        private var items = ArrayList<Categories>()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriesViewHolder {
            var layout = ItemNewRecipeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return CategoriesViewHolder(layout)
        }

        fun updateList() {
            items.add(
                Categories(
                    strCategory = "Beef",
                    strCategoryThumb = "https://www.themealdb.com/images/category/beef.png"
                )
            )
            items.add(
                Categories(
                    strCategory = "Chicken",
                    strCategoryThumb = "https://www.themealdb.com/images/category/chicken.png"
                )
            )
            items.add(
                Categories(
                    strCategory = "Desert",
                    strCategoryThumb = "https://www.themealdb.com/images/category/dessert.png"
                )
            )
            items.add(
                Categories(
                    strCategory = "Lamb",
                    strCategoryThumb = "https://www.themealdb.com/images/category/lamb.png"
                )
            )
            items.add(
                Categories(
                    strCategory = "Pasta",
                    strCategoryThumb = "https://www.themealdb.com/images/category/pasta.png"
                )
            )
            items.add(Categories(strCategory = "Fifth"))
            notifyDataSetChanged()
        }

        override fun getItemCount(): Int {
            return items.size
        }

        inner class CategoriesViewHolder(val binding: ItemNewRecipeBinding) :
            RecyclerView.ViewHolder(binding.root)

        override fun onBindViewHolder(holder: CategoriesViewHolder, position: Int) {
            with(holder) {
                with(items[position]) {
                    binding.title.text = this.strCategory
                    Glide.with(binding.root.context)
                        .load(this.strCategoryThumb)
                        .into(binding.thumbnail)
                }

                binding.root.setOnClickListener {
                }
            }
        }
    }


