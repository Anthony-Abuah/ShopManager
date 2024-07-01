package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.home.expenses

import android.widget.Toast
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.example.myshopmanagerapp.core.Constants.AllTime
import com.example.myshopmanagerapp.core.Constants.SelectRange
import com.example.myshopmanagerapp.core.Constants.emptyString
import com.example.myshopmanagerapp.core.Constants.listOfExpenseSortItems
import com.example.myshopmanagerapp.core.Constants.listOfListNumbers
import com.example.myshopmanagerapp.core.Constants.listOfPeriods
import com.example.myshopmanagerapp.core.ExpenseEntities
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
fun ExpenseListScreenTopBar(
    entireExpenses: ExpenseEntities,
    allExpenses: ExpenseEntities,
    showSearchBar: Boolean,
    showComparisonBar: Boolean,
    showDateRangePickerBar: Boolean,
    getPersonnelName: (String)-> String,
    openDialogInfo: (String)-> Unit,
    openSearchBar: ()-> Unit,
    openDateRangePickerBar: ()-> Unit,
    closeSearchBar: ()-> Unit,
    closeDateRangePickerBar: ()-> Unit,
    closeComparisonBar: ()-> Unit,
    openComparisonBar: ()-> Unit,
    printExpenses: ()-> Unit,
    getExpenses: (ExpenseEntities)-> Unit,
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
                goBack = {
                     closeSearchBar()
                },
                getSearchValue = {_searchValue->
                    if (_searchValue.isBlank()){
                        coroutineScope.launch {
                            delay(10000L)
                            getExpenses(entireExpenses)
                            openDialogInfo(emptyString)
                            openDialogInfo(emptyString)
                            closeSearchBar()
                        }
                    }else{
                        getExpenses(entireExpenses.filter { it.expenseType.contains(_searchValue, true) || it.expenseName.contains(_searchValue, true) || getPersonnelName(it.uniquePersonnelId).contains(_searchValue, true) })
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
                        getExpenses(allExpenses.filter { it.expenseAmount >= convertToDouble(_value) })
                    }
                    else{
                        getExpenses(allExpenses.filter { it.expenseAmount <= convertToDouble(_value) })
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
                    getExpenses(allExpenses.filter { it.date in startDate..endDate })
                    closeDateRangePickerBar()
                    openDialogInfo(emptyString)
                    openDialogInfo(emptyString)
                }
            )
        }
        else->{
            ExpenseListTopBar(
                topBarTitleText = "Expenses",
                print = { printExpenses() },
                onSort = { _value ->
                    when (_value.number) {
                        1 -> {
                            getExpenses(entireExpenses.sortedBy { it.date })
                            openDialogInfo(emptyString)
                            openDialogInfo(emptyString)
                            val dialogMessage = "Most recent expenses will appear at the bottom"
                            Toast.makeText(context, dialogMessage, Toast.LENGTH_LONG).show()
                        }
                        2 -> {
                            getExpenses(allExpenses.sortedByDescending { it.date })
                            openDialogInfo(emptyString)
                            openDialogInfo(emptyString)
                            val dialogMessage = "Most recent expenses will appear on top"
                            Toast.makeText(context, dialogMessage, Toast.LENGTH_LONG).show()
                        }
                        3 -> {
                            getExpenses(allExpenses.sortedBy { it.expenseAmount })
                            openDialogInfo(emptyString)
                            openDialogInfo(emptyString)
                            val dialogMessage = "Lowest expenses will appear at the top"
                            Toast.makeText(context, dialogMessage, Toast.LENGTH_LONG).show()
                        }
                        4 -> {
                            getExpenses(allExpenses.sortedByDescending { it.expenseAmount })
                            openDialogInfo(emptyString)
                            openDialogInfo(emptyString)
                            val dialogMessage = "Highest expenses will appear at the top"
                            Toast.makeText(context, dialogMessage, Toast.LENGTH_LONG).show()
                        }
                        5 -> {
                            getExpenses(allExpenses.sortedBy { it.expenseName.take(1) })
                            openDialogInfo(emptyString)
                            openDialogInfo(emptyString)
                            val dialogMessage = "Highest expenses will appear at the top"
                            Toast.makeText(context, dialogMessage, Toast.LENGTH_LONG).show()
                        }
                        6 -> {
                            getExpenses(allExpenses.sortedByDescending { it.expenseName.take(1) })
                            openDialogInfo(emptyString)
                            openDialogInfo(emptyString)
                            val dialogMessage = "Highest expenses will appear at the top"
                            Toast.makeText(context, dialogMessage, Toast.LENGTH_LONG).show()
                        }
                        7 -> {
                            comparisonIsGreater = true
                            openComparisonBar()
                        }
                        8 -> {
                            comparisonIsGreater = false
                            openComparisonBar()
                        }
                    }
                },
                onClickPeriodItem = { _period ->
                    val firstDate = _period.firstDate.toDate().time
                    val lastDate = _period.lastDate.toDate().time
                    when(true){
                        (_period.titleText == AllTime) -> { getExpenses(entireExpenses)
                            openDialogInfo(emptyString)
                            openDialogInfo(emptyString)
                        }
                        (_period.titleText == SelectRange) -> { openDateRangePickerBar() }
                        else -> {
                            getExpenses(allExpenses.filter { it.date in firstDate until lastDate })
                            openDialogInfo(emptyString)
                            openDialogInfo(emptyString)
                        }
                    }
                },
                onClickListItem = { _listNumber ->
                    val number = _listNumber.number
                    when (_listNumber.number) {
                        0 -> {
                            getExpenses(entireExpenses)
                            openDialogInfo(emptyString)
                            openDialogInfo(emptyString)
                            val dialogMessage = "All expenses are selected"
                            Toast.makeText(context, dialogMessage, Toast.LENGTH_LONG).show()
                        }
                        1 -> {
                            Toast.makeText(context, "Search", Toast.LENGTH_LONG).show()
                            openSearchBar()
                        }
                        2 -> {
                            val dialogMessage =
                                "Total number of debts on this list are ${allExpenses.size}" +
                                "\nTotal amount of debts on this list is GHS ${
                                allExpenses.sumOf { it.expenseAmount }.toTwoDecimalPlaces()}"
                            openDialogInfo(dialogMessage)
                        }
                        else -> {
                            getExpenses(allExpenses.take(number))
                            val dialogMessage = "First $number debts selected"
                            Toast.makeText(context, dialogMessage, Toast.LENGTH_LONG).show()
                        }
                    }
                },
                periodDropDownItems = listOfPeriods,
                listDropDownItems = listOfListNumbers,
                listOfSortItems = listOfExpenseSortItems,
            ) {
                navigateBack()
            }
        }
    }
}
