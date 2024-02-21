package com.tasty.recipeapp.ui.notifications

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.tasty.recipeapp.BR
import com.tasty.recipeapp.MainApplication
import com.tasty.recipeapp.R
import com.tasty.recipeapp.databinding.ActivityNotificationsBinding
import com.tasty.recipeapp.di.components.ActivityComponent
import com.tasty.recipeapp.di.components.DaggerActivityComponent
import com.tasty.recipeapp.di.modules.ActivityModule
import com.tasty.recipeapp.model.response.NotificationExtraData
import com.tasty.recipeapp.model.response.NotificationResponse
import com.tasty.recipeapp.ui.notifications.adapters.NotificationAdapter
import com.tasty.recipeapp.ui.notifications.adapters.NotificationClickListener
import com.tasty.recipeapp.ui.recipeDetails.RecipeDetailsScreen
import javax.inject.Inject

class Notifications : AppCompatActivity(), NotificationClickListener {

    lateinit var component: ActivityComponent
    lateinit var binding: ActivityNotificationsBinding

    @Inject
    lateinit var viewModelFactory: NotificationViewModelFactory

    val viewModel: NotificationViewModel by viewModels { viewModelFactory }

    @Inject
    lateinit var notificationAdapter: NotificationAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initDependencyInjection()

        viewModel.getNotificationData()

        binding.notifications.layoutManager = LinearLayoutManager(this)
        binding.notifications.adapter = notificationAdapter
        notificationAdapter.setClickListener(this)

        viewModel.getNotifications().observe(this) {
            notificationAdapter.updateList(it)
        }

        binding.back.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }


    private fun initDependencyInjection() {
        component = DaggerActivityComponent.builder()
            .activityModule(ActivityModule(this))
            .applicationComponent((application as MainApplication).applicationComponent)
            .build()

        component.injectActivity(this)
        initDataBinding()
    }

    private fun initDataBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_notifications)
        binding.setVariable(BR.viewModel, viewModel)
        binding.executePendingBindings()
    }

    override fun onNotificationClick(notificationResponse: NotificationResponse) {
        if (!notificationResponse.extra_data.isNullOrEmpty() && notificationResponse.extra_data != "{}") {

            val notificationData =
                Gson().fromJson(notificationResponse.extra_data, NotificationExtraData::class.java)

            val intent1 = Intent(this, RecipeDetailsScreen::class.java)
            if (notificationData.firebase.isNotEmpty()) {
                intent1.putExtra("id", notificationData.mealId)
                intent1.putExtra("firebase", notificationData.firebase)
            }
            startActivity(intent1)

        }
    }
}