package com.tasty.recipeapp.ui.recipeDetails

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.tasty.recipeapp.BR
import com.tasty.recipeapp.MainApplication
import com.tasty.recipeapp.R
import com.tasty.recipeapp.databinding.ActivityRecipeDetailsScreenBinding
import com.tasty.recipeapp.di.components.ActivityComponent
import com.tasty.recipeapp.di.components.DaggerActivityComponent
import com.tasty.recipeapp.di.modules.ActivityModule
import com.tasty.recipeapp.ui.dialogs.FullScreenImageDialog
import com.tasty.recipeapp.ui.recipeDetails.adapters.ThumbnailAdapter
import com.tasty.recipeapp.ui.recipeDetails.adapters.ThumbnailClickListener
import javax.inject.Inject

class RecipeDetailsScreen : AppCompatActivity(), ThumbnailClickListener {

    private lateinit var binding: ActivityRecipeDetailsScreenBinding

    private lateinit var component: ActivityComponent

    @Inject
    lateinit var thumbnailAdapter: ThumbnailAdapter

    @Inject
    lateinit var recipeDetailsViewModelFactory: RecipeDetailsViewModelFactory

    private val viewModel: RecipeDetailsViewModel by viewModels { recipeDetailsViewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initDependencyInjection()

        showData()


        viewModel.getRecipes().observe(this) {

        }

        viewModel.getImage().observe(this) {
            Glide.with(this)
                .load(it)
                .placeholder(R.drawable.rounded_grey_container)
                .into(binding.thumbnail)
        }

        binding.ingredientImages.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.ingredientImages.adapter = thumbnailAdapter
        thumbnailAdapter.setClickListener(this)
        viewModel.getIngredientImages().observe(this) {
            thumbnailAdapter.updateList(it)
        }

        binding.retry.setOnClickListener {
            showData()
        }

    }

    private fun showData() {
        val fromFirebase = intent.getStringExtra("firebase") != null
        if (fromFirebase) {
            viewModel.getRecipeDetailsFromFirebase(intent.getStringExtra("id").toString())
        } else {
            viewModel.getRecipeDetails(intent.getStringExtra("id").toString())
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

    override fun onThumbnailClick(image: String, name: String) {
        FullScreenImageDialog.newInstance(image, name)
            .show(supportFragmentManager, "FullScreenImageDialog")
    }
}