package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.report.inventory

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myshopmanagerapp.R
import com.example.myshopmanagerapp.core.Constants.listOfPeriods
import com.example.myshopmanagerapp.core.FormRelatedString.ExpectedProfit
import com.example.myshopmanagerapp.core.FormRelatedString.ExpectedProfitPercentage
import com.example.myshopmanagerapp.core.FormRelatedString.ExpectedSales
import com.example.myshopmanagerapp.core.FormRelatedString.InventoryItemValue
import com.example.myshopmanagerapp.core.FormRelatedString.LeastAvailableItem
import com.example.myshopmanagerapp.core.FormRelatedString.LeastExpensiveItem
import com.example.myshopmanagerapp.core.FormRelatedString.MostAvailableItem
import com.example.myshopmanagerapp.core.FormRelatedString.MostExpensiveItem
import com.example.myshopmanagerapp.core.FormRelatedString.NumberOfInventoryItems
import com.example.myshopmanagerapp.core.Functions.toEllipses
import com.example.myshopmanagerapp.feature_app.domain.model.ItemValue
import com.example.myshopmanagerapp.feature_app.domain.model.ItemValues
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.BarChartCard
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.HorizontalInfoDisplayCard
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.InfoDisplayCard
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.TimeRange
import com.example.myshopmanagerapp.feature_app.presentation.ui.theme.*


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
    priceOfMostExpensiveInventoryItem: String,
    priceOfLeastExpensiveInventoryItem: String,
    getSelectedPeriod: (String)-> Unit,
    navigateToViewInventoryItemsScreen: ()-> Unit,
){
    val mainBackgroundColor = if (isSystemInDarkTheme()) Grey10 else Grey99
    val alternateBackgroundColor = if (isSystemInDarkTheme()) Grey15 else Grey95
    val cardBackgroundColor = if (isSystemInDarkTheme()) Grey15 else BlueGrey90

    Column(modifier = Modifier
        .background(mainBackgroundColor)
        .fillMaxSize()
    ) {
        val listOfPeriods = listOfPeriods.map { it.titleText }
        Box(
            modifier = Modifier.padding(LocalSpacing.current.smallMedium),
            contentAlignment = Alignment.Center
        ) {
            TimeRange(listOfTimes = listOfPeriods.dropLast(1),
                getSelectedItem = { selectedPeriod ->
                    getSelectedPeriod(selectedPeriod)
                }
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .background(mainBackgroundColor)
                .padding(LocalSpacing.current.noPadding)
                .verticalScroll(state = rememberScrollState(), enabled = true),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
        ) {
            Box(
                modifier = Modifier
                    .background(mainBackgroundColor)
                    .fillMaxWidth()
                    .height(150.dp)
                    .padding(LocalSpacing.current.small),
                contentAlignment = Alignment.Center
            ) {
                InfoDisplayCard(
                    image = R.drawable.inventory_item,
                    currency = currency,
                    currencySize = 36.sp,
                    bigText = "$currency $totalInventoryItemsValue",
                    bigTextSize = 28.sp,
                    smallText = InventoryItemValue,
                    smallTextSize = 16.sp,
                    backgroundColor = cardBackgroundColor,
                    elevation = LocalSpacing.current.small,
                    isAmount = false
                )
            }

            Spacer(modifier = Modifier.height(LocalSpacing.current.smallMedium))

            Box(
                modifier = Modifier
                    .background(alternateBackgroundColor)
                    .fillMaxWidth()
                    .height(LocalSpacing.current.textFieldHeight)
                    .clickable {navigateToViewInventoryItemsScreen()},
                contentAlignment = Alignment.Center
            ) {
                HorizontalInfoDisplayCard(
                    icon = R.drawable.quantity,
                    modifier = Modifier.padding(LocalSpacing.current.small),
                    name = NumberOfInventoryItems,
                    nameTextSize = 16.sp,
                    valueText = "$numberOfInventoryItems item(s)",
                    valueTextSize = 16.sp
                )
            }

            Box(
                modifier = Modifier
                    .background(mainBackgroundColor)
                    .fillMaxWidth()
                    .height(LocalSpacing.current.textFieldHeight)
                    .clickable {},
                contentAlignment = Alignment.Center
            ) {
                HorizontalInfoDisplayCard(
                    icon = R.drawable.shop_value,
                    modifier = Modifier.padding(LocalSpacing.current.small),
                    name = ExpectedSales,
                    nameTextSize = 16.sp,
                    valueText = "$currency $expectedSalesAmount",
                    valueTextSize = 16.sp
                )
            }


            Box(
                modifier = Modifier
                    .background(alternateBackgroundColor)
                    .fillMaxWidth()
                    .height(LocalSpacing.current.textFieldHeight)
                    .clickable {},
                contentAlignment = Alignment.Center
            ) {
                HorizontalInfoDisplayCard(
                    icon = R.drawable.profit,
                    modifier = Modifier.padding(LocalSpacing.current.small),
                    name = ExpectedProfit,
                    nameTextSize = 16.sp,
                    valueText = "$currency $expectedProfitAmount",
                    valueTextSize = 16.sp
                )
            }


            Box(
                modifier = Modifier
                    .background(mainBackgroundColor)
                    .fillMaxWidth()
                    .height(LocalSpacing.current.textFieldHeight)
                    .clickable {},
                contentAlignment = Alignment.Center
            ) {
                HorizontalInfoDisplayCard(
                    icon = R.drawable.percentage,
                    modifier = Modifier.padding(LocalSpacing.current.small),
                    name = ExpectedProfitPercentage,
                    nameTextSize = 16.sp,
                    valueText = "$expectedProfitPercentage %",
                    valueTextSize = 16.sp
                )
            }

            Box(
                modifier = Modifier
                    .background(alternateBackgroundColor)
                    .fillMaxWidth()
                    .wrapContentHeight(),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .padding(
                            vertical = LocalSpacing.current.smallMedium,
                            horizontal = LocalSpacing.current.small
                        )
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    contentAlignment = Alignment.Center
                ) {
                    if (inventoryItems.isNotEmpty()) {
                        val barData = ItemValues(inventoryItems).toBarData()
                        BarChartCard("Inventory Items", barData)
                        Spacer(modifier = Modifier.height(LocalSpacing.current.medium))
                    } else {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(400.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "No chart to display",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }

            Column(modifier = Modifier
                .background(mainBackgroundColor)
                .fillMaxWidth()
                .height(600.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(LocalSpacing.current.smallMedium))
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(LocalSpacing.current.small),
                    contentAlignment = Alignment.Center
                ) {
                    InfoDisplayCard(
                        image = R.drawable.quantity,
                        imageWidth = 32.dp,
                        currency = currency,
                        currencySize = 20.sp,
                        bigText = "${mostAvailableInventoryItem.toEllipses(35)}\n$numberOfMostAvailableInventoryItem item(s)",
                        bigTextSize = 18.sp,
                        smallText = MostAvailableItem,
                        smallTextSize = 12.sp,
                        backgroundColor = cardBackgroundColor,
                        shape = MaterialTheme.shapes.medium,
                        elevation = LocalSpacing.current.small,
                        isAmount = false
                    )
                }
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(LocalSpacing.current.small),
                    contentAlignment = Alignment.Center
                ) {
                    InfoDisplayCard(
                        image = R.drawable.quantity,
                        imageWidth = 32.dp,
                        currency = currency,
                        currencySize = 20.sp,
                        bigText = "${leastAvailableInventoryItem.toEllipses(35)}\n$numberOfLeastAvailableInventoryItem item(s)",
                        bigTextSize = 18.sp,
                        smallText = LeastAvailableItem,
                        smallTextSize = 12.sp,
                        backgroundColor = cardBackgroundColor,
                        shape = MaterialTheme.shapes.medium,
                        elevation = LocalSpacing.current.small,
                        isAmount = false
                    )
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(LocalSpacing.current.small),
                    contentAlignment = Alignment.Center
                ) {
                    InfoDisplayCard(
                        image = R.drawable.price,
                        imageWidth = 32.dp,
                        currency = currency,
                        currencySize = 20.sp,
                        bigText = "${mostExpensiveInventoryItem.toEllipses(35)}\n$currency $priceOfMostExpensiveInventoryItem",
                        bigTextSize = 18.sp,
                        smallText = MostExpensiveItem,
                        smallTextSize = 12.sp,
                        backgroundColor = cardBackgroundColor,
                        shape = MaterialTheme.shapes.medium,
                        elevation = LocalSpacing.current.small,
                        isAmount = false
                    )
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(LocalSpacing.current.small),
                    contentAlignment = Alignment.Center
                ) {
                    InfoDisplayCard(
                        image = R.drawable.price,
                        imageWidth = 32.dp,
                        currency = currency,
                        currencySize = 20.sp,
                        bigText = "${leastExpensiveInventoryItem.toEllipses(35)}\n$currency $priceOfLeastExpensiveInventoryItem",
                        bigTextSize = 18.sp,
                        smallText = LeastExpensiveItem,
                        smallTextSize = 12.sp,
                        backgroundColor = cardBackgroundColor,
                        shape = MaterialTheme.shapes.medium,
                        elevation = LocalSpacing.current.small,
                        isAmount = false
                    )
                }
            }
            Spacer(modifier = Modifier.height(LocalSpacing.current.default))

        }
    }

}