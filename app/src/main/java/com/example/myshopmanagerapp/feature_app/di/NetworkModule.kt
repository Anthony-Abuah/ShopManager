package com.example.myshopmanagerapp.feature_app.di

import com.example.myshopmanagerapp.core.Routes.BASE_URL
import com.example.myshopmanagerapp.feature_app.data.remote.ShopManagerDatabaseApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideShopManagerDatabaseApi(): ShopManagerDatabaseApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ShopManagerDatabaseApi::class.java)
    }

}