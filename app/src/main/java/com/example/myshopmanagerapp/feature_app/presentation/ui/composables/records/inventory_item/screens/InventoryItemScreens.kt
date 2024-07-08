package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.records.inventory_item.screens

sealed class InventoryItemScreens(val route: String){
    object InventoryItemListScreen: InventoryItemScreens("to_inventory_item_list_screen")
    object AddInventoryItemNavigation: InventoryItemScreens("to_add_inventory_item_navigation")
    object AddInventoryItemScreen: InventoryItemScreens("to_add_inventory_item_screen")
    object ViewInventoryItemNavigation: InventoryItemScreens("to_view_inventory_item_navigation")
    object ViewInventoryItemCostPricesScreen: InventoryItemScreens("to_view_inventory_item_cost_prices_screen")
    object ViewInventoryItemSellingPricesScreen: InventoryItemScreens("to_view_inventory_item_selling_prices_screen")
    object ViewInventoryItemScreen: InventoryItemScreens("to_view_inventory_item_screen")
    object InventoryItemPhotoScreen: InventoryItemScreens("to_inventory_item_photo_screen")
    object ViewInventoryStockScreen: InventoryItemScreens("to_view_inventory_stock_screen")
    object AddQuantityCategorizationScreen: InventoryItemScreens("to_add_quantity_categorization_screen")
    object UpdateQuantityCategorizationScreen: InventoryItemScreens("to_update_quantity_categorization_screen")

    fun withArgs(vararg args: String): String {
        return buildString {
            append(route)
            args.forEach {arg->
                append("/$arg")
            }
        }
    }
}