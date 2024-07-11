package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.report.expense

import androidx.compose.foundation.layout.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.myshopmanagerapp.core.FormRelatedString.AverageDailyExpense
import com.example.myshopmanagerapp.core.FormRelatedString.DayWithHighestExpense
import com.example.myshopmanagerapp.core.FormRelatedString.DayWithLowestExpense
import com.example.myshopmanagerapp.core.FormRelatedString.RevenuePercentageAsExpense
import com.example.myshopmanagerapp.core.FormRelatedString.TotalExpenses
import com.example.myshopmanagerapp.feature_app.data.local.entities.expenses.ExpenseEntity
import com.example.myshopmanagerapp.feature_app.domain.model.ItemValue
import com.example.myshopmanagerapp.feature_app.domain.model.ListOfExpenses
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.BasicScreenColumnWithoutBottomBar
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.LineChartCard
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.ViewTextValueRow
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.ViewTextValueWithExtraValueRow
import com.example.myshopmanagerapp.feature_app.presentation.ui.theme.LocalSpacing

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
    minimumExpenseDay: String
){
    BasicScreenColumnWithoutBottomBar{

        HorizontalDivider()

        // Total Expenses
        ViewTextValueRow(viewTitle = TotalExpenses, viewValue = "$currency $totalExpenseAmount")

        HorizontalDivider()

        //Expense Types
        if (expenseTypes.isNotEmpty()) {
            expenseTypes.forEach { expenseType ->
                ViewTextValueRow(
                    viewTitle = expenseType.itemName,
                    viewValue = "$currency ${expenseType.value}"
                )
                HorizontalDivider()
            }
        }

        // Expense as percentage of revenge
        ViewTextValueRow(viewTitle = RevenuePercentageAsExpense, viewValue = "$revenuePercentageAsExpense %")

        HorizontalDivider()

        // Average Daily Revenue
        ViewTextValueRow(
            viewTitle = AverageDailyExpense,
            viewValue = "$currency $averageDailyExpense"
        )

        HorizontalDivider()


        // Maximum expense day
        ViewTextValueWithExtraValueRow(
            viewTitle = DayWithHighestExpense,
            viewValue = maxExpenseDay,
            extraValue = "$currency $maximumExpenseAmount"
        )

        HorizontalDivider()

        // Minimum expense day
        ViewTextValueWithExtraValueRow(
            viewTitle = DayWithLowestExpense,
            viewValue = minimumExpenseDay,
            extraValue = "$currency $minimumExpenseAmount"
        )

        HorizontalDivider()

        Spacer(modifier = Modifier.height(LocalSpacing.current.medium))

        Box(modifier = Modifier
            .fillMaxWidth()
            .padding(LocalSpacing.current.small)) {
            val expensePoints = ListOfExpenses(allExpenses).toLinePoints()
            LineChartCard(
                lineTitle = "Expenses",
                pointsData = expensePoints
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

