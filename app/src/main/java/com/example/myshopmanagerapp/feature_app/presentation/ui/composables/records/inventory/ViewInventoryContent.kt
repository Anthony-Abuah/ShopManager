package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.records.inventory

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.myshopmanagerapp.R
import com.example.myshopmanagerapp.core.Constants.emptyString
import com.example.myshopmanagerapp.core.FormRelatedString.AmountPlaceholder
import com.example.myshopmanagerapp.core.FormRelatedString.Date
import com.example.myshopmanagerapp.core.FormRelatedString.DayOfTheWeek
import com.example.myshopmanagerapp.core.FormRelatedString.EnterReceiptId
import com.example.myshopmanagerapp.core.FormRelatedString.EnterShortDescription
import com.example.myshopmanagerapp.core.FormRelatedString.EnterTotalAmount
import com.example.myshopmanagerapp.core.FormRelatedString.EnterUnitCostPrice
import com.example.myshopmanagerapp.core.FormRelatedString.InventoryInformation
import com.example.myshopmanagerapp.core.FormRelatedString.InventoryItemName
import com.example.myshopmanagerapp.core.FormRelatedString.InventoryOtherInfoPlaceholder
import com.example.myshopmanagerapp.core.FormRelatedString.InventoryQuantity
import com.example.myshopmanagerapp.core.FormRelatedString.InventoryReceiptId
import com.example.myshopmanagerapp.core.FormRelatedString.InventoryReceiptPlaceholder
import com.example.myshopmanagerapp.core.FormRelatedString.ShortNotes
import com.example.myshopmanagerapp.core.FormRelatedString.TotalInventoryCost
import com.example.myshopmanagerapp.core.FormRelatedString.UniqueInventoryId
import com.example.myshopmanagerapp.core.FormRelatedString.UnitCostPrice
import com.example.myshopmanagerapp.core.FormRelatedString.UpdateChanges
import com.example.myshopmanagerapp.core.Functions.convertToDouble
import com.example.myshopmanagerapp.core.Functions.getTotalNumberOfUnits
import com.example.myshopmanagerapp.core.Functions.toDate
import com.example.myshopmanagerapp.core.Functions.toDateString
import com.example.myshopmanagerapp.core.Functions.toNotNull
import com.example.myshopmanagerapp.core.ItemQuantities
import com.example.myshopmanagerapp.feature_app.data.local.entities.inventory.InventoryEntity
import com.example.myshopmanagerapp.feature_app.data.local.entities.inventory_items.InventoryItemEntity
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.*
import com.example.myshopmanagerapp.feature_app.presentation.ui.theme.LocalSpacing


