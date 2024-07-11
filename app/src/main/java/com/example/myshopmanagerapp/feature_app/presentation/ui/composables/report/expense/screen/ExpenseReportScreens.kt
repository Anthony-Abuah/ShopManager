package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.report.expense.screen

sealed class ExpenseReportScreens(val route: String){
    object MainExpenseReportScreen: ExpenseReportScreens("to_main_expense_report_screen")

    fun withArgs(vararg args: String): String {
        return buildString {
            append(route)
            args.forEach {arg->
                append("/$arg")
            }
        }
    }
}