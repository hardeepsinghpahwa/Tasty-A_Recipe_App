package com.tasty.recipeapp.ui.dashboardScreen

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging
import com.tasty.recipeapp.BR
import com.tasty.recipeapp.MainApplication
import com.tasty.recipeapp.R
import com.tasty.recipeapp.databinding.FragmentDashboardScreenBinding
import com.tasty.recipeapp.di.components.ActivityComponent
import com.tasty.recipeapp.di.components.DaggerActivityComponent
import com.tasty.recipeapp.di.modules.ActivityModule
import com.tasty.recipeapp.ui.dashboardScreen.adapters.CategoriesAdapter
import com.tasty.recipeapp.ui.dashboardScreen.adapters.CategoryClickListener
import com.tasty.recipeapp.ui.dashboardScreen.adapters.NewRecipeClickListener
import com.tasty.recipeapp.ui.dashboardScreen.adapters.NewRecipesAdapter
import com.tasty.recipeapp.ui.dashboardScreen.adapters.ShowCaseOnClickListener
import com.tasty.recipeapp.ui.dashboardScreen.adapters.ShowcaseAdapter
import com.tasty.recipeapp.ui.notifications.Notifications
import com.tasty.recipeapp.ui.recipeDetails.RecipeDetailsScreen
import com.tasty.recipeapp.ui.searchScreen.SearchScreen
import javax.inject.Inject


class DashboardScreen : Fragment(), ShowCaseOnClickListener, CategoryClickListener,
    NewRecipeClickListener {

    private lateinit var binding: FragmentDashboardScreenBinding

    private lateinit var component: ActivityComponent

    @Inject
    lateinit var categoriesAdapter: CategoriesAdapter

    @Inject
    lateinit var showcaseAdapter: ShowcaseAdapter

    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    @Inject
    lateinit var newRecipesAdapter: NewRecipesAdapter

    @Inject
    lateinit var dashboardViewModelFactory: DashboardViewModelFactory

    private val viewModel: DashboardViewModel by viewModels { dashboardViewModelFactory }


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
            DataBindingUtil.inflate(inflater, R.layout.fragment_dashboard_screen, container, false)

        initDependencyInjection()
        initRecyclerViews()
        setProfilePhoto()

        viewModel.getRecipes().observe(viewLifecycleOwner) {
            if (it != null) {
                showcaseAdapter.updateList(it)
            }
        }

        viewModel.getCategories().observe(viewLifecycleOwner) {
            categoriesAdapter.updateList(it)
        }

        binding.retry.setOnClickListener {
            initData()
        }

        binding.search.setOnClickListener {
            startActivity(Intent(activity, SearchScreen::class.java))
        }

        viewModel.getNewRecipes().observe(viewLifecycleOwner) {
            newRecipesAdapter.updateList(it)
        }

        askNotificationPermission()
        checkFirebaseToken()

        binding.notifications.setOnClickListener {
            startActivity(Intent(activity, Notifications::class.java))
        }


        return binding.root
    }

    private fun setProfilePhoto() {
        Glide.with(this)
            .load(firebaseAuth.currentUser?.photoUrl.toString())
            .into(
                binding.profilePic
            )
    }

    private fun initRecyclerViews() {
        binding.categories.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        binding.categories.adapter = categoriesAdapter

        categoriesAdapter.setClickListener(this)
        showcaseAdapter.setClickListener(this)
        newRecipesAdapter.setNewRecipeClickListener(this)
        binding.showcase.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        binding.showcase.adapter = showcaseAdapter

        binding.newRecipes.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        binding.newRecipes.adapter = newRecipesAdapter

    }

    private fun initData() {
        viewModel.getCategoriesData()
        if (categoriesAdapter.getCategory() != 0) {
            categoriesAdapter
            viewModel.getRecipeData(categoriesAdapter.getCategoryName())
        } else {
            viewModel.getRecipeData("Vegetarian")
        }
        viewModel.getNewRecipeData()
    }

    private fun checkFirebaseToken() {
        if (firebaseAuth.currentUser != null) {
            FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    return@OnCompleteListener
                }

                // Get new FCM registration token
                val token = task.result

                if (token != null) {
                    viewModel.saveFirebaseToken(firebaseAuth.currentUser!!.uid, token)
                }
            })
        }
    }

    override fun onResume() {
        super.onResume()
        initData()
    }

    private fun initDependencyInjection() {
        component = DaggerActivityComponent.builder()
            .activityModule(ActivityModule(this))
            .applicationComponent((requireActivity().application as MainApplication).getComponent())
            .build()

        component.injectFragment(this)
    }

    override fun onShowCaseClick(mealId: String) {

        val intent = (Intent(activity, RecipeDetailsScreen::class.java))
        intent.putExtra("id", mealId)
        startActivity(intent)
    }

    override fun onCategoryClick(categoryName: String) {
        viewModel.getRecipeData(categoryName)
    }

    override fun newRecipeClick(mealId: String) {
        val intent = (Intent(activity, RecipeDetailsScreen::class.java))
        intent.putExtra("id", mealId)
        intent.putExtra("firebase", "true")
        startActivity(intent)
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { isGranted: Boolean ->
        if (isGranted) {
            // FCM SDK (and your app) can post notifications.
        } else {
            // TODO: Inform user that that your app will not show notifications.
        }
    }

    private fun askNotificationPermission() {
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.POST_NOTIFICATIONS
                ) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                // FCM SDK (and your app) can post notifications.
            } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                // TODO: display an educational UI explaining to the user the features that will be enabled
                //       by them granting the POST_NOTIFICATION permission. This UI should provide the user
                //       "OK" and "No thanks" buttons. If the user selects "OK," directly request the permission.
                //       If the user selects "No thanks," allow the user to continue without notifications.
            } else {
                // Directly ask for the permission
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

}