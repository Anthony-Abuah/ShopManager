package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.report.inventory

import androidx.compose.foundation.layout.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import co.yml.charts.common.model.Point
import co.yml.charts.ui.barchart.models.BarData
import com.example.myshopmanagerapp.core.Constants.emptyString
import com.example.myshopmanagerapp.feature_app.domain.model.ItemValue
import com.example.myshopmanagerapp.feature_app.domain.model.ItemValues
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.BarChartCard
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.BasicScreenColumnWithoutBottomBar
import com.example.myshopmanagerapp.feature_app.presentation.ui.theme.Colors
import com.example.myshopmanagerapp.feature_app.presentation.ui.theme.LocalSpacing
import java.util.*
import kotlin.random.Random


@Composable
fun MainInventoryReportContent(
    itemValues: List<ItemValue>,
    totalNumberOfInventoryItems: String,
    inventoryValue: String,
    expectedSalesAmount: String,
    expectedProfitAmount: String,
    expectedProfitPercentage: String,
    mostAvailableInventoryItem: String,
    leastAvailableInventoryItem: String,
    numberOfMostAvailableInventoryItem: String,
    numberOfLeastAvailableInventoryItem: String,
){
    BasicScreenColumnWithoutBottomBar{

        Box(modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = "Inventory Summary",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }

        HorizontalDivider(
            modifier = Modifier.padding(vertical = LocalSpacing.current.small),
            color = MaterialTheme.colorScheme.onBackground,
            thickness = 0.25.dp
        )

        // Number of inventoryItems
        Column(modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = LocalSpacing.current.small),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = "Total number of inventory items",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.Light
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = LocalSpacing.current.small),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = totalNumberOfInventoryItems,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.Normal
                )
            }
        }

        HorizontalDivider(
            modifier = Modifier.padding(vertical = LocalSpacing.current.small),
            color = MaterialTheme.colorScheme.onBackground,
            thickness = 0.25.dp
        )


        // InventoryItem Value Info
        Column(modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = LocalSpacing.current.small),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = "All inventory items value based on current stock",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.Light
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = LocalSpacing.current.small),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = inventoryValue,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.Normal
                )
            }
        }

        HorizontalDivider(
            modifier = Modifier.padding(vertical = LocalSpacing.current.small),
            color = MaterialTheme.colorScheme.onBackground,
            thickness = 0.25.dp
        )

        // Expected Sales Amount
        Column(modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = LocalSpacing.current.small),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = "Expected sales amount based on current stock",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.Light
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = LocalSpacing.current.small),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = expectedSalesAmount,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.Normal
                )
            }
        }

        HorizontalDivider(
            modifier = Modifier.padding(vertical = LocalSpacing.current.small),
            color = MaterialTheme.colorScheme.onBackground,
            thickness = 0.25.dp
        )

        // Expected Profit Amount
        Column(modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = LocalSpacing.current.small),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = "Expected profit amount based on current stock",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.Light
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = LocalSpacing.current.small),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = expectedProfitAmount,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.Normal
                )
            }
        }

        HorizontalDivider(
            modifier = Modifier.padding(vertical = LocalSpacing.current.small),
            color = MaterialTheme.colorScheme.onBackground,
            thickness = 0.25.dp
        )

        // Expected Profit Percentage
        Column(modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = LocalSpacing.current.small),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = "Expected profit percentage based on current stock",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.Light
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = LocalSpacing.current.small),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = expectedProfitPercentage,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.Normal
                )
            }
        }

        HorizontalDivider(
            modifier = Modifier.padding(vertical = LocalSpacing.current.small),
            color = MaterialTheme.colorScheme.onBackground,
            thickness = 0.25.dp
        )

        // Most available item
        Column(modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = LocalSpacing.current.small),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = "Most available inventory item",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.Light
                )
            }

            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .weight(2f)
                        .padding(end = LocalSpacing.current.small),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(
                        text = mostAvailableInventoryItem.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() },
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onBackground,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = FontWeight.Normal
                    )
                }
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = LocalSpacing.current.small),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    Text(
                        text = "$numberOfMostAvailableInventoryItem units",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onBackground,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = FontWeight.Normal
                    )
                }
            }
        }

        HorizontalDivider(
            modifier = Modifier.padding(vertical = LocalSpacing.current.small),
            color = MaterialTheme.colorScheme.onBackground,
            thickness = 0.25.dp
        )


        // Least Available item
        Column(modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = LocalSpacing.current.small),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = "Least available inventory item",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.Light
                )
            }
            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .weight(2f)
                        .padding(end = LocalSpacing.current.small),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(
                        text = leastAvailableInventoryItem.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() },
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onBackground,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = FontWeight.Normal
                    )
                }
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = LocalSpacing.current.small),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    Text(
                        text = "$numberOfLeastAvailableInventoryItem units",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onBackground,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = FontWeight.Normal
                    )
                }
            }
        }

        HorizontalDivider(
            modifier = Modifier.padding(vertical = LocalSpacing.current.small),
            color = MaterialTheme.colorScheme.onBackground,
            thickness = 0.25.dp
        )
        Spacer(modifier = Modifier.height(LocalSpacing.current.medium))
        if (itemValues.isNotEmpty()) {
            val barData = ItemValues(itemValues).toBarData()
            BarChartCard("Inventory", barData )
            Spacer(modifier = Modifier.height(LocalSpacing.current.medium))
        }
    }
}