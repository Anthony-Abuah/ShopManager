package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.records.inventory_item

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.myshopmanagerapp.core.StockEntities
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.BasicScreenColumnWithoutBottomBar
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.records.stock.StockCard


@Composable
fun ViewInventoryStockContent(
    stockEntities: StockEntities,
    inventoryItemName: String,
) {
    val context = LocalContext.current
    Column(modifier = Modifier.fillMaxSize()) {
        HorizontalDivider(thickness = 0.25.dp, color = MaterialTheme.colorScheme.onBackground)
        if (stockEntities.isEmpty()){
                Box(modifier = Modifier.fillMaxSize()
                    .background(MaterialTheme.colorScheme.background),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "No stock inventories to show",
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
                stockEntities.forEachIndexed {index, stock ->
                    StockCard(
                        stock = stock,
                        number = index.plus(1).toString(),
                        edit = { Toast.makeText(context, "Cannot edit this stock", Toast.LENGTH_LONG).show() },
                        delete = { Toast.makeText(context, "Cannot delete stock here", Toast.LENGTH_LONG).show() },
                        showAll = { Toast.makeText(context, "Showing all stock items", Toast.LENGTH_LONG).show() },
                        inventoryItemName = inventoryItemName
                    )
                }
            }
        }
    }
}
