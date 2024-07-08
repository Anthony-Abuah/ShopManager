package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.records.stock.screens

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.example.myshopmanagerapp.core.Constants
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.BasicButton
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.records.inventory_item.screens.AddInventoryItemScreen
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.records.inventory_item.screens.AddQuantityCategorizationScreen
import com.example.myshopmanagerapp.feature_app.presentation.view_models.InventoryItemViewModel
import com.example.myshopmanagerapp.feature_app.presentation.view_models.StockViewModel
import com.example.myshopmanagerapp.feature_app.presentation.view_models.sharedViewModel

@Composable
fun StockNavGraph(
    navHostController: NavHostController
){
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = StockScreens.StockListScreen.route)
    {
        composable(route = StockScreens.StockListScreen.route,
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
            val stockViewModel = it.sharedViewModel<StockViewModel>(navHostController = navController)
            StockListScreen(
                stockViewModel = stockViewModel,
                navigateToAddStockScreen = {navController.navigate(StockScreens.AddStockScreen.route)},
                navigateToViewStockScreen = {uniqueStockId->
                    navController.navigate(StockScreens.ViewStockScreen.withArgs(uniqueStockId))}
            ) {
                navHostController.popBackStack()
            }
        }

        composable(route = StockScreens.AddStockScreen.route,
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
            val stockViewModel = it.sharedViewModel<StockViewModel>(navHostController = navController)
            AddStockScreen(stockViewModel = stockViewModel) {
                navController.popBackStack()
            }
        }

        composable(route = StockScreens.ViewStockScreen.route + "/{uniqueStockId}",
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
                navArgument("uniqueStockId") {
                    type = NavType.StringType
                    defaultValue = Constants.emptyString
                    nullable = false
                }
            )){
            val uniqueStockId = it.arguments?.getString("uniqueStockId")
            val stockViewModel = it.sharedViewModel<StockViewModel>(navHostController = navController)
            if (uniqueStockId != null) {
                ViewStockScreen(
                    stockViewModel = stockViewModel,
                    uniqueStockId = uniqueStockId) {
                    navController.popBackStack()
                }
            }
        }

        navigation(
            startDestination = StockScreens.AddItemScreen.route,
            route = StockScreens.ItemNavGraph.route
        ) {
            composable(route = StockScreens.AddItemScreen.route,
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
                val inventoryItemViewModel = it.sharedViewModel<InventoryItemViewModel>(navHostController = navController)
                AddInventoryItemScreen(
                    inventoryItemViewModel = inventoryItemViewModel,
                    navigateToQuantityCategorizationScreen = { navController.navigate(StockScreens.ItemQuantityCategorizationScreen.route)},
                    navigateToTakePhotoScreen = { navController.navigate(StockScreens.ItemPhotoScreen.route)}
                ) {
                    navController.popBackStack()
                }
            }

            composable(route = StockScreens.ItemQuantityCategorizationScreen.route,
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

            composable(route = StockScreens.ItemPhotoScreen.route,
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
                Box(modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ){
                    BasicButton(buttonName = "Go Back") {
                        navController.popBackStack()
                    }
                }
            }

        }


    }
}