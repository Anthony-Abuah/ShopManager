package com.example.myshopmanagerapp.feature_app.di

import android.content.Context
import com.example.myshopmanagerapp.feature_app.MyShopManagerApp
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideApplication(@ApplicationContext app: Context): MyShopManagerApp {
        return app as MyShopManagerApp
    }
}