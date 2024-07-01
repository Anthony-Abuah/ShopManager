package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.home.inventory_item

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import com.example.myshopmanagerapp.R
import com.example.myshopmanagerapp.core.Constants.NotAvailable
import com.example.myshopmanagerapp.core.Constants.emptyString
import com.example.myshopmanagerapp.core.FormRelatedString.AddCategory
import com.example.myshopmanagerapp.core.FormRelatedString.AddManufacturer
import com.example.myshopmanagerapp.core.FormRelatedString.CategoryNotAdded
import com.example.myshopmanagerapp.core.FormRelatedString.CategoryPlaceholder
import com.example.myshopmanagerapp.core.FormRelatedString.EnterInventoryItemManufacturer
import com.example.myshopmanagerapp.core.FormRelatedString.EnterInventoryItemName
import com.example.myshopmanagerapp.core.FormRelatedString.EnterItemCategory
import com.example.myshopmanagerapp.core.FormRelatedString.EnterManufacturerName
import com.example.myshopmanagerapp.core.FormRelatedString.EnterShortDescription
import com.example.myshopmanagerapp.core.FormRelatedString.InventoryItemCategory
import com.example.myshopmanagerapp.core.FormRelatedString.InventoryItemCategoryPlaceholder
import com.example.myshopmanagerapp.core.FormRelatedString.InventoryItemCostPrice
import com.example.myshopmanagerapp.core.FormRelatedString.InventoryItemInformation
import com.example.myshopmanagerapp.core.FormRelatedString.InventoryItemManufacturer
import com.example.myshopmanagerapp.core.FormRelatedString.InventoryItemManufacturerPlaceholder
import com.example.myshopmanagerapp.core.FormRelatedString.InventoryItemName
import com.example.myshopmanagerapp.core.FormRelatedString.InventoryItemNamePlaceholder
import com.example.myshopmanagerapp.core.FormRelatedString.InventoryItemQuantityInStock
import com.example.myshopmanagerapp.core.FormRelatedString.InventoryItemSellingPrice
import com.example.myshopmanagerapp.core.FormRelatedString.InventoryItemShortNotesPlaceholder
import com.example.myshopmanagerapp.core.FormRelatedString.InventoryStockInfo
import com.example.myshopmanagerapp.core.FormRelatedString.ManufacturerNotAdded
import com.example.myshopmanagerapp.core.FormRelatedString.ManufacturerPlaceholder
import com.example.myshopmanagerapp.core.FormRelatedString.QuantityCategories
import com.example.myshopmanagerapp.core.FormRelatedString.SelectInventoryItemCategory
import com.example.myshopmanagerapp.core.FormRelatedString.ShortNotes
import com.example.myshopmanagerapp.core.FormRelatedString.UniqueInventoryItemId
import com.example.myshopmanagerapp.core.FormRelatedString.UpdateChanges
import com.example.myshopmanagerapp.core.Functions.removeBoxBracket
import com.example.myshopmanagerapp.core.Functions.toNotNull
import com.example.myshopmanagerapp.core.Functions.toTwoDecimalPlaces
import com.example.myshopmanagerapp.core.TypeConverters.toItemCategories
import com.example.myshopmanagerapp.core.TypeConverters.toItemCategoriesJson
import com.example.myshopmanagerapp.core.TypeConverters.toManufacturers
import com.example.myshopmanagerapp.core.TypeConverters.toManufacturersJson
import com.example.myshopmanagerapp.core.UserPreferences
import com.example.myshopmanagerapp.feature_app.data.local.entities.inventory_items.InventoryItemEntity
import com.example.myshopmanagerapp.feature_app.domain.model.ItemCategory
import com.example.myshopmanagerapp.feature_app.domain.model.Manufacturer
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.*
import com.example.myshopmanagerapp.feature_app.presentation.ui.theme.LocalSpacing
import kotlinx.coroutines.launch
import java.util.*

