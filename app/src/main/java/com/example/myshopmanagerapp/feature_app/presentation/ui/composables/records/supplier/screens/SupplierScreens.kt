package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.records.supplier.screens

sealed class SupplierScreens(val route: String){
    object SupplierListScreen: SupplierScreens("to_supplier_list_screen")
    object AddSupplierScreen: SupplierScreens("to_add_supplier_screen")
    object ViewSupplierScreen: SupplierScreens("to_view_supplier_screen")
    object SupplierRoleScreen: SupplierScreens("to_supplier_role_screen")

    fun withArgs(vararg args: String): String {
        return buildString {
            append(route)
            args.forEach {arg->
                append("/$arg")
            }
        }
    }
}