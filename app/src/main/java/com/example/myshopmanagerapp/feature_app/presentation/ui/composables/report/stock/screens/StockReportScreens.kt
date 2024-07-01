package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.report.stock.screens

sealed class StockReportScreens(val route: String){
    object MainStockReportScreen: StockReportScreens("to_main_stock_report_screen")

    fun withArgs(vararg args: String): String {
        return buildString {
            append(route)
            args.forEach {arg->
                append("/$arg")
            }
        }
    }
}