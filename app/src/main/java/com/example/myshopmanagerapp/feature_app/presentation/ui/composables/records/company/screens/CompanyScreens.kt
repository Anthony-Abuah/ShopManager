package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.records.company.screens

sealed class CompanyScreens(val route: String){
    object CompanyListScreen: CompanyScreens("to_company_list_screen")
    object AddCompanyScreen: CompanyScreens("to_add_company_screen")
    object ViewCompanyScreen: CompanyScreens("to_view_company_screen")

    fun withArgs(vararg args: String): String {
        return buildString {
            append(route)
            args.forEach {arg->
                append("/$arg")
            }
        }
    }
}