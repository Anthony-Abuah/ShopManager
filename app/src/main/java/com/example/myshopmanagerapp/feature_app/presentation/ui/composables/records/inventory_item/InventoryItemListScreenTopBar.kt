package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.records.inventory_item

import android.widget.Toast
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.example.myshopmanagerapp.core.Constants.emptyString
import com.example.myshopmanagerapp.core.Constants.listOfInventoryItemsSortItems
import com.example.myshopmanagerapp.core.Constants.listOfListNumbers
import com.example.myshopmanagerapp.core.FormRelatedString.EnterValue
import com.example.myshopmanagerapp.core.FormRelatedString.SearchPlaceholder
import com.example.myshopmanagerapp.core.Functions.convertToDouble
import com.example.myshopmanagerapp.core.Functions.toNotNull
import com.example.myshopmanagerapp.core.Functions.toTwoDecimalPlaces
import com.example.myshopmanagerapp.core.InventoryItemEntities
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.ComparisonTopBar
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.SearchTopBar
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun InventoryItemListScreenTopBar(
    entireInventoryItems: InventoryItemEntities,
    allInventoryItems: InventoryItemEntities,
    showSearchBar: Boolean,
    showComparisonBar: Boolean,
    openDialogInfo: (String)-> Unit,
    openSearchBar: ()-> Unit,
    closeSearchBar: ()-> Unit,
    closeComparisonBar: ()-> Unit,
    openComparisonBar: ()-> Unit,
    printInventoryItems: ()-> Unit,
    getInventoryItems: (InventoryItemEntities)-> Unit,
    navigateBack: ()-> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    var comparisonIsGreater by remember {
        mutableStateOf(false)
    }
    var compareCostPrice by remember {
        mutableStateOf(false)
    }
    val context = LocalContext.current
    when(true){
        showSearchBar->{
            SearchTopBar(
                placeholder = SearchPlaceholder,
                goBack = { closeSearchBar() },
                getSearchValue = {_searchValue->
                    if (_searchValue.isBlank()){
                        coroutineScope.launch {
                            delay(10000L)
                            getInventoryItems(entireInventoryItems)
                            openDialogInfo(emptyString)
                            openDialogInfo(emptyString)
                            closeSearchBar()
                        }
                    }else{
                        getInventoryItems(entireInventoryItems.filter { (it.manufacturerName?.contains(_searchValue, true) == true) || (it.inventoryItemName.contains(_searchValue, true)) || (it.itemCategory.contains(_searchValue, true)) })
                        openDialogInfo(emptyString)
                        openDialogInfo(emptyString)
                    }
                }
            )
        }
        showComparisonBar->{
            ComparisonTopBar(
                placeholder = EnterValue,
                goBack = {
                    closeComparisonBar()
                    openDialogInfo(emptyString)
                    openDialogInfo(emptyString)
                },
                getComparisonValue = {_value->
                    if (comparisonIsGreater) {
                        when(true){
                            compareCostPrice -> {
                                getInventoryItems(allInventoryItems.filter { it.currentCostPrice.toNotNull() >= convertToDouble(_value) })
                            }
                            else -> {
                                getInventoryItems(allInventoryItems.filter { it.currentSellingPrice.toNotNull() >= convertToDouble(_value) })
                            }
                        }
                    }
                    else{
                        when(true){
                            compareCostPrice -> {
                                getInventoryItems(allInventoryItems.filter { it.currentCostPrice.toNotNull() <= convertToDouble(_value) })
                            }
                            else -> {
                                getInventoryItems(allInventoryItems.filter { it.currentSellingPrice.toNotNull() <= convertToDouble(_value) })
                            }
                        }
                    }
                    closeComparisonBar()
                    openDialogInfo(emptyString)
                    openDialogInfo(emptyString)
                }
            )
        }
        else->{
            InventoryItemListTopBar(
                topBarTitleText = "Inventory Items",
                listDropDownItems = listOfListNumbers,
                onClickListItem = { _listNumber ->
                    val number = _listNumber.number
                    when (_listNumber.number) {
                        0 -> {
                            getInventoryItems(entireInventoryItems)
                            openDialogInfo(emptyString)
                            openDialogInfo(emptyString)
                            val dialogMessage = "All items are selected"
                            Toast.makeText(context, dialogMessage, Toast.LENGTH_LONG).show()
                        }
                        1 -> {
                            Toast.makeText(context, "Search", Toast.LENGTH_LONG).show()
                            openSearchBar()
                        }
                        2 -> {
                            val dialogMessage =
                                "Total number of items on this list are ${allInventoryItems.size}" +
                                "\nSum of all current selling prices of items on this list is GHS ${
                                allInventoryItems.sumOf { it.currentSellingPrice.toNotNull() }.toTwoDecimalPlaces()}"
                            openDialogInfo(dialogMessage)
                        }
                        else -> {
                            getInventoryItems(allInventoryItems.take(number))
                            val dialogMessage = "First $number items selected"
                            Toast.makeText(context, dialogMessage, Toast.LENGTH_LONG).show()
                        }
                    }
                },
                listOfSortItems = listOfInventoryItemsSortItems,
                onSort = { _value ->
                    when (_value.number) {
                        1 -> {
                            getInventoryItems(entireInventoryItems.sortedBy { it.inventoryItemName.take(1) })
                            openDialogInfo(emptyString)
                            openDialogInfo(emptyString)
                            val dialogMessage = "Sorted ascending by item name"
                            Toast.makeText(context, dialogMessage, Toast.LENGTH_LONG).show()
                        }
                        2 -> {
                            getInventoryItems(allInventoryItems.sortedByDescending { it.inventoryItemName.take(1) })
                            openDialogInfo(emptyString)
                            openDialogInfo(emptyString)
                            val dialogMessage = "Sorted descending by item name"
                            Toast.makeText(context, dialogMessage, Toast.LENGTH_LONG).show()
                        }
                        3 -> {
                            getInventoryItems(allInventoryItems.sortedBy { it.currentSellingPrice.toNotNull() })
                            openDialogInfo(emptyString)
                            openDialogInfo(emptyString)
                            val dialogMessage = "Sorted ascending by selling price"
                            Toast.makeText(context, dialogMessage, Toast.LENGTH_LONG).show()
                        }
                        4 -> {
                            getInventoryItems(allInventoryItems.sortedByDescending { it.currentSellingPrice.toNotNull() })
                            openDialogInfo(emptyString)
                            openDialogInfo(emptyString)
                            val dialogMessage = "Sorted descending by selling price"
                            Toast.makeText(context, dialogMessage, Toast.LENGTH_LONG).show()
                        }
                        5 -> {
                            getInventoryItems(allInventoryItems.sortedBy { it.currentCostPrice.toNotNull() })
                            openDialogInfo(emptyString)
                            openDialogInfo(emptyString)
                            val dialogMessage = "Sorted ascending by cost price"
                            Toast.makeText(context, dialogMessage, Toast.LENGTH_LONG).show()
                        }
                        6 -> {
                            getInventoryItems(allInventoryItems.sortedByDescending { it.currentCostPrice.toNotNull() })
                            openDialogInfo(emptyString)
                            openDialogInfo(emptyString)
                            val dialogMessage = "Sorted descending by cost price"
                            Toast.makeText(context, dialogMessage, Toast.LENGTH_LONG).show()
                        }
                        7 -> {
                            compareCostPrice = true
                            comparisonIsGreater = true
                            openComparisonBar()
                        }
                        8 -> {
                            compareCostPrice = true
                            comparisonIsGreater = false
                            openComparisonBar()
                        }
                        9 -> {
                            compareCostPrice = false
                            comparisonIsGreater = true
                            openComparisonBar()
                        }
                        10 -> {
                            compareCostPrice = false
                            comparisonIsGreater = false
                            openComparisonBar()
                        }
                    }
                },
                print = { printInventoryItems() },
            ) {
                navigateBack()
            }
        }
    }
}
