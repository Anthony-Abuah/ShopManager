package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.bottom_nav.records

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.myshopmanagerapp.R
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.BasicScreenColumnWithBottomBar
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.HomeCard
import com.example.myshopmanagerapp.feature_app.presentation.ui.theme.*


@Composable
fun HomeContent(
    navigateToRevenueListScreen: () -> Unit,
    navigateToExpenseListScreen: () -> Unit,
    navigateToInventoryItemListScreen: () -> Unit,
    navigateToInventoryListScreen: () -> Unit,
    navigateToStockListScreen: () -> Unit
) {
    BasicScreenColumnWithBottomBar {

        //Revenue Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(LocalSpacing.current.small),
        ) {
            HomeCard(
                title = "Revenue",
                semiTitle = "Manage all your revenue records",
                description = "You can view, add, update and delete all your revenue records here",
                icon = R.drawable.ic_money_filled,
                contentColor = if (isSystemInDarkTheme()) Gold10 else Gold95,
                cardContainerColor = if (isSystemInDarkTheme()) Gold80 else Gold40,
                onOpenCard = {
                    navigateToRevenueListScreen()
                }
            )
        }


        //Expenses Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(LocalSpacing.current.small),
        ) {
            HomeCard(
                title = "Expenses",
                semiTitle = "Manage all your expenses records",
                description = "You can view, add, update and delete all your expenses records here",
                icon = R.drawable.ic_money_filled,
                contentColor = if (isSystemInDarkTheme()) DarkViolet10 else DarkViolet95,
                cardContainerColor = if (isSystemInDarkTheme()) DarkViolet80 else DarkViolet40,
                onOpenCard = {
                    navigateToExpenseListScreen()
                }
            )
        }

        //Inventory Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(LocalSpacing.current.small),
        ) {
            HomeCard(
                title = "Inventory",
                semiTitle = "Manage all your inventory info",
                description = "You can view, add, update and delete all your inventory info here",
                icon = R.drawable.ic_personnel,
                contentColor = if (isSystemInDarkTheme()) Cyan10 else Cyan95,
                cardContainerColor = if (isSystemInDarkTheme()) Cyan80 else Cyan40,
                onOpenCard = {
                    navigateToInventoryListScreen()
                }
            )
        }

        //Inventory Item Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(LocalSpacing.current.small),
        ) {
            HomeCard(
                title = "Inventory Item",
                semiTitle = "Manage all your inventory items",
                description = "You can view, add, update and delete all your inventory items here",
                icon = R.drawable.ic_inventory,
                contentColor = if (isSystemInDarkTheme()) Green10 else Green95,
                cardContainerColor = if (isSystemInDarkTheme()) Green80 else Green40,
                onOpenCard = {
                    navigateToInventoryItemListScreen()
                }
            )
        }

        //Stock Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(LocalSpacing.current.small),
        ) {
            HomeCard(
                title = "Stock",
                semiTitle = "Manage all your stock info",
                description = "You can view, add, update and delete all your stock info here",
                icon = R.drawable.ic_personnel,
                contentColor = if (isSystemInDarkTheme()) Blue10 else Blue95,
                cardContainerColor = if (isSystemInDarkTheme()) Blue80 else Blue40,
                onOpenCard = {
                    navigateToStockListScreen()
                }
            )
        }
    }
}
