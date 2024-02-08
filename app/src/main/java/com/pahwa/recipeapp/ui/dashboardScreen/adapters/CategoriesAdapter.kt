package com.pahwa.recipeapp.ui.dashboardScreen.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.pahwa.recipeapp.R
import com.pahwa.recipeapp.databinding.ItemCategoryBinding
import com.pahwa.recipeapp.model.response.Categories
import javax.inject.Inject


class CategoriesAdapter @Inject constructor() :
    RecyclerView.Adapter<CategoriesAdapter.CategoriesViewHolder>() {

    private var items = ArrayList<Categories>()
    private var selectedCategory = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriesViewHolder {
        var layout = ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoriesViewHolder(layout)
    }

    fun updateList(){
        items.add(Categories(strCategory = "All"))
        items.add(Categories(strCategory = "First"))
        items.add(Categories(strCategory = "Second"))
        items.add(Categories(strCategory = "Third"))
        items.add(Categories(strCategory = "Fourth"))
        items.add(Categories(strCategory = "Fifth"))
        notifyDataSetChanged()
    }

    fun selectCategory(selected: Int) {
        selectedCategory = selected
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class CategoriesViewHolder(val binding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onBindViewHolder(holder: CategoriesViewHolder, position: Int) {
        with(holder) {
            with(items[position]) {
                binding.name.text = this.strCategory

                if (position == selectedCategory) {
                    binding.name.background = ContextCompat.getDrawable(
                        binding.name.context,
                        R.drawable.selected_category
                    )
                    binding.name.setTextColor(ContextCompat.getColor(binding.name.context,R.color.white))
                    binding.name.typeface= ResourcesCompat.getFont(binding.name.context, R.font.poppins_bold)
                } else {
                    binding.name.background = ContextCompat.getDrawable(
                        binding.name.context,
                        R.drawable.unselected_category
                    )
                    binding.name.typeface= ResourcesCompat.getFont(binding.name.context, R.font.poppins)
                    binding.name.setTextColor(ContextCompat.getColor(binding.name.context,R.color.tealGreen))
                }
            }

            binding.name.setOnClickListener {
                selectCategory(position)
            }
        }
    }
}


