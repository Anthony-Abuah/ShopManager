package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.home.savings.screens

sealed class SavingsScreens(val route: String){
    object SavingsListScreen: SavingsScreens("to_savings_list_screen")
    object AddSavingsScreen: SavingsScreens("to_add_savings_screen")
    object ViewSavingsScreen: SavingsScreens("to_view_savings_screen")
    object AddBankNavGraph: SavingsScreens("to_add_savings_bank_nav_graph")
    object AddPersonnelNavGraph: SavingsScreens("to_add_savings_personnel_nav_graph")
    object AddBankScreen: SavingsScreens("to_add_savings_bank_screen")
    object AddPersonnelScreen: SavingsScreens("to_add_savings_personnel_screen")
    object AddPersonnelRoleScreen: SavingsScreens("to_add_savings_personnel_role_screen")
    object AddPersonnelPhotoScreen: SavingsScreens("to_add_savings_personnel_photo_screen")
    object AddBankPhotoScreen: SavingsScreens("to_add_savings_bank_photo_screen")


    fun withArgs(vararg args: String): String {
        return buildString {
            append(route)
            args.forEach {arg->
                append("/$arg")
            }
        }
    }
}