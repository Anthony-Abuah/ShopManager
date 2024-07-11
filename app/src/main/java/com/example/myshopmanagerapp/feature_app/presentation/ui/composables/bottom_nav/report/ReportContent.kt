package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.bottom_nav.report

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.myshopmanagerapp.R
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.BasicScreenColumnWithBottomBar
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.ReportCard
import com.example.myshopmanagerapp.feature_app.presentation.ui.theme.*


@Composable
fun ReportContent(
    navigateToViewRevenueReportScreen: ()-> Unit,
    navigateToViewExpenseReportScreen: ()-> Unit,
    navigateToViewStockReportScreen: ()-> Unit,
    navigateToCashInReportScreen: ()-> Unit,
    navigateToViewGeneralReportScreen: ()-> Unit,
    navigateToViewInventoryReportScreen: ()-> Unit
) {
    BasicScreenColumnWithBottomBar {
        //General Report Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(LocalSpacing.current.small),
        ) {
            ReportCard(
                title = "General Shop Report",
                semiTitle = "View all your shop info",
                description = "You can see all your basic shop information and records here",
                icon = R.drawable.ic_money_filled,
                contentColor = if (isSystemInDarkTheme()) DarkViolet10 else DarkViolet95,
                cardContainerColor = if (isSystemInDarkTheme()) DarkViolet80 else DarkViolet40,
                onOpenCard = {
                    navigateToViewGeneralReportScreen()
                }
            )
        }

        //General Report Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(LocalSpacing.current.small),
        ) {
            ReportCard(
                title = "Cash flow",
                semiTitle = "View all your cash flows",
                description = "You can see all your cash flow information and records here",
                icon = R.drawable.ic_money_filled,
                contentColor = if (isSystemInDarkTheme()) YellowishGreen10 else YellowishGreen95,
                cardContainerColor = if (isSystemInDarkTheme()) YellowishGreen80 else YellowishGreen40,
                onOpenCard = {
                    navigateToCashInReportScreen()
                }
            )
        }

        //Revenue Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(LocalSpacing.current.small),
        ) {
            ReportCard(
                title = "Revenue Report",
                semiTitle = "View all your revenue records",
                description = "You can see all your daily revenue here",
                icon = R.drawable.ic_money_filled,
                contentColor = if (isSystemInDarkTheme()) Green10 else Green95,
                cardContainerColor = if (isSystemInDarkTheme()) Green80 else Green40,
                onOpenCard = {
                    navigateToViewRevenueReportScreen()
                }
            )
        }

        //Expense Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(LocalSpacing.current.small),
        ) {
            ReportCard(
                title = "Expense Report",
                semiTitle = "View all your expense records",
                description = "You can see all your expense report here",
                icon = R.drawable.ic_money_filled,
                contentColor = if (isSystemInDarkTheme()) Yellow10 else Yellow95,
                cardContainerColor = if (isSystemInDarkTheme()) Yellow80 else Yellow40,
                onOpenCard = {
                    navigateToViewExpenseReportScreen()
                }
            )
        }

        //Stock Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(LocalSpacing.current.small),
        ) {
            ReportCard(
                title = "Stock Report",
                semiTitle = "View all your stock records",
                description = "You can see all your inventory items, their quantities and values",
                icon = R.drawable.ic_money_filled,
                contentColor = if (isSystemInDarkTheme()) Gold10 else Gold95,
                cardContainerColor = if (isSystemInDarkTheme()) Gold80 else Gold40,
                onOpenCard = {
                    navigateToViewStockReportScreen()
                }
            )
        }

        //Inventory Report Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(LocalSpacing.current.small),
        ) {
            ReportCard(
                title = "Inventory Report",
                semiTitle = "View all your inventory records",
                description = "You can see all your inventory items, their quantities and values",
                icon = R.drawable.ic_money_filled,
                contentColor = if (isSystemInDarkTheme()) Blue10 else Blue95,
                cardContainerColor = if (isSystemInDarkTheme()) Blue80 else Blue40,
                onOpenCard = {
                    navigateToViewInventoryReportScreen()
                }
            )
        }

    }
}
