package com.tasty.recipeapp.ui.addNewRecipe

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.tasty.recipeapp.BR
import com.tasty.recipeapp.MainApplication
import com.tasty.recipeapp.R
import com.tasty.recipeapp.databinding.ActivityAddNewRecipeBinding
import com.tasty.recipeapp.di.components.ActivityComponent
import com.tasty.recipeapp.di.components.DaggerActivityComponent
import com.tasty.recipeapp.di.modules.ActivityModule
import com.tasty.recipeapp.model.response.Ingredient
import com.tasty.recipeapp.ui.dialogs.SelectIngredientsDialog
import javax.inject.Inject


class AddNewRecipe : AppCompatActivity(), SelectIngredientsDialog.GetSelectedIngredientData {

    private lateinit var component: ActivityComponent

    lateinit var binding: ActivityAddNewRecipeBinding

    @Inject
    lateinit var addNewRecipeViewModelFactory: AddNewRecipeViewModelFactory

    var ingredients = ArrayList<Ingredient>()

    val viewModel: AddNewRecipeViewModel by viewModels { addNewRecipeViewModelFactory }


    private var ingredientNames = ArrayList<String>()
    private var ingredientMeasures = ArrayList<String>()
    private var selectedIngredients = ArrayList<Ingredient>()

    private val pickImageIntent =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == Activity.RESULT_OK) {
                val data = it.data
                val imgUri = data?.data

                binding.pickImage.setImageURI(imgUri)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initDependencyInjection()

        initData()

        binding.pickImage.setOnClickListener {
            val pickImg = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            pickImageIntent.launch(pickImg)
        }

        val areas = resources.getStringArray(R.array.area)
        val arrayAdapter = ArrayAdapter(this, R.layout.dropdown_item, areas)

        binding.area.setText(areas[0].toString())
        binding.area.setAdapter(arrayAdapter)

        val categories = resources.getStringArray(R.array.categories)
        val arrayAdapter2 = ArrayAdapter(this, R.layout.dropdown_item, categories)
        binding.category.setText(categories[0].toString())
        binding.category.setAdapter(arrayAdapter2)

        binding.ingredients.setOnClickListener {
            SelectIngredientsDialog.newInstance(
                ingredients,
                this,
                selectedIngredients,
                ingredientNames,
                ingredientMeasures
            )
                .show(supportFragmentManager, "SelectIngredientsDialog")
        }

        viewModel.getIngredients().observe(this) {
            ingredients.clear()
            ingredients.addAll(it)
        }

        binding.retry.setOnClickListener {
            initData()
        }

    }

    private fun initData() {
        viewModel.getIngredientList()
    }

    private fun initDependencyInjection() {
        component = DaggerActivityComponent.builder()
            .activityModule(ActivityModule(this))
            .applicationComponent((application as MainApplication).getComponent())
            .build()

        component.injectActivity(this)

        initViewDataBinding()
    }

    private fun initViewDataBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_new_recipe)
        binding.setVariable(BR.viewModel, viewModel)
        binding.executePendingBindings()
    }

    override fun sendIngredientsData() {
        var ing = "Ingredients : "
        for (i in 0..<selectedIngredients.size) {
            ing =
                ing + selectedIngredients[i].strIngredient + " " + selectedIngredients[i].measure + ","
        }
        binding.ingredients.setText(ing)
    }

}