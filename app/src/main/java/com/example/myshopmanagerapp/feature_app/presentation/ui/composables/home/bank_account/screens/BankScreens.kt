package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.home.bank_account.screens

sealed class BankScreens(val route: String){
    object BankListScreen: BankScreens("to_bank_list_screen")
    object AddBankScreen: BankScreens("to_add_bank_screen")
    object ViewBankScreen: BankScreens("to_view_bank_screen")

    fun withArgs(vararg args: String): String {
        return buildString {
            append(route)
            args.forEach {arg->
                append("/$arg")
            }
        }
    }
}