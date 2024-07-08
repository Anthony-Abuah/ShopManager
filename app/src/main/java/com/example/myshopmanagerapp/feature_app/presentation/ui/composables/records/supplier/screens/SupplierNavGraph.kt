package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.records.supplier.screens

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
import com.example.myshopmanagerapp.core.Constants
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.BasicButton
import com.example.myshopmanagerapp.feature_app.presentation.view_models.SupplierViewModel
import com.example.myshopmanagerapp.feature_app.presentation.view_models.sharedViewModel

@Composable
fun SupplierNavGraph(
    navHostController: NavHostController
){
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = SupplierScreens.SupplierListScreen.route)
    {
        composable(route = SupplierScreens.SupplierListScreen.route,
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
            val supplierViewModel = it.sharedViewModel<SupplierViewModel>(navHostController = navController)
            SupplierListScreen(
                supplierViewModel = supplierViewModel,
                navigateToAddSupplierScreen = {navController.navigate(SupplierScreens.AddSupplierScreen.route)},
                navigateToViewSupplierScreen = {navController.navigate(SupplierScreens.ViewSupplierScreen.route)}
            ) {
                navHostController.popBackStack()
            }
        }

        composable(route = SupplierScreens.AddSupplierScreen.route,
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
            AddSupplierScreen {
                navController.popBackStack()
            }
        }

        composable(route = SupplierScreens.ViewSupplierScreen.route + "/{uniqueSupplierId}",
            arguments = listOf(
                navArgument("uniqueSupplierId") {
                    type = NavType.StringType
                    defaultValue = Constants.emptyString
                    nullable = false
                }
            ),
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
            val supplierViewModel = it.sharedViewModel<SupplierViewModel>(navHostController = navController)
            val uniqueSupplierId = it.arguments?.getString("uniqueSupplierId")
            if (uniqueSupplierId != null) {
                ViewSupplierScreen(
                    supplierViewModel = supplierViewModel,
                    uniqueSupplierId = uniqueSupplierId
                ) { navController.popBackStack() }
            }
        }

        composable(route = SupplierScreens.SupplierRoleScreen.route,
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
            ) {
                BasicButton(buttonName = "Go Back") {
                    navController.popBackStack()
                }
            }
        }

    }
}