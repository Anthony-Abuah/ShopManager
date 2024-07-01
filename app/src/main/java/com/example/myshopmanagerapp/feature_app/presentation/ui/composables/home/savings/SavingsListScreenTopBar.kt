package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.home.savings

import android.widget.Toast
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.example.myshopmanagerapp.core.Constants.AllTime
import com.example.myshopmanagerapp.core.Constants.SelectRange
import com.example.myshopmanagerapp.core.Constants.emptyString
import com.example.myshopmanagerapp.core.Constants.listOfListNumbers
import com.example.myshopmanagerapp.core.Constants.listOfPeriods
import com.example.myshopmanagerapp.core.Constants.listOfRevenueSortItems
import com.example.myshopmanagerapp.core.Constants.listOfSavingsSortItems
import com.example.myshopmanagerapp.core.FormRelatedString.EnterValue
import com.example.myshopmanagerapp.core.FormRelatedString.SearchPlaceholder
import com.example.myshopmanagerapp.core.Functions.convertToDouble
import com.example.myshopmanagerapp.core.Functions.toDate
import com.example.myshopmanagerapp.core.Functions.toLocalDate
import com.example.myshopmanagerapp.core.Functions.toTimestamp
import com.example.myshopmanagerapp.core.Functions.toTwoDecimalPlaces
import com.example.myshopmanagerapp.core.RevenueEntities
import com.example.myshopmanagerapp.core.SavingsEntities
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.ComparisonTopBar
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.DateRangePickerTopBar
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.SearchScreenTopBar
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDate


@Composable
fun SavingsListScreenTopBar(
    entireSavings: SavingsEntities,
    allSavings: SavingsEntities,
    showSearchBar: Boolean,
    showComparisonBar: Boolean,
    showDateRangePickerBar: Boolean,
    getPersonnelName: (String)-> String,
    getBankAccountName: (String)-> String,
    openDialogInfo: (String)-> Unit,
    openSearchBar: ()-> Unit,
    closeSearchBar: ()-> Unit,
    closeDateRangePickerBar: ()-> Unit,
    closeComparisonBar: ()-> Unit,
    openComparisonBar: ()-> Unit,
    openDateRangePickerBar: ()-> Unit,
    printSavings: ()-> Unit,
    getSavings: (SavingsEntities)-> Unit,
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
                            getSavings(entireSavings)
                            openDialogInfo(emptyString)
                            openDialogInfo(emptyString)
                            closeSearchBar()
                        }
                    }else{
                        getSavings(entireSavings.filter { getBankAccountName(it.uniqueBankAccountId).contains(_searchValue, true) || getPersonnelName(it.uniquePersonnelId).contains(_searchValue, true) })
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
                        getSavings(allSavings.filter { it.savingsAmount >= convertToDouble(_value) })
                    } else{
                        getSavings(allSavings.filter { it.savingsAmount <= convertToDouble(_value) })
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
                    getSavings(allSavings.filter { it.date in startDate..endDate })
                    closeDateRangePickerBar()
                    openDialogInfo(emptyString)
                    openDialogInfo(emptyString)
                }
            )
        }
        else->{
            SavingsListTopBar(
                topBarTitleText = "Savings",
                print = { printSavings() },
                onSort = { _value ->
                    when (_value.number) {
                        1 -> {
                            getSavings(allSavings.sortedBy { it.date })
                            openDialogInfo(emptyString)
                            openDialogInfo(emptyString)
                            val dialogMessage = "Most recent savings will appear at the bottom"
                            Toast.makeText(context, dialogMessage, Toast.LENGTH_LONG).show()
                        }
                        2 -> {
                            getSavings(allSavings.sortedByDescending { it.date })
                            openDialogInfo(emptyString)
                            openDialogInfo(emptyString)
                            val dialogMessage = "Most recent savings will appear on top"
                            Toast.makeText(context, dialogMessage, Toast.LENGTH_LONG).show()
                        }
                        3 -> {
                            getSavings(allSavings.sortedBy { it.savingsAmount })
                            openDialogInfo(emptyString)
                            openDialogInfo(emptyString)
                            val dialogMessage = "Lowest savings will appear at the top"
                            Toast.makeText(context, dialogMessage, Toast.LENGTH_LONG).show()
                        }
                        4 -> {
                            getSavings(allSavings.sortedByDescending { it.savingsAmount })
                            openDialogInfo(emptyString)
                            openDialogInfo(emptyString)
                            val dialogMessage = "Highest savings will appear at the top"
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
                        (_period.titleText == AllTime) -> { getSavings(entireSavings)
                            openDialogInfo(emptyString)
                            openDialogInfo(emptyString)
                        }
                        (_period.titleText == SelectRange) -> { openDateRangePickerBar() }
                        else -> {
                            getSavings(allSavings.filter { it.date in firstDate until lastDate })
                            openDialogInfo(emptyString)
                            openDialogInfo(emptyString)
                        }
                    }
                },
                onClickListItem = { _listNumber ->
                    val number = _listNumber.number
                    when (_listNumber.number) {
                        0 -> {
                            getSavings(entireSavings)
                            openDialogInfo(emptyString)
                            openDialogInfo(emptyString)
                            val dialogMessage = "All savings are selected"
                            Toast.makeText(context, dialogMessage, Toast.LENGTH_LONG).show()
                        }
                        1 -> {
                            Toast.makeText(context, "Search", Toast.LENGTH_LONG).show()
                            openSearchBar()
                        }
                        2 -> {
                            val dialogMessage =
                                "Total number of savings on this list are ${allSavings.size}" +
                                "\nTotal amount of savings on this list is GHS ${
                                allSavings.sumOf { it.savingsAmount }.toTwoDecimalPlaces()}"
                            openDialogInfo(dialogMessage)
                        }
                        else -> {
                            getSavings(allSavings.take(number))
                            val dialogMessage = "First $number savings selected"
                            Toast.makeText(context, dialogMessage, Toast.LENGTH_LONG).show()
                        }
                    }
                },
                periodDropDownItems = listOfPeriods,
                listDropDownItems = listOfListNumbers,
                listOfSortItems = listOfSavingsSortItems,
            ) {
                navigateBack()
            }
        }
    }
}
