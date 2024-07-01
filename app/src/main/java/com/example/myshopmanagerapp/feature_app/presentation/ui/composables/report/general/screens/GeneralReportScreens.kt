package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.report.general.screens

sealed class GeneralReportScreens(val route: String){
    object GeneralReportScreen: GeneralReportScreens("to_general_report_screen")

    fun withArgs(vararg args: String): String {
        return buildString {
            append(route)
            args.forEach {arg->
                append("/$arg")
            }
        }
    }
}