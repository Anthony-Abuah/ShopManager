package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.home.bank_account

import android.widget.Toast
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.example.myshopmanagerapp.core.Constants.emptyString
import com.example.myshopmanagerapp.core.Constants.listOfBankAccountSortItems
import com.example.myshopmanagerapp.core.Constants.listOfListNumbers
import com.example.myshopmanagerapp.core.BankAccountEntities
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
fun BankAccountListScreenTopBar(
    entireBankAccounts: BankAccountEntities,
    allBankAccounts: BankAccountEntities,
    showSearchBar: Boolean,
    showComparisonBar: Boolean,
    openDialogInfo: (String)-> Unit,
    openSearchBar: ()-> Unit,
    openComparisonBar: ()-> Unit,
    closeSearchBar: ()-> Unit,
    closeComparisonBar: ()-> Unit,
    printBankAccounts: ()-> Unit,
    getBankAccounts: (BankAccountEntities)-> Unit,
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
                            getBankAccounts(entireBankAccounts)
                            openDialogInfo(emptyString)
                            openDialogInfo(emptyString)
                            closeSearchBar()
                        }
                    }else{
                        getBankAccounts(entireBankAccounts.filter { it.bankAccountName.contains(_searchValue, true) || it.bankLocation.toNotNull().contains(_searchValue, true)  || it.bankName.toNotNull().contains(_searchValue, true) })
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
                        getBankAccounts(allBankAccounts.filter { it.accountBalance.toNotNull() >= convertToDouble(_value) })
                    }
                    else{
                        getBankAccounts(allBankAccounts.filter { it.accountBalance.toNotNull() <= convertToDouble(_value) })
                    }
                    closeComparisonBar()
                    openDialogInfo(emptyString)
                    openDialogInfo(emptyString)
                }
            )
        }
        else->{
            BankAccountListTopBar(
                topBarTitleText = "Bank Accounts",
                print = { printBankAccounts() },
                onSort = { _value ->
                    when (_value.number) {
                        1 -> {
                            getBankAccounts(entireBankAccounts.sortedBy { it.bankAccountName.take(1) })
                            openDialogInfo(emptyString)
                            openDialogInfo(emptyString)
                            val dialogMessage = "Sorted ascending by bank account names"
                            Toast.makeText(context, dialogMessage, Toast.LENGTH_LONG).show()
                        }
                        2 -> {
                            getBankAccounts(allBankAccounts.sortedByDescending { it.bankAccountName.take(1) })
                            openDialogInfo(emptyString)
                            openDialogInfo(emptyString)
                            val dialogMessage = "Sorted descending by bank account names"
                            Toast.makeText(context, dialogMessage, Toast.LENGTH_LONG).show()
                        }
                        3 -> {
                            getBankAccounts(allBankAccounts.sortedBy { it.accountBalance.toNotNull() })
                            openDialogInfo(emptyString)
                            openDialogInfo(emptyString)
                            val dialogMessage = "Sorted ascending by account balance"
                            Toast.makeText(context, dialogMessage, Toast.LENGTH_LONG).show()
                        }
                        4 -> {
                            getBankAccounts(allBankAccounts.sortedByDescending { it.accountBalance.toNotNull() })
                            openDialogInfo(emptyString)
                            openDialogInfo(emptyString)
                            val dialogMessage = "Sorted descending by account balance"
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
                            getBankAccounts(entireBankAccounts)
                            openDialogInfo(emptyString)
                            openDialogInfo(emptyString)
                            val dialogMessage = "All bank accounts are selected"
                            Toast.makeText(context, dialogMessage, Toast.LENGTH_LONG).show()
                        }
                        1 -> {
                            Toast.makeText(context, "Search", Toast.LENGTH_LONG).show()
                            openSearchBar()
                        }
                        2 -> {
                            val dialogMessage =
                                "Total number of bank accounts on this list are ${allBankAccounts.size}" +
                                "\nTotal account balance on this list is GHS ${
                                allBankAccounts.sumOf { it.accountBalance.toNotNull() }.toTwoDecimalPlaces()}"
                            openDialogInfo(dialogMessage)
                        }
                        else -> {
                            getBankAccounts(allBankAccounts.take(number))
                            val dialogMessage = "First $number bankAccounts selected"
                            Toast.makeText(context, dialogMessage, Toast.LENGTH_LONG).show()
                        }
                    }
                },
                listDropDownItems = listOfListNumbers,
                listOfSortItems = listOfBankAccountSortItems,
            ) {
                navigateBack()
            }
        }
    }
}
