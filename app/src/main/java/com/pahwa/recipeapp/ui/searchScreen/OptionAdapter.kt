package com.pahwa.recipeapp.ui.searchScreen

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.pahwa.recipeapp.R
import com.pahwa.recipeapp.databinding.ItemFilterBinding
import com.pahwa.recipeapp.model.response.MealsItem
import javax.inject.Inject

class OptionAdapter @Inject constructor() :
    RecyclerView.Adapter<OptionAdapter.MealsItemViewHolder>() {

    private var items = ArrayList<MealsItem>()
    private var type: String = "Category"
    private var selectedItems = ArrayList<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealsItemViewHolder {
        var layout = ItemFilterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MealsItemViewHolder(layout)
    }

    fun updateList(data: ArrayList<MealsItem>, type: String) {
        this.type = type
        items.clear()

        var temp = ArrayList<MealsItem>()
        var temp2 = ArrayList<String>()

        if (type == "Category") {
            for (i in data) {
                if (!temp2.contains(i.strCategory)) {
                    temp.add(i)
                    temp2.add(i.strCategory)
                }
            }
        } else if (type == "Area") {
            for (i in data) {
                if (!temp2.contains(i.strArea)) {
                    temp.add(i)
                    temp2.add(i.strArea)
                }
            }
        }
        items.addAll(temp)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun getFilteredResult():ArrayList<String>{
        return selectedItems
    }

    inner class MealsItemViewHolder(val binding: ItemFilterBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onBindViewHolder(holder: MealsItemViewHolder, position: Int) {
        with(holder) {
            with(items[position]) {

                if (type == "Category") {
                    binding.name.text = this.strCategory

                    binding.name.setOnClickListener {
                        if (selectedItems.contains(this.strCategory)) {
                            selectedItems.remove(this.strCategory)
                            notifyDataSetChanged()
                        } else {
                            selectedItems.add(this.strCategory)
                            notifyDataSetChanged()
                        }

                        Log.d("FILTERED",selectedItems.toString())
                    }

                    if (selectedItems.contains(this.strCategory)) {
                        binding.name.background = ContextCompat.getDrawable(
                            binding.name.context,
                            R.drawable.selected_category
                        )
                        binding.name.setTextColor(
                            ContextCompat.getColor(
                                binding.name.context,
                                R.color.white
                            )
                        )
                        binding.name.typeface =
                            ResourcesCompat.getFont(binding.name.context, R.font.poppins_bold)
                    } else {
                        binding.name.background = ContextCompat.getDrawable(
                            binding.name.context,
                            R.drawable.stroked_corners
                        )
                        binding.name.typeface =
                            ResourcesCompat.getFont(binding.name.context, R.font.poppins)
                        binding.name.setTextColor(
                            ContextCompat.getColor(
                                binding.name.context,
                                R.color.tealGreen
                            )
                        )
                    }
                } else if (type == "Area") {
                    binding.name.text = this.strArea

                    binding.name.setOnClickListener {
                        if (selectedItems.contains(this.strArea)) {
                            selectedItems.remove(this.strArea)
                            notifyDataSetChanged()
                        } else {
                            selectedItems.add(this.strArea)
                            notifyDataSetChanged()
                        }

                        Log.d("FILTERED",selectedItems.toString())
                    }

                    if (selectedItems.contains(this.strArea)) {
                        binding.name.background = ContextCompat.getDrawable(
                            binding.name.context,
                            R.drawable.selected_category
                        )
                        binding.name.setTextColor(
                            ContextCompat.getColor(
                                binding.name.context,
                                R.color.white
                            )
                        )
                        binding.name.typeface =
                            ResourcesCompat.getFont(binding.name.context, R.font.poppins_bold)
                    } else {
                        binding.name.background = ContextCompat.getDrawable(
                            binding.name.context,
                            R.drawable.stroked_corners
                        )
                        binding.name.typeface =
                            ResourcesCompat.getFont(binding.name.context, R.font.poppins)
                        binding.name.setTextColor(
                            ContextCompat.getColor(
                                binding.name.context,
                                R.color.tealGreen
                            )
                        )
                    }
                }



            }
        }
    }
}


