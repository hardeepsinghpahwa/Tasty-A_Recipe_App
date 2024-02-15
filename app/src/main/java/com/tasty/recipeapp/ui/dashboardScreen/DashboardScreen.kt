package com.tasty.recipeapp.ui.dashboardScreen

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.tasty.recipeapp.BR
import com.tasty.recipeapp.MainApplication
import com.tasty.recipeapp.R
import com.tasty.recipeapp.databinding.ActivityDashboardScreenBinding
import com.tasty.recipeapp.di.components.ActivityComponent
import com.tasty.recipeapp.di.components.DaggerActivityComponent
import com.tasty.recipeapp.di.modules.ActivityModule
import com.tasty.recipeapp.ui.dashboardScreen.adapters.CategoriesAdapter
import com.tasty.recipeapp.ui.dashboardScreen.adapters.CategoryClickListener
import com.tasty.recipeapp.ui.dashboardScreen.adapters.NewRecipeClickListener
import com.tasty.recipeapp.ui.dashboardScreen.adapters.NewRecipesAdapter
import com.tasty.recipeapp.ui.dashboardScreen.adapters.ShowCaseOnClickListener
import com.tasty.recipeapp.ui.dashboardScreen.adapters.ShowcaseAdapter
import com.tasty.recipeapp.ui.recipeDetails.RecipeDetailsScreen
import com.tasty.recipeapp.ui.searchScreen.SearchScreen
import javax.inject.Inject


class DashboardScreen : AppCompatActivity(), ShowCaseOnClickListener, CategoryClickListener,
    NewRecipeClickListener {

    private lateinit var binding: ActivityDashboardScreenBinding

    private lateinit var component: ActivityComponent

    @Inject
    lateinit var categoriesAdapter: CategoriesAdapter

    @Inject
    lateinit var showcaseAdapter: ShowcaseAdapter

    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    @Inject
    lateinit var newRecipesAdapter: NewRecipesAdapter

    @Inject
    lateinit var dashboardViewModelFactory: DashboardViewModelFactory

    private val viewModel: DashboardViewModel by viewModels { dashboardViewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initDependencyInjection()
        initRecyclerViews()
        setProfilePhoto()

        viewModel.getRecipes().observe(this) {
            showcaseAdapter.updateList(it)
        }

        viewModel.getCategories().observe(this) {
            categoriesAdapter.updateList(it)
        }

        binding.retry.setOnClickListener {
            initData()
        }

        binding.search.setOnClickListener {
            startActivity(Intent(this, SearchScreen::class.java))
        }


        viewModel.getNewRecipeData()

        viewModel.getNewRecipes().observe(this) {
            newRecipesAdapter.updateList(it)
        }
    }

    private fun setProfilePhoto() {
        Glide.with(this)
            .load(firebaseAuth.currentUser?.photoUrl.toString())
            .into(
                binding.profilePic
            )
    }

    private fun initRecyclerViews() {
        binding.categories.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.categories.adapter = categoriesAdapter

        categoriesAdapter.setClickListener(this)
        showcaseAdapter.setClickListener(this)
        newRecipesAdapter.setNewRecipeClickListener(this)
        binding.showcase.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.showcase.adapter = showcaseAdapter

        binding.newRecipes.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.newRecipes.adapter = newRecipesAdapter

    }

    private fun initData() {
        viewModel.getCategoriesData()
        viewModel.getRecipeData("Vegetarian")
    }


    override fun onResume() {
        super.onResume()
        initData()
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
        binding = DataBindingUtil.setContentView(this, R.layout.activity_dashboard_screen)
        binding.setVariable(BR.viewModel, viewModel)
        binding.executePendingBindings()
    }

    override fun onShowCaseClick(mealId: String) {

        val intent = (Intent(this, RecipeDetailsScreen::class.java))
        intent.putExtra("id", mealId)
        startActivity(intent)
    }

    override fun onCategoryClick(categoryName: String) {
        viewModel.getRecipeData(categoryName)
    }

    override fun newRecipeClick(mealId: String) {
        val intent = (Intent(this, RecipeDetailsScreen::class.java))
        intent.putExtra("id", mealId)
        intent.putExtra("firebase", "true")
        startActivity(intent)
    }


}