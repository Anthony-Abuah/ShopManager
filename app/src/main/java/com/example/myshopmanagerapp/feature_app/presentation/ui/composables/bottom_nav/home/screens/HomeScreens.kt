package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.bottom_nav.home.screens

sealed class HomeScreens(val route: String){
    object MainHomeScreen: HomeScreens("to_main_home_screen")
    object PersonnelProfileNavGraph: HomeScreens("to_personnelProfile_nav_graph")
    object RevenueNavGraph: HomeScreens("to_revenue_nav_graph")
    object WithdrawalNavGraph: HomeScreens("to_withdrawal_nav_graph")
    object SavingsNavGraph: HomeScreens("to_savings_nav_graph")
    object CustomerNavGraph: HomeScreens("to_customer_nav_graph")
    object SupplierNavGraph: HomeScreens("to_supplier_nav_graph")
    object PersonnelNavGraph: HomeScreens("to_personnel_nav_graph")
    object BankNavGraph: HomeScreens("to_bank_nav_graph")
    object CompanyNavGraph: HomeScreens("to_company_nav_graph")
    object ExpenseNavGraph: HomeScreens("to_expense_nav_graph")
    object InventoryNavGraph: HomeScreens("to_inventory_nav_graph")
    object InventoryItemNavGraph: HomeScreens("to_inventory_item_nav_graph")
    object DebtNavGraph: HomeScreens("to_debt_nav_graph")
    object DebtRepaymentNavGraph: HomeScreens("to_debt_repayment_nav_graph")
    object StockNavGraph: HomeScreens("to_stock_nav_graph")
    object ReceiptNavGraph: HomeScreens("to_receipt_nav_graph")

    fun withArgs(vararg args: String): String {
        return buildString {
            append(route)
            args.forEach {arg->
                append("/$arg")
            }
        }
    }
}