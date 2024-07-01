package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.home.stock

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.myshopmanagerapp.core.Constants.ONE
import com.example.myshopmanagerapp.core.Constants.emptyString
import com.example.myshopmanagerapp.core.StockEntities
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.BasicScreenColumnWithoutBottomBar
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.ConfirmationInfoDialog
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.DeleteConfirmationDialog
import com.example.myshopmanagerapp.feature_app.presentation.ui.theme.LocalSpacing


@Composable
fun StockListContent(
    allStocks: StockEntities?,
    deleteMessage: String?,
    deleteIsSuccessful: Boolean,
    isDeletingStock: Boolean,
    onConfirmDelete: (String)-> Unit,
    reloadStockItems: ()-> Unit,
    getInventoryItemName: (String)-> String,
    navigateToViewStockScreen: (String)-> Unit
) {
    val context = LocalContext.current
    var openDeleteConfirmation by remember {
        mutableStateOf(false)
    }
    var deleteConfirmationInfoDialog by remember {
        mutableStateOf(false)
    }
    var uniqueStockId by remember {
        mutableStateOf(emptyString)
    }

    if (allStocks.isNullOrEmpty()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "No stocks to show!",
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    } else {
        BasicScreenColumnWithoutBottomBar {
            HorizontalDivider()
            val groupedStocks = allStocks.groupBy { it.uniqueInventoryItemId }
            groupedStocks.keys.forEachIndexed { index, key ->
                Column(
                    modifier = Modifier.padding(LocalSpacing.current.small),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    var showAllItemStocks by remember {
                        mutableStateOf(false)
                    }
                    val groupedInventoryStock = groupedStocks[key]?.sortedByDescending { it.date } ?: emptyList()

                    if (groupedInventoryStock.isNotEmpty()) {
                        StockCard(
                            stock = groupedInventoryStock.first(),
                            number = if (showAllItemStocks) ONE.toString() else index.plus(1).toString(),
                            showAllItems = showAllItemStocks,
                            open = { navigateToViewStockScreen(groupedInventoryStock.first().uniqueStockId) },
                            edit = { navigateToViewStockScreen(groupedInventoryStock.first().uniqueStockId) },
                            showAll = { showAllItemStocks = ! showAllItemStocks },
                            delete = {
                                uniqueStockId = groupedInventoryStock.first().uniqueStockId
                                openDeleteConfirmation = !openDeleteConfirmation },
                            inventoryItemName = getInventoryItemName(groupedInventoryStock.first().uniqueInventoryItemId)
                        )
                        AnimatedVisibility(
                            modifier = Modifier
                                .background(MaterialTheme.colorScheme.surface),
                            visible = showAllItemStocks) {
                            Column(modifier = Modifier.fillMaxWidth()
                                .padding(LocalSpacing.current.small)
                            ) {
                                groupedInventoryStock.minus(groupedInventoryStock.first())
                                    .forEachIndexed { index, _stock ->
                                        StockCard(
                                            stock = _stock,
                                            number = index.plus(2).toString(),
                                            showAllItems = showAllItemStocks,
                                            open = { navigateToViewStockScreen(_stock.uniqueStockId) },
                                            edit = { Toast.makeText(context, "Cannot update or delete this stock", Toast.LENGTH_LONG).show() },
                                            delete = { Toast.makeText(context, "Cannot delete this stock", Toast.LENGTH_LONG).show() },
                                            showAll = { showAllItemStocks = !showAllItemStocks },
                                            inventoryItemName = getInventoryItemName(_stock.uniqueInventoryItemId)
                                        )
                                    }
                            }
                        }
                    }
                }
            }
        }
        DeleteConfirmationDialog(
            openDialog = openDeleteConfirmation,
            title = "Delete Stock",
            textContent = "Are you sure you want to permanently delete this stock",
            unconfirmedDeletedToastText = "Stock not deleted",
            confirmedDeleteToastText = null,
            confirmDelete = {
                onConfirmDelete(uniqueStockId)
                deleteConfirmationInfoDialog = !deleteConfirmationInfoDialog
            }
        ) {
            openDeleteConfirmation = false
        }

        ConfirmationInfoDialog(
            openDialog = deleteConfirmationInfoDialog,
            isLoading = isDeletingStock,
            title = null,
            textContent = deleteMessage ?: emptyString,
            unconfirmedDeletedToastText = null,
            confirmedDeleteToastText = null
        ) {
            if (deleteIsSuccessful){
                reloadStockItems()
            }
            deleteConfirmationInfoDialog = false
        }
    }
}
