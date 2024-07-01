package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.bottom_nav

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.bottom_nav.home.screens.HomeNavGraph
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.bottom_nav.settings.screens.SettingsNavGraph
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.bottom_nav.report.screens.ReportNavGraph

@Composable
fun BottomNavGraph (){
    val mainNavController = rememberNavController()
    NavHost(
        navController = mainNavController,
        startDestination = BottomNavScreens.Home.route)
    {
        composable(route = BottomNavScreens.Home.route){
            HomeNavGraph(navHostController = mainNavController)
        }
        composable(route = BottomNavScreens.Report.route){
            ReportNavGraph(navHostController = mainNavController)
        }
        composable(route = BottomNavScreens.Settings.route){
            SettingsNavGraph(navHostController = mainNavController)
        }
    }
}

