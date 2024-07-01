package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.home.withdrawal.screens

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
import com.example.myshopmanagerapp.core.Constants.emptyString
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.home.bank_account.screens.AddBankAccountScreen
import com.example.myshopmanagerapp.feature_app.presentation.view_models.WithdrawalViewModel
import com.example.myshopmanagerapp.feature_app.presentation.view_models.sharedViewModel

@Composable
fun WithdrawalNavGraph(
    navHostController: NavHostController
){
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = WithdrawalScreens.WithdrawalListScreen.route)
    {
        composable(route = WithdrawalScreens.WithdrawalListScreen.route,
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
            val withdrawalViewModel = it.sharedViewModel<WithdrawalViewModel>(navHostController = navController)
            WithdrawalListScreen(
                withdrawalViewModel = withdrawalViewModel,
                navigateToAddWithdrawalScreen = {navController.navigate(WithdrawalScreens.AddWithdrawalScreen.route)},
                navigateToViewWithdrawalScreen = {_uniqueWithdrawalId->
                    navController.navigate(WithdrawalScreens.ViewWithdrawalScreen.withArgs(_uniqueWithdrawalId))
                }
            ) {
                navHostController.popBackStack()
            }
        }

        composable(route = WithdrawalScreens.AddWithdrawalScreen.route,
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
            val withdrawalViewModel = it.sharedViewModel<WithdrawalViewModel>(navHostController = navController)
            AddWithdrawalScreen(
                withdrawalViewModel = withdrawalViewModel,
                navigateToAddBankScreen = {
                    navController.navigate(WithdrawalScreens.AddBankScreen.route)
                },
                navigateToAddPersonnelScreen = {
                    navController.navigate(WithdrawalScreens.AddPersonnelNavGraph.route)
                }
            ) {
                navController.popBackStack()
            }
        }

        composable(route = WithdrawalScreens.ViewWithdrawalScreen.route + "/{uniqueWithdrawalId}",
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
                navArgument("uniqueWithdrawalId") {
                    type = NavType.StringType
                    defaultValue = emptyString
                    nullable = false
                }
            )
        ){
            val uniqueWithdrawalId = it.arguments?.getString("uniqueWithdrawalId")
            val withdrawalViewModel = it.sharedViewModel<WithdrawalViewModel>(navHostController = navController)
            if (uniqueWithdrawalId != null) {
                ViewWithdrawalScreen(
                    withdrawalViewModel = withdrawalViewModel,
                    uniqueWithdrawalId = uniqueWithdrawalId
                ) {
                    navController.popBackStack()
                }
            }
        }

        composable(route = WithdrawalScreens.AddBankScreen.route,
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
            }) {
            AddBankAccountScreen {
                navController.popBackStack()
            }
        }

    }
}