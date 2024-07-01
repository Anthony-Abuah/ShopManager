package com.example.myshopmanagerapp.feature_app.di

import android.app.Application
import androidx.room.Room
import com.example.myshopmanagerapp.core.Constants.ShopAppDatabase
import com.example.myshopmanagerapp.feature_app.data.local.AppDatabase
import com.example.myshopmanagerapp.feature_app.data.local.Converters
import com.example.myshopmanagerapp.feature_app.data.util.GsonParser
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(app: Application): AppDatabase {
        return Room.databaseBuilder(
            app, AppDatabase::class.java, ShopAppDatabase)
            .addTypeConverter(Converters(GsonParser(GsonBuilder().serializeSpecialFloatingPointValues().create())))
            .fallbackToDestructiveMigration()
            .build()
    }

}