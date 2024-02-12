package com.tasty.recipeapp

import android.app.Activity
import android.app.Application
import com.tasty.recipeapp.di.components.ApplicationComponent
import com.tasty.recipeapp.di.components.DaggerApplicationComponent
import com.tasty.recipeapp.di.modules.ApplicationModule

class MainApplication : Application() {

    lateinit var applicationComponent: ApplicationComponent

    override fun onCreate() {
        super.onCreate()

        applicationComponent = DaggerApplicationComponent.builder()
            .applicationModule(ApplicationModule(this))
            .build()
        applicationComponent.injectMainApplication(this)
    }

    companion object {
        private lateinit var application: MainApplication

        operator fun get(activity: Activity): MainApplication {
            val app = activity.application
            return app as MainApplication
        }

        fun getAppApplication(): MainApplication {
            return application
        }
    }

    fun getComponent(): ApplicationComponent {
        if (!::applicationComponent.isInitialized) {
            applicationComponent = DaggerApplicationComponent.builder()
                .build()
            applicationComponent.injectMainApplication(this)
        }
        return applicationComponent
    }
}