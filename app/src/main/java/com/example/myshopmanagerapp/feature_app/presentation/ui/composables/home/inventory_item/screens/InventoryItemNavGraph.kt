package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.home.inventory_item.screens

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.example.myshopmanagerapp.core.Constants.emptyString
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.BasicButton
import com.example.myshopmanagerapp.feature_app.presentation.view_models.InventoryItemViewModel
import com.example.myshopmanagerapp.feature_app.presentation.view_models.sharedViewModel

@Composable
fun InventoryItemNavGraph(
    navHostController: NavHostController
){
    val navController = rememberNavController()
    var uniqueInventoryItemId by remember {
        mutableStateOf(emptyString)
    }
    NavHost(
        navController = navController,
        startDestination = InventoryItemScreens.InventoryItemListScreen.route)
    {
        composable(route = InventoryItemScreens.InventoryItemListScreen.route,
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
            InventoryItemListScreen(
                inventoryItemViewModel = inventoryItemViewModel,
                navigateToAddInventoryItemScreen = {navController.navigate(InventoryItemScreens.AddInventoryItemNavigation.route)},
                navigateToViewInventoryItemScreen = {_uniqueInventoryItemId->
                    uniqueInventoryItemId = _uniqueInventoryItemId
                    navController.navigate(InventoryItemScreens.ViewInventoryItemNavigation.route)
                }
            ) {
                navHostController.popBackStack()
            }
        }

        navigation(
            startDestination = InventoryItemScreens.AddInventoryItemScreen.route,
            route = InventoryItemScreens.AddInventoryItemNavigation.route
        ) {
            composable(route = InventoryItemScreens.AddInventoryItemScreen.route,
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
                    navigateToQuantityCategorizationScreen = {
                        navController.navigate(InventoryItemScreens.AddQuantityCategorizationScreen.route) },
                    navigateToTakePhotoScreen = {
                        navController.navigate(InventoryItemScreens.InventoryItemPhotoScreen.route)
                    }
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
        }


        navigation(
            startDestination = InventoryItemScreens.ViewInventoryItemScreen.route,
            route = InventoryItemScreens.ViewInventoryItemNavigation.route
        ) {
            composable(route = InventoryItemScreens.ViewInventoryItemScreen.route,
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
            ) {
                val inventoryItemViewModel = it.sharedViewModel<InventoryItemViewModel>(navHostController = navController)
                ViewInventoryItemScreen(
                    inventoryItemViewModel = inventoryItemViewModel,
                    uniqueInventoryItemId = uniqueInventoryItemId,
                    navigateToQuantityCategoriesScreen = {
                        navController.navigate(InventoryItemScreens.UpdateQuantityCategorizationScreen.route)
                    },
                    navigateToViewCostPricesScreen = {
                        navController.navigate(InventoryItemScreens.ViewInventoryItemCostPricesScreen.route)
                    },
                    navigateToViewSellingPricesScreen = {
                        navController.navigate(InventoryItemScreens.ViewInventoryItemSellingPricesScreen.route)
                    },
                    navigateToViewInventoryStockScreen = {
                        navController.navigate(InventoryItemScreens.ViewInventoryStockScreen.route)
                    }
                ) {
                    navController.popBackStack()
                }
            }

            composable(route = InventoryItemScreens.UpdateQuantityCategorizationScreen.route,
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
                UpdateQuantityCategorizationScreen(inventoryItemViewModel) {
                    navController.popBackStack()
                }
            }

            composable(route = InventoryItemScreens.ViewInventoryItemCostPricesScreen.route,
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
                ViewInventoryCostPricesScreen(inventoryItemViewModel) {
                    navController.popBackStack()
                }
            }

            composable(route = InventoryItemScreens.ViewInventoryItemSellingPricesScreen.route,
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
                ViewInventorySellingPricesScreen(inventoryItemViewModel) {
                    navController.popBackStack()
                }
            }

            composable(route = InventoryItemScreens.ViewInventoryStockScreen.route,
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
                ViewInventoryStockScreen(inventoryItemViewModel = inventoryItemViewModel) {
                    navController.popBackStack()
                }
            }

        }

        composable(route = InventoryItemScreens.InventoryItemPhotoScreen.route,
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
           Box(modifier = Modifier.fillMaxSize()){
               BasicButton(buttonName = "Go Back") {
                   navController.popBackStack()
               }
           }
        }

    }
}