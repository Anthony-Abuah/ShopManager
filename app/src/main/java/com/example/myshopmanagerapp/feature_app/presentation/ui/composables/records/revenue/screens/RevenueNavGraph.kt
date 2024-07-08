package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.records.revenue.screens

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
import com.example.myshopmanagerapp.feature_app.presentation.view_models.RevenueViewModel
import com.example.myshopmanagerapp.feature_app.presentation.view_models.sharedViewModel

@Composable
fun RevenueNavGraph(
    navHostController: NavHostController
){
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = RevenueScreens.RevenueListScreen.route)
    {
        composable(route = RevenueScreens.RevenueListScreen.route){
            val revenueViewModel = it.sharedViewModel<RevenueViewModel>(navHostController = navController)
            RevenueListScreen(
                revenueViewModel = revenueViewModel,
                navigateToAddRevenueScreen = {navController.navigate(RevenueScreens.AddRevenueScreen.route)},
                navigateToViewRevenueScreen = {uniqueRevenueId->
                    navController.navigate(RevenueScreens.ViewRevenueScreen.withArgs(uniqueRevenueId))}
            ) {
                navHostController.popBackStack()
            }
        }

        composable(route = RevenueScreens.AddRevenueScreen.route){
            AddRevenueScreen { navController.popBackStack() }
        }

        composable(route = RevenueScreens.ViewRevenueScreen.route + "/{uniqueRevenueId}",
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
                navArgument("uniqueRevenueId") {
                    type = NavType.StringType
                    defaultValue = Constants.emptyString
                    nullable = false
                }
            )){
            val uniqueRevenueId = it.arguments?.getString("uniqueRevenueId")
            val revenueViewModel = it.sharedViewModel<RevenueViewModel>(navHostController = navController)
            if (uniqueRevenueId != null) {
                ViewRevenueScreen(revenueViewModel = revenueViewModel, uniqueRevenueId = uniqueRevenueId) {
                    navController.popBackStack()
                }
            }
        }
    }
}