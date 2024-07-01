package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.home.generate_receipt.screen

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
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.home.customer.screens.AddCustomerScreen
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.home.customer.screens.CustomerCameraScreen
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.home.inventory_item.screens.AddInventoryItemScreen
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.home.inventory_item.screens.AddQuantityCategorizationScreen
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.home.personnel.screens.PersonnelCameraScreen
import com.example.myshopmanagerapp.feature_app.presentation.view_models.CompanyViewModel
import com.example.myshopmanagerapp.feature_app.presentation.view_models.CustomerViewModel
import com.example.myshopmanagerapp.feature_app.presentation.view_models.InventoryItemViewModel
import com.example.myshopmanagerapp.feature_app.presentation.view_models.sharedViewModel

@Composable
fun ReceiptNavGraph(
    navHostController: NavHostController
){
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = ReceiptScreens.ReceiptListScreen.route)
    {
        composable(route = ReceiptScreens.ReceiptListScreen.route,
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
            ReceiptListScreen(
                navigateToAddReceiptScreen = { navController.navigate(ReceiptScreens.AddReceiptScreen.route) },
                navigateToUpdateReceipt = { navController.navigate(ReceiptScreens.UpdateReceiptScreen.withArgs(it)) }
            ) {
                navHostController.popBackStack()
            }
        }

        composable(route = ReceiptScreens.AddReceiptScreen.route,
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
            val companyViewModel = it.sharedViewModel<CompanyViewModel>(navHostController = navController)
            GenerateReceiptScreen(
                companyViewModel = companyViewModel,
                navigateToAddInventoryItemScreen = {
                    navController.navigate(ReceiptScreens.AddReceiptInventoryItemNavGraph.route)
                },
                navigateToAddCustomerScreen = {
                    navController.navigate(ReceiptScreens.AddReceiptCustomerNavGraph.route)
                }
            ) {
              navController.popBackStack()
            }
        }

        composable(route = ReceiptScreens.UpdateReceiptScreen.route+ "/{uniqueReceiptId}",
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
                navArgument("uniqueReceiptId") {
                    type = NavType.StringType
                    defaultValue = emptyString
                    nullable = false
                })
        ){
            val uniqueReceiptId = it.arguments?.getString("uniqueReceiptId")

            val companyViewModel = it.sharedViewModel<CompanyViewModel>(navHostController = navController)
            if (uniqueReceiptId != null) {
                UpdateReceiptScreen(
                    uniqueReceiptId = uniqueReceiptId,
                    companyViewModel = companyViewModel,
                    navigateToAddInventoryItemScreen = { navController.navigate(ReceiptScreens.AddReceiptInventoryItemNavGraph.route) },
                    navigateToAddCustomerScreen = { navController.navigate(ReceiptScreens.AddReceiptCustomerNavGraph.route) }) {
                    navController.popBackStack()
                }
            }
        }

        navigation(
            startDestination = ReceiptScreens.AddReceiptCustomerScreen.route,
            route = ReceiptScreens.AddReceiptCustomerNavGraph.route
        ) {
            composable(route = ReceiptScreens.AddReceiptCustomerScreen.route,
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
                val customerViewModel = it.sharedViewModel<CustomerViewModel>(navHostController = navController)
                AddCustomerScreen(
                    customerViewModel = customerViewModel,
                    navigateToTakePhotoScreen = { navController.navigate(ReceiptScreens.AddReceiptCustomerPhotoScreen.route)}
                ) {
                    navController.popBackStack()
                }
            }


            composable(route = ReceiptScreens.AddReceiptCustomerPhotoScreen.route,
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
            startDestination = ReceiptScreens.AddReceiptInventoryItemScreen.route,
            route = ReceiptScreens.AddReceiptInventoryItemNavGraph.route
        ) {
            composable(route = ReceiptScreens.AddReceiptInventoryItemScreen.route,
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
                val inventoryItemViewModel = it.sharedViewModel<InventoryItemViewModel>(navHostController = navController)
                AddInventoryItemScreen(
                    inventoryItemViewModel = inventoryItemViewModel,
                    navigateToTakePhotoScreen = { navController.navigate(ReceiptScreens.AddReceiptInventoryItemPhotoScreen.route) },
                    navigateToQuantityCategorizationScreen = { navController.navigate(ReceiptScreens.AddReceiptInventoryQuantityCategorizationScreen.route) },
                ) {
                    navController.popBackStack()
                }
            }

            composable(route = ReceiptScreens.AddReceiptInventoryQuantityCategorizationScreen.route,
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
                val inventoryItemViewModel = it.sharedViewModel<InventoryItemViewModel>(navHostController = navController)
                AddQuantityCategorizationScreen(inventoryItemViewModel) {
                    navController.popBackStack()
                }
            }

            composable(route = ReceiptScreens.AddReceiptInventoryItemPhotoScreen.route,
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