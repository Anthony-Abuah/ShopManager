package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.home.debt_repayment.screens

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
fun DebtRepaymentNavGraph(
    navHostController: NavHostController
){
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = DebtRepaymentScreens.DebtRepaymentListScreen.route)
    {
        composable(route = DebtRepaymentScreens.DebtRepaymentListScreen.route,
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
            DebtRepaymentListScreen(
                navigateToAddDebtRepaymentScreen = {navController.navigate(DebtRepaymentScreens.AddDebtRepaymentScreen.route)},
                navigateToViewDebtRepaymentScreen = {_uniqueDebtRepaymentId->
                    navController.navigate(DebtRepaymentScreens.ViewDebtRepaymentScreen.withArgs(_uniqueDebtRepaymentId))
                }
            ) {
                navHostController.popBackStack()
            }
        }

        composable(route = DebtRepaymentScreens.AddDebtRepaymentScreen.route,
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
            AddDebtRepaymentScreen {
                navController.popBackStack()
            }
        }

        composable(route = DebtRepaymentScreens.ViewDebtRepaymentScreen.route + "/{uniqueDebtRepaymentId}",
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
                navArgument("uniqueDebtRepaymentId") {
                    type = NavType.StringType
                    defaultValue = Constants.emptyString
                    nullable = false
                }
            )
        ){
            val uniqueDebtRepaymentId = it.arguments?.getString("uniqueDebtRepaymentId")
            if (uniqueDebtRepaymentId != null) {
                ViewDebtRepaymentScreen(
                    uniqueDebtRepaymentId = uniqueDebtRepaymentId
                ) {
                    navController.popBackStack()
                }
            }
        }

    }
}