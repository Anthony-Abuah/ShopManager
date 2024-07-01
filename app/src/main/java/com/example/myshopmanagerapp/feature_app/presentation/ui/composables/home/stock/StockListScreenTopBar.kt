package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.home.stock

import android.widget.Toast
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.example.myshopmanagerapp.core.Constants.AllTime
import com.example.myshopmanagerapp.core.Constants.SelectRange
import com.example.myshopmanagerapp.core.Constants.emptyString
import com.example.myshopmanagerapp.core.Constants.listOfListNumbers
import com.example.myshopmanagerapp.core.Constants.listOfPeriods
import com.example.myshopmanagerapp.core.Constants.listOfStockSortItems
import com.example.myshopmanagerapp.core.FormRelatedString.EnterValue
import com.example.myshopmanagerapp.core.FormRelatedString.SearchPlaceholder
import com.example.myshopmanagerapp.core.Functions.convertToDouble
import com.example.myshopmanagerapp.core.Functions.toDate
import com.example.myshopmanagerapp.core.Functions.toLocalDate
import com.example.myshopmanagerapp.core.Functions.toTimestamp
import com.example.myshopmanagerapp.core.StockEntities
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.ComparisonTopBar
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.DateRangePickerTopBar
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.SearchScreenTopBar
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDate


@Composable
fun StockListScreenTopBar(
    entireStocks: StockEntities,
    allStocks: StockEntities,
    showSearchBar: Boolean,
    showComparisonBar: Boolean,
    showDateRangePickerBar: Boolean,
    getItemName: (String)-> String,
    getPersonnelName: (String)-> String,
    openDialogInfo: (String)-> Unit,
    openSearchBar: ()-> Unit,
    closeSearchBar: ()-> Unit,
    closeDateRangePickerBar: ()-> Unit,
    closeComparisonBar: ()-> Unit,
    openComparisonBar: ()-> Unit,
    openDateRangePickerBar: ()-> Unit,
    printStocks: ()-> Unit,
    getStocks: (StockEntities)-> Unit,
    navigateBack: ()-> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    var comparisonIsGreater by remember {
        mutableStateOf(false)
    }
    val context = LocalContext.current
    when(true){
        showSearchBar->{
            SearchScreenTopBar(
                placeholder = SearchPlaceholder,
                goBack = {
                    closeSearchBar()
                    closeComparisonBar()
                    closeDateRangePickerBar()
                },
                getSearchValue = {_searchValue->
                    if (_searchValue.isBlank()){
                        coroutineScope.launch {
                            delay(10000L)
                            getStocks(entireStocks)
                            openDialogInfo(emptyString)
                            openDialogInfo(emptyString)
                            closeSearchBar()
                        }
                    }else{
                        getStocks(entireStocks.filter { getItemName(it.uniqueInventoryItemId).contains(_searchValue, true) || getPersonnelName(it.uniquePersonnelId).contains(_searchValue, true) })
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
                    closeSearchBar()
                    openDialogInfo(emptyString)
                    openDialogInfo(emptyString)
                },
                getComparisonValue = {_value->
                    if (comparisonIsGreater) {
                        getStocks(allStocks.filter { it.totalNumberOfUnits >= convertToDouble(_value) })
                    } else{
                        getStocks(allStocks.filter { it.totalNumberOfUnits <= convertToDouble(_value) })
                    }
                    closeComparisonBar()
                    openDialogInfo(emptyString)
                    openDialogInfo(emptyString)
                }
            )
        }
        showDateRangePickerBar->{
            DateRangePickerTopBar(
                startDate = LocalDate.now().minusDays(LocalDate.now().dayOfMonth.minus(1).toLong()).toTimestamp(),
                endDate = LocalDate.now().toTimestamp(),
                goBack = {
                    closeComparisonBar()
                    closeSearchBar()
                    closeDateRangePickerBar()
                },
                getDateRange = {_startDateString, _endDateString->
                    val startDate = _startDateString.toLocalDate().toTimestamp()
                    val endDate = _endDateString.toLocalDate().toTimestamp()
                    getStocks(allStocks.filter { it.date in startDate..endDate })
                    closeDateRangePickerBar()
                    openDialogInfo(emptyString)
                    openDialogInfo(emptyString)
                }
            )
        }
        else->{
            StockListTopBar(
                topBarTitleText = "Stock",
                print = { printStocks() },
                onSort = { _value ->
                    when (_value.number) {
                        1 -> {
                            getStocks(allStocks.sortedBy { it.date })
                            openDialogInfo(emptyString)
                            openDialogInfo(emptyString)
                            val dialogMessage = "Sorted ascending by date"
                            Toast.makeText(context, dialogMessage, Toast.LENGTH_LONG).show()
                        }
                        2 -> {
                            getStocks(allStocks.sortedByDescending { it.date })
                            openDialogInfo(emptyString)
                            openDialogInfo(emptyString)
                            val dialogMessage = "Sorted descending by date"
                            Toast.makeText(context, dialogMessage, Toast.LENGTH_LONG).show()
                        }
                        3 -> {
                            getStocks(allStocks.sortedBy { it.totalNumberOfUnits })
                            openDialogInfo(emptyString)
                            openDialogInfo(emptyString)
                            val dialogMessage = "Sorted ascending by total number of units"
                            Toast.makeText(context, dialogMessage, Toast.LENGTH_LONG).show()
                        }
                        4 -> {
                            getStocks(allStocks.sortedByDescending { it.totalNumberOfUnits })
                            openDialogInfo(emptyString)
                            openDialogInfo(emptyString)
                            val dialogMessage = "Sorted descending by total number of units"
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
                onClickPeriodItem = { _period ->
                    val firstDate = _period.firstDate.toDate().time
                    val lastDate = _period.lastDate.toDate().time
                    when(true){
                        (_period.titleText == AllTime) -> { getStocks(entireStocks)
                            openDialogInfo(emptyString)
                            openDialogInfo(emptyString)
                        }
                        (_period.titleText == SelectRange) -> { openDateRangePickerBar() }
                        else -> {
                            getStocks(allStocks.filter { it.date in firstDate until lastDate })
                            openDialogInfo(emptyString)
                            openDialogInfo(emptyString)
                        }
                    }
                },
                onClickListItem = { _listNumber ->
                    val number = _listNumber.number
                    when (_listNumber.number) {
                        0 -> {
                            getStocks(entireStocks)
                            openDialogInfo(emptyString)
                            openDialogInfo(emptyString)
                            val dialogMessage = "All stocks are selected"
                            Toast.makeText(context, dialogMessage, Toast.LENGTH_LONG).show()
                        }
                        1 -> {
                            Toast.makeText(context, "Search", Toast.LENGTH_LONG).show()
                            openSearchBar()
                        }
                        2 -> {
                            val dialogMessage =
                                "Total number of stocks on this list are ${allStocks.size}"
                            openDialogInfo(dialogMessage)
                        }
                        else -> {
                            getStocks(allStocks.take(number))
                            val dialogMessage = "First $number stocks selected"
                            Toast.makeText(context, dialogMessage, Toast.LENGTH_LONG).show()
                        }
                    }
                },
                periodDropDownItems = listOfPeriods,
                listDropDownItems = listOfListNumbers,
                listOfSortItems = listOfStockSortItems,
            ) {
                navigateBack()
            }
        }
    }
}
