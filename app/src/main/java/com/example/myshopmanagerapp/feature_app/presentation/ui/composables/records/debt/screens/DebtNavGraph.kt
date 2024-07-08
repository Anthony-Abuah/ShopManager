package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.records.debt.screens

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
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.records.customer.screens.AddCustomerScreen
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.records.customer.screens.CustomerCameraScreen
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.records.personnel.screens.AddPersonnelScreen
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.records.personnel.screens.PersonnelCameraScreen
import com.example.myshopmanagerapp.feature_app.presentation.view_models.CustomerViewModel
import com.example.myshopmanagerapp.feature_app.presentation.view_models.DebtViewModel
import com.example.myshopmanagerapp.feature_app.presentation.view_models.PersonnelViewModel
import com.example.myshopmanagerapp.feature_app.presentation.view_models.sharedViewModel

@Composable
fun DebtNavGraph(
    navHostController: NavHostController
){
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = DebtScreens.DebtListScreen.route)
    {
        composable(route = DebtScreens.DebtListScreen.route,
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
            val debtViewModel = it.sharedViewModel<DebtViewModel>(navHostController = navController)
            DebtListScreen(
                debtViewModel = debtViewModel,
                navigateToAddDebtScreen = {navController.navigate(DebtScreens.AddDebtScreen.route)},
                navigateToViewDebtScreen = {_uniqueDebtId->
                    navController.navigate(DebtScreens.ViewDebtScreen.withArgs(_uniqueDebtId))
                }
            ) {
                navHostController.popBackStack()
            }
        }

        composable(route = DebtScreens.AddDebtScreen.route,
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
            val debtViewModel = it.sharedViewModel<DebtViewModel>(navHostController = navController)
            AddDebtScreen(
                debtViewModel = debtViewModel,
                navigateToAddCustomerScreen = {
                    navController.navigate(DebtScreens.AddCustomerNavGraph.route)
                }
            ) {
                navController.popBackStack()
            }
        }

        composable(route = DebtScreens.ViewDebtScreen.route + "/{uniqueDebtId}",
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
                navArgument("uniqueDebtId") {
                    type = NavType.StringType
                    defaultValue = emptyString
                    nullable = false
                }
            )
        ){
            val uniqueDebtId = it.arguments?.getString("uniqueDebtId")
            val debtViewModel = it.sharedViewModel<DebtViewModel>(navHostController = navController)
            if (uniqueDebtId != null) {
                ViewDebtScreen(
                    debtViewModel = debtViewModel,
                    uniqueDebtId = uniqueDebtId
                ) {
                    navController.popBackStack()
                }
            }
        }

        navigation(
            startDestination = DebtScreens.AddCustomerScreen.route,
            route = DebtScreens.AddCustomerNavGraph.route
        ) {
            composable(route = DebtScreens.AddCustomerScreen.route,
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

                val customerViewModel = it.sharedViewModel<CustomerViewModel>(navHostController = navController)
                AddCustomerScreen(
                    customerViewModel = customerViewModel,
                    navigateToTakePhotoScreen = { navController.navigate(DebtScreens.AddCustomerPhotoScreen.route)}
                ) {
                    navController.popBackStack()
                }
            }


            composable(route = DebtScreens.AddCustomerPhotoScreen.route,
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
                CustomerCameraScreen {
                    navController.popBackStack()
                }
            }

        }

        navigation(
            startDestination = DebtScreens.AddPersonnelScreen.route,
            route = DebtScreens.AddPersonnelNavGraph.route
        ) {
            composable(route = DebtScreens.AddPersonnelScreen.route,
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
                val personnelViewModel =
                    it.sharedViewModel<PersonnelViewModel>(navHostController = navController)
                AddPersonnelScreen(
                    personnelViewModel = personnelViewModel,
                    navigateToTakePhoto = {
                        navController.navigate(DebtScreens.AddPersonnelPhotoScreen.route)
                    }
                ) {
                    navController.popBackStack()
                }
            }

            composable(route = DebtScreens.AddPersonnelRoleScreen.route,
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

            composable(route = DebtScreens.AddPersonnelPhotoScreen.route,
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