package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.report.revenue.screens

sealed class RevenueReportScreens(val route: String){
    object MainRevenueReportScreen: RevenueReportScreens("to_main_revenue_report_screen")

    fun withArgs(vararg args: String): String {
        return buildString {
            append(route)
            args.forEach {arg->
                append("/$arg")
            }
        }
    }
}