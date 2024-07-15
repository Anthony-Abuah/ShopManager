package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.bottom_nav.records.screens

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.bottom_nav.personnel.screens.PersonnelProfileNavGraph
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.records.bank_account.screens.BankAccountNavGraph
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.records.company.screens.CompanyNavGraph
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.records.customer.screens.CustomerNavGraph
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.records.debt.screens.DebtNavGraph
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.records.debt_repayment.screens.DebtRepaymentNavGraph
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.records.expenses.screens.ExpenseNavGraph
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.records.generate_receipt.screen.ReceiptNavGraph
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.records.inventory.screens.InventoryNavGraph
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.records.inventory_item.screens.InventoryItemNavGraph
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.records.personnel.screens.PersonnelNavGraph
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.records.revenue.screens.RevenueNavGraph
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.records.savings.screens.SavingsNavGraph
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.records.stock.screens.StockNavGraph
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.records.supplier.screens.SupplierNavGraph
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.records.withdrawal.screens.WithdrawalNavGraph

@Composable
fun HomeNavGraph(
    navHostController: NavHostController
){
    val navController = rememberNavController()
    var isLoggedIn by remember {
        mutableStateOf(false)
    }
    NavHost(
        navController = navController,
        startDestination = HomeScreens.MainHomeScreen.route)
    {
        composable(route = HomeScreens.MainHomeScreen.route,
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
            HomeScreen(
                navController = navController,
                navHostController = navHostController,
                navigateToPersonnelNavGraph = {
                    isLoggedIn = it
                    navController.navigate(HomeScreens.PersonnelProfileNavGraph.route)
                },
                navigateToRevenueListScreen = {
                    navController.navigate(HomeScreens.RevenueNavGraph.route)
                },
                navigateToExpenseListScreen = {
                    navController.navigate(HomeScreens.ExpenseNavGraph.route)
                },
                navigateToInventoryListScreen = {
                    navController.navigate(HomeScreens.InventoryNavGraph.route)
                },
                navigateToStockListScreen = {
                    navController.navigate(HomeScreens.StockNavGraph.route)
                },
                navigateToDebtListScreen = {
                    navController.navigate(HomeScreens.DebtNavGraph.route)
                },
                navigateToWithdrawalListScreen = {
                    navController.navigate(HomeScreens.WithdrawalNavGraph.route)
                },
                navigateToSavingsListScreen = {
                    navController.navigate(HomeScreens.SavingsNavGraph.route)
                },
                navigateToDebtRepaymentListScreen = {
                    navController.navigate(HomeScreens.DebtRepaymentNavGraph.route)
                },
            ) {
                navController.navigate(HomeScreens.InventoryItemNavGraph.route)
            }
        }

        composable(route = HomeScreens.RevenueNavGraph.route,
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
            RevenueNavGraph(navHostController = navController)
        }

        composable(route = HomeScreens.PersonnelProfileNavGraph.route,
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
            PersonnelProfileNavGraph(isLoggedIn = isLoggedIn,
                navController = navController)
        }

        composable(route = HomeScreens.SavingsNavGraph.route,
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
            SavingsNavGraph(navHostController = navController)
        }

        composable(route = HomeScreens.WithdrawalNavGraph.route,
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
            WithdrawalNavGraph(navHostController = navController)
        }

        composable(route = HomeScreens.CustomerNavGraph.route,
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
            CustomerNavGraph(navHostController = navController)
        }

        composable(route = HomeScreens.SupplierNavGraph.route,
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
            SupplierNavGraph(navHostController = navController)
        }

        composable(route = HomeScreens.PersonnelNavGraph.route){
            PersonnelNavGraph(navHostController = navController)
        }

        composable(route = HomeScreens.BankNavGraph.route,
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
            BankAccountNavGraph(navHostController = navController)
        }

        composable(route = HomeScreens.CompanyNavGraph.route,
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
            CompanyNavGraph(navHostController = navController)
        }

        composable(route = HomeScreens.DebtNavGraph.route,
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
            DebtNavGraph(navHostController = navController)
        }

        composable(route = HomeScreens.DebtRepaymentNavGraph.route,
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
            DebtRepaymentNavGraph(navHostController = navController)
        }

        composable(route = HomeScreens.ExpenseNavGraph.route,
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
            ExpenseNavGraph(navHostController = navController)
        }

        composable(route = HomeScreens.InventoryItemNavGraph.route,
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
            InventoryItemNavGraph(navHostController = navController)
        }

        composable(route = HomeScreens.InventoryNavGraph.route,
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
            InventoryNavGraph(navHostController = navController)
        }

        composable(route = HomeScreens.StockNavGraph.route,
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
            StockNavGraph(navHostController = navController)
        }


        composable(route = HomeScreens.ReceiptNavGraph.route,
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
            ReceiptNavGraph(navHostController = navController)
        }


    }
}