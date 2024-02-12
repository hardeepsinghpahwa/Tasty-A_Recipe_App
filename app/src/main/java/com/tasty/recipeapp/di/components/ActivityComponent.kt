package com.tasty.recipeapp.di.components

import com.tasty.recipeapp.di.ActivityScope
import com.tasty.recipeapp.di.modules.ActivityModule
import com.tasty.recipeapp.ui.dashboardScreen.DashboardScreen
import com.tasty.recipeapp.ui.introScreen.IntroScreen
import com.tasty.recipeapp.ui.loginScreen.LoginScreen
import com.tasty.recipeapp.ui.recipeDetails.RecipeDetailsScreen
import com.tasty.recipeapp.ui.searchScreen.SearchScreen
import dagger.Component

@ActivityScope
@Component(
    dependencies = [ApplicationComponent::class],
    modules = [ActivityModule::class]
)
interface ActivityComponent {

    fun injectActivity(loginScreen: IntroScreen)
    fun injectActivity(loginScreen: LoginScreen)
    fun injectActivity(dashboardScreen: DashboardScreen)
    fun injectActivity(searchScreen: SearchScreen)
    fun injectActivity(recipeDetailsScreen: RecipeDetailsScreen)

}