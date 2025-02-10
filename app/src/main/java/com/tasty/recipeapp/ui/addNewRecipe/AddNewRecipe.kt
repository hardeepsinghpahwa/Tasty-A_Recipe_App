package com.tasty.recipeapp.ui.addNewRecipe

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.Observable
import androidx.databinding.Observable.OnPropertyChangedCallback
import com.tasty.recipeapp.BR
import com.tasty.recipeapp.MainApplication
import com.tasty.recipeapp.R
import com.tasty.recipeapp.databinding.ActivityAddNewRecipeBinding
import com.tasty.recipeapp.di.components.ActivityComponent
import com.tasty.recipeapp.di.components.DaggerActivityComponent
import com.tasty.recipeapp.di.modules.ActivityModule
import com.tasty.recipeapp.model.response.Ingredient
import com.tasty.recipeapp.ui.dialogs.SelectIngredientsDialog
import com.tasty.recipeapp.utils.CommonUtils
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

                viewModel.uri=imgUri

                binding.pickImage.setImageURI(imgUri)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initDependencyInjection()
        initData()

        binding.back.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.ratingBar2.setOnRatingBarChangeListener { _, fl, b ->
            viewModel.rating.set(fl.toInt())
        }

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

        viewModel.getIngredient().observe(this) {
            ingredients.clear()
            ingredients.addAll(it)
        }

        binding.retry.setOnClickListener {
            initData()
        }

        binding.saveRecipe.setOnClickListener {
            if (checkValidations()) {
                viewModel.addRecipeCall()
            }
        }

        addListeners()

        viewModel.getSuccessMessage().observe(this) {
            //CommonUtils.showSuccessSnackBar(it, binding.root)
            Toast.makeText(applicationContext, it, Toast.LENGTH_SHORT).show()
            finish()
        }
        viewModel.getFailureMessage().observe(this) {
            CommonUtils.showFailureSnackBar(it, binding.root)
        }
    }

    private fun addListeners() {
        viewModel.area.addOnPropertyChangedCallback(object : OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                binding.areaLayout.isErrorEnabled = false
            }

        })

        viewModel.category.addOnPropertyChangedCallback(object : OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                binding.categoryLayout.isErrorEnabled = false
            }

        })


        viewModel.name.addOnPropertyChangedCallback(object : OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                binding.nameLayout.isErrorEnabled = false
            }

        })


        viewModel.instructions.addOnPropertyChangedCallback(object : OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                binding.instructionLayout.isErrorEnabled = false
            }

        })

        viewModel.ytLink.addOnPropertyChangedCallback(object : OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                binding.ytLinkLayout.isErrorEnabled = false
            }

        })

        viewModel.time.addOnPropertyChangedCallback(object : OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                binding.timeLayout.isErrorEnabled = false
            }

        })


    }

    private fun checkValidations(): Boolean {
        if (viewModel.name.get()!!.isEmpty()) {
            binding.nameLayout.error = "Enter valid name"
            binding.nameLayout.requestFocus()
            return false
        }

        if (viewModel.area.get()!!.isEmpty() || viewModel.area.get() == "Select Area") {
            binding.areaLayout.error = "Select an area"
            binding.nameLayout.requestFocus()
            return false
        }


        if (viewModel.category.get()!!.isEmpty() || viewModel.category.get() == "Select Category") {
            binding.categoryLayout.error = "Select an area"
            binding.categoryLayout.requestFocus()
            return false
        }


        if (viewModel.selectedIngredients.isEmpty()) {
            binding.ingredientLayout.error = "Select some ingredients"
            binding.ingredientLayout.requestFocus()
            return false
        }

        if (viewModel.instructions.get()!!.isEmpty()) {
            binding.instructionLayout.error = "Enter valid name"
            binding.instructionLayout.requestFocus()
            return false
        }

        if (viewModel.ytLink.get()!!.isNotEmpty() && !isYoutubeUrl(viewModel.ytLink.get()!!)) {
            binding.ytLinkLayout.error = "Enter valid youtube url"
            binding.ytLinkLayout.requestFocus()
            return false
        }

        if (viewModel.time.get()!!.isEmpty()) {
            binding.timeLayout.error = "Enter time"
            binding.timeLayout.requestFocus()
            return false
        }

        if (viewModel.rating.get() == 0) {
            Toast.makeText(applicationContext, "Select a rating", Toast.LENGTH_SHORT).show()
            return false
        }


        return true
    }

    private fun isYoutubeUrl(youTubeURl: String): Boolean {
        val success: Boolean
        val pattern = "^(http(s)?:\\/\\/)?((w){3}.)?youtu(be|.be)?(\\.com)?\\/.+"
        success = // Not Valid youtube URL
            youTubeURl.isNotEmpty() && youTubeURl.matches(pattern.toRegex())
        return success
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
                ing + selectedIngredients[i].measure + " " + selectedIngredients[i].strIngredient + ","
        }
        binding.ingredients.setText(ing)
        viewModel.selectedIngredients.clear()
        viewModel.selectedIngredients.addAll(selectedIngredients)
    }

}