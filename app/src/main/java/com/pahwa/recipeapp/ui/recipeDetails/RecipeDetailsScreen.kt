package com.pahwa.recipeapp.ui.recipeDetails

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.pahwa.recipeapp.BR
import com.pahwa.recipeapp.MainApplication
import com.pahwa.recipeapp.R
import com.pahwa.recipeapp.databinding.ActivityRecipeDetailsScreenBinding
import com.pahwa.recipeapp.di.components.ActivityComponent
import com.pahwa.recipeapp.di.components.DaggerActivityComponent
import com.pahwa.recipeapp.di.modules.ActivityModule
import javax.inject.Inject

class RecipeDetailsScreen : AppCompatActivity() {

    private lateinit var binding: ActivityRecipeDetailsScreenBinding

    private lateinit var component: ActivityComponent

    @Inject
    lateinit var recipeDetailsViewModelFactory: RecipeDetailsViewModelFactory

    private val viewModel: RecipeDetailsViewModel by viewModels { recipeDetailsViewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initDependencyInjection()

        viewModel.getRecipeDetails(intent.getStringExtra("id").toString())

        viewModel.getRecipes().observe(this) {

        }

        viewModel.getImage().observe(this) {
            Glide.with(this)
                .load(it)
                .placeholder(R.drawable.rounded_grey_container)
                .into(binding.thumbnail)
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
        binding = DataBindingUtil.setContentView(this, R.layout.activity_recipe_details_screen)
        binding.setVariable(BR.viewModel, viewModel)
        binding.executePendingBindings()
    }
}