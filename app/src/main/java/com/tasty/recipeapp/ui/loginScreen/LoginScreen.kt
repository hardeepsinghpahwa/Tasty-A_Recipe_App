package com.tasty.recipeapp.ui.loginScreen

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.tasty.recipeapp.R
import com.tasty.recipeapp.databinding.ActivityLoginScreenBinding
import com.tasty.recipeapp.MainApplication
import com.tasty.recipeapp.di.components.ActivityComponent
import com.tasty.recipeapp.di.components.DaggerActivityComponent
import com.tasty.recipeapp.di.modules.ActivityModule
import com.tasty.recipeapp.ui.dashboardScreen.DashboardScreen
import javax.inject.Inject


class LoginScreen : AppCompatActivity() {

    lateinit var binding: ActivityLoginScreenBinding
    private val REQ_ONE_TAP = 2  // Can be any integer unique to the Activity

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
            viewModel.googleSignIn()
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

    private fun initAuthentication() {
        viewModel.checkUserLogin()
    }

    private fun initDataBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login_screen)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            REQ_ONE_TAP -> {
                Log.d("SIGNIN", "REQ_ONE_TAP")
                try {
                    val credential = oneTapClient.getSignInCredentialFromIntent(data)
                    val idToken = credential.googleIdToken
                    when {
                        idToken != null -> {
                            Log.d("SIGNIN", "Got ID token.")
                            val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
                            auth.signInWithCredential(firebaseCredential)
                                .addOnCompleteListener(this) { task ->
                                    if (task.isSuccessful) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d("SIGNIN", "signInWithCredential:success")
                                        val user = auth.currentUser

                                        if (user != null) {
                                            Toast.makeText(
                                                this,
                                                "Google sign in success",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            startActivity(Intent(this, DashboardScreen::class.java))
                                        }

                                        Log.d("SIGNIN", "User signed In")
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Toast.makeText(
                                            this,
                                            "Google sign in failed. Please try again",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        Log.w(
                                            "SIGNIN",
                                            "signInWithCredential:failure",
                                            task.exception
                                        )
                                    }
                                }
                        }

                        else -> {
                            // Shouldn't happen.
                            Log.d("SIGNIN", "No ID token!")
                        }
                    }
                } catch (e: ApiException) {
                    Log.d("SIGNIN", "Exception ${e.message}")
                    // ...
                }
            }
        }
    }


}
