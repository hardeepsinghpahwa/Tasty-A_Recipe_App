package com.tasty.recipeapp.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.tasty.recipeapp.R
import com.tasty.recipeapp.databinding.ActivityMainScreenBinding
import com.tasty.recipeapp.ui.addNewRecipe.AddNewRecipe

class MainScreen : AppCompatActivity() {

    lateinit var binding: ActivityMainScreenBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initDataBinding()

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.main) as NavHostFragment

        navController = navHostFragment.navController

        setupWithNavController(binding.bottomNavigationView, navController)


        binding.addNew.setOnClickListener {
            startActivity(Intent(this, AddNewRecipe::class.java))
        }
    }

    private fun initDataBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main_screen)
        binding.executePendingBindings()
    }
}