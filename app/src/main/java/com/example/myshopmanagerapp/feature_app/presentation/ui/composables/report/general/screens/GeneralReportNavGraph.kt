package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.report.general.screens

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

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
                navigateToViewInventoryItemsScreen = {
                    navController.navigate(GeneralReportScreens.InventoryItemsReportScreen.route)
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
        composable(route = GeneralReportScreens.InventoryItemsReportScreen.route,
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
            InventoryItemsReportScreen {
                navHostController.popBackStack()
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
                navHostController.popBackStack()
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
                navHostController.popBackStack()
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
                navHostController.popBackStack()
            }
        }
    }
}