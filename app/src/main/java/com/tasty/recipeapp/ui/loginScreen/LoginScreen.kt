package com.tasty.recipeapp.ui.loginScreen

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.FirebaseAuth
import com.tasty.recipeapp.MainApplication
import com.tasty.recipeapp.R
import com.tasty.recipeapp.databinding.ActivityLoginScreenBinding
import com.tasty.recipeapp.di.components.ActivityComponent
import com.tasty.recipeapp.di.components.DaggerActivityComponent
import com.tasty.recipeapp.di.modules.ActivityModule
import com.tasty.recipeapp.ui.main.MainScreen
import com.tasty.recipeapp.utils.CommonUtils
import javax.inject.Inject


class LoginScreen : AppCompatActivity() {

    lateinit var binding: ActivityLoginScreenBinding

    @Inject
    lateinit var oneTapClient: SignInClient

    @Inject
    lateinit var auth: FirebaseAuth

    private lateinit var component: ActivityComponent

    @Inject
    lateinit var viewModelFactory: LoginViewModelFactory

    private val viewModel: LoginViewModel by viewModels { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initDependencyInjection()
        initDataBinding()
        initAuthentication()

        binding.googleSignin.setOnClickListener {
            viewModel.signIn(this)
        }

        viewModel.getSuccessMessage().observe(this) {
            CommonUtils.showSuccessSnackBar(it, binding.root)
        }

        viewModel.getFailureMessage().observe(this) {
            CommonUtils.showFailureSnackBar(it, binding.root)
        }

        viewModel.getLoggedIn().observe(this) {
            if (it) {
                finish()
                startActivity(Intent(this, MainScreen::class.java))
            }
        }
    }

    @SuppressLint("SuspiciousIndentation")
    private fun initDependencyInjection() {
        component = DaggerActivityComponent.builder().activityModule(ActivityModule(this))
            .applicationComponent(
                (application as MainApplication).getComponent()
            ).build()

        component.injectActivity(this)
    }

    private fun initAuthentication() {
        viewModel.checkUserLogin()
    }

    private fun initDataBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login_screen)
    }

}
