package com.tasty.recipeapp.ui.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.tasty.recipeapp.R
import com.tasty.recipeapp.databinding.DialogAddMeasureBinding
import com.tasty.recipeapp.databinding.DialogSelectIngredientsBinding
import com.tasty.recipeapp.model.response.Ingredient
import com.tasty.recipeapp.ui.addNewRecipe.adapters.IngredientAdapter
import com.tasty.recipeapp.ui.addNewRecipe.adapters.IngredientClickListener

class SelectIngredientsDialog : DialogFragment(), IngredientClickListener {


    lateinit var binding: DialogSelectIngredientsBinding
    val adapter = IngredientAdapter()
    private val adapter2 = IngredientAdapter()


    interface GetSelectedIngredientData {
        fun sendIngredientsData(
        )
    }

    internal companion object {
        private var ingredientNames = ArrayList<String>()
        private var ingredientMeasures = ArrayList<String>()
        private var selectedIngredients = ArrayList<Ingredient>()
        lateinit var ingredients: ArrayList<Ingredient>
        private lateinit var getSelectedIngredientData: GetSelectedIngredientData
        fun newInstance(
            ingredients: ArrayList<Ingredient>,
            getSelectedIngredientData: GetSelectedIngredientData,
            selectedIngredients: ArrayList<Ingredient>,
            ingredientNames: ArrayList<String>,
            ingredientMeasures: ArrayList<String>

        ): SelectIngredientsDialog {
            this.ingredients = ingredients
            this.getSelectedIngredientData = getSelectedIngredientData
            this.ingredientNames = ingredientNames
            this.selectedIngredients = selectedIngredients
            this.ingredientMeasures = ingredientMeasures
            return SelectIngredientsDialog()
        }
    }

    override fun onDetach() {
        super.onDetach()
        getSelectedIngredientData.sendIngredientsData()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)


        binding = DataBindingUtil.inflate(
            LayoutInflater.from(
                context
            ), R.layout.dialog_select_ingredients, null, false
        )


        adapter.updateList(ingredients)
        adapter.setClickListener(this)
        binding.ingredients.adapter = adapter
        binding.ingredients.layoutManager = LinearLayoutManager(requireContext())


        adapter2.setClickListener(object : IngredientClickListener {
            override fun onIngredientClick(ingredient: Ingredient) {

            }

            override fun onIngredientDelete(position: Int) {
                ingredientNames.removeAt(position)
                ingredientMeasures.removeAt(position)
                selectedIngredients.removeAt(position)
                adapter2.updateList(selectedIngredients)
            }

        })
        adapter2.setSelected(true)
        binding.ingredientsNames.adapter = adapter2
        binding.ingredientsNames.layoutManager = LinearLayoutManager(requireContext())
        adapter2.updateList(selectedIngredients)
        binding.ingredientsNames.adapter = adapter2

        binding.close.setOnClickListener {
            dialog?.dismiss()
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
    }

    override fun onIngredientClick(ingredient: Ingredient) {
        val builder = AlertDialog.Builder(requireContext(), R.style.AlertDialogCustom)
        //builder.setTitle("Enter Quantity/Measure");

        // set the custom layout

        val customLayout = DialogAddMeasureBinding.inflate(layoutInflater)
        builder.setView(customLayout.root);



        builder.setPositiveButton(
            "Add Ingredient"
        ) { p0, p1 ->

            if (!ingredientNames.contains(ingredient.strIngredient)) {
                ingredientNames.add(ingredient.strIngredient)
                ingredientMeasures.add(customLayout.name.text.toString())

                ingredient.measure = customLayout.name.text.toString()
                selectedIngredients.add(ingredient)
                adapter2.updateList(selectedIngredients)
            } else {
                Toast.makeText(requireContext(), "Ingredient Already Added", Toast.LENGTH_SHORT)
                    .show()
            }

        }

        val dialog1 = builder.create();
        dialog1.show();

        dialog1.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )

    }

    override fun onIngredientDelete(position: Int) {

    }
}