package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.home.customer.screens

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
import com.example.myshopmanagerapp.feature_app.presentation.view_models.CustomerViewModel
import com.example.myshopmanagerapp.feature_app.presentation.view_models.sharedViewModel

@Composable
fun CustomerNavGraph(
    navHostController: NavHostController
){
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = CustomerScreens.CustomerListScreen.route,
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
        composable(
            route = CustomerScreens.CustomerListScreen.route,
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
            CustomerListScreen(
                customerViewModel,
                navigateToAddCustomerScreen = {
                navController.navigate(
                    CustomerScreens.AddCustomerScreen.route
                )
            },
                navigateToViewCustomerScreen = { uniqueCustomerId ->
                    navController.navigate(
                        CustomerScreens.ViewCustomerScreen.withArgs(
                            uniqueCustomerId
                        )
                    )
                }
            ) {
                navHostController.popBackStack()
            }
        }

        composable(
            route = CustomerScreens.AddCustomerScreen.route,
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
                navigateToTakePhotoScreen = { navController.navigate(CustomerScreens.CustomerCameraScreen.route) }
            ) { navController.popBackStack() }
        }

        composable(
            route = CustomerScreens.ViewCustomerScreen.route + "/{uniqueCustomerId}",
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
                navArgument("uniqueCustomerId") {
                    type = NavType.StringType
                    defaultValue = emptyString
                    nullable = false
                })
        ) { entry ->
            val uniqueCustomerId = entry.arguments?.getString("uniqueCustomerId")
            val customerViewModel = entry.sharedViewModel<CustomerViewModel>(navHostController = navController)
            if (uniqueCustomerId != null) {
                ViewCustomerScreen(
                    customerViewModel = customerViewModel,
                    uniqueCustomerId = uniqueCustomerId.ifBlank { customerViewModel.customerInfo.uniqueCustomerId },
                    navigateToTakePhotoScreen = { navController.navigate(CustomerScreens.CustomerCameraScreen.route) },
                ) {
                    navController.popBackStack()
                }
            }

        }

        composable(route = CustomerScreens.CustomerCameraScreen.route,
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
}
