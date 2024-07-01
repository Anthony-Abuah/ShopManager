package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.bottom_nav.report.screens

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.report.general.screens.GeneralReportNavGraph
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.report.inventory.screens.InventoryReportNavGraph
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.report.revenue.screens.RevenueReportNavGraph
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.report.stock.screens.StockReportNavGraph

@Composable
fun ReportNavGraph(
    navHostController: NavHostController
){
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = ReportScreens.MainReportScreen.route)
    {

        composable(route = ReportScreens.MainReportScreen.route){
            ReportScreen(navController = navController,
                navHostController = navHostController,
                navigateToViewRevenueReportScreen = {
                    navController.navigate(ReportScreens.ViewRevenueReportNavGraph.route)
                },
                navigateToInventoryReportScreen = {
                    navController.navigate(ReportScreens.ViewInventoryReportNavGraph.route)
                },
                navigateToStockReportScreen = {
                    navController.navigate(ReportScreens.ViewStockReportNavGraph.route)
                },
                navigateToViewGeneralReportScreen = {
                    navController.navigate(ReportScreens.GeneralReportNavGraph.route)
                }
            )
        }

        composable(route = ReportScreens.ViewStockReportNavGraph.route){
            StockReportNavGraph(navHostController = navController)
        }
        composable(route = ReportScreens.ViewRevenueReportNavGraph.route){
            RevenueReportNavGraph(navHostController = navController)
        }
        composable(route = ReportScreens.ViewInventoryReportNavGraph.route){
            InventoryReportNavGraph(navHostController = navController)
        }
        composable(route = ReportScreens.GeneralReportNavGraph.route){
            GeneralReportNavGraph(navHostController = navController)
        }
    }
}