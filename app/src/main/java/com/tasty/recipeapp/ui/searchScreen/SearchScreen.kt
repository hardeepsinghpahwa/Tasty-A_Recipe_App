package com.tasty.recipeapp.ui.searchScreen

import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.TextView.OnEditorActionListener
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.tasty.recipeapp.BR
import com.tasty.recipeapp.MainApplication
import com.tasty.recipeapp.R
import com.tasty.recipeapp.databinding.ActivitySearchScreenBinding
import com.tasty.recipeapp.databinding.FilterBottomSheetBinding
import com.tasty.recipeapp.di.components.ActivityComponent
import com.tasty.recipeapp.di.components.DaggerActivityComponent
import com.tasty.recipeapp.di.modules.ActivityModule
import com.tasty.recipeapp.model.response.MealsItem
import com.tasty.recipeapp.ui.recipeDetails.RecipeDetailsScreen
import com.tasty.recipeapp.utils.CommonUtils
import javax.inject.Inject


class SearchScreen : AppCompatActivity(), SearchItemClickListener {

    lateinit var binding: ActivitySearchScreenBinding

    @Inject
    lateinit var viewModelFactory: SearchViewModelFactory

    private val viewModel: SearchViewModel by viewModels { viewModelFactory }

    private lateinit var component: ActivityComponent

    @Inject
    lateinit var searchAdapter: SearchAdapter

    @Inject
    lateinit var catOptions: OptionAdapter

    @Inject
    lateinit var areaOptions: OptionAdapter

    var items = ArrayList<MealsItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initDependencyInjection()

        binding.search.requestFocus()

        binding.searchResults.adapter = searchAdapter
        binding.searchResults.layoutManager = GridLayoutManager(this, 2)

        searchAdapter.setOnClickListener(this)

        viewModel.getSearchResults().observe(this) {
            searchAdapter.updateList(it)
            items.clear()
            items.addAll(it)
            catOptions.updateList(it, "Category")
            areaOptions.updateList(it, "Area")

            if(it.isEmpty()){
                viewModel.empty.set(true)
            }else{
                viewModel.empty.set(false)
            }
        }

        binding.back.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }


        binding.search.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                CommonUtils.hideKeyboard(this)
                viewModel.searchData()
                return@OnEditorActionListener true
            }
            false
        })

        binding.filter.setOnClickListener {

            // on below line we are creating a new bottom sheet dialog.
            val bottomSheet = BottomSheetDialog(this)
            val bindingSheet = DataBindingUtil.inflate<FilterBottomSheetBinding>(
                layoutInflater,
                R.layout.filter_bottom_sheet,
                null,
                false
            )

            val layoutManager = FlexboxLayoutManager(applicationContext).apply {
                flexDirection = FlexDirection.ROW
            }

            val layoutManager2 = FlexboxLayoutManager(applicationContext).apply {
                flexDirection = FlexDirection.ROW
            }
            bindingSheet.catFilters.layoutManager = layoutManager
            bindingSheet.catFilters.adapter = catOptions

            bindingSheet.areaFilters.layoutManager = layoutManager2
            bindingSheet.areaFilters.adapter = areaOptions

            bottomSheet.setContentView(bindingSheet.root)

            bindingSheet.clearFilter.setOnClickListener {
                bottomSheet.dismiss()
                catOptions.clearFilter()
                areaOptions.clearFilter()
                viewModel.searchData()
            }

            bindingSheet.filter.setOnClickListener {

                val filteredCats = catOptions.getFilteredResult()
                val filteredAreas = areaOptions.getFilteredResult()

                var temp = ArrayList<MealsItem>()

                for (i in items) {
                    if (filteredCats.isEmpty() && filteredAreas.isNotEmpty()) {
                        if (filteredAreas.contains(i.strArea)) {
                            temp.add(i)
                        }
                    } else if (filteredCats.isNotEmpty() && filteredAreas.isEmpty()) {
                        if (filteredCats.contains(i.strCategory)) {
                            temp.add(i)
                        }
                    } else if ((filteredCats.isNotEmpty() && filteredAreas.isNotEmpty()) && (filteredCats.contains(
                            i.strCategory
                        ) && filteredAreas.contains(i.strArea))
                    ) {
                        temp.add(i)
                    }
                }

                viewModel.searchTitle.set("Search Results (${temp.size})")

                viewModel.searchResults.postValue(temp)
                bottomSheet.dismiss()
            }

            bottomSheet.show()
        }
    }

    private fun initDependencyInjection() {
        component = DaggerActivityComponent.builder()
            .activityModule(ActivityModule(this))
            .applicationComponent((application as MainApplication).getComponent())
            .build()

        component.injectActivity(this)

        initDataBinding()
    }

    private fun initDataBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search_screen)
        binding.setVariable(BR.viewModel, viewModel)
        binding.executePendingBindings()
    }

    override fun onSearchItemClick(mealId: String) {
        val intent = (Intent(this, RecipeDetailsScreen::class.java))
        intent.putExtra("id", mealId)
        startActivity(intent)
    }
}