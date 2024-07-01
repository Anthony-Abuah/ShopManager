package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.report.inventory.screens

sealed class InventoryReportScreens(val route: String){
    object MainInventoryReportScreen: InventoryReportScreens("to_main_inventory_report_screen")

    fun withArgs(vararg args: String): String {
        return buildString {
            append(route)
            args.forEach {arg->
                append("/$arg")
            }
        }
    }
}