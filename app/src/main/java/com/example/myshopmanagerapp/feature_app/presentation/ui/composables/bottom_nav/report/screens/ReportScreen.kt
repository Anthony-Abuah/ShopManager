package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.bottom_nav.report.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.bottom_nav.BottomBar
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.bottom_nav.report.ReportContent
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.HomeScreenTopBar
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.ReportScreenTopBar


@Composable
fun ReportScreen(
    navController: NavHostController,
    navHostController: NavHostController,
    navigateToViewRevenueReportScreen: ()-> Unit,
    navigateToStockReportScreen: ()-> Unit,
    navigateToCashInReportScreen: ()-> Unit,
    navigateToViewGeneralReportScreen: ()-> Unit,
    navigateToInventoryReportScreen: ()-> Unit,
) {

    Scaffold(
        topBar = { ReportScreenTopBar(topBarTitleText = "Report") },
        bottomBar = { BottomBar(navHostController) }
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            ReportContent(
                navigateToViewRevenueReportScreen = navigateToViewRevenueReportScreen,
                navigateToCashInReportScreen = navigateToCashInReportScreen,
                navigateToViewStockReportScreen = navigateToStockReportScreen,
                navigateToViewGeneralReportScreen = navigateToViewGeneralReportScreen,
            ) {
                navigateToInventoryReportScreen()
            }
        }
    }
}
