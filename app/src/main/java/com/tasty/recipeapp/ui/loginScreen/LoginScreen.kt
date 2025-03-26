package com.tasty.recipeapp.ui.loginScreen

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.viewpager2.widget.ViewPager2
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.material.tabs.TabLayoutMediator
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

    private lateinit var pagerAdapter: PagerAdapter

    private val handler = Handler(Looper.getMainLooper())

    private var currentPage = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initDependencyInjection()
        initDataBinding()
        initAuthentication()

        binding.googleSignin.setOnClickListener {
            viewModel.loading.set(true)
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

        val imageUrlList = listOf(
            "https://images.unsplash.com/photo-1467003909585-2f8a72700288?q=80&w=1974&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
            "https://images.unsplash.com/photo-1540189549336-e6e99c3679fe?q=80&w=1974&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
            "https://images.unsplash.com/photo-1510629954389-c1e0da47d414?q=80&w=1974&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
            "https://images.unsplash.com/photo-1593560704563-f176a2eb61db?q=80&w=1935&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"
        )

        //initializing the adapter
        pagerAdapter = PagerAdapter(imageUrlList)

        setUpViewPager()
    }

    private fun setUpViewPager() {

        binding.viewPager.adapter = pagerAdapter

        //set the orientation of the viewpager using ViewPager2.orientation
        binding.viewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        //select any page you want as your starting page
        val currentPageIndex = 0
        binding.viewPager.currentItem = currentPageIndex


        binding.apply {

            TabLayoutMediator(intoTabLayout, viewPager) { tab, position -> }.attach() //The Magical Line

        }

        val runnable = object : Runnable {
            override fun run() {
                if (binding.viewPager.adapter?.itemCount == 0) return
                currentPage = (currentPage + 1) % (binding.viewPager.adapter?.itemCount ?: 1)
                binding.viewPager.setCurrentItem(currentPage, true)
                handler.postDelayed(this, 3000L)
            }
        }

        handler.postDelayed(runnable, 3000L)

    }

    override fun onDestroy() {
        super.onDestroy()

        // unregistering the onPageChangedCallback
        binding.viewPager.unregisterOnPageChangeCallback(
            object : ViewPager2.OnPageChangeCallback() {}
        )
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
