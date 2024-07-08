package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.records.withdrawal.screens

sealed class WithdrawalScreens(val route: String){
    object WithdrawalListScreen: WithdrawalScreens("to_withdrawal_list_screen")
    object AddWithdrawalScreen: WithdrawalScreens("to_add_withdrawal_screen")
    object ViewWithdrawalScreen: WithdrawalScreens("to_view_withdrawal_screen")
    object AddPersonnelNavGraph: WithdrawalScreens("to_add_withdrawal_personnel_nav_graph")
    object AddBankScreen: WithdrawalScreens("to_add_withdrawal_bank_screen")
    object AddPersonnelScreen: WithdrawalScreens("to_add_withdrawal_personnel_screen")
    object AddPersonnelPhotoScreen: WithdrawalScreens("to_add_withdrawal_personnel_photo_screen")


    fun withArgs(vararg args: String): String {
        return buildString {
            append(route)
            args.forEach {arg->
                append("/$arg")
            }
        }
    }
}