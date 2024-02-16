package com.tasty.recipeapp.ui.introScreen

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.firebase.auth.FirebaseAuth
import com.tasty.recipeapp.MainApplication
import com.tasty.recipeapp.R
import com.tasty.recipeapp.databinding.ActivityIntroScreenBinding
import com.tasty.recipeapp.di.components.ActivityComponent
import com.tasty.recipeapp.di.components.DaggerActivityComponent
import com.tasty.recipeapp.di.modules.ActivityModule
import com.tasty.recipeapp.ui.dashboardScreen.DashboardScreen
import com.tasty.recipeapp.ui.loginScreen.LoginScreen
import javax.inject.Inject

class IntroScreen : AppCompatActivity() {

    private lateinit var binding: ActivityIntroScreenBinding

    @Inject
    lateinit var firebaseAuth: FirebaseAuth
    private lateinit var component: ActivityComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initDataBinding()
        initDependencyInjection()

        checkIfSignedIn()

        binding.next.setOnClickListener {
            startActivity(Intent(this, LoginScreen::class.java))
            finish()
        }

    }

    @SuppressLint("SuspiciousIndentation")
    private fun initDependencyInjection() {
        component = DaggerActivityComponent.builder()
            .activityModule(ActivityModule(this))
            .applicationComponent(
                (application as MainApplication).getComponent()
            ).build()

        component.injectActivity(this)
    }

    private fun checkIfSignedIn() {
        val user = firebaseAuth.currentUser
        if (user != null) {
            startActivity(Intent(this, DashboardScreen::class.java))
            finish()
        }
    }


    private fun initDataBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_intro_screen)
    }
}