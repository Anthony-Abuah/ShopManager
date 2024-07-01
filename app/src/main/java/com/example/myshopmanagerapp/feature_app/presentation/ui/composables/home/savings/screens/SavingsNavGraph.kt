package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.home.savings.screens

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
import com.example.myshopmanagerapp.core.Constants.emptyString
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.home.bank_account.screens.AddBankAccountScreen
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.home.personnel.screens.AddPersonnelScreen
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.home.personnel.screens.PersonnelCameraScreen
import com.example.myshopmanagerapp.feature_app.presentation.view_models.PersonnelViewModel
import com.example.myshopmanagerapp.feature_app.presentation.view_models.SavingsViewModel
import com.example.myshopmanagerapp.feature_app.presentation.view_models.sharedViewModel

@Composable
fun SavingsNavGraph(
    navHostController: NavHostController
){
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = SavingsScreens.SavingsListScreen.route)
    {
        composable(route = SavingsScreens.SavingsListScreen.route,
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
            val savingsViewModel = it.sharedViewModel<SavingsViewModel>(navHostController = navController)
            SavingsListScreen(
                savingsViewModel = savingsViewModel,
                navigateToAddSavingsScreen = {navController.navigate(SavingsScreens.AddSavingsScreen.route)},
                navigateToViewSavingsScreen = {_uniqueSavingsId->
                    navController.navigate(SavingsScreens.ViewSavingsScreen.withArgs(_uniqueSavingsId))
                }
            ) {
                navHostController.popBackStack()
            }
        }

        composable(route = SavingsScreens.AddSavingsScreen.route,
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
            val savingsViewModel = it.sharedViewModel<SavingsViewModel>(navHostController = navController)
            AddSavingsScreen(
                savingsViewModel = savingsViewModel,
                navigateToAddBankScreen = {
                    navController.navigate(SavingsScreens.AddBankScreen.route)
                }
            ) {
                navController.popBackStack()
            }
        }

        composable(route = SavingsScreens.ViewSavingsScreen.route + "/{uniqueSavingsId}",
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
                navArgument("uniqueSavingsId") {
                    type = NavType.StringType
                    defaultValue = emptyString
                    nullable = false
                }
            )
        ){
            val uniqueSavingsId = it.arguments?.getString("uniqueSavingsId")
            val savingsViewModel = it.sharedViewModel<SavingsViewModel>(navHostController = navController)
            if (uniqueSavingsId != null) {
                ViewSavingsScreen(
                    savingsViewModel = savingsViewModel,
                    uniqueSavingsId = uniqueSavingsId
                ) {
                    navController.popBackStack()
                }
            }
        }


        composable(route = SavingsScreens.AddBankScreen.route,
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
            AddBankAccountScreen() {
                navController.popBackStack()
            }
        }

        navigation(
            startDestination = SavingsScreens.AddPersonnelScreen.route,
            route = SavingsScreens.AddPersonnelNavGraph.route
        ) {
            composable(route = SavingsScreens.AddPersonnelScreen.route,
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
                        navController.navigate(SavingsScreens.AddPersonnelPhotoScreen.route)
                    }
                ) {
                    navController.popBackStack()
                }
            }

            composable(route = SavingsScreens.AddPersonnelPhotoScreen.route,
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