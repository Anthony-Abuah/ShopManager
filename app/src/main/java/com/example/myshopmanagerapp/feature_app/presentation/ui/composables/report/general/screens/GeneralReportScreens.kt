package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.report.general.screens

sealed class GeneralReportScreens(val route: String){
    object GeneralReportScreen: GeneralReportScreens("to_general_report_screen")
    object InventoryItemsReportScreen: GeneralReportScreens("to_inventory_items_report_screen")
    object OwingCustomersReportScreen: GeneralReportScreens("to_owing_customers_report_screen")
    object PersonnelReportScreen: GeneralReportScreens("to_personnel_report_screen")
    object BankAccountsReportScreen: GeneralReportScreens("to_bank_account_report_screen")

    fun withArgs(vararg args: String): String {
        return buildString {
            append(route)
            args.forEach {arg->
                append("/$arg")
            }
        }
    }
}