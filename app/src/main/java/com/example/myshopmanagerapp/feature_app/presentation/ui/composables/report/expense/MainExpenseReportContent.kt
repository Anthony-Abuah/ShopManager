package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.report.expense

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myshopmanagerapp.R
import com.example.myshopmanagerapp.core.Constants
import com.example.myshopmanagerapp.core.Constants.emptyString
import com.example.myshopmanagerapp.core.FormRelatedString.AverageDailyExpense
import com.example.myshopmanagerapp.core.FormRelatedString.DayWithHighestExpense
import com.example.myshopmanagerapp.core.FormRelatedString.DayWithLowestExpense
import com.example.myshopmanagerapp.core.FormRelatedString.MaxExpenseAmount
import com.example.myshopmanagerapp.core.FormRelatedString.MinExpenseAmount
import com.example.myshopmanagerapp.core.FormRelatedString.RevenuePercentageAsExpense
import com.example.myshopmanagerapp.core.FormRelatedString.TotalExpenses
import com.example.myshopmanagerapp.feature_app.data.local.entities.expenses.ExpenseEntity
import com.example.myshopmanagerapp.feature_app.domain.model.ItemValue
import com.example.myshopmanagerapp.feature_app.domain.model.ListOfExpenses
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.*
import com.example.myshopmanagerapp.feature_app.presentation.ui.theme.*

@Composable
fun MainExpenseReportContent(
    currency: String,
    allExpenses: List<ExpenseEntity>,
    expenseTypes: List<ItemValue>,
    averageDailyExpense: String,
    revenuePercentageAsExpense: String,
    totalExpenseAmount: String,
    maxExpenseDay: String,
    maximumExpenseAmount: String,
    minimumExpenseAmount: String,
    minimumExpenseDay: String,
    getSelectedPeriod: (String)-> Unit,
){
    val mainBackgroundColor = if (isSystemInDarkTheme()) Grey10 else Grey99
    val alternateBackgroundColor = if (isSystemInDarkTheme()) Grey15 else Grey95
    val cardBackgroundColor = if (isSystemInDarkTheme()) Grey15 else BlueGrey90

    Column(modifier = Modifier
        .background(mainBackgroundColor)
        .fillMaxSize()
    ) {
        val listOfPeriods = Constants.listOfPeriods.map { it.titleText }
        Box(
            modifier = Modifier.padding(LocalSpacing.current.smallMedium),
            contentAlignment = Alignment.Center
        ) {
            TimeRange(listOfTimes = listOfPeriods.dropLast(1),
                getSelectedItem = { selectedPeriod->
                    getSelectedPeriod(selectedPeriod)
                }
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .background(mainBackgroundColor)
                .padding(LocalSpacing.current.noPadding)
                .verticalScroll(state = rememberScrollState(), enabled = true),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
        ) {
            Box(modifier = Modifier
                .background(mainBackgroundColor)
                .fillMaxWidth()
                .height(150.dp)
                .padding(LocalSpacing.current.small),
                contentAlignment = Alignment.Center
            ){
                InfoDisplayCard(
                    icon = R.drawable.expense,
                    currency = currency,
                    currencySize = 36.sp,
                    bigText = "$currency $totalExpenseAmount",
                    bigTextSize = 28.sp,
                    smallText = TotalExpenses,
                    smallTextSize = 16.sp,
                    backgroundColor = cardBackgroundColor,
                    elevation = LocalSpacing.current.small,
                    isAmount = false
                )
            }

            Spacer(modifier = Modifier.height(LocalSpacing.current.smallMedium))

            Row(modifier = Modifier
                .background(alternateBackgroundColor)
                .fillMaxWidth()
                .height(150.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(
                        horizontal = LocalSpacing.current.small,
                        vertical = LocalSpacing.current.smallMedium),
                    contentAlignment = Alignment.Center
                ){
                    InfoDisplayCard(
                        icon = R.drawable.expense,
                        imageWidth = 40.dp,
                        currency = currency,
                        currencySize = 36.sp,
                        bigText = "$currency $averageDailyExpense",
                        bigTextSize = 18.sp,
                        smallText = AverageDailyExpense,
                        smallTextSize = 10.sp,
                        backgroundColor = mainBackgroundColor,
                        elevation = LocalSpacing.current.small,
                        isAmount = false
                    )
                }

                Box(modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(
                        horizontal = LocalSpacing.current.small,
                        vertical = LocalSpacing.current.smallMedium
                    ),
                    contentAlignment = Alignment.Center
                ){
                    InfoDisplayCard(
                        icon = R.drawable.expense,
                        imageWidth = 40.dp,
                        currency = currency,
                        currencySize = 36.sp,
                        bigText = "$revenuePercentageAsExpense %",
                        bigTextSize = 18.sp,
                        smallText = RevenuePercentageAsExpense,
                        smallTextSize = 10.sp,
                        backgroundColor = mainBackgroundColor,
                        elevation = LocalSpacing.current.small,
                        isAmount = false
                    )
                }
            }

            Box(modifier = Modifier
                .background(mainBackgroundColor)
                .fillMaxWidth()
                .padding(vertical = LocalSpacing.current.smallMedium)
            ) {
                val expensePoints = ListOfExpenses(allExpenses).toLinePoints()
                LineChartCard(
                    lineTitle = "Expenses",
                    pointsData = expensePoints
                )
            }


            Row(modifier = Modifier
                .background(alternateBackgroundColor)
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
                            icon = R.drawable.expense,
                            imageWidth = 32.dp,
                            currency = currency,
                            currencySize = 20.sp,
                            bigText = maxExpenseDay,
                            bigTextSize = 16.sp,
                            smallText = DayWithHighestExpense,
                            smallTextSize = 10.sp,
                            backgroundColor = mainBackgroundColor,
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
                            icon = R.drawable.expense,
                            imageWidth = 32.dp,
                            currency = currency,
                            currencySize = 20.sp,
                            bigText = minimumExpenseDay,
                            bigTextSize = 16.sp,
                            smallText = DayWithLowestExpense,
                            smallTextSize = 10.sp,
                            backgroundColor = mainBackgroundColor,
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
                            bigText = "$currency $maximumExpenseAmount",
                            bigTextSize = 16.sp,
                            smallText = MaxExpenseAmount,
                            smallTextSize = 10.sp,
                            backgroundColor = mainBackgroundColor,
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
                            icon = R.drawable.expense,
                            imageWidth = 32.dp,
                            currency = currency,
                            currencySize = 20.sp,
                            bigText = "$currency $minimumExpenseAmount",
                            bigTextSize = 16.sp,
                            smallText = MinExpenseAmount,
                            smallTextSize = 10.sp,
                            backgroundColor = mainBackgroundColor,
                            shape = MaterialTheme.shapes.medium,
                            elevation = LocalSpacing.current.small,
                            isAmount = false
                        )
                    }
                }
            }

            Box(modifier = Modifier
                .padding(vertical = LocalSpacing.current.smallMedium)
                .background(mainBackgroundColor)
                .fillMaxWidth()
                .height(LocalSpacing.current.large)
                .clickable {},
                contentAlignment = Alignment.Center
            ) {
                HorizontalInfoDisplayCard(
                    icon = R.drawable.ic_expense_type,
                    imageWidth = 0.dp,
                    modifier = Modifier.padding(horizontal = LocalSpacing.current.smallMedium),
                    name = "Expense types",
                    nameTextSize = 16.sp,
                    valueText = " ",
                    valueTextSize = 16.sp
                )
            }

            val listOfCardColors = listOf(alternateBackgroundColor, mainBackgroundColor)
            expenseTypes.forEachIndexed { index, type ->
                val color = listOfCardColors[index%2]
                Box(modifier = Modifier
                    .background(color)
                    .fillMaxWidth()
                    .height(LocalSpacing.current.textFieldHeight)
                    .clickable {},
                    contentAlignment = Alignment.Center
                ) {
                    HorizontalInfoDisplayCard(
                        icon = R.drawable.ic_expense_type,
                        modifier = Modifier.padding(LocalSpacing.current.small),
                        name = type.itemName,
                        nameTextSize = 16.sp,
                        valueText = "$currency ${type.value}",
                        valueTextSize = 16.sp
                    )
                }
            }


        }
    }

}

