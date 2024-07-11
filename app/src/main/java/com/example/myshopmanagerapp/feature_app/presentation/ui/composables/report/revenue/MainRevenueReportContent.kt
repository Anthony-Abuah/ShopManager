package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.report.revenue

import androidx.compose.foundation.layout.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.myshopmanagerapp.core.FormRelatedString.AverageDailyRevenue
import com.example.myshopmanagerapp.core.FormRelatedString.AverageHourlyRevenue
import com.example.myshopmanagerapp.core.FormRelatedString.AverageHoursPerDay
import com.example.myshopmanagerapp.core.FormRelatedString.DayWithHighestRevenue
import com.example.myshopmanagerapp.core.FormRelatedString.DayWithLowestRevenue
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
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.BasicScreenColumnWithoutBottomBar
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.LineChartCard
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.ViewTextValueRow
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.ViewTextValueWithExtraValueRow
import com.example.myshopmanagerapp.feature_app.presentation.ui.theme.LocalSpacing

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
    debtBalance: String,
    totalRevenueAmount: String,
    totalDebtAmount: String,
    totalDebtRepaymentAmount: String,
    maxRevenueDay: String,
    maximumRevenueAmount: String,
    minimumRevenueAmount: String,
    minimumRevenueDay: String
){
    BasicScreenColumnWithoutBottomBar{

        HorizontalDivider()

        // Total Revenue
        ViewTextValueRow(viewTitle = TotalRevenues, viewValue = "$currency $totalRevenueAmount")

        HorizontalDivider()

        // Total number of days
        ViewTextValueRow(viewTitle = NumberOfDaysOpened, viewValue = "$totalDays day(s)")

        HorizontalDivider()

        // Average Daily Revenue
        ViewTextValueRow(
            viewTitle = AverageDailyRevenue,
            viewValue = "$currency $averageDailyRevenue"
        )

        HorizontalDivider()

        // Total number of hours
        ViewTextValueRow(viewTitle = TotalNumberOfHours, viewValue = "$totalHours hour(s)")

        HorizontalDivider()

        // Average Hours Opened
        ViewTextValueRow(
            viewTitle = AverageHoursPerDay,
            viewValue = "$averageDailyHours hour(s)"
        )

        HorizontalDivider()

        // Average Hourly Revenue
        ViewTextValueRow(
            viewTitle = AverageHourlyRevenue,
            viewValue = "$currency $averageHourlyRevenue"
        )

        HorizontalDivider()

        // Debt Amount
        ViewTextValueRow(viewTitle = TotalDebtAmount, viewValue = "$currency $totalDebtAmount")

        HorizontalDivider()

        // Revenue % as Debt
        ViewTextValueRow(viewTitle = RevenuePercentageAsDebt, viewValue = "$debtPercentage %")

        HorizontalDivider()

        // Total Debt Repayment Amount
        ViewTextValueRow(
            viewTitle = TotalDebtRepaymentAmount,
            viewValue = "$currency $totalDebtRepaymentAmount"
        )

        HorizontalDivider()

        // Outstanding Debt
        ViewTextValueRow(
            viewTitle = TotalOutstandingDebtAmount,
            viewValue = "$currency $debtBalance"
        )

        HorizontalDivider()

        // Inventory Cost
        ViewTextValueRow(viewTitle = TotalInventoryCost, viewValue = "$currency $inventoryCost")

        HorizontalDivider()

        // Maximum revenue day
        ViewTextValueWithExtraValueRow(
            viewTitle = DayWithHighestRevenue,
            viewValue = maxRevenueDay,
            extraValue = "$currency $maximumRevenueAmount"
        )

        HorizontalDivider()

        // Minimum revenue day
        ViewTextValueWithExtraValueRow(
            viewTitle = DayWithLowestRevenue,
            viewValue = minimumRevenueDay,
            extraValue = "$currency $minimumRevenueAmount"
        )

        HorizontalDivider()

        Spacer(modifier = Modifier.height(LocalSpacing.current.medium))

        Box(modifier = Modifier.fillMaxWidth().padding(LocalSpacing.current.small)) {
            val revenuePoints = ListOfRevenues(allRevenues).toLinePoints()
            LineChartCard(
                lineTitle = "Revenues",
                pointsData = revenuePoints
            )
        }

        Spacer(modifier = Modifier.height(LocalSpacing.current.large))
/*

        Box(modifier = Modifier.fillMaxWidth()) {
            val expensePoints = ListOfExpenses(allExpenses).toLinePoints()
            LineChartCard(
                lineTitle = "Expenses",
                pointsData = expensePoints
            )
            Spacer(modifier = Modifier.height(LocalSpacing.current.large))
        }
*/

    }

}