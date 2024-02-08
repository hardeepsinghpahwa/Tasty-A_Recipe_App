package com.pahwa.recipeapp.ui.introScreen

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.firebase.auth.FirebaseAuth
import com.pahwa.recipeapp.MainApplication
import com.pahwa.recipeapp.R
import com.pahwa.recipeapp.databinding.ActivityIntroScreenBinding
import com.pahwa.recipeapp.di.components.ActivityComponent
import com.pahwa.recipeapp.di.components.DaggerActivityComponent
import com.pahwa.recipeapp.di.modules.ActivityModule
import com.pahwa.recipeapp.ui.dashboardScreen.DashboardScreen
import com.pahwa.recipeapp.ui.loginScreen.LoginScreen
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

    fun checkIfSignedIn() {
        val user = firebaseAuth.currentUser
        if (user != null) {
            startActivity(Intent(this, DashboardScreen::class.java))
        }
    }


    fun initDataBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_intro_screen)
    }
}