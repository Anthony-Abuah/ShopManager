package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.bottom_nav.report.screens

sealed class ReportScreens(val route: String){
    object MainReportScreen: ReportScreens("to_main_report_screen")
    object ViewStockReportNavGraph: ReportScreens("to_view_stock_report_nav_graph")
    object ViewRevenueReportNavGraph: ReportScreens("to_view_revenue_report_nav_graph")
    object ViewInventoryReportNavGraph: ReportScreens("to_view_inventory_report_nav_graph")
    object GeneralReportNavGraph: ReportScreens("to_general_report_nav_graph")

    object ViewCashInReportNavGraph: ReportScreens("to_cash_in_report_nav_graph")


    fun withArgs(vararg args: String): String {
        return buildString {
            append(route)
            args.forEach {arg->
                append("/$arg")
            }
        }
    }
}