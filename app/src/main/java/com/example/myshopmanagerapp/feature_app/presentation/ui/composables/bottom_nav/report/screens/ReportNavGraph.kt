package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.bottom_nav.report.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.report.expense.screen.ExpenseReportNavGraph
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.report.general.screens.GeneralReportNavGraph
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.report.inventory.screens.InventoryReportNavGraph
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.report.revenue.screens.RevenueReportNavGraph

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
            ReportScreen(navHostController = navHostController,
                navigateToViewRevenueReportScreen = {
                    navController.navigate(ReportScreens.ViewRevenueReportNavGraph.route)
                },
                navigateToViewExpenseReportScreen = {
                    navController.navigate(ReportScreens.ViewExpenseReportNavGraph.route)
                },
                navigateToCashInReportScreen = {
                    navController.navigate(ReportScreens.ViewCashInReportNavGraph.route)
                },
                navigateToViewGeneralReportScreen = {
                    navController.navigate(ReportScreens.GeneralReportNavGraph.route)
                }
            ) {
                navController.navigate(ReportScreens.ViewInventoryReportNavGraph.route)
            }
        }

        composable(route = ReportScreens.ViewRevenueReportNavGraph.route){
            RevenueReportNavGraph(navHostController = navController)
        }
        composable(route = ReportScreens.ViewExpenseReportNavGraph.route){
            ExpenseReportNavGraph(navHostController = navController)
        }
        composable(route = ReportScreens.ViewInventoryReportNavGraph.route){
            InventoryReportNavGraph(navHostController = navController)
        }
        composable(route = ReportScreens.GeneralReportNavGraph.route){
            GeneralReportNavGraph(navHostController = navController)
        }
        composable(route = ReportScreens.ViewCashInReportNavGraph.route){
            Box(modifier = Modifier.fillMaxSize()){
                Text(modifier = Modifier.align(Alignment.Center), text = "Cash in report")
            }
        }
    }
}