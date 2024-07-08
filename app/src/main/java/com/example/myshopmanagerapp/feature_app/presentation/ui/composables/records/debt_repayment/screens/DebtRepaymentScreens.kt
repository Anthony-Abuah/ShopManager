package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.records.debt_repayment.screens

sealed class DebtRepaymentScreens(val route: String){
    object DebtRepaymentListScreen: DebtRepaymentScreens("to_debt_repayment_list_screen")
    object AddDebtRepaymentScreen: DebtRepaymentScreens("to_add_debt_repayment_screen")
    object ViewDebtRepaymentScreen: DebtRepaymentScreens("to_update_debt_repayment_screen")

    fun withArgs(vararg args: String): String {
        return buildString {
            append(route)
            args.forEach {arg->
                append("/$arg")
            }
        }
    }
}