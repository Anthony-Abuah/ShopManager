package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.bottom_nav.records.screens

import com.example.myshopmanagerapp.core.NavDrawerItem
import com.example.myshopmanagerapp.R
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.bottom_nav.BottomNavScreens

object HomeNavDrawerItems {

    val homeNavDrawerItems = listOf(
        NavDrawerItem(
            title = "Records",
            selectedIcon = R.drawable.ic_records,
            unselectedIcon = R.drawable.ic_records,
            badgeCount = null
        ),
        NavDrawerItem(
            title = "Report",
            selectedIcon = R.drawable.ic_report,
            unselectedIcon = R.drawable.ic_report,
            badgeCount = null,
            route = BottomNavScreens.Report.route
        ),
        NavDrawerItem(
            title = "Actions",
            selectedIcon = R.drawable.ic_activity,
            unselectedIcon = R.drawable.ic_activity,
            badgeCount = null,
            route = BottomNavScreens.Actions.route
        ),
        NavDrawerItem(
            title = "Customer",
            selectedIcon = R.drawable.ic_person_filled,
            unselectedIcon = R.drawable.ic_person_filled,
            badgeCount = null,
            route = HomeScreens.CustomerNavGraph.route
        ),
        NavDrawerItem(
            title = "Add Inventory Item",
            selectedIcon = R.drawable.ic_inventory_item,
            unselectedIcon = R.drawable.ic_inventory_item,
            badgeCount = null,
            route = HomeScreens.InventoryItemNavGraph.route
        ),
        NavDrawerItem(
            title = "Personnel",
            selectedIcon = R.drawable.ic_personnel,
            unselectedIcon = R.drawable.ic_personnel,
            badgeCount = null,
            route = HomeScreens.PersonnelNavGraph.route
        ),
        NavDrawerItem(
            title = "Supplier",
            selectedIcon = R.drawable.ic_supplier,
            unselectedIcon = R.drawable.ic_supplier,
            badgeCount = null,
            route = HomeScreens.SupplierNavGraph.route
        ),
        NavDrawerItem(
            title = "Bank Account",
            selectedIcon = R.drawable.ic_bank_account,
            unselectedIcon = R.drawable.ic_bank_account,
            badgeCount = null,
            route = HomeScreens.BankNavGraph.route
        ),
        NavDrawerItem(
            title = "Generate Receipt",
            selectedIcon = R.drawable.ic_receipt,
            unselectedIcon = R.drawable.ic_receipt,
            badgeCount = null,
            route = HomeScreens.ReceiptNavGraph.route
        ),
        NavDrawerItem(
            title = "Preferences",
            selectedIcon = R.drawable.ic_preferences,
            unselectedIcon = R.drawable.ic_preferences,
            badgeCount = null
        ),
        NavDrawerItem(
            title = "Back up",
            selectedIcon = R.drawable.ic_backup,
            unselectedIcon = R.drawable.ic_backup,
            badgeCount = null,
        ),
        NavDrawerItem(
            title = "User Guide",
            selectedIcon = R.drawable.ic_user_guide,
            unselectedIcon = R.drawable.ic_user_guide,
            badgeCount = null
        ),
    )
}