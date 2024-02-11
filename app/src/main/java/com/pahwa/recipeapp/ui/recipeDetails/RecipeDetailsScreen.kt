package com.pahwa.recipeapp.ui.recipeDetails

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.pahwa.recipeapp.BR
import com.pahwa.recipeapp.MainApplication
import com.pahwa.recipeapp.R
import com.pahwa.recipeapp.databinding.ActivityRecipeDetailsScreenBinding
import com.pahwa.recipeapp.di.components.ActivityComponent
import com.pahwa.recipeapp.di.components.DaggerActivityComponent
import com.pahwa.recipeapp.di.modules.ActivityModule
import com.pahwa.recipeapp.ui.dialogs.FullScreenImageDialog
import com.pahwa.recipeapp.ui.recipeDetails.adapters.ThumbnailAdapter
import com.pahwa.recipeapp.ui.recipeDetails.adapters.ThumbnailClickListener
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

        viewModel.getRecipeDetails(intent.getStringExtra("id").toString())

        viewModel.getRecipes().observe(this) {

        }

        viewModel.getImage().observe(this) {
            Glide.with(this)
                .load(it)
                .placeholder(R.drawable.rounded_grey_container)
                .into(binding.thumbnail)
        }

        binding.ingredientImages.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        binding.ingredientImages.adapter = thumbnailAdapter
        thumbnailAdapter.setClickListener(this)
        viewModel.getIngredientImages().observe(this) {
            Log.d("IMAGESSS",it.toString())
            thumbnailAdapter.updateList(it)
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

    override fun onThumbnailClick(mealId: String) {
        FullScreenImageDialog.newInstance(mealId).show(supportFragmentManager,"FullScreenImageDialog")
    }
}