package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.bottom_nav.records

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
fun HomeContent(
    navigateToRevenueListScreen: () -> Unit,
    navigateToExpenseListScreen: () -> Unit,
    navigateToInventoryItemListScreen: () -> Unit,
    navigateToInventoryListScreen: () -> Unit,
    navigateToDebtListScreen: () -> Unit,
    navigateToDebtRepaymentListScreen: () -> Unit,
    navigateToSavingsListScreen: () -> Unit,
    navigateToWithdrawalListScreen: () -> Unit,
    navigateToStockListScreen: () -> Unit
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
            .padding(LocalSpacing.current.noPadding)
            .verticalScroll(state = rememberScrollState(), enabled = true),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
    ) {

        Spacer(modifier = Modifier.height(LocalSpacing.current.default))

        //Revenue Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(LocalSpacing.current.small),
        ) {
            HomeCard(
                title = "Revenue",
                description = "All sales and other kinds of revenue can be recorded and viewed here",
                icon = R.drawable.revenue,
                descriptionColor = descriptionColor,
                titleColor = titleColor,
                cardContainerColor = cardBackgroundColor,
                cardShadowColor = shadowColor
            ) {
                navigateToRevenueListScreen()
            }
        }


        //Expenses Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(LocalSpacing.current.small),
        ) {
            HomeCard(
                title = "Expenses",
                description = "Manage all your expenses records",
                icon = R.drawable.expense,
                descriptionColor = descriptionColor,
                titleColor = titleColor,
                cardContainerColor = cardBackgroundColor,
                cardShadowColor = shadowColor
            ) {
                navigateToExpenseListScreen()
            }
        }

        //Inventory Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(LocalSpacing.current.small),
        ) {
            HomeCard(
                title = "Inventory",
                description = "Manage all your inventory info",
                icon = R.drawable.inventory_item,
                descriptionColor = descriptionColor,
                titleColor = titleColor,
                cardContainerColor = cardBackgroundColor,
                cardShadowColor = shadowColor
            ) {
                navigateToInventoryListScreen()
            }
        }

        //Inventory Item Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(LocalSpacing.current.small),
        ) {
            HomeCard(
                title = "Inventory Item",
                description = "Manage all your inventory items",
                icon = R.drawable.inventory_item,
                descriptionColor = descriptionColor,
                titleColor = titleColor,
                cardContainerColor = cardBackgroundColor,
                cardShadowColor = shadowColor
            ) {
                navigateToInventoryItemListScreen()
            }
        }

        //Stock Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(LocalSpacing.current.small),
        ) {
            HomeCard(
                title = "Stock",
                description = "Manage all your stock info",
                icon = R.drawable.cash,
                descriptionColor = descriptionColor,
                titleColor = titleColor,
                cardContainerColor = cardBackgroundColor,
                cardShadowColor = shadowColor
            ) {
                navigateToStockListScreen()
            }
        }

        //Debt Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(LocalSpacing.current.small),
        ) {
            HomeCard(
                title = "Debt",
                description = "Manage all your debt info",
                icon = R.drawable.debt,
                descriptionColor = descriptionColor,
                titleColor = titleColor,
                cardContainerColor = cardBackgroundColor,
                cardShadowColor = shadowColor
            ) {
                navigateToDebtListScreen()
            }
        }

        //Debt Repayment Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(LocalSpacing.current.small),
        ) {
            HomeCard(
                title = "Debt Repayment",
                description = "Manage all your debt repayment info",
                icon = R.drawable.payment,
                descriptionColor = descriptionColor,
                titleColor = titleColor,
                cardContainerColor = cardBackgroundColor,
                cardShadowColor = shadowColor
            ) {
                navigateToDebtRepaymentListScreen()
            }
        }

        //Savings Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(LocalSpacing.current.small),
        ) {
            HomeCard(
                title = "Savings",
                description = "Manage all your savings repayment info",
                icon = R.drawable.savings,
                descriptionColor = descriptionColor,
                titleColor = titleColor,
                cardContainerColor = cardBackgroundColor,
                cardShadowColor = shadowColor
            ) {
                navigateToSavingsListScreen()
            }
        }

        //Withdrawal Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(LocalSpacing.current.small),
        ) {
            HomeCard(
                title = "Withdrawal",
                description = "Manage all your withdrawal info",
                icon = R.drawable.withdrawal,
                descriptionColor = descriptionColor,
                titleColor = titleColor,
                cardContainerColor = cardBackgroundColor,
                cardShadowColor = shadowColor
            ) {
                navigateToWithdrawalListScreen()
            }
        }
        Spacer(modifier = Modifier.height(LocalSpacing.current.bottomNavBarSize))

    }
}
