package com.pahwa.recipeapp.di.components

import com.pahwa.recipeapp.di.ActivityScope
import com.pahwa.recipeapp.di.modules.ActivityModule
import com.pahwa.recipeapp.ui.dashboardScreen.DashboardScreen
import com.pahwa.recipeapp.ui.introScreen.IntroScreen
import com.pahwa.recipeapp.ui.loginScreen.LoginScreen
import com.pahwa.recipeapp.ui.recipeDetails.RecipeDetailsScreen
import com.pahwa.recipeapp.ui.searchScreen.SearchScreen
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