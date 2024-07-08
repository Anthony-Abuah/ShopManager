package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.records.supplier

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import com.example.myshopmanagerapp.core.Constants.emptyString
import com.example.myshopmanagerapp.core.Constants.listOfListNumbers
import com.example.myshopmanagerapp.core.Constants.listOfSupplierSortItems
import com.example.myshopmanagerapp.core.FormRelatedString.SearchPlaceholder
import com.example.myshopmanagerapp.core.Functions.toNotNull
import com.example.myshopmanagerapp.core.SupplierEntities
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.SearchTopBar
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun SupplierListScreenTopBar(
    entireSuppliers: SupplierEntities,
    allSuppliers: SupplierEntities,
    showSearchBar: Boolean,
    openDialogInfo: (String)-> Unit,
    openSearchBar: ()-> Unit,
    closeSearchBar: ()-> Unit,
    printSuppliers: ()-> Unit,
    getSuppliers: (SupplierEntities)-> Unit,
    navigateBack: ()-> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
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
                            getSuppliers(entireSuppliers)
                            openDialogInfo(emptyString)
                            openDialogInfo(emptyString)
                            closeSearchBar()
                        }
                    }else{
                        getSuppliers(entireSuppliers.filter { it.supplierName.contains(_searchValue, true) || it.supplierLocation.toNotNull().contains(_searchValue, true)  || it.supplierRole.toNotNull().contains(_searchValue, true) })
                        openDialogInfo(emptyString)
                        openDialogInfo(emptyString)
                    }
                }
            )
        }
        else->{
            SupplierListTopBar(
                topBarTitleText = "Suppliers",
                print = { printSuppliers() },
                onSort = { _value ->
                    when (_value.number) {
                        1 -> {
                            getSuppliers(entireSuppliers.sortedBy { it.supplierName.take(1) })
                            openDialogInfo(emptyString)
                            openDialogInfo(emptyString)
                            val dialogMessage = "Sorted ascending by supplier names"
                            Toast.makeText(context, dialogMessage, Toast.LENGTH_LONG).show()
                        }
                        2 -> {
                            getSuppliers(allSuppliers.sortedByDescending { it.supplierName.take(1) })
                            openDialogInfo(emptyString)
                            openDialogInfo(emptyString)
                            val dialogMessage = "Sorted descending by supplier names"
                            Toast.makeText(context, dialogMessage, Toast.LENGTH_LONG).show()
                        }
                        3 -> {
                            getSuppliers(allSuppliers.sortedBy { it.supplierRole.toNotNull().take(1) })
                            openDialogInfo(emptyString)
                            openDialogInfo(emptyString)
                            val dialogMessage = "Sorted ascending by supplier roles"
                            Toast.makeText(context, dialogMessage, Toast.LENGTH_LONG).show()
                        }
                        4 -> {
                            getSuppliers(allSuppliers.sortedByDescending { it.supplierRole.toNotNull().take(1) })
                            openDialogInfo(emptyString)
                            openDialogInfo(emptyString)
                            val dialogMessage = "Sorted descending by supplier role"
                            Toast.makeText(context, dialogMessage, Toast.LENGTH_LONG).show()
                        }
                    }
                },
                onClickListItem = { _listNumber ->
                    val number = _listNumber.number
                    when (_listNumber.number) {
                        0 -> {
                            getSuppliers(entireSuppliers)
                            openDialogInfo(emptyString)
                            openDialogInfo(emptyString)
                            val dialogMessage = "All suppliers are selected"
                            Toast.makeText(context, dialogMessage, Toast.LENGTH_LONG).show()
                        }
                        1 -> {
                            Toast.makeText(context, "Search", Toast.LENGTH_LONG).show()
                            openSearchBar()
                        }
                        2 -> {
                            val dialogMessage = "Total number of suppliers on this list are ${allSuppliers.size}"
                            openDialogInfo(dialogMessage)
                        }
                        else -> {
                            getSuppliers(allSuppliers.take(number))
                            val dialogMessage = "First $number suppliers selected"
                            Toast.makeText(context, dialogMessage, Toast.LENGTH_LONG).show()
                        }
                    }
                },
                listDropDownItems = listOfListNumbers,
                listOfSortItems = listOfSupplierSortItems,
            ) {
                navigateBack()
            }
        }
    }
}
