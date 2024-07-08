package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.bottom_nav

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.bottom_nav.records.screens.HomeNavGraph
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.bottom_nav.actions.screens.SettingsNavGraph
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.bottom_nav.report.screens.ReportNavGraph

@Composable
fun BottomNavGraph (){
    val mainNavController = rememberNavController()
    NavHost(
        navController = mainNavController,
        startDestination = BottomNavScreens.Records.route)
    {
        composable(route = BottomNavScreens.Records.route){
            HomeNavGraph(navHostController = mainNavController)
        }
        composable(route = BottomNavScreens.Report.route){
            ReportNavGraph(navHostController = mainNavController)
        }
        composable(route = BottomNavScreens.Actions.route){
            SettingsNavGraph(navHostController = mainNavController)
        }
    }
}

