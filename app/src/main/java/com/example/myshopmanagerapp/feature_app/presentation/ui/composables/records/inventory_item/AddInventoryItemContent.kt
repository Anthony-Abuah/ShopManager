package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.records.inventory_item

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import com.example.myshopmanagerapp.R
import com.example.myshopmanagerapp.core.Constants.emptyString
import com.example.myshopmanagerapp.core.FormRelatedString.AddCategory
import com.example.myshopmanagerapp.core.FormRelatedString.AddManufacturer
import com.example.myshopmanagerapp.core.FormRelatedString.AddQuantityCategorization
import com.example.myshopmanagerapp.core.FormRelatedString.CategoryNotAdded
import com.example.myshopmanagerapp.core.FormRelatedString.CategoryPlaceholder
import com.example.myshopmanagerapp.core.FormRelatedString.EnterInventoryItemManufacturer
import com.example.myshopmanagerapp.core.FormRelatedString.EnterInventoryItemName
import com.example.myshopmanagerapp.core.FormRelatedString.EnterInventoryItemSellingPrice
import com.example.myshopmanagerapp.core.FormRelatedString.EnterItemCategory
import com.example.myshopmanagerapp.core.FormRelatedString.EnterManufacturerName
import com.example.myshopmanagerapp.core.FormRelatedString.InventoryItemCategoryPlaceholder
import com.example.myshopmanagerapp.core.FormRelatedString.InventoryItemManufacturerPlaceholder
import com.example.myshopmanagerapp.core.FormRelatedString.InventoryItemNamePlaceholder
import com.example.myshopmanagerapp.core.FormRelatedString.InventoryItemSellingPricePlaceholder
import com.example.myshopmanagerapp.core.FormRelatedString.InventoryItemShortNotes
import com.example.myshopmanagerapp.core.FormRelatedString.InventoryItemShortNotesPlaceholder
import com.example.myshopmanagerapp.core.FormRelatedString.ManufacturerNotAdded
import com.example.myshopmanagerapp.core.FormRelatedString.ManufacturerPlaceholder
import com.example.myshopmanagerapp.core.FormRelatedString.Save
import com.example.myshopmanagerapp.core.FormRelatedString.SelectInventoryItemCategory
import com.example.myshopmanagerapp.core.Functions.amountIsNotValid
import com.example.myshopmanagerapp.core.Functions.fromCategoriesJson
import com.example.myshopmanagerapp.core.Functions.fromManufacturersJson
import com.example.myshopmanagerapp.core.Functions.generateUniqueInventoryItemId
import com.example.myshopmanagerapp.core.Functions.removeBoxBracket
import com.example.myshopmanagerapp.core.Functions.toNotNull
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
fun AddInventoryItemContent(
    inventoryItem: InventoryItemEntity,
    isSavingInventoryItem: Boolean,
    saveInventoryItemMessage: String?,
    inventorySavingIsSuccessful: Boolean,
    addItemName: (String) -> Unit,
    addItemManufacturerName: (String) -> Unit,
    addItemCategory: (String) -> Unit,
    addItemSellingPrice: (String) -> Unit,
    addItemOtherInfo: (String) -> Unit,
    onTakePhoto: () -> Unit,
    navigateToQuantityCategorizationScreen: () -> Unit,
    addInventoryItem: (InventoryItemEntity) -> Unit,
    navigateBack: () -> Unit,
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var confirmationInfoDialog by remember {
        mutableStateOf(false)
    }
    var openCategoriesDialog by remember {
        mutableStateOf(false)
    }
    var openManufacturersDialog by remember {
        mutableStateOf(false)
    }
    var itemCategoriesJson = UserPreferences(context).getItemCategories.collectAsState(initial = null).value
    var manufacturers = UserPreferences(context).getManufacturers.collectAsState(initial = null).value

    var priceIsWrong by remember {
        mutableStateOf(false)
    }
    BasicScreenColumnWithoutBottomBar {
        // Item route
        Box(modifier = Modifier.padding(LocalSpacing.current.small),
            contentAlignment = Alignment.Center
        ){
            var inventoryItemName by remember {
                mutableStateOf(inventoryItem.inventoryItemName)
            }
            BasicTextFieldWithTrailingIconError(
                value = inventoryItemName,
                onValueChange = {
                    inventoryItemName = it
                    addItemName(it.trim())
                },
                isError = false,
                readOnly = false,
                placeholder = InventoryItemNamePlaceholder,
                label = EnterInventoryItemName,
                icon = R.drawable.ic_inventory_item,
                keyboardType = KeyboardType.Text
            )
        }

        // Manufacturer
        Box(modifier = Modifier.padding(LocalSpacing.current.small),
            contentAlignment = Alignment.Center
        ){
            val manufacturerList = fromManufacturersJson(manufacturers)
            AutoCompleteWithAddButton(
                label = EnterInventoryItemManufacturer,
                placeholder = InventoryItemManufacturerPlaceholder,
                listItems = manufacturerList.map {manufacturer -> manufacturer.manufacturer
                    .lowercase(Locale.ROOT).replaceFirstChar{ if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() } },
                readOnly = false,
                expandedIcon = R.drawable.ic_inventory,
                unexpandedIcon = R.drawable.ic_inventory,
                onClickAddButton = { openManufacturersDialog = !openManufacturersDialog},
                getSelectedItem = { addItemManufacturerName(it.trim()) }
            )
        }


        // Item ItemCategory
        Box(modifier = Modifier.padding(LocalSpacing.current.small),
            contentAlignment = Alignment.Center
        ){
            val categoryList = fromCategoriesJson(itemCategoriesJson)
            AutoCompleteWithAddButton(
                label = SelectInventoryItemCategory,
                placeholder = InventoryItemCategoryPlaceholder,
                listItems = categoryList.map {category -> category.itemCategory.lowercase(Locale.ROOT).replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() } },
                readOnly = true,
                expandedIcon = R.drawable.ic_category,
                unexpandedIcon = R.drawable.ic_category,
                onClickAddButton = { openCategoriesDialog = !openCategoriesDialog},
                getSelectedItem = { addItemCategory(it.trim()) }
            )
        }

        // Item Photo
        Box(modifier = Modifier.padding(LocalSpacing.current.small),
            contentAlignment = Alignment.Center
        ){
            PhotoTextField {
                onTakePhoto()
            }
        }

        // QuantityCategorization
        Box(modifier = Modifier.padding(LocalSpacing.current.small),
            contentAlignment = Alignment.Center
        ){
            BasicTextFieldWithTrailingIconError(
                value = inventoryItem.quantityCategorizations.map { it.sizeName }.toString().removeBoxBracket(),
                onClickTrailingIcon = { navigateToQuantityCategorizationScreen() },
                onValueChange = {},
                readOnly = true,
                isError = false,
                placeholder = emptyString,
                label = AddQuantityCategorization,
                icon = R.drawable.ic_add,
                keyboardType = KeyboardType.Text
            )
        }


        // Selling Price
        Box(modifier = Modifier.padding(LocalSpacing.current.small),
            contentAlignment = Alignment.Center
        ){
            var price by remember {
                mutableStateOf(inventoryItem.currentSellingPrice.toNotNull().toString() )
            }
            BasicTextFieldWithTrailingIconError(
                value = price,
                onValueChange = {
                    price = it
                    priceIsWrong = amountIsNotValid(price)
                    addItemSellingPrice(price)
                },
                isError = priceIsWrong,
                readOnly = false,
                placeholder = InventoryItemSellingPricePlaceholder,
                label = EnterInventoryItemSellingPrice,
                icon = R.drawable.ic_money_filled,
                keyboardType = KeyboardType.Number
            )
        }

        // Short notes
        Box(modifier = Modifier.padding(LocalSpacing.current.small),
            contentAlignment = Alignment.Center
        ){
            var otherInfo by remember {
                mutableStateOf(inventoryItem.otherInfo.toNotNull())
            }
            DescriptionTextFieldWithTrailingIcon(
                value = otherInfo,
                onValueChange = {
                    otherInfo = it
                    addItemOtherInfo(it)
                },
                placeholder = InventoryItemShortNotesPlaceholder,
                label = InventoryItemShortNotes,
                icon = R.drawable.ic_short_notes,
                keyboardType = KeyboardType.Text
            )
        }


        Box(modifier = Modifier.padding(
            vertical = LocalSpacing.current.smallMedium,
            horizontal = LocalSpacing.current.small,
        ),
            contentAlignment = Alignment.Center
        ){
            BasicButton(buttonName = Save) {
                val uniqueInventoryItemId = generateUniqueInventoryItemId(inventoryItem.inventoryItemName)
                when(true){
                    (inventoryItem.inventoryItemName.isEmpty())-> {
                        Toast.makeText(context, "Please enter item route", Toast.LENGTH_LONG).show()
                    }
                    priceIsWrong -> {
                        Toast.makeText(context, "Please enter valid price", Toast.LENGTH_LONG).show()
                    }
                    else->{
                        addInventoryItem(inventoryItem.copy(uniqueInventoryItemId = uniqueInventoryItemId))
                        confirmationInfoDialog = !confirmationInfoDialog
                    }
                }
            }
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
                val thisManufacturer = manufacturers.toManufacturers()
                val mutableManufacturers = mutableListOf<Manufacturer>()
                if (thisManufacturer.map { it.manufacturer.trim().lowercase(Locale.ROOT) }.contains(_newManufacturer.trim().lowercase(Locale.ROOT))) {
                    Toast.makeText(context, "Manufacturer: $_newManufacturer already exists", Toast.LENGTH_LONG).show()
                    openManufacturersDialog = false
                }
                else {
                    mutableManufacturers.addAll(thisManufacturer)
                    mutableManufacturers.add(newManufacturer)
                    manufacturers = mutableManufacturers.toManufacturersJson()
                    coroutineScope.launch {
                        UserPreferences(context).saveManufacturers(manufacturers.toNotNull())
                    }
                    Toast.makeText(context, "Manufacturer: $_newManufacturer successfully added", Toast.LENGTH_LONG).show()
                }
            }
        ) {
            openManufacturersDialog = false
        }


        ConfirmationInfoDialog(
            openDialog = confirmationInfoDialog,
            isLoading = isSavingInventoryItem,
            title = null,
            textContent = saveInventoryItemMessage ?: emptyString,
            unconfirmedDeletedToastText = null,
            confirmedDeleteToastText = null
        ) {
            if (inventorySavingIsSuccessful) { navigateBack() }
            confirmationInfoDialog = false
        }
    }

}
