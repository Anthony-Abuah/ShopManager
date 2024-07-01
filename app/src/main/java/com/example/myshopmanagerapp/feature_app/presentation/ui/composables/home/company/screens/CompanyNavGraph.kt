package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.home.company.screens

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
fun CompanyNavGraph(
    navHostController: NavHostController
){
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = CompanyScreens.CompanyListScreen.route)
    {
        composable(route = CompanyScreens.CompanyListScreen.route,
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { -500 },
                    animationSpec = tween(
                        durationMillis = 500,
                        easing = LinearOutSlowInEasing
                    )
                ) + fadeOut(animationSpec = tween(500))
            },
            popEnterTransition = {
                slideInHorizontally(
                    initialOffsetX = { 500 },
                    animationSpec = tween(
                        durationMillis = 500,
                        easing = LinearOutSlowInEasing
                    )
                ) + fadeIn(animationSpec = tween(500))
            }
        ){
            CompanyListScreen(
                navigateToAddCompanyScreen = {navController.navigate(CompanyScreens.AddCompanyScreen.route)},
                navigateToViewCompanyScreen = {_uniqueCompanyId->
                    navController.navigate(CompanyScreens.ViewCompanyScreen.withArgs(_uniqueCompanyId))}
            ) {
                navHostController.popBackStack()
            }
        }

        composable(route = CompanyScreens.AddCompanyScreen.route,
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { -500 },
                    animationSpec = tween(
                        durationMillis = 500,
                        easing = LinearOutSlowInEasing
                    )
                ) + fadeOut(animationSpec = tween(500))
            },
            popEnterTransition = {
                slideInHorizontally(
                    initialOffsetX = { 500 },
                    animationSpec = tween(
                        durationMillis = 500,
                        easing = LinearOutSlowInEasing
                    )
                ) + fadeIn(animationSpec = tween(500))
            }
        ){
            AddCompanyScreen {
                navController.popBackStack()
            }
        }

        composable(route = CompanyScreens.ViewCompanyScreen.route + "/{uniqueCompanyId}",
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { -500 },
                    animationSpec = tween(
                        durationMillis = 500,
                        easing = LinearOutSlowInEasing
                    )
                ) + fadeOut(animationSpec = tween(500))
            },
            popEnterTransition = {
                slideInHorizontally(
                    initialOffsetX = { 500 },
                    animationSpec = tween(
                        durationMillis = 500,
                        easing = LinearOutSlowInEasing
                    )
                ) + fadeIn(animationSpec = tween(500))
            },
            arguments = listOf(
                navArgument("uniqueCompanyId") {
                    type = NavType.StringType
                    defaultValue = Constants.emptyString
                    nullable = false
                }
            )
        ){
            val uniqueCompanyId = it.arguments?.getString("uniqueCompanyId")
            if (uniqueCompanyId != null) {
                ViewCompanyScreen(
                    uniqueCompanyId = uniqueCompanyId
                ) {
                    navController.popBackStack()
                }
            }
        }
    }
}