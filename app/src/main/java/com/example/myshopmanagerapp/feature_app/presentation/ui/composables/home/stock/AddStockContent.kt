package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.home.stock

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import com.example.myshopmanagerapp.R
import com.example.myshopmanagerapp.core.Constants.emptyString
import com.example.myshopmanagerapp.core.FormRelatedString.AddInventoryQuantity
import com.example.myshopmanagerapp.core.FormRelatedString.DayOfTheWeek
import com.example.myshopmanagerapp.core.FormRelatedString.EnterShortDescription
import com.example.myshopmanagerapp.core.FormRelatedString.InventoryItemQuantityPlaceholder
import com.example.myshopmanagerapp.core.FormRelatedString.Save
import com.example.myshopmanagerapp.core.FormRelatedString.SelectStockDate
import com.example.myshopmanagerapp.core.FormRelatedString.SelectStockItem
import com.example.myshopmanagerapp.core.FormRelatedString.StockItemPlaceholder
import com.example.myshopmanagerapp.core.Functions.generateUniqueStockId
import com.example.myshopmanagerapp.core.Functions.getTotalNumberOfUnits
import com.example.myshopmanagerapp.core.Functions.toDateString
import com.example.myshopmanagerapp.core.Functions.toNotNull
import com.example.myshopmanagerapp.core.ItemQuantities
import com.example.myshopmanagerapp.feature_app.data.local.entities.inventory_items.InventoryItemEntity
import com.example.myshopmanagerapp.feature_app.data.local.entities.stock.StockEntity
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.*
import com.example.myshopmanagerapp.feature_app.presentation.ui.theme.LocalSpacing


@Composable
fun AddStockContent(
    stock: StockEntity,
    mapOfItems: Map<String, String>,
    addStockDate: (String)-> Unit,
    getInventoryItem: (String)-> InventoryItemEntity?,
    addStockQuantities: (ItemQuantities)-> Unit,
    addStockItemId: (String)-> Unit,
    addStockOtherInfo: (String)-> Unit,
    addStock: (StockEntity)-> Unit,
    stockSavingMessage: String?,
    isSavingStockInfo: Boolean,
    stockSavingIsSuccessful: Boolean,
    navigateBack: ()-> Unit
) {
    val context = LocalContext.current

    var itemName by remember {
        mutableStateOf(emptyString)
    }

    var confirmationInfoDialog by remember {
        mutableStateOf(false)
    }


    BasicScreenColumnWithoutBottomBar {
        Box(modifier = Modifier.padding(LocalSpacing.current.small),
            contentAlignment = Alignment.Center
        ){
            DatePickerTextField(
                defaultDate = "${stock.dayOfWeek}, ${stock.date.toDateString()}",
                context = context,
                onValueChange = {_dateString->
                    addStockDate(_dateString)
                },
                label = SelectStockDate
            )
        }

        Box(modifier = Modifier.padding(LocalSpacing.current.small),
            contentAlignment = Alignment.Center
        ){

            BasicTextField1(
                value = stock.dayOfWeek.toNotNull(),
                onValueChange = {},
                placeholder = emptyString,
                label = DayOfTheWeek,
                readOnly = true,
                keyboardType = KeyboardType.Text
            )
        }
        var inventoryItem by remember {
            mutableStateOf<InventoryItemEntity?>(null)
        }
        Box(modifier = Modifier.padding(LocalSpacing.current.small),
            contentAlignment = Alignment.Center
        ){
            AutoCompleteTextField(
                label = SelectStockItem,
                listItems = mapOfItems.keys.toList(),
                placeholder = StockItemPlaceholder,
                readOnly = true,
                expandedIcon = R.drawable.ic_person_filled,
                unexpandedIcon = R.drawable.ic_person_outline,
                getSelectedItem = {
                    itemName = it
                    addStockItemId(mapOfItems[it] ?: emptyString)
                    inventoryItem = getInventoryItem(mapOfItems[it].toNotNull())
                }
            )
        }

        var expandItemQuantity by remember {
            mutableStateOf(false)
        }

        Box(modifier = Modifier.padding(LocalSpacing.current.small),
            contentAlignment = Alignment.Center
        ){
            val totalNumberOfUnits = stock.stockQuantityInfo.getTotalNumberOfUnits().toString()
            ItemQuantityCategorizationTextField(
                value = "Total units: $totalNumberOfUnits units",
                onValueChange = {},
                placeholder = InventoryItemQuantityPlaceholder,
                label = AddInventoryQuantity,
                readOnly = true,
                onClickIcon = {
                    if (inventoryItem == null){
                        Toast.makeText(context, "You have not selected an inventory item", Toast.LENGTH_LONG).show()
                    }
                    else { expandItemQuantity = !expandItemQuantity }
                },
                icon = R.drawable.ic_quantity
            )
        }

        if (inventoryItem != null) {
            AnimatedVisibility(
                modifier = Modifier
                    .padding(LocalSpacing.current.small)
                    .background(MaterialTheme.colorScheme.surface),
                visible = expandItemQuantity
            ) {
                QuantityCategorizationCard(
                    inventoryItem = inventoryItem!!,
                    itemQuantities = inventoryItem!!.itemQuantityInfo ?: emptyList(),
                    discardChanges = { expandItemQuantity = false },
                    getQuantities = {
                        addStockQuantities(it)
                        expandItemQuantity = false
                    }
                )
            }
        }

        Box(modifier = Modifier.padding(LocalSpacing.current.small),
            contentAlignment = Alignment.Center
        ){
            var otherInfo by remember {
                mutableStateOf(stock.otherInfo.toNotNull())
            }
            DescriptionTextFieldWithTrailingIcon(
                value = otherInfo,
                onValueChange = {
                    otherInfo = it
                    addStockOtherInfo(it)
                },
                placeholder = emptyString,
                label = EnterShortDescription ,
                icon = R.drawable.ic_short_notes,
                keyboardType = KeyboardType.Text
            )
        }

        Box(modifier = Modifier.padding(
            horizontal = LocalSpacing.current.small,
            vertical = LocalSpacing.current.smallMedium,
        ),
            contentAlignment = Alignment.Center
        ){
            BasicButton(
                buttonName = Save
            ) {
                val uniqueStockId = generateUniqueStockId("$itemName-${stock.date.toDateString()}")
                when(true){
                    (stock.stockQuantityInfo.isEmpty())->{
                        Toast.makeText(context, "Please add stock quantity", Toast.LENGTH_LONG).show()
                    }
                    (uniqueStockId.isBlank())->{
                        Toast.makeText(context, "Please select stock item", Toast.LENGTH_LONG).show()
                    }
                    (itemName.isBlank())->{
                        Toast.makeText(context, "Please select stock item", Toast.LENGTH_LONG).show()
                    }
                    else->{
                        val stockInfo = stock.copy(uniqueStockId = uniqueStockId)
                        addStock(stockInfo)
                        confirmationInfoDialog = !confirmationInfoDialog
                    }
                }
            }
        }
    }


    ConfirmationInfoDialog(
        openDialog = confirmationInfoDialog,
        isLoading = isSavingStockInfo,
        title = null,
        textContent = stockSavingMessage ?: emptyString,
        unconfirmedDeletedToastText = null,
        confirmedDeleteToastText = null
    ) {
        if (stockSavingIsSuccessful){
            navigateBack()
        }
        confirmationInfoDialog = false
    }
}
