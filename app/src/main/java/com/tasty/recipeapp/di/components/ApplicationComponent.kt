package com.tasty.recipeapp.di.components

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.tasty.recipeapp.MainApplication
import com.tasty.recipeapp.api.ApiService
import com.tasty.recipeapp.di.ApplicationScope
import com.tasty.recipeapp.di.modules.ApplicationModule
import dagger.Component

@ApplicationScope
@Component(
    modules = [ApplicationModule::class]
)
interface ApplicationComponent {

    fun injectMainApplication(application: MainApplication)

    fun providesFirebaseAuth():FirebaseAuth

    fun providesApiRetrofit():ApiService

    fun providesFirebaseFirestore():FirebaseFirestore

}