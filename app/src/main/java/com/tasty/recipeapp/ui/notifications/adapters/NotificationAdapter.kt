package com.tasty.recipeapp.ui.notifications.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tasty.recipeapp.R
import com.tasty.recipeapp.databinding.ItemNotificationBinding
import com.tasty.recipeapp.model.response.NotificationResponse
import javax.inject.Inject


class NotificationAdapter @Inject constructor() :
    RecyclerView.Adapter<NotificationAdapter.MealsItemViewHolder>() {

    private var items = ArrayList<NotificationResponse>()

    private lateinit var listener: NotificationClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealsItemViewHolder {
        val layout =
            ItemNotificationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MealsItemViewHolder(layout)
    }

    fun setClickListener(listener: NotificationClickListener) {
        this.listener = listener
    }

    fun updateList(data: ArrayList<NotificationResponse>) {
        items.clear()
        items.addAll(data)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class MealsItemViewHolder(val binding: ItemNotificationBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onBindViewHolder(holder: MealsItemViewHolder, position: Int) {
        with(holder) {
            with(items[position]) {

                if (!this.image.isNullOrEmpty()) {
                    Glide.with(binding.root.context)
                        .load(this.image)
                        .placeholder(R.drawable.placeholder)
                        .into(binding.thumbnail)
                }

                binding.title.text = this.title
                binding.desc.text = this.message

                binding.root.setOnClickListener {
                    listener.onNotificationClick(this)
                }
            }

        }
    }
}

interface NotificationClickListener {
    fun onNotificationClick(notificationResponse: NotificationResponse)
}