@Composable
fun ViewInventoryItemContent(
    inventoryItem: InventoryItemEntity,
    isUpdatingInventoryItem: Boolean,
    inventoryItemUpdateIsSuccessful: Boolean,
    updateInventoryItemMessage: String?,
    getUpdatedItemName: (String) -> Unit,
    getUpdatedItemManufacturer: (String) -> Unit,
    getUpdatedItemCategory: (String) -> Unit,
    getUpdatedOtherInfo: (shortNotes: String) -> Unit,
    navigateToCostPricesScreen: (String) -> Unit,
    navigateToSellingPricesScreen: (String) -> Unit,
    navigateToStocksScreen: () -> Unit,
    navigateToQuantityCategoriesScreen: () -> Unit,
    updateInventoryItem: (InventoryItemEntity) -> Unit,
    navigateBack: () -> Unit,
){
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var openCategoriesDialog by remember {
        mutableStateOf(false)
    }
    var itemCategoriesJson = UserPreferences(context).getItemCategories.collectAsState(initial = null).value
    var manufacturersJson = UserPreferences(context).getManufacturers.collectAsState(initial = null).value
    val thisCurrency = UserPreferences(context).getCurrency.collectAsState(initial = "GHS").value
    val currency = if (thisCurrency.isNullOrBlank()) "GHS" else thisCurrency
    var openManufacturersDialog by remember {
        mutableStateOf(false)
    }
    var updateInventoryItemConfirmation by remember {
        mutableStateOf(false)
    }

    BasicScreenColumnWithoutBottomBar {
        // Inventory Item Photo
        ViewPhoto(icon = R.drawable.ic_inventory_item)

        HorizontalDivider(thickness = LocalSpacing.current.extraSmall)

        //Inventory Item Info
        ViewInfo(InventoryItemInformation)

        HorizontalDivider(thickness = LocalSpacing.current.extraSmall)

        // Unique Inventory Id
        ViewTextValueRow(
            viewTitle = UniqueInventoryItemId,
            viewValue = inventoryItem.uniqueInventoryItemId
        )

        HorizontalDivider()

        // InventoryItem Name
        ViewOrUpdateTextValueRow(
            viewTitle = InventoryItemName,
            viewValue = inventoryItem.inventoryItemName,
            placeholder = InventoryItemNamePlaceholder,
            label = EnterInventoryItemName,
            icon = R.drawable.ic_inventory_item,
            getUpdatedValue = { getUpdatedItemName(it) }
        )

        HorizontalDivider()

        // InventoryItem Manufacturer
        ViewOrUpdateAutoCompleteValueRow(
            viewTitle = InventoryItemManufacturer,
            viewValue = inventoryItem.manufacturerName.toNotNull(),
            placeholder = InventoryItemManufacturerPlaceholder,
            label = EnterInventoryItemManufacturer,
            expandedIcon = R.drawable.ic_manufacturer,
            unexpandedIcon = R.drawable.ic_manufacturer,
            listItems = manufacturersJson.toManufacturers().map { it.manufacturer },
            onClickAddButton = { openManufacturersDialog = !openManufacturersDialog },
            getUpdatedValue = { getUpdatedItemManufacturer(it) }
        )

        HorizontalDivider()

        // Inventory Item Category
        ViewOrUpdateAutoCompleteValueRow(
            viewTitle = InventoryItemCategory,
            viewValue = inventoryItem.itemCategory,
            placeholder = InventoryItemCategoryPlaceholder,
            label = SelectInventoryItemCategory,
            expandedIcon = R.drawable.ic_category,
            unexpandedIcon = R.drawable.ic_category,
            listItems = itemCategoriesJson.toItemCategories().map { it.itemCategory },
            onClickAddButton = { openCategoriesDialog = !openCategoriesDialog },
            getUpdatedValue = { getUpdatedItemCategory(it) }
        )

        HorizontalDivider()

        // InventoryItem Cost Price
        ViewTextValueRow(
            viewTitle = InventoryItemCostPrice,
            viewValue = "$currency ${inventoryItem.currentCostPrice?.toTwoDecimalPlaces() ?: NotAvailable}",
            showInfo = true,
            onClick = { navigateToCostPricesScreen(inventoryItem.uniqueInventoryItemId) }
        )

        HorizontalDivider()

        // InventoryItem Selling Price
        ViewTextValueRow(
            viewTitle = InventoryItemSellingPrice,
            viewValue = "$currency ${inventoryItem.currentSellingPrice?.toTwoDecimalPlaces() ?: NotAvailable}",
            showInfo = true,
            onClick = { navigateToSellingPricesScreen(inventoryItem.uniqueInventoryItemId) }
        )

        HorizontalDivider()

        // InventoryItem Stock Info
        ViewTextValueRow(
            viewTitle = InventoryStockInfo,
            viewValue = inventoryItem.stockInfo?.size?.toNotNull().toString(),
            showInfo = true,
            onClick = { navigateToStocksScreen() }
        )

        HorizontalDivider()

        // InventoryItem Quantity Info
        ViewTextValueRow(
            viewTitle = InventoryItemQuantityInStock,
            viewValue = inventoryItem.totalNumberOfUnits?.toString()?.plus(" units") ?: NotAvailable
        )


        HorizontalDivider()

        // Quantity Categories
        ViewTextValueRow(
            viewTitle = QuantityCategories,
            viewValue = inventoryItem.quantityCategorizations.map { it.sizeName }.toString().removeBoxBracket(),
            showInfo = true,
            onClick = { navigateToQuantityCategoriesScreen() }
        )

        HorizontalDivider()

        // Inventory Item Short Notes
        ViewOrUpdateDescriptionValueRow(
            viewTitle = ShortNotes,
            viewValue = inventoryItem.otherInfo.toNotNull(),
            placeholder = InventoryItemShortNotesPlaceholder,
            label = EnterShortDescription,
            icon = R.drawable.ic_short_notes,
            getUpdatedValue = {getUpdatedOtherInfo(it)}
        )

        HorizontalDivider()

        // Update Button
        Box(modifier = Modifier.padding(LocalSpacing.current.smallMedium)){
            BasicButton(buttonName = UpdateChanges) {
                when(true){
                    (inventoryItem.inventoryItemName.isEmpty())->{
                        Toast.makeText(context, "Please enter item route", Toast.LENGTH_LONG).show()
                    }
                    else->{
                        updateInventoryItemConfirmation = !updateInventoryItemConfirmation
                        updateInventoryItem(inventoryItem)
                    }
                }
            }
        }

        BasicTextFieldAlertDialog(
            openDialog = openManufacturersDialog,
            title = AddManufacturer,
            textContent = emptyString,
            placeholder = ManufacturerPlaceholder,
            label = EnterManufacturerName,
            icon = R.drawable.ic_nav_item,
            keyboardType = KeyboardType.Text,
            unconfirmedUpdatedToastText = ManufacturerNotAdded,
            confirmedUpdatedToastText = null,
            getValue = { _newManufacturer ->
                val newManufacturer = Manufacturer(_newManufacturer)
                val thisManufacturer = manufacturersJson.toManufacturers()
                val mutableManufacturers = mutableListOf<Manufacturer>()
                if (thisManufacturer.map { it.manufacturer.trim().lowercase(Locale.ROOT) }
                        .contains(_newManufacturer.trim().lowercase(Locale.ROOT))
                ) {
                    Toast.makeText(context, "Manufacturer: $_newManufacturer already exists", Toast.LENGTH_LONG).show()
                    openManufacturersDialog = false
                }
                else {
                    mutableManufacturers.addAll(thisManufacturer)
                    mutableManufacturers.add(newManufacturer)
                    manufacturersJson = mutableManufacturers.toManufacturersJson()
                    coroutineScope.launch {
                        UserPreferences(context).saveManufacturers(manufacturersJson.toNotNull())
                    }
                    Toast.makeText(context, "Manufacturer: $_newManufacturer successfully added", Toast.LENGTH_LONG).show()
                }
            }
        ) {
            openManufacturersDialog = false
        }

        BasicTextFieldAlertDialog(
            openDialog = openCategoriesDialog,
            title = AddCategory,
            textContent = emptyString,
            placeholder = CategoryPlaceholder,
            label = EnterItemCategory,
            icon = R.drawable.ic_category,
            keyboardType = KeyboardType.Text,
            unconfirmedUpdatedToastText = CategoryNotAdded,
            confirmedUpdatedToastText = null,
            getValue = { _newCategory ->
                val newItemCategory = ItemCategory(_newCategory)
                val categories = itemCategoriesJson.toItemCategories()
                val mutableCategories = mutableListOf<ItemCategory>()
                if (categories.map { it.itemCategory.trim().lowercase(Locale.ROOT) }.contains(_newCategory.trim().lowercase(Locale.ROOT))) {
                    Toast.makeText(context, "ItemCategory: $_newCategory already exists", Toast.LENGTH_LONG).show()
                    openCategoriesDialog = false
                } else {
                    mutableCategories.addAll(categories)
                    mutableCategories.add(newItemCategory)
                    itemCategoriesJson = mutableCategories.toItemCategoriesJson()
                    coroutineScope.launch {
                        UserPreferences(context).saveItemCategories(itemCategoriesJson.toNotNull())
                    }
                    Toast.makeText(context, "ItemCategory: $_newCategory successfully added", Toast.LENGTH_LONG).show()
                }
            }
        ) {
            openCategoriesDialog = false
        }

        ConfirmationInfoDialog(
            openDialog = updateInventoryItemConfirmation,
            isLoading = isUpdatingInventoryItem,
            title = null,
            textContent = updateInventoryItemMessage ?: emptyString,
            unconfirmedDeletedToastText = null,
            confirmedDeleteToastText = null
        ) {
            if (inventoryItemUpdateIsSuccessful){
                navigateBack()
            }
            updateInventoryItemConfirmation = false
        }
    }
}