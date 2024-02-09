package com.pahwa.recipeapp.ui.dashboardScreen

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.pahwa.recipeapp.BR
import com.pahwa.recipeapp.MainApplication
import com.pahwa.recipeapp.R
import com.pahwa.recipeapp.databinding.ActivityDashboardScreenBinding
import com.pahwa.recipeapp.di.components.ActivityComponent
import com.pahwa.recipeapp.di.components.DaggerActivityComponent
import com.pahwa.recipeapp.di.modules.ActivityModule
import com.pahwa.recipeapp.ui.dashboardScreen.adapters.CategoriesAdapter
import com.pahwa.recipeapp.ui.dashboardScreen.adapters.NewrecipesAdapter
import com.pahwa.recipeapp.ui.dashboardScreen.adapters.ShowcaseAdapter
import javax.inject.Inject


class DashboardScreen : AppCompatActivity() {

    private lateinit var binding: ActivityDashboardScreenBinding

    private lateinit var component: ActivityComponent

    @Inject
    lateinit var categoriesAdapter: CategoriesAdapter

    @Inject
    lateinit var showcaseAdapter: ShowcaseAdapter

    @Inject
    lateinit var newRecipesAdapter: NewrecipesAdapter

    @Inject
    lateinit var dashboardViewModelFactory: DashboardViewModelFactory

    private val viewModel: DashboardViewModel by viewModels { dashboardViewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initDependencyInjection()

        binding.categories.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.categories.adapter = categoriesAdapter

        binding.showcase.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.showcase.adapter = showcaseAdapter

        binding.newRecipes.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.newRecipes.adapter = newRecipesAdapter

        newRecipesAdapter.updateList()
        Log.d("COUNT", categoriesAdapter.itemCount.toString())

        viewModel.getRecipes().observe(this) {
            showcaseAdapter.updateList(it)
        }

        viewModel.getCategories().observe(this) {
            categoriesAdapter.updateList(it)
        }

        binding.retry.setOnClickListener {
            initData()
        }

    }

    private fun initData() {
        viewModel.getCategorieData()
        viewModel.getRecipeData()
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
}