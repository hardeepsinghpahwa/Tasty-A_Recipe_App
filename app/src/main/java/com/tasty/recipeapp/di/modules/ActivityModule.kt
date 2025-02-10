package com.tasty.recipeapp.di.modules

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import androidx.credentials.CredentialManager
import androidx.fragment.app.Fragment
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.tasty.recipeapp.di.ActivityScope
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable

@Module
class ActivityModule {

    private val activity: Activity

    constructor(activity: AppCompatActivity) {
        this.activity = activity
    }

    constructor(activity: Activity) {
        this.activity = activity
    }

    constructor(fragment: Fragment) {
        this.activity = fragment.requireActivity()
    }

    @Provides
    @ActivityScope
    fun activity(): Activity {
        return activity
    }


    @Provides
    @ActivityScope
    fun providesOneTapClient(activity: Activity): SignInClient {
        return Identity.getSignInClient(activity)
    }

    @Provides
    @ActivityScope
    fun compositeDisposable(): CompositeDisposable {
        return CompositeDisposable()
    }

    @Provides
    @ActivityScope
    fun providesCredentialsManager(activity: Activity): CredentialManager {
        return CredentialManager.create(activity)
    }
}