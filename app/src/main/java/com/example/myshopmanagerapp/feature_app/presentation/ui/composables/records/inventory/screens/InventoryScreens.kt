package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.records.inventory.screens

sealed class InventoryScreens(val route: String){
    object InventoryListScreen: InventoryScreens("to_inventory_list_screen")
    object AddInventoryScreen: InventoryScreens("to_add_inventory_screen")
    object ViewInventoryScreen: InventoryScreens("to_view_inventory_screen")
    object AddInventoryItemNavGraph: InventoryScreens("to_add_inventory_item_nav_graph_inventory")
    object AddInventoryItemScreen: InventoryScreens("to_add_inventory_item_screen_inventory")
    object AddInventoryItemCamera: InventoryScreens("to_add_inventory_item_camera_inventory")


    fun withArgs(vararg args: String): String {
        return buildString {
            append(route)
            args.forEach {arg->
                append("/$arg")
            }
        }
    }
}