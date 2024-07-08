package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.records.inventory.screens

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
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.records.inventory_item.screens.AddInventoryItemScreen
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.records.inventory_item.screens.InventoryItemScreens
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.records.inventory_item.screens.AddQuantityCategorizationScreen
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.records.personnel.screens.PersonnelCameraScreen
import com.example.myshopmanagerapp.feature_app.presentation.view_models.InventoryItemViewModel
import com.example.myshopmanagerapp.feature_app.presentation.view_models.InventoryViewModel
import com.example.myshopmanagerapp.feature_app.presentation.view_models.sharedViewModel

@Composable
fun InventoryNavGraph(
    navHostController: NavHostController
){
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = InventoryScreens.InventoryListScreen.route)
    {
        composable(route = InventoryScreens.InventoryListScreen.route){
            InventoryListScreen(navigateToAddInventoryScreen = {navController.navigate(InventoryScreens.AddInventoryScreen.route)},
                navigateToViewInventoryScreen = {
                    navController.navigate(InventoryScreens.ViewInventoryScreen.withArgs(it))}
            ) {
                navHostController.popBackStack()
            }
        }

        composable(route = InventoryScreens.AddInventoryScreen.route){
            val inventoryViewModel = it.sharedViewModel<InventoryViewModel>(navHostController = navController)
            AddInventoryScreen(
                inventoryViewModel,
                navigateToAddInventoryItemScreen = {
                    navController.navigate(InventoryScreens.AddInventoryItemScreen.route)
                }
            ) {
                navController.popBackStack()
            }
        }

        composable(route = InventoryScreens.ViewInventoryScreen.route + "/{uniqueInventoryId}",
            arguments = listOf(
                navArgument("uniqueInventoryId") {
                    type = NavType.StringType
                    defaultValue = Constants.emptyString
                    nullable = false
                }
            )
        ){
            val uniqueInventoryId = it.arguments?.getString("uniqueInventoryId")
            if (uniqueInventoryId != null) {
                ViewInventoryScreen(
                    uniqueInventoryId = uniqueInventoryId
                ) {
                    navController.popBackStack()
                }
            }
        }

        navigation(
            startDestination = InventoryScreens.AddInventoryItemScreen.route,
            route = InventoryScreens.AddInventoryItemNavGraph.route
        ) {
            composable(route = InventoryScreens.AddInventoryItemScreen.route,
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
                    navigateToTakePhotoScreen = { navController.navigate(InventoryScreens.AddInventoryItemCamera.route) },
                    navigateToQuantityCategorizationScreen = { navController.navigate(InventoryItemScreens.AddQuantityCategorizationScreen.route) }
                ) {
                    navController.popBackStack()
                }
            }

            composable(route = InventoryItemScreens.AddQuantityCategorizationScreen.route,
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

            composable(route = InventoryScreens.AddInventoryItemCamera.route,
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