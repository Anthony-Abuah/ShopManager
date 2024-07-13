package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.report.general.screens

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
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.report.inventory.screens.ViewInventoryItemsReportScreen

@Composable
fun GeneralReportNavGraph(
    navHostController: NavHostController
){
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = GeneralReportScreens.GeneralReportScreen.route)
    {
        composable(route = GeneralReportScreens.GeneralReportScreen.route,
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
            }
        ){
            GeneralReportScreen (
                navigateToViewBankAccountsScreen = {
                    navController.navigate(GeneralReportScreens.BankAccountsReportScreen.route)
                },
                navigateToViewInventoryItemsScreen = {_periodJson->
                    navController.navigate(GeneralReportScreens.InventoryItemsReportScreen.withArgs(_periodJson))
                },
                navigateToViewOwingCustomersScreen = {
                    navController.navigate(GeneralReportScreens.OwingCustomersReportScreen.route)
                },
                navigateToViewPersonnelScreen = {
                    navController.navigate(GeneralReportScreens.PersonnelReportScreen.route)
                },
            ){
                navHostController.popBackStack()
            }
        }
        composable(route = GeneralReportScreens.InventoryItemsReportScreen.route + "/{period}",
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
        composable(route = GeneralReportScreens.OwingCustomersReportScreen.route,
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
            }
        ){
            OwingCustomersReportScreen {
                navController.popBackStack()
            }
        }
        composable(route = GeneralReportScreens.BankAccountsReportScreen.route,
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
            }
        ){
            BankAccountsReportScreen {
                navController.popBackStack()
            }
        }
        composable(route = GeneralReportScreens.PersonnelReportScreen.route,
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
            }
        ){
            PersonnelReportScreen {
                navController.popBackStack()
            }
        }
    }
}