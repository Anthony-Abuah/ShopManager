package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.report.revenue

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myshopmanagerapp.R
import com.example.myshopmanagerapp.core.Constants.listOfPeriods
import com.example.myshopmanagerapp.core.FormRelatedString.AverageDailyRevenue
import com.example.myshopmanagerapp.core.FormRelatedString.AverageHourlyRevenue
import com.example.myshopmanagerapp.core.FormRelatedString.AverageHoursPerDay
import com.example.myshopmanagerapp.core.FormRelatedString.DayWithHighestRevenue
import com.example.myshopmanagerapp.core.FormRelatedString.DayWithLowestRevenue
import com.example.myshopmanagerapp.core.FormRelatedString.MaxRevenueAmount
import com.example.myshopmanagerapp.core.FormRelatedString.MinRevenueAmount
import com.example.myshopmanagerapp.core.FormRelatedString.NumberOfDaysOpened
import com.example.myshopmanagerapp.core.FormRelatedString.RevenuePercentageAsDebt
import com.example.myshopmanagerapp.core.FormRelatedString.TotalDebtAmount
import com.example.myshopmanagerapp.core.FormRelatedString.TotalDebtRepaymentAmount
import com.example.myshopmanagerapp.core.FormRelatedString.TotalInventoryCost
import com.example.myshopmanagerapp.core.FormRelatedString.TotalNumberOfHours
import com.example.myshopmanagerapp.core.FormRelatedString.TotalOutstandingDebtAmount
import com.example.myshopmanagerapp.core.FormRelatedString.TotalRevenues
import com.example.myshopmanagerapp.feature_app.data.local.entities.revenue.RevenueEntity
import com.example.myshopmanagerapp.feature_app.domain.model.ListOfRevenues
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.*
import com.example.myshopmanagerapp.feature_app.presentation.ui.theme.*

@Composable
fun MainRevenueReportContent(
    currency: String,
    allRevenues: List<RevenueEntity>,
    inventoryCost: String,
    debtPercentage: String,
    averageDailyHours: String,
    averageDailyRevenue: String,
    averageHourlyRevenue: String,
    totalDays: String,
    totalHours: String,
    outstandingDebtAmount: String,
    totalRevenueAmount: String,
    totalDebtAmount: String,
    totalDebtRepaymentAmount: String,
    maxRevenueDay: String,
    maximumRevenueAmount: String,
    minimumRevenueAmount: String,
    minimumRevenueDay: String,
    getSelectedPeriod: (String)-> Unit,
){

    val mainBackgroundColor = if (isSystemInDarkTheme()) Grey10 else Grey99
    val alternateBackgroundColor = if (isSystemInDarkTheme()) Grey15 else Grey95
    val cardBackgroundColor = if (isSystemInDarkTheme()) Grey15 else BlueGrey90

    Column(modifier = Modifier
        .background(mainBackgroundColor)
        .fillMaxSize()
    ) {
        val listOfPeriods = listOfPeriods.map { it.titleText }
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
                .fillMaxWidth()
                .height(150.dp)
                .padding(LocalSpacing.current.small),
                contentAlignment = Alignment.Center
            ){
                InfoDisplayCard(
                    icon = R.drawable.revenue,
                    currency = currency,
                    currencySize = 36.sp,
                    bigText = "$currency $totalRevenueAmount",
                    bigTextSize = 28.sp,
                    smallText = TotalRevenues,
                    smallTextSize = 16.sp,
                    backgroundColor = cardBackgroundColor,
                    elevation = LocalSpacing.current.small,
                    isAmount = false
                )
            }

            Spacer(modifier = Modifier.height(LocalSpacing.current.smallMedium))

            Box(modifier = Modifier
                .background(alternateBackgroundColor)
                .fillMaxWidth()
                .height(LocalSpacing.current.textFieldHeight)
                .clickable {
                    // navigateToViewInventoryItemsScreen()
                },
                contentAlignment = Alignment.Center
            ) {
                HorizontalInfoDisplayCard(
                    icon = R.drawable.days,
                    modifier = Modifier.padding(LocalSpacing.current.small),
                    name = NumberOfDaysOpened,
                    nameTextSize = 16.sp,
                    valueText = "$totalDays days",
                    valueTextSize = 16.sp
                )
            }

            Box(modifier = Modifier
                .background(mainBackgroundColor)
                .fillMaxWidth()
                .height(LocalSpacing.current.textFieldHeight)
                .clickable {},
                contentAlignment = Alignment.Center
            ) {
                HorizontalInfoDisplayCard(
                    icon = R.drawable.cash,
                    modifier = Modifier.padding(LocalSpacing.current.small),
                    name = AverageDailyRevenue,
                    nameTextSize = 16.sp,
                    valueText = "$currency $averageDailyRevenue",
                    valueTextSize = 16.sp
                )
            }


            Box(modifier = Modifier
                .background(alternateBackgroundColor)
                .fillMaxWidth()
                .height(LocalSpacing.current.textFieldHeight)
                .clickable {},
                contentAlignment = Alignment.Center
            ) {
                HorizontalInfoDisplayCard(
                    icon = R.drawable.time,
                    modifier = Modifier.padding(LocalSpacing.current.small),
                    name = TotalNumberOfHours,
                    nameTextSize = 16.sp,
                    valueText = "$totalHours hour(s)",
                    valueTextSize = 16.sp
                )
            }

            Box(modifier = Modifier
                .background(mainBackgroundColor)
                .fillMaxWidth()
                .height(LocalSpacing.current.textFieldHeight)
                .clickable {},
                contentAlignment = Alignment.Center
            ) {
                HorizontalInfoDisplayCard(
                    icon = R.drawable.cash,
                    modifier = Modifier.padding(LocalSpacing.current.small),
                    name = AverageHourlyRevenue,
                    nameTextSize = 16.sp,
                    valueText = "$currency $averageHourlyRevenue",
                    valueTextSize = 16.sp
                )
            }

            Box(modifier = Modifier
                .background(alternateBackgroundColor)
                .fillMaxWidth()
                .height(LocalSpacing.current.textFieldHeight)
                .clickable {},
                contentAlignment = Alignment.Center
            ) {
                HorizontalInfoDisplayCard(
                    icon = R.drawable.time,
                    modifier = Modifier.padding(LocalSpacing.current.small),
                    name = AverageHoursPerDay,
                    nameTextSize = 16.sp,
                    valueText = "$averageDailyHours hours(s)",
                    valueTextSize = 16.sp
                )
            }

            Box(modifier = Modifier
                .background(mainBackgroundColor)
                .fillMaxWidth()
                .height(LocalSpacing.current.textFieldHeight)
                .clickable {},
                contentAlignment = Alignment.Center
            ) {
                HorizontalInfoDisplayCard(
                    icon = R.drawable.inventory_item,
                    modifier = Modifier.padding(LocalSpacing.current.small),
                    name = TotalInventoryCost,
                    nameTextSize = 16.sp,
                    valueText = "$currency $inventoryCost",
                    valueTextSize = 16.sp
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
                            icon = R.drawable.debt,
                            imageWidth = 32.dp,
                            currency = currency,
                            currencySize = 20.sp,
                            bigText = totalDebtAmount,
                            bigTextSize = 18.sp,
                            smallText = TotalDebtAmount,
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
                            icon = R.drawable.percentage,
                            imageWidth = 32.dp,
                            currency = currency,
                            currencySize = 20.sp,
                            bigText = "$debtPercentage %",
                            bigTextSize = 18.sp,
                            smallText = RevenuePercentageAsDebt,
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
                            icon = R.drawable.debt_payment,
                            imageWidth = 32.dp,
                            currency = currency,
                            currencySize = 20.sp,
                            bigText = totalDebtRepaymentAmount,
                            bigTextSize = 18.sp,
                            smallText = TotalDebtRepaymentAmount,
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
                            icon = R.drawable.withdrawal,
                            imageWidth = 32.dp,
                            currency = currency,
                            currencySize = 20.sp,
                            bigText = outstandingDebtAmount,
                            bigTextSize = 18.sp,
                            smallText = TotalOutstandingDebtAmount,
                            smallTextSize = 10.sp,
                            backgroundColor = mainBackgroundColor,
                            shape = MaterialTheme.shapes.medium,
                            elevation = LocalSpacing.current.small,
                            isAmount = false
                        )
                    }
                }
            }

            Column(modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = LocalSpacing.current.small,
                    vertical = LocalSpacing.current.smallMedium
                )) {
                val revenuePoints = ListOfRevenues(allRevenues).toLinePoints()
                LineChartCard(
                    lineTitle = "Revenues",
                    pointsData = revenuePoints
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
                            icon = R.drawable.highest_revenue,
                            imageWidth = 32.dp,
                            currency = currency,
                            currencySize = 20.sp,
                            bigText = maxRevenueDay,
                            bigTextSize = 16.sp,
                            smallText = DayWithHighestRevenue,
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
                            icon = R.drawable.lowest_revenue,
                            imageWidth = 32.dp,
                            currency = currency,
                            currencySize = 20.sp,
                            bigText = minimumRevenueDay,
                            bigTextSize = 16.sp,
                            smallText = DayWithLowestRevenue,
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
                            icon = R.drawable.cash,
                            imageWidth = 32.dp,
                            currency = currency,
                            currencySize = 20.sp,
                            bigText = "$currency $maximumRevenueAmount",
                            bigTextSize = 16.sp,
                            smallText = MaxRevenueAmount,
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
                            icon = R.drawable.cash,
                            imageWidth = 32.dp,
                            currency = currency,
                            currencySize = 20.sp,
                            bigText = "$currency $minimumRevenueAmount",
                            bigTextSize = 16.sp,
                            smallText = MinRevenueAmount,
                            smallTextSize = 10.sp,
                            backgroundColor = mainBackgroundColor,
                            shape = MaterialTheme.shapes.medium,
                            elevation = LocalSpacing.current.small,
                            isAmount = false
                        )
                    }
                }
            }


        }
    }


}