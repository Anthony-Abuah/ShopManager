package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.report.inventory.screens

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.myshopmanagerapp.core.Constants

@Composable
fun InventoryReportNavGraph(
    navHostController: NavHostController
){
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = InventoryReportScreens.MainInventoryReportScreen.route)
    {
        composable(route = InventoryReportScreens.MainInventoryReportScreen.route,
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { 500 },
                    animationSpec = tween(
                        durationMillis = 500,
                        easing = LinearOutSlowInEasing
                    )
                ) + fadeIn(animationSpec = tween(500))
            },
            popExitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { -500 },
                    animationSpec = tween(
                        durationMillis = 500,
                        easing = LinearOutSlowInEasing
                    )
                ) + fadeOut(animationSpec = tween(500))
            }){
            InventoryAndStockReportScreen (
                navigateToViewInventoryItems = {period->
                    navController.navigate(InventoryReportScreens.ViewInventoryItemReportScreen.withArgs(period))
                }
            ){
                navHostController.popBackStack()
            }
        }
        composable(route = InventoryReportScreens.ViewInventoryItemReportScreen.route + "/{period}",
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { 500 },
                    animationSpec = tween(
                        durationMillis = 500,
                        easing = LinearOutSlowInEasing
                    )
                ) + fadeIn(animationSpec = tween(500))
            },
            popExitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { -500 },
                    animationSpec = tween(
                        durationMillis = 500,
                        easing = LinearOutSlowInEasing
                    )
                ) + fadeOut(animationSpec = tween(500))
            },
            arguments = listOf(
                navArgument("period") {
                    type = NavType.StringType
                    defaultValue = Constants.emptyString
                    nullable = false
                })
        ){entry->
            val periodJson = entry.arguments?.getString("period")
            if (periodJson != null) {
                ViewInventoryItemsReportScreen(periodJson = periodJson) {
                    navController.popBackStack()
                }
            }
        }
    }
}