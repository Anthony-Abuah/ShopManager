package com.example.myshopmanagerapp.feature_app

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyShopManagerApp: Application(){
    init {
        instance = this
    }
    companion object{
        private var instance : MyShopManagerApp? = null
        fun applicationContext(): Context{
            return requireNotNull(instance).applicationContext
        }
    }
}