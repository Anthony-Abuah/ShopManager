package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.home.expenses.screens

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
import androidx.navigation.navigation
import com.example.myshopmanagerapp.core.Constants
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.home.personnel.screens.AddPersonnelScreen
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.home.personnel.screens.PersonnelCameraScreen
import com.example.myshopmanagerapp.feature_app.presentation.view_models.ExpenseViewModel
import com.example.myshopmanagerapp.feature_app.presentation.view_models.PersonnelViewModel
import com.example.myshopmanagerapp.feature_app.presentation.view_models.sharedViewModel

@Composable
fun ExpenseNavGraph(
    navHostController: NavHostController
){
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = ExpenseScreens.ExpenseListScreen.route)
    {
        composable(route = ExpenseScreens.ExpenseListScreen.route,
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
            val expenseViewModel = it.sharedViewModel<ExpenseViewModel>(navHostController = navController)
            ExpenseListScreen(
                expenseViewModel = expenseViewModel,
                navigateToAddExpenseScreen = {navController.navigate(ExpenseScreens.AddExpenseScreen.route)},
                navigateToViewExpenseScreen = { _uniqueExpenseId->
                    navController.navigate(ExpenseScreens.ViewExpenseScreen.withArgs(_uniqueExpenseId))}
            ) {
                navHostController.popBackStack()
            }
        }

        composable(route = ExpenseScreens.AddExpenseScreen.route,
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
            val expenseViewModel = it.sharedViewModel<ExpenseViewModel>(navHostController = navController)
            AddExpenseScreen(
                expenseViewModel = expenseViewModel
            ) {
                navController.popBackStack()
            }
        }

        composable(route = ExpenseScreens.ViewExpenseScreen.route + "/{uniqueExpenseId}",
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
                navArgument("uniqueExpenseId") {
                    type = NavType.StringType
                    defaultValue = Constants.emptyString
                    nullable = false
                }
            )){
            val uniqueExpenseId = it.arguments?.getString("uniqueExpenseId")
            val expenseViewModel = it.sharedViewModel<ExpenseViewModel>(navHostController = navController)
            if (uniqueExpenseId != null) {
                ViewExpenseScreen(
                    expenseViewModel = expenseViewModel,
                    uniqueExpenseId = uniqueExpenseId
                ) {
                    navController.popBackStack()
                }
            }
        }

        navigation(
            startDestination = ExpenseScreens.AddPersonnelScreen.route,
            route = ExpenseScreens.PersonnelNavGraph.route
        ) {
            composable(route = ExpenseScreens.AddPersonnelScreen.route,
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
            ) {
                val personnelViewModel = it.sharedViewModel<PersonnelViewModel>(navHostController = navController)
                AddPersonnelScreen(
                    personnelViewModel = personnelViewModel,
                    navigateToTakePhoto = {
                        navController.navigate(ExpenseScreens.AddPersonnelPhotoScreen.route)
                    }
                ) {
                    navController.popBackStack()
                }
            }
            composable(route = ExpenseScreens.AddPersonnelPhotoScreen.route,
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
            ) {
                PersonnelCameraScreen {
                    navController.popBackStack()
                }
            }
        }

    }
}