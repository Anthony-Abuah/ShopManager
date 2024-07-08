package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.records.debt.screens

sealed class DebtScreens(val route: String){
    object DebtListScreen: DebtScreens("to_debt_list_screen")
    object AddDebtScreen: DebtScreens("to_add_debt_screen")
    object ViewDebtScreen: DebtScreens("to_view_debt_screen")
    object AddCustomerNavGraph: DebtScreens("to_add_debt_customer_nav_graph")
    object AddPersonnelNavGraph: DebtScreens("to_add_debt_personnel_nav_graph")
    object AddCustomerScreen: DebtScreens("to_add_debt_customer_screen")
    object AddPersonnelScreen: DebtScreens("to_add_debt_personnel_screen")
    object AddPersonnelRoleScreen: DebtScreens("to_add_debt_personnel_role_screen")
    object AddPersonnelPhotoScreen: DebtScreens("to_add_debt_personnel_photo_screen")
    object AddCustomerPhotoScreen: DebtScreens("to_add_debt_customer_photo_screen")


    fun withArgs(vararg args: String): String {
        return buildString {
            append(route)
            args.forEach {arg->
                append("/$arg")
            }
        }
    }
}