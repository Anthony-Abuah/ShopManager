package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.records.stock.screens

sealed class StockScreens(val route: String){
    object StockListScreen: StockScreens("to_stock_list_screen")
    object AddStockScreen: StockScreens("to_add_stock_screen")
    object ViewStockScreen: StockScreens("to_view_stock_screen")
    object AddItemScreen: StockScreens("to_add_item_screen")
    object ItemNavGraph: StockScreens("to_item_nav_graph")
    object ItemPhotoScreen: StockScreens("to_add_photo_screen")
    object ItemCategoryScreen: StockScreens("to_add_category_screen")
    object ItemQuantityCategorizationScreen: StockScreens("to_item_quantity_categorization_screen")

    fun withArgs(vararg args: String): String {
        return buildString {
            append(route)
            args.forEach {arg->
                append("/$arg")
            }
        }
    }
}