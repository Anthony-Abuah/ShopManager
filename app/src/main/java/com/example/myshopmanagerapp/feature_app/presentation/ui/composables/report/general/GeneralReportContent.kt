package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.report.general

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myshopmanagerapp.R
import com.example.myshopmanagerapp.core.Constants.emptyString
import com.example.myshopmanagerapp.core.Constants.listOfPeriods
import com.example.myshopmanagerapp.core.FormRelatedString.NumberOfActivePersonnel
import com.example.myshopmanagerapp.core.FormRelatedString.NumberOfBankAccounts
import com.example.myshopmanagerapp.core.FormRelatedString.NumberOfInventoryItems
import com.example.myshopmanagerapp.core.FormRelatedString.NumberOfOwingCustomers
import com.example.myshopmanagerapp.core.FormRelatedString.ShopValue
import com.example.myshopmanagerapp.core.FormRelatedString.ShopValueInfo
import com.example.myshopmanagerapp.core.FormRelatedString.TotalExpenses
import com.example.myshopmanagerapp.core.FormRelatedString.TotalRevenues
import com.example.myshopmanagerapp.core.FormRelatedString.TotalSavingsAmount
import com.example.myshopmanagerapp.core.FormRelatedString.TotalWithdrawals
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.*
import com.example.myshopmanagerapp.feature_app.presentation.ui.theme.*

@Composable
fun GeneralReportContent(
    currency: String,
    numberOfInventoryItems: String,
    totalSavings: String,
    numberOfOwingCustomers: String,
    netIncome: String,
    totalRevenues: String,
    totalExpenses: String,
    totalWithdrawals: String,
    numberOfPersonnel: String,
    numberOfBankAccounts: String,
    shopName: String,
    productsSold: String,
    totalOutstandingDebtAmount: String,
    shopValue: String,
    getSelectedPeriod: (String)-> Unit,
    navigateToViewInventoryItemsScreen: ()-> Unit,
    navigateToViewOwingCustomersScreen: ()-> Unit,
    navigateToViewPersonnelScreen: ()-> Unit,
    navigateToViewBankAccountsScreen: ()-> Unit,
){
    val mainBackgroundColor = if (isSystemInDarkTheme()) Grey10 else Grey99
    val alternateBackgroundColor = if (isSystemInDarkTheme()) Grey15 else Grey95
    val cardBackgroundColor = if (isSystemInDarkTheme()) Grey15 else BlueGrey90
    var openShowValueInfo by remember {
        mutableStateOf(false)
    }
    Column(modifier = Modifier
        .background(mainBackgroundColor)
        .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .background(mainBackgroundColor)
                .padding(LocalSpacing.current.noPadding)
                .verticalScroll(state = rememberScrollState(), enabled = true),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
        ) {
            Column(modifier = Modifier
                .background(mainBackgroundColor)
                .fillMaxWidth()
                .height(160.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier.padding(LocalSpacing.current.smallMedium),
                    contentAlignment = Alignment.Center
                ) {
                    ShopNameDisplayCard(
                        icon = R.drawable.shop,
                        title = shopName,
                        info = "We sell $productsSold"
                    )
                }

                val listOfPeriods = listOfPeriods.map { it.titleText }
                Box(
                    modifier = Modifier.padding(LocalSpacing.current.smallMedium),
                    contentAlignment = Alignment.Center
                ) {
                    TimeRange(listOfTimes = listOfPeriods.dropLast(1),
                        getSelectedItem = {selectedPeriod->
                            getSelectedPeriod(selectedPeriod)
                        })
                }
            }

            Row(modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .clickable { openShowValueInfo = !openShowValueInfo },
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(modifier = Modifier
                    .weight(1f)
                    .padding(LocalSpacing.current.small),
                    contentAlignment = Alignment.Center
                ){
                    InfoDisplayCard(
                        icon = R.drawable.shop_value,
                        currency = currency,
                        currencySize = 36.sp,
                        bigText = "$currency $shopValue",
                        bigTextSize = 28.sp,
                        smallText = ShopValue,
                        smallTextSize = 16.sp,
                        backgroundColor = cardBackgroundColor,
                        elevation = LocalSpacing.current.small,
                        isAmount = false
                    )
                }
            }

            Row(modifier = Modifier
                .fillMaxWidth()
                .height(250.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier
                    .weight(1f)
                    .padding(vertical = LocalSpacing.current.smallMedium)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .padding(LocalSpacing.current.small),
                        contentAlignment = Alignment.Center
                    ) {
                        InfoDisplayCard(
                            icon = R.drawable.revenue,
                            imageWidth = 32.dp,
                            currency = currency,
                            currencySize = 20.sp,
                            bigText = totalRevenues,
                            bigTextSize = 18.sp,
                            smallText = TotalRevenues,
                            smallTextSize = 10.sp,
                            backgroundColor = cardBackgroundColor,
                            shape = MaterialTheme.shapes.medium,
                            elevation = LocalSpacing.current.small,
                            isAmount = false
                        )
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .padding(LocalSpacing.current.small),
                        contentAlignment = Alignment.Center
                    ) {
                        InfoDisplayCard(
                            icon = R.drawable.savings,
                            imageWidth = 32.dp,
                            currency = currency,
                            currencySize = 20.sp,
                            bigText = totalSavings,
                            bigTextSize = 18.sp,
                            smallText = TotalSavingsAmount,
                            smallTextSize = 10.sp,
                            backgroundColor = cardBackgroundColor,
                            shape = MaterialTheme.shapes.medium,
                            elevation = LocalSpacing.current.small,
                            isAmount = false
                        )
                    }
                }

                Column(modifier = Modifier
                    .weight(1f)
                    .padding(vertical = LocalSpacing.current.smallMedium)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .padding(LocalSpacing.current.small),
                        contentAlignment = Alignment.Center
                    ) {
                        InfoDisplayCard(
                            icon = R.drawable.expense,
                            imageWidth = 32.dp,
                            currency = currency,
                            currencySize = 20.sp,
                            bigText = totalExpenses,
                            bigTextSize = 18.sp,
                            smallText = TotalExpenses,
                            smallTextSize = 10.sp,
                            backgroundColor = cardBackgroundColor,
                            shape = MaterialTheme.shapes.medium,
                            elevation = LocalSpacing.current.small,
                            isAmount = false
                        )
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .padding(LocalSpacing.current.small),
                        contentAlignment = Alignment.Center
                    ) {
                        InfoDisplayCard(
                            icon = R.drawable.withdrawal,
                            imageWidth = 32.dp,
                            currency = currency,
                            currencySize = 20.sp,
                            bigText = totalWithdrawals,
                            bigTextSize = 18.sp,
                            smallText = TotalWithdrawals,
                            smallTextSize = 10.sp,
                            backgroundColor = cardBackgroundColor,
                            shape = MaterialTheme.shapes.medium,
                            elevation = LocalSpacing.current.small,
                            isAmount = false
                        )
                    }
                }

                Column(modifier = Modifier
                    .weight(1f)
                    .padding(vertical = LocalSpacing.current.smallMedium)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .padding(LocalSpacing.current.small),
                        contentAlignment = Alignment.Center
                    ) {
                        InfoDisplayCard(
                            icon = R.drawable.shop_value,
                            imageWidth = 32.dp,
                            currency = currency,
                            currencySize = 20.sp,
                            bigText = netIncome,
                            bigTextSize = 18.sp,
                            smallText = "Net Income",
                            smallTextSize = 10.sp,
                            backgroundColor = cardBackgroundColor,
                            shape = MaterialTheme.shapes.medium,
                            elevation = LocalSpacing.current.small,
                            isAmount = false
                        )
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .padding(LocalSpacing.current.small),
                        contentAlignment = Alignment.Center
                    ) {
                        InfoDisplayCard(
                            icon = R.drawable.debt,
                            imageWidth = 32.dp,
                            currency = currency,
                            currencySize = 20.sp,
                            bigText = totalOutstandingDebtAmount,
                            bigTextSize = 18.sp,
                            smallText = "Outstanding Debt",
                            smallTextSize = 10.sp,
                            backgroundColor = cardBackgroundColor,
                            shape = MaterialTheme.shapes.medium,
                            elevation = LocalSpacing.current.small,
                            isAmount = false
                        )
                    }
                }

            }


            Box(modifier = Modifier
                .background(alternateBackgroundColor)
                .fillMaxWidth()
                .height(LocalSpacing.current.textFieldHeight)
                .clickable { navigateToViewInventoryItemsScreen() },
                contentAlignment = Alignment.Center
            ) {
                HorizontalInfoDisplayCard(
                    icon = R.drawable.inventory_item,
                    modifier = Modifier.padding(LocalSpacing.current.small),
                    name = NumberOfInventoryItems,
                    nameTextSize = 16.sp,
                    valueText = numberOfInventoryItems,
                    valueTextSize = 16.sp
                )
            }

            Box(modifier = Modifier
                .fillMaxWidth()
                .height(LocalSpacing.current.textFieldHeight)
                .clickable { navigateToViewPersonnelScreen() },
                contentAlignment = Alignment.Center
            ) {
                HorizontalInfoDisplayCard(
                    icon = R.drawable.personnel,
                    modifier = Modifier.padding(LocalSpacing.current.small),
                    name = NumberOfActivePersonnel,
                    nameTextSize = 16.sp,
                    valueText = numberOfPersonnel,
                    valueTextSize = 16.sp
                )
            }

            Box(modifier = Modifier
                .background(alternateBackgroundColor)
                .fillMaxWidth()
                .height(LocalSpacing.current.textFieldHeight)
                .clickable { navigateToViewBankAccountsScreen() },
                contentAlignment = Alignment.Center
            ) {
                HorizontalInfoDisplayCard(
                    icon = R.drawable.bank_account,
                    modifier = Modifier.padding(LocalSpacing.current.small),
                    name = NumberOfBankAccounts,
                    nameTextSize = 16.sp,
                    valueText = numberOfBankAccounts,
                    valueTextSize = 16.sp
                )
            }

            Box(modifier = Modifier
                .fillMaxWidth()
                .height(LocalSpacing.current.textFieldHeight)
                .clickable { navigateToViewOwingCustomersScreen() },
                contentAlignment = Alignment.Center
            ) {
                HorizontalInfoDisplayCard(
                    icon = R.drawable.customer,
                    modifier = Modifier.padding(LocalSpacing.current.small),
                    name = NumberOfOwingCustomers,
                    nameTextSize = 16.sp,
                    valueText = numberOfOwingCustomers,
                    valueTextSize = 16.sp
                )
            }

            Spacer(modifier = Modifier.height(LocalSpacing.current.medium))

        }
    }
    ConfirmationInfoDialog(
        openDialog = openShowValueInfo,
        isLoading = false,
        title = emptyString,
        textContent = ShopValueInfo,
        unconfirmedDeletedToastText = null,
        confirmedDeleteToastText = null
    ) {
        openShowValueInfo = false
    }

}