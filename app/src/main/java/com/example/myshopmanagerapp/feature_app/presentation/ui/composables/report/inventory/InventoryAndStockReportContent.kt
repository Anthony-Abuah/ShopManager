package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.report.inventory

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.myshopmanagerapp.core.FormRelatedString.ExpectedProfit
import com.example.myshopmanagerapp.core.FormRelatedString.ExpectedProfitPercentage
import com.example.myshopmanagerapp.core.FormRelatedString.ExpectedSales
import com.example.myshopmanagerapp.core.FormRelatedString.InventoryItemValue
import com.example.myshopmanagerapp.core.FormRelatedString.LeastAvailableItem
import com.example.myshopmanagerapp.core.FormRelatedString.LeastExpensiveItem
import com.example.myshopmanagerapp.core.FormRelatedString.MostAvailableItem
import com.example.myshopmanagerapp.core.FormRelatedString.MostExpensiveItem
import com.example.myshopmanagerapp.core.FormRelatedString.NumberOfInventoryItems
import com.example.myshopmanagerapp.feature_app.domain.model.ItemValue
import com.example.myshopmanagerapp.feature_app.domain.model.ItemValues
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.BarChartCard
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.BasicScreenColumnWithoutBottomBar
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.ViewTextValueRow
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.ViewTextValueWithExtraValueRow
import com.example.myshopmanagerapp.feature_app.presentation.ui.theme.LocalSpacing


@Composable
fun InventoryAndStockReportContent(
    currency: String,
    inventoryItems: List<ItemValue>,
    numberOfInventoryItems: String,
    totalInventoryItemsValue: String,
    expectedSalesAmount: String,
    expectedProfitAmount: String,
    expectedProfitPercentage: String,
    mostAvailableInventoryItem: String,
    leastAvailableInventoryItem: String,
    numberOfMostAvailableInventoryItem: String,
    numberOfLeastAvailableInventoryItem: String,
    mostExpensiveInventoryItem: String,
    leastExpensiveInventoryItem: String,
    numberOfMostExpensiveInventoryItem: String,
    numberOfLeastExpensiveInventoryItem: String,
    navigateToViewInventoryItemsScreen: ()-> Unit,
){
    BasicScreenColumnWithoutBottomBar{
        HorizontalDivider()

        // Number of inventoryItems
        ViewTextValueRow(
            viewTitle = NumberOfInventoryItems,
            viewValue = "$numberOfInventoryItems item(s)",
            icon = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            showInfo = true,
            onClick = navigateToViewInventoryItemsScreen
        )

        HorizontalDivider()

        // InventoryItem Value Info
        ViewTextValueRow(
            viewTitle = InventoryItemValue,
            viewValue = "$currency $totalInventoryItemsValue"
        )

        HorizontalDivider()

        // Expected Sales Amount
        ViewTextValueRow(
            viewTitle = ExpectedSales,
            viewValue = "$currency $expectedSalesAmount"
        )

        HorizontalDivider()


        // Expected Profit Amount
        ViewTextValueRow(
            viewTitle = ExpectedProfit,
            viewValue = "$currency $expectedProfitAmount"
        )

        HorizontalDivider()

        // Expected Profit Percentage
        ViewTextValueRow(
            viewTitle = ExpectedProfitPercentage,
            viewValue = "$expectedProfitPercentage %"
        )

        HorizontalDivider()

        // Most available item
        ViewTextValueWithExtraValueRow(
            viewTitle = MostAvailableItem,
            viewValue = mostAvailableInventoryItem,
            extraValue = "$numberOfMostAvailableInventoryItem unit(s)"
        )

        HorizontalDivider()


        // Least available item
        ViewTextValueWithExtraValueRow(
            viewTitle = LeastAvailableItem,
            viewValue = leastAvailableInventoryItem,
            extraValue = "$numberOfLeastAvailableInventoryItem unit(s)"
        )

        HorizontalDivider()

        // Most expensive item
        ViewTextValueWithExtraValueRow(
            viewTitle = MostExpensiveItem,
            viewValue = mostExpensiveInventoryItem,
            extraValue = "$currency $numberOfMostExpensiveInventoryItem"
        )

        HorizontalDivider()

        // Least expensive item
        ViewTextValueWithExtraValueRow(
            viewTitle = LeastExpensiveItem,
            viewValue = leastExpensiveInventoryItem,
            extraValue = "$currency $numberOfLeastExpensiveInventoryItem"
        )

        HorizontalDivider()

        Spacer(modifier = Modifier.height(LocalSpacing.current.medium))
        if (inventoryItems.isNotEmpty()) {
            val barData = ItemValues(inventoryItems).toBarData()
            BarChartCard("Inventory Items", barData )
            Spacer(modifier = Modifier.height(LocalSpacing.current.medium))
        }
    }
}