package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.records.debt

import android.widget.Toast
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.example.myshopmanagerapp.core.Constants.AllTime
import com.example.myshopmanagerapp.core.Constants.SelectRange
import com.example.myshopmanagerapp.core.Constants.emptyString
import com.example.myshopmanagerapp.core.Constants.listOfDebtSortItems
import com.example.myshopmanagerapp.core.Constants.listOfListNumbers
import com.example.myshopmanagerapp.core.Constants.listOfPeriods
import com.example.myshopmanagerapp.core.DebtEntities
import com.example.myshopmanagerapp.core.FormRelatedString.EnterValue
import com.example.myshopmanagerapp.core.FormRelatedString.SearchPlaceholder
import com.example.myshopmanagerapp.core.Functions.convertToDouble
import com.example.myshopmanagerapp.core.Functions.toDate
import com.example.myshopmanagerapp.core.Functions.toLocalDate
import com.example.myshopmanagerapp.core.Functions.toTimestamp
import com.example.myshopmanagerapp.core.Functions.toTwoDecimalPlaces
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.ComparisonTopBar
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.DateRangePickerTopBar
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.SearchTopBar
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDate


@Composable
fun DebtListScreenTopBar(
    entireDebts: DebtEntities,
    allDebts: DebtEntities,
    showSearchBar: Boolean,
    showComparisonBar: Boolean,
    showDateRangePickerBar: Boolean,
    getPersonnelName: (String)-> String,
    getCustomerName: (String)-> String,
    openDialogInfo: (String)-> Unit,
    openSearchBar: ()-> Unit,
    openDateRangePickerBar: ()-> Unit,
    closeSearchBar: ()-> Unit,
    closeDateRangePickerBar: ()-> Unit,
    closeComparisonBar: ()-> Unit,
    openComparisonBar: ()-> Unit,
    printDebts: ()-> Unit,
    getDebts: (DebtEntities)-> Unit,
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
                            getDebts(entireDebts)
                            openDialogInfo(emptyString)
                            openDialogInfo(emptyString)
                            closeSearchBar()
                        }
                    }else{
                        getDebts(entireDebts.filter { getCustomerName(it.uniqueCustomerId).contains(_searchValue, true) || getPersonnelName(it.uniquePersonnelId).contains(_searchValue, true) })
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
                        getDebts(allDebts.filter { it.debtAmount >= convertToDouble(_value) })
                    }
                    else{
                        getDebts(allDebts.filter { it.debtAmount <= convertToDouble(_value) })
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
                    getDebts(allDebts.filter { it.date in startDate..endDate })
                    closeDateRangePickerBar()
                    openDialogInfo(emptyString)
                    openDialogInfo(emptyString)
                }
            )
        }
        else->{
            DebtListTopBar(
                topBarTitleText = "Debts",
                print = { printDebts() },
                onSort = { _value ->
                    when (_value.number) {
                        1 -> {
                            getDebts(entireDebts.sortedBy { it.date })
                            openDialogInfo(emptyString)
                            openDialogInfo(emptyString)
                            val dialogMessage = "Most recent debts will appear at the bottom"
                            Toast.makeText(context, dialogMessage, Toast.LENGTH_LONG).show()
                        }
                        2 -> {
                            getDebts(allDebts.sortedByDescending { it.date })
                            openDialogInfo(emptyString)
                            openDialogInfo(emptyString)
                            val dialogMessage = "Most recent debts will appear on top"
                            Toast.makeText(context, dialogMessage, Toast.LENGTH_LONG).show()
                        }
                        3 -> {
                            getDebts(allDebts.sortedBy { it.debtAmount })
                            openDialogInfo(emptyString)
                            openDialogInfo(emptyString)
                            val dialogMessage = "Lowest debts will appear at the top"
                            Toast.makeText(context, dialogMessage, Toast.LENGTH_LONG).show()
                        }
                        4 -> {
                            getDebts(allDebts.sortedByDescending { it.debtAmount })
                            openDialogInfo(emptyString)
                            openDialogInfo(emptyString)
                            val dialogMessage = "Highest debts will appear at the top"
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
                        (_period.titleText == AllTime) -> { getDebts(entireDebts)
                            openDialogInfo(emptyString)
                            openDialogInfo(emptyString)
                        }
                        (_period.titleText == SelectRange) -> { openDateRangePickerBar() }
                        else -> {
                            getDebts(entireDebts.filter { it.date in firstDate until lastDate })
                            openDialogInfo(emptyString)
                            openDialogInfo(emptyString)
                        }
                    }
                },
                onClickListItem = { _listNumber ->
                    val number = _listNumber.number
                    when (_listNumber.number) {
                        0 -> {
                            getDebts(entireDebts)
                            openDialogInfo(emptyString)
                            openDialogInfo(emptyString)
                            val dialogMessage = "All debts are selected"
                            Toast.makeText(context, dialogMessage, Toast.LENGTH_LONG).show()
                        }
                        1 -> {
                            Toast.makeText(context, "Search", Toast.LENGTH_LONG).show()
                            openSearchBar()
                        }
                        2 -> {
                            val dialogMessage =
                                "Total number of debts on this list are ${allDebts.size}" +
                                "\nTotal amount of debts on this list is GHS ${
                                allDebts.sumOf { it.debtAmount }.toTwoDecimalPlaces()}"
                            openDialogInfo(dialogMessage)
                        }
                        else -> {
                            getDebts(allDebts.take(number))
                            val dialogMessage = "First $number debts selected"
                            Toast.makeText(context, dialogMessage, Toast.LENGTH_LONG).show()
                        }
                    }
                },
                periodDropDownItems = listOfPeriods,
                listDropDownItems = listOfListNumbers,
                listOfSortItems = listOfDebtSortItems,
            ) {
                navigateBack()
            }
        }
    }
}
