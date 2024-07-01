package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.home.stock

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.myshopmanagerapp.R
import com.example.myshopmanagerapp.core.FormRelatedString
import com.example.myshopmanagerapp.core.FormRelatedString.AddInventoryQuantity
import com.example.myshopmanagerapp.core.FormRelatedString.Date
import com.example.myshopmanagerapp.core.FormRelatedString.DayOfTheWeek
import com.example.myshopmanagerapp.core.FormRelatedString.InventoryItemQuantityPlaceholder
import com.example.myshopmanagerapp.core.FormRelatedString.IsInventoryStock
import com.example.myshopmanagerapp.core.FormRelatedString.ShortNotes
import com.example.myshopmanagerapp.core.FormRelatedString.ShortNotesPlaceholder
import com.example.myshopmanagerapp.core.FormRelatedString.StockInformation
import com.example.myshopmanagerapp.core.FormRelatedString.StockItemName
import com.example.myshopmanagerapp.core.FormRelatedString.StockQuantity
import com.example.myshopmanagerapp.core.FormRelatedString.UniqueStockId
import com.example.myshopmanagerapp.core.FormRelatedString.UpdateChanges
import com.example.myshopmanagerapp.core.Functions.getTotalNumberOfUnits
import com.example.myshopmanagerapp.core.Functions.toDate
import com.example.myshopmanagerapp.core.Functions.toDateString
import com.example.myshopmanagerapp.core.Functions.toNotNull
import com.example.myshopmanagerapp.core.ItemQuantities
import com.example.myshopmanagerapp.feature_app.data.local.entities.inventory_items.InventoryItemEntity
import com.example.myshopmanagerapp.feature_app.data.local.entities.stock.StockEntity
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.*
import com.example.myshopmanagerapp.feature_app.presentation.ui.theme.LocalSpacing

@Composable
fun ViewStockContent(
    stock: StockEntity,
    updateStockMessage: String?,
    stockUpdatingIsSuccessful: Boolean,
    getInventoryItem: (String)-> InventoryItemEntity?,
    getUpdatedStockDate: (String)-> Unit,
    getUpdatedOtherInfo: (String)-> Unit,
    getUpdatedStockQuantities: (ItemQuantities)-> Unit,
    updateStock: (StockEntity)-> Unit,
    isUpdatingStockInfo: Boolean,
    navigateBack: ()-> Unit
) {
    val context = LocalContext.current

    var confirmationInfoDialog by remember {
        mutableStateOf(false)
    }

    BasicScreenColumnWithoutBottomBar {
        // Stock Photo
        ViewPhoto(icon = R.drawable.ic_inventory)

        HorizontalDivider(thickness = LocalSpacing.current.extraSmall)

        ViewInfo(StockInformation)

        HorizontalDivider(thickness = LocalSpacing.current.extraSmall)

        // Unique Inventory Id
        ViewTextValueRow(
            viewTitle = UniqueStockId,
            viewValue = stock.uniqueStockId
        )

        HorizontalDivider()

        // Date
        ViewOrUpdateDateValueRow(
            viewTitle = Date,
            viewDateString = "${stock.dayOfWeek}, ${stock.date.toDate().toDateString()}",
            getUpdatedDate = { getUpdatedStockDate(it) }
        )

        HorizontalDivider()

        // DayOfWeek
        ViewTextValueRow(
            viewTitle = DayOfTheWeek,
            viewValue = stock.dayOfWeek.toNotNull(),
        )

        HorizontalDivider()

        // Item Name
        ViewTextValueRow(
            viewTitle = StockItemName,
            viewValue = getInventoryItem(stock.uniqueInventoryItemId)?.inventoryItemName.toNotNull(),
        )

        HorizontalDivider()

        ViewTextValueRow(
            viewTitle = IsInventoryStock,
            viewValue = if(stock.isInventoryStock) "Yes" else "No",
        )

        HorizontalDivider()

        var openItemQuantityCard by remember {
            mutableStateOf(false)
        }
        // Stock Quantity
        ViewTextValueRow(
            viewTitle = StockQuantity,
            viewValue = "${stock.totalNumberOfUnits} units",
            onClick = { openItemQuantityCard = !openItemQuantityCard }
        )

        AnimatedVisibility(visible = openItemQuantityCard) {

            var expandItemQuantity by remember {
                mutableStateOf(false)
            }
            Box(
                modifier = Modifier.padding(LocalSpacing.current.small),
                contentAlignment = Alignment.Center
            ) {
                val totalNumberOfUnits = stock.stockQuantityInfo.getTotalNumberOfUnits().toString()
                ItemQuantityCategorizationTextField(
                    value = "Total units: $totalNumberOfUnits units",
                    onValueChange = {},
                    placeholder = InventoryItemQuantityPlaceholder,
                    label = AddInventoryQuantity,
                    readOnly = true,
                    onClickIcon = {
                        if (getInventoryItem(stock.uniqueInventoryItemId) == null) {
                            Toast.makeText(context, "You have not selected an inventory item", Toast.LENGTH_LONG).show()
                        } else {
                            expandItemQuantity = !expandItemQuantity
                        }
                    },
                    icon = R.drawable.ic_quantity
                )
            }

            if (getInventoryItem(stock.uniqueInventoryItemId) != null) {
                AnimatedVisibility(
                    modifier = Modifier
                        .padding(LocalSpacing.current.small)
                        .background(MaterialTheme.colorScheme.surface),
                    visible = expandItemQuantity
                ) {
                    QuantityCategorizationCard(
                        inventoryItem = getInventoryItem(stock.uniqueInventoryItemId)!!,
                        itemQuantities = getInventoryItem(stock.uniqueInventoryItemId)!!.itemQuantityInfo ?: emptyList(),
                        discardChanges = { expandItemQuantity = false },
                        getQuantities = {
                            getUpdatedStockQuantities(it)
                            expandItemQuantity = false
                        }
                    )
                }
            }
        }
        HorizontalDivider()

        // Stock Short Notes
        ViewOrUpdateDescriptionValueRow(
            viewTitle = ShortNotes,
            viewValue = stock.otherInfo.toNotNull(),
            placeholder = ShortNotesPlaceholder,
            label = FormRelatedString.EnterShortDescription,
            icon = R.drawable.ic_short_notes,
            getUpdatedValue = {getUpdatedOtherInfo(it)}
        )

        HorizontalDivider()

        if (!stock.isInventoryStock) {
            Box(modifier = Modifier.padding(LocalSpacing.current.medium)) {
                BasicButton(
                    buttonName = UpdateChanges
                ) {
                    when(true){
                        (getInventoryItem(stock.uniqueInventoryItemId)?.inventoryItemName.toNotNull().isBlank()) -> {
                            Toast.makeText(context, "Please select inventory item", Toast.LENGTH_LONG).show()
                        }
                        else->{
                            updateStock(stock)
                            confirmationInfoDialog = !confirmationInfoDialog
                        }
                    }
                }
            }
        }

        ConfirmationInfoDialog(
            openDialog = confirmationInfoDialog,
            isLoading = isUpdatingStockInfo,
            title = null,
            textContent = updateStockMessage.toNotNull(),
            unconfirmedDeletedToastText = null,
            confirmedDeleteToastText = null
        ) {
            confirmationInfoDialog = false
            if (stockUpdatingIsSuccessful) {
                navigateBack()
            }
        }
    }
}
