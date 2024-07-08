package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.records.expenses.screens

sealed class ExpenseScreens(val route: String){
    object ExpenseListScreen: ExpenseScreens("to_expense_list_screen")
    object AddExpenseScreen: ExpenseScreens("to_add_expense_screen")
    object ViewExpenseScreen: ExpenseScreens("to_view_expense_screen")
    object PersonnelNavGraph: ExpenseScreens("to_expense_personnel_nav_graph")
    object AddPersonnelRoleScreen: ExpenseScreens("to_expense_personnel_role_screen")
    object AddPersonnelPhotoScreen: ExpenseScreens("to_expense_personnel_photo_screen")
    object AddPersonnelScreen: ExpenseScreens("to_expense_personnel_screen")
    object ExpenseNameScreen: ExpenseScreens("to_expense_name_screen")
    object ExpenseTypeScreen: ExpenseScreens("to_expense_type_screen")


    fun withArgs(vararg args: String): String {
        return buildString {
            append(route)
            args.forEach {arg->
                append("/$arg")
            }
        }
    }
}