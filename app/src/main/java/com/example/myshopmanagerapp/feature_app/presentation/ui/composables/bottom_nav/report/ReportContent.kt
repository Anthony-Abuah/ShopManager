package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.bottom_nav.report

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.myshopmanagerapp.R
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.HomeCard
import com.example.myshopmanagerapp.feature_app.presentation.ui.theme.*


@Composable
fun ReportContent(
    navigateToViewRevenueReportScreen: () -> Unit,
    navigateToViewExpenseReportScreen: () -> Unit,
    navigateToCashInReportScreen: () -> Unit,
    navigateToViewGeneralReportScreen: () -> Unit,
    navigateToViewInventoryReportScreen: () -> Unit
) {
    val mainBackgroundColor = if (isSystemInDarkTheme()) Grey20 else Grey99
    val cardBackgroundColor = if (isSystemInDarkTheme()) Grey15 else Grey90
    val shadowColor = if (isSystemInDarkTheme()) Grey10 else Grey80
    val descriptionColor = if (isSystemInDarkTheme()) Grey70 else Grey40
    val titleColor = if (isSystemInDarkTheme()) Grey99 else Grey10


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(mainBackgroundColor)
            .padding(LocalSpacing.current.noPadding),
            //.verticalScroll(state = rememberScrollState(), enabled = true),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
    ) {
        //General Report Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(LocalSpacing.current.small),
        ) {
            HomeCard(
                title = "General Shop Report",
                description = "You can see all your basic shop information and records here",
                icon = R.drawable.shop,
                descriptionColor = descriptionColor,
                titleColor = titleColor,
                cardContainerColor = cardBackgroundColor,
                cardShadowColor = shadowColor
            ) {
                navigateToViewGeneralReportScreen()
            }
        }

        //Cash Flow
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(LocalSpacing.current.small),
        ) {
            HomeCard(
                title = "Cash Flow",
                description = "You can see all your cash flow information and records here",
                icon = R.drawable.cash,
                descriptionColor = descriptionColor,
                titleColor = titleColor,
                cardContainerColor = cardBackgroundColor,
                cardShadowColor = shadowColor
            ) {
                navigateToCashInReportScreen()
            }
        }

        //Revenue Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(LocalSpacing.current.small),
        ) {
            HomeCard(
                title = "Revenue Report",
                description = "You can view all your revenue information and records here",
                icon = R.drawable.revenue,
                descriptionColor = descriptionColor,
                titleColor = titleColor,
                cardContainerColor = cardBackgroundColor,
                cardShadowColor = shadowColor
            ) {
                navigateToViewRevenueReportScreen()
            }
        }

        //Expense Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(LocalSpacing.current.small),
        ) {
            HomeCard(
                title = "Expense Report",
                description = "You can view all your expense information and records here",
                icon = R.drawable.expense,
                descriptionColor = descriptionColor,
                titleColor = titleColor,
                cardContainerColor = cardBackgroundColor,
                cardShadowColor = shadowColor
            ) {
                navigateToViewExpenseReportScreen()
            }
        }

        //Inventory Report Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(LocalSpacing.current.small),
        ) {
            HomeCard(
                title = "Inventory & Stock Report",
                description = "You can view all your inventory and stock information and records here",
                icon = R.drawable.inventory_item,
                descriptionColor = descriptionColor,
                titleColor = titleColor,
                cardContainerColor = cardBackgroundColor,
                cardShadowColor = shadowColor
            ) {
                navigateToViewInventoryReportScreen()
            }
        }

    }
}
