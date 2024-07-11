package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.report.expense.screen

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
fun ExpenseReportNavGraph(
    navHostController: NavHostController
){
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = ExpenseReportScreens.MainExpenseReportScreen.route)
    {
        composable(route = ExpenseReportScreens.MainExpenseReportScreen.route,
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
            MainExpenseReportScreen {
                navHostController.popBackStack()
            }
        }


    }
}