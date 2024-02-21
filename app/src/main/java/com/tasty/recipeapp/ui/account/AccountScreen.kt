package com.tasty.recipeapp.ui.account

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.Observable
import androidx.databinding.Observable.OnPropertyChangedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.tasty.recipeapp.BR
import com.tasty.recipeapp.MainApplication
import com.tasty.recipeapp.R
import com.tasty.recipeapp.databinding.FragmentAccountScreenBinding
import com.tasty.recipeapp.di.components.ActivityComponent
import com.tasty.recipeapp.di.components.DaggerActivityComponent
import com.tasty.recipeapp.di.modules.ActivityModule
import com.tasty.recipeapp.ui.introScreen.IntroScreen
import javax.inject.Inject

class AccountScreen : Fragment(), AccountUtil {

    lateinit var component: ActivityComponent
    lateinit var binding: FragmentAccountScreenBinding

    @Inject
    lateinit var accountViewModelFactory: AccountViewModelFactory
    val viewModel: AccountViewModel by viewModels { accountViewModelFactory }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.setVariable(BR.viewModel, viewModel)
        binding.executePendingBindings()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_account_screen, container, false)

        initDependencyInjection()
        init()


        initDependencyInjection()
        viewModel.initAccountDetails()
        viewModel.setUtils(this)
        viewModel.image.observe(viewLifecycleOwner) {
            Glide.with(this)
                .load(it)
                .into(binding.profilePic)
        }


        binding.edit.setOnClickListener {
            viewModel.edit.set(true)
        }

        binding.save.setOnClickListener {
            if (validations()) {
                viewModel.saveDetails()
            }
        }

        binding.logout.setOnClickListener {
            viewModel.logout()
        }

        return binding.root
    }

    private fun init() {

        viewModel.cuisine.addOnPropertyChangedCallback(object : OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                binding.cuisineLayout.isErrorEnabled = false
            }

        })

        viewModel.food.addOnPropertyChangedCallback(object : OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                binding.foodLayout.isErrorEnabled = false
            }

        })


        viewModel.restaurant.addOnPropertyChangedCallback(object : OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                binding.restaurantLayout.isErrorEnabled = false
            }

        })


        viewModel.chef.addOnPropertyChangedCallback(object : OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                binding.chefLayout.isErrorEnabled = false
            }

        })

        viewModel.getDetails()
    }

    private fun validations(): Boolean {
        if (viewModel.cuisine.get().isNullOrEmpty()) {
            binding.cuisineLayout.error = "Enter cuisine name"
            binding.cuisineLayout.requestFocus()
            return false
        }

        if (viewModel.food.get().isNullOrEmpty()) {
            binding.foodLayout.error = "Enter food name"
            binding.foodLayout.requestFocus()
            return false
        }


        if (viewModel.chef.get().isNullOrEmpty()) {
            binding.chefLayout.error = "Enter chef name"
            binding.chefLayout.requestFocus()
            return false
        }


        if (viewModel.restaurant.get().isNullOrEmpty()) {
            binding.restaurantLayout.error = "Enter restaurant name"
            binding.restaurantLayout.requestFocus()
            return false
        }

        return true
    }

    private fun initDependencyInjection() {
        component = DaggerActivityComponent.builder()
            .activityModule(ActivityModule(this))
            .applicationComponent((requireActivity().application as MainApplication).getComponent())
            .build()

        component.injectFragment(this)
    }

    override fun openIntroScreen() {
        requireActivity().finish()
        startActivity(Intent(requireActivity(), IntroScreen::class.java))
    }
}