package com.tasty.recipeapp.di.modules

import android.os.Build
import androidx.annotation.RequiresApi
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.tasty.recipeapp.MainApplication
import com.tasty.recipeapp.api.ApiService
import com.tasty.recipeapp.di.ApplicationScope
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.time.Duration

@Module
class ApplicationModule {

    lateinit var application: MainApplication

    constructor(application: MainApplication) {
        this.application = application
    }

    @Provides
    @ApplicationScope
    fun providesFirebaseAuth(): FirebaseAuth {
        return Firebase.auth
    }

    @Provides
    @ApplicationScope
    fun providesFirebaseFirestore(): FirebaseFirestore {
        return Firebase.firestore
    }

    @Provides
    @ApplicationScope
    fun providesApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Provides
    @ApplicationScope
    fun providesRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://www.themealdb.com/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @Provides
    @ApplicationScope
    fun providesOkHttpClient(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        return OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .writeTimeout(Duration.ofSeconds(30))
            .connectTimeout(Duration.ofSeconds(30))
            .readTimeout(Duration.ofSeconds(30))
            .build()
    }
}