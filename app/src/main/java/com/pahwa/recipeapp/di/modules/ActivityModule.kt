package com.pahwa.recipeapp.di.modules

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.pahwa.recipeapp.di.ActivityScope
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

}