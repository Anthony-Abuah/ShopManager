package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.records.bank_account.screens

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
fun BankAccountNavGraph(
    navHostController: NavHostController
){
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = BankScreens.BankListScreen.route)
    {
        composable(route = BankScreens.BankListScreen.route,
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
            BankAccountListScreen(
                navigateToAddBankScreen = {navController.navigate(BankScreens.AddBankScreen.route)},
                navigateToViewBankScreen = {uniqueBankId->
                    navController.navigate(BankScreens.ViewBankScreen.withArgs(uniqueBankId))}
            ) {
                navHostController.popBackStack()
            }
        }

        composable(route = BankScreens.AddBankScreen.route,
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
            AddBankAccountScreen {
                navController.popBackStack()
            }
        }

        composable(route = BankScreens.ViewBankScreen.route + "/{uniqueBankId}",
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
                navArgument("uniqueBankId") {
                    type = NavType.StringType
                    defaultValue = Constants.emptyString
                    nullable = false
                }
            )
        ){entry->
            val uniqueBankId = entry.arguments?.getString("uniqueBankId")
            if (uniqueBankId != null) {
                ViewBankAccountScreen(
                    uniqueBankId = uniqueBankId
                ) {
                    navController.popBackStack()
                }
            }
        }

    }
}