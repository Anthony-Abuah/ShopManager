package com.example.myshopmanagerapp.feature_app.domain.model

import com.example.myshopmanagerapp.core.Constants

data class ItemQuantity(
    val sizeName: String = Constants.Unit,
    val quantity: Int,
    val unitsPerSize: Int,
){
    fun toPrices(date: Long, price: Double): Price{
        return Price( date, sizeName, price )
    }
    fun addQuantity(itemQuantity: ItemQuantity):ItemQuantity{
        return ItemQuantity(
            sizeName,
            quantity.plus(itemQuantity.quantity),
            unitsPerSize
        )
    }
    fun subtractQuantity(itemQuantity: ItemQuantity):ItemQuantity{
        return ItemQuantity(
            sizeName,
            quantity.minus(itemQuantity.quantity),
            unitsPerSize
        )
    }
    fun toQuantityCategorization():  QuantityCategorization{
        return QuantityCategorization(sizeName,unitsPerSize)
    }
}
