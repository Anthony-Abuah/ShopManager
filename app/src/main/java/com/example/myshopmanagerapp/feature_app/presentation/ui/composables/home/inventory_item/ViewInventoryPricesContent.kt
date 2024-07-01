package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.home.inventory_item

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.myshopmanagerapp.core.Prices
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.BasicScreenColumnWithoutBottomBar


@Composable
fun ViewInventoryPricesContent(
    prices: Prices,
    inventoryItemName: String,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        HorizontalDivider(thickness = 0.25.dp, color = MaterialTheme.colorScheme.onBackground)
        if (prices.isEmpty()){
                Box(modifier = Modifier.fillMaxSize()
                    .background(MaterialTheme.colorScheme.background),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "No prices to show",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Normal,
                        color = MaterialTheme.colorScheme.onBackground

                    )
                }
            }
        else {
            BasicScreenColumnWithoutBottomBar {
                prices.forEachIndexed {index, price ->
                    PriceCard(
                        price = price,
                        number = index.plus(1).toString(),
                        currency = "GHS",
                        inventoryItemName = inventoryItemName
                    )
                }
            }
        }
    }
}
