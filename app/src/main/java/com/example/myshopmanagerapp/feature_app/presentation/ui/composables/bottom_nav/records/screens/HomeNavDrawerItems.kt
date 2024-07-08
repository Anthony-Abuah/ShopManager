package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.bottom_nav.records.screens

import com.example.myshopmanagerapp.core.NavDrawerItem
import com.example.myshopmanagerapp.R

object HomeNavDrawerItems {

    val homeNavDrawerItems = listOf(
        NavDrawerItem(
            title = "Records",
            selectedIcon = R.drawable.ic_home,
            unselectedIcon = R.drawable.ic_home,
            badgeCount = null,
        ),
        NavDrawerItem(
            title = "Report",
            selectedIcon = R.drawable.ic_report,
            unselectedIcon = R.drawable.ic_report,
            badgeCount = null
        ),
        NavDrawerItem(
            title = "Actions",
            selectedIcon = R.drawable.ic_settings,
            unselectedIcon = R.drawable.ic_settings,
            badgeCount = null
        ),
        NavDrawerItem(
            title = "Customer",
            selectedIcon = R.drawable.ic_person_filled,
            unselectedIcon = R.drawable.ic_person_outline,
            badgeCount = null,
            route = HomeScreens.CustomerNavGraph.route
        ),
        NavDrawerItem(
            title = "Personnel",
            selectedIcon = R.drawable.ic_person_filled,
            unselectedIcon = R.drawable.ic_person_outline,
            badgeCount = null,
            route = HomeScreens.PersonnelNavGraph.route
        ),
        NavDrawerItem(
            title = "Debt",
            selectedIcon = R.drawable.ic_money_filled,
            unselectedIcon = R.drawable.ic_money_outline,
            badgeCount = null,
            route = HomeScreens.DebtNavGraph.route
        ),
        NavDrawerItem(
            title = "Debt Repayment",
            selectedIcon = R.drawable.ic_money_filled,
            unselectedIcon = R.drawable.ic_money_outline,
            badgeCount = null,
            route = HomeScreens.DebtRepaymentNavGraph.route
        ),
        NavDrawerItem(
            title = "Bank",
            selectedIcon = R.drawable.ic_bank,
            unselectedIcon = R.drawable.ic_bank,
            badgeCount = null,
            route = HomeScreens.BankNavGraph.route
        ),
        NavDrawerItem(
            title = "Savings",
            selectedIcon = R.drawable.ic_money_filled,
            unselectedIcon = R.drawable.ic_money_outline,
            badgeCount = null,
            route = HomeScreens.SavingsNavGraph.route
        ),
        NavDrawerItem(
            title = "Withdrawal",
            selectedIcon = R.drawable.ic_money_filled,
            unselectedIcon = R.drawable.ic_money_outline,
            badgeCount = null,
            route = HomeScreens.WithdrawalNavGraph.route
        ),
        NavDrawerItem(
            title = "Supplier",
            selectedIcon = R.drawable.ic_person_filled,
            unselectedIcon = R.drawable.ic_person_outline,
            badgeCount = null,
            route = HomeScreens.SupplierNavGraph.route
        ),
        NavDrawerItem(
            title = "Company",
            selectedIcon = R.drawable.ic_company,
            unselectedIcon = R.drawable.ic_company,
            badgeCount = null,
            route = HomeScreens.CompanyNavGraph.route
        ),
        NavDrawerItem(
            title = "Preferences",
            selectedIcon = R.drawable.ic_person_filled,
            unselectedIcon = R.drawable.ic_person_outline,
            badgeCount = null
        ),
        NavDrawerItem(
            title = "Back up",
            selectedIcon = R.drawable.ic_backup,
            unselectedIcon = R.drawable.ic_backup,
            badgeCount = null,
        ),
        NavDrawerItem(
            title = "Generate Receipt",
            selectedIcon = R.drawable.ic_receipt,
            unselectedIcon = R.drawable.ic_receipt,
            badgeCount = null,
            route = HomeScreens.ReceiptNavGraph.route
        ),
        NavDrawerItem(
            title = "User Guide",
            selectedIcon = R.drawable.ic_settings,
            unselectedIcon = R.drawable.ic_settings,
            badgeCount = null
        ),
    )
}