package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.home.generate_receipt.screen

sealed class ReceiptScreens(val route: String){
    object ReceiptListScreen: ReceiptScreens("to_receipt_list_screen")
    object AddReceiptScreen: ReceiptScreens("to_add_receipt_screen")
    object UpdateReceiptScreen: ReceiptScreens("to_update_receipt_screen")
    object AddReceiptInventoryItemNavGraph: ReceiptScreens("to_add_receipt_inventory_item_nav_graph")
    object AddReceiptInventoryItemScreen: ReceiptScreens("to_add_receipt_inventory_item_screen")
    object AddReceiptInventoryItemPhotoScreen: ReceiptScreens("to_add_receipt_inventory_item_photo_screen")
    object AddReceiptInventoryQuantityCategorizationScreen: ReceiptScreens("to_add_receipt_inventory_quantity_categorization_screen")
    object AddReceiptCustomerNavGraph: ReceiptScreens("to_add_receipt_customer_nav_graph")
    object AddReceiptCustomerScreen: ReceiptScreens("to_add_receipt_customer_screen")
    object AddReceiptCustomerPhotoScreen: ReceiptScreens("to_add_receipt_customer_photo_screen")


    fun withArgs(vararg args: String): String {
        return buildString {
            append(route)
            args.forEach {arg->
                append("/$arg")
            }
        }
    }
}