package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.home.customer

import android.widget.Toast
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.example.myshopmanagerapp.core.Constants.emptyString
import com.example.myshopmanagerapp.core.Constants.listOfCustomerSortItems
import com.example.myshopmanagerapp.core.Constants.listOfListNumbers
import com.example.myshopmanagerapp.core.CustomerEntities
import com.example.myshopmanagerapp.core.FormRelatedString.EnterValue
import com.example.myshopmanagerapp.core.FormRelatedString.SearchPlaceholder
import com.example.myshopmanagerapp.core.Functions.convertToDouble
import com.example.myshopmanagerapp.core.Functions.toNotNull
import com.example.myshopmanagerapp.core.Functions.toTwoDecimalPlaces
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.ComparisonTopBar
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.SearchTopBar
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun CustomerListScreenTopBar(
    entireCustomers: CustomerEntities,
    allCustomers: CustomerEntities,
    showSearchBar: Boolean,
    showComparisonBar: Boolean,
    openDialogInfo: (String)-> Unit,
    openSearchBar: ()-> Unit,
    openComparisonBar: ()-> Unit,
    closeSearchBar: ()-> Unit,
    closeComparisonBar: ()-> Unit,
    printCustomers: ()-> Unit,
    getCustomers: (CustomerEntities)-> Unit,
    navigateBack: ()-> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    var comparisonIsGreater by remember {
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
                            getCustomers(entireCustomers)
                            openDialogInfo(emptyString)
                            openDialogInfo(emptyString)
                            closeSearchBar()
                        }
                    }else{
                        getCustomers(entireCustomers.filter { it.customerName.contains(_searchValue, true) || it.customerLocation.toNotNull().contains(_searchValue, true) })
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
                        getCustomers(allCustomers.filter { it.debtAmount.toNotNull() >= convertToDouble(_value) })
                    }
                    else{
                        getCustomers(allCustomers.filter { it.debtAmount.toNotNull() <= convertToDouble(_value) })
                    }
                    closeComparisonBar()
                    openDialogInfo(emptyString)
                    openDialogInfo(emptyString)
                }
            )
        }
        else->{
            CustomerListTopBar(
                topBarTitleText = "Customers",
                print = { printCustomers() },
                onSort = { _value ->
                    when (_value.number) {
                        1 -> {
                            getCustomers(entireCustomers.sortedBy { it.customerName.take(1) })
                            openDialogInfo(emptyString)
                            openDialogInfo(emptyString)
                            val dialogMessage = "Sorted ascending by customer names"
                            Toast.makeText(context, dialogMessage, Toast.LENGTH_LONG).show()
                        }
                        2 -> {
                            getCustomers(allCustomers.sortedByDescending { it.customerName.take(1) })
                            openDialogInfo(emptyString)
                            openDialogInfo(emptyString)
                            val dialogMessage = "Sorted descending by customer names"
                            Toast.makeText(context, dialogMessage, Toast.LENGTH_LONG).show()
                        }
                        3 -> {
                            getCustomers(allCustomers.sortedBy { it.debtAmount.toNotNull() })
                            openDialogInfo(emptyString)
                            openDialogInfo(emptyString)
                            val dialogMessage = "Sorted ascending by debt amount"
                            Toast.makeText(context, dialogMessage, Toast.LENGTH_LONG).show()
                        }
                        4 -> {
                            getCustomers(allCustomers.sortedByDescending { it.debtAmount.toNotNull() })
                            openDialogInfo(emptyString)
                            openDialogInfo(emptyString)
                            val dialogMessage = "Sorted descending by debt amount"
                            Toast.makeText(context, dialogMessage, Toast.LENGTH_LONG).show()
                        }
                        5 -> {
                            comparisonIsGreater = true
                            openComparisonBar()
                        }
                        6 -> {
                            comparisonIsGreater = false
                            openComparisonBar()
                        }
                    }
                },
                onClickListItem = { _listNumber ->
                    val number = _listNumber.number
                    when (_listNumber.number) {
                        0 -> {
                            getCustomers(entireCustomers)
                            openDialogInfo(emptyString)
                            openDialogInfo(emptyString)
                            val dialogMessage = "All customers are selected"
                            Toast.makeText(context, dialogMessage, Toast.LENGTH_LONG).show()
                        }
                        1 -> {
                            Toast.makeText(context, "Search", Toast.LENGTH_LONG).show()
                            openSearchBar()
                        }
                        2 -> {
                            val dialogMessage =
                                "Total number of customers on this list are ${allCustomers.size}" +
                                "\nTotal amount of debts on this list is GHS ${
                                allCustomers.sumOf { it.debtAmount.toNotNull() }.toTwoDecimalPlaces()}"
                            openDialogInfo(dialogMessage)
                        }
                        else -> {
                            getCustomers(allCustomers.take(number))
                            val dialogMessage = "First $number customers selected"
                            Toast.makeText(context, dialogMessage, Toast.LENGTH_LONG).show()
                        }
                    }
                },
                listDropDownItems = listOfListNumbers,
                listOfSortItems = listOfCustomerSortItems,
            ) {
                navigateBack()
            }
        }
    }
}