@Composable
fun ViewInventoryContent(
    inventory: InventoryEntity,
    updateInventoryMessage: String?,
    inventoryUpdatingIsSuccessful: Boolean,
    isUpdatingInventory: Boolean,
    currency: String,
    getInventoryItem: (String) -> InventoryItemEntity,
    getUpdatedInventoryDate: (String) -> Unit,
    getUpdatedInventoryItemQuantity: (ItemQuantities) -> Unit,
    getUpdatedReceiptId: (String) -> Unit,
    getUpdatedCostPrices: (String, String) -> Unit,
    getUpdatedOtherInfo: (String) -> Unit,
    updateInventory: (InventoryEntity) -> Unit,
    navigateBack: () -> Unit,
) {

    var confirmationInfoDialog by remember {
        mutableStateOf(false)
    }

    var totalNumberOfUnits by remember {
        mutableStateOf(inventory.quantityInfo.getTotalNumberOfUnits().toString())
    }

    BasicScreenColumnWithoutBottomBar {
        // Inventory Photo
        ViewPhoto(icon = R.drawable.ic_inventory)

        HorizontalDivider(thickness = LocalSpacing.current.extraSmall)

        //Inventory Info
        ViewInfo(InventoryInformation)

        HorizontalDivider(thickness = LocalSpacing.current.extraSmall)

        // Unique Inventory Id
        ViewTextValueRow(
            viewTitle = UniqueInventoryId,
            viewValue = inventory.uniqueInventoryId
        )

        HorizontalDivider()

        // Date
        ViewOrUpdateDateValueRow(
            viewTitle = Date,
            viewDateString = "${inventory.dayOfWeek}, ${inventory.date.toDate().toDateString()}",
            getUpdatedDate = { getUpdatedInventoryDate(it) }
        )

        HorizontalDivider()

        // DayOfWeek
        ViewTextValueRow(
            viewTitle = DayOfTheWeek,
            viewValue = inventory.dayOfWeek,
        )

        HorizontalDivider()

        // Inventory Item Name
        ViewTextValueRow(
            viewTitle = InventoryItemName,
            viewValue = getInventoryItem(inventory.uniqueInventoryItemId).inventoryItemName
        )

        HorizontalDivider()

        // Inventory Unit Cost
        ViewOrUpdateNumberValueRow(
            viewTitle = UnitCostPrice,
            viewValue = "$currency ${inventory.unitCostPrice}",
            insideValue = inventory.unitCostPrice.toString(),
            placeholder = AmountPlaceholder,
            label = EnterUnitCostPrice,
            icon = R.drawable.ic_money_filled,
            getUpdatedValue = {
                val totalCostPrice = inventory.quantityInfo.getTotalNumberOfUnits().times(convertToDouble(it))
                getUpdatedCostPrices(it, totalCostPrice.toString())
            }
        )

        HorizontalDivider()

        // Item Total Cost
        ViewOrUpdateNumberValueRow(
            viewTitle = TotalInventoryCost,
            viewValue = "$currency ${inventory.totalCostPrice}",
            insideValue = inventory.totalCostPrice.toString(),
            placeholder = AmountPlaceholder,
            label = EnterTotalAmount,
            icon = R.drawable.ic_money_filled,
            getUpdatedValue = {
                val unitCostPrice = convertToDouble(it).div(inventory.quantityInfo.getTotalNumberOfUnits())
                getUpdatedCostPrices(unitCostPrice.toString(), it)
            }
        )

        HorizontalDivider()

        var openInventoryDisplay by remember {
            mutableStateOf(false)
        }
        totalNumberOfUnits = inventory.totalNumberOfUnits.toString()
        // Inventory Quantity
        ViewTextValueRow(
            viewTitle = InventoryQuantity,
            viewValue = totalNumberOfUnits.plus(" units"),
            showInfo = true,
            icon = Icons.Default.Edit,
            onClick = { openInventoryDisplay = !openInventoryDisplay }
        )
        AnimatedVisibility(
            modifier = Modifier
                .padding(LocalSpacing.current.small)
                .background(MaterialTheme.colorScheme.surfaceVariant),
            visible = openInventoryDisplay
        ){
            UpdateInventoryItemQuantitiesAndCost(
                closeInventoryDisplay = { openInventoryDisplay = false },
                inventory = inventory,
                inventoryItem = getInventoryItem(inventory.uniqueInventoryItemId),
                addInventoryQuantities = { getUpdatedInventoryItemQuantity(it)
                    //totalNumberOfUnits = it.quantityInfo.getTotalNumberOfUnits().toString()
                }
            )
        }

        HorizontalDivider(
            modifier = Modifier.padding(vertical = LocalSpacing.current.small),
            color = MaterialTheme.colorScheme.onBackground,
            thickness = 0.25.dp
        )

        // Receipt Id
        ViewOrUpdateTextValueRow(
            viewTitle = InventoryReceiptId,
            viewValue = inventory.receiptId.toNotNull(),
            placeholder = InventoryReceiptPlaceholder,
            label = EnterReceiptId,
            icon = R.drawable.ic_receipt,
            getUpdatedValue = { getUpdatedReceiptId(it) }
        )

        HorizontalDivider()

        // Inventory Other Info
        ViewOrUpdateDescriptionValueRow(
            viewTitle = ShortNotes,
            viewValue = inventory.otherInfo.toNotNull(),
            placeholder = InventoryOtherInfoPlaceholder,
            label = EnterShortDescription,
            icon = R.drawable.ic_short_notes,
            getUpdatedValue = {getUpdatedOtherInfo(it)}
        )


        HorizontalDivider(
            modifier = Modifier.padding(vertical = LocalSpacing.current.small),
            color = MaterialTheme.colorScheme.onBackground,
            thickness = 0.25.dp
        )

        Box(modifier = Modifier.padding(LocalSpacing.current.smallMedium)){
            BasicButton(buttonName = UpdateChanges) {
                confirmationInfoDialog = !confirmationInfoDialog
                updateInventory(inventory)
            }
        }

        ConfirmationInfoDialog(
            openDialog = confirmationInfoDialog,
            isLoading = isUpdatingInventory,
            title = null,
            textContent = updateInventoryMessage ?: emptyString,
            unconfirmedDeletedToastText = null,
            confirmedDeleteToastText = null
        ) {
            confirmationInfoDialog = false
            if (inventoryUpdatingIsSuccessful) {
                navigateBack()
            }
        }
    }
}
