package com.pahwa.recipeapp.di.components

import com.google.firebase.auth.FirebaseAuth
import com.pahwa.recipeapp.MainApplication
import com.pahwa.recipeapp.api.ApiService
import com.pahwa.recipeapp.di.ApplicationScope
import com.pahwa.recipeapp.di.modules.ApplicationModule
import dagger.Component

@ApplicationScope
@Component(
    modules = [ApplicationModule::class]
)
interface ApplicationComponent {

    fun injectMainApplication(application: MainApplication)

    fun providesFirebaseAuth():FirebaseAuth

    fun providesApiRetrofit():ApiService

}