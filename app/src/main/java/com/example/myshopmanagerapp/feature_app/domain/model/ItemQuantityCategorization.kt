package com.example.myshopmanagerapp.feature_app.domain.model

data class ItemQuantityCategorization(
    val unit: Int,
    val size1: Int,
    val size2: Int = 0,
    val size3: Int = 0,
    val size4: Int = 0,
    val size5: Int = 0,
    val size6: Int = 0,
    val numberOfUnitsPerSize1: Int = 0,
    val numberOfSize1PerSize2: Int = 0,
    val numberOfSize2PerSize3: Int = 0,
    val numberOfSize3PerSize4: Int = 0,
    val numberOfSize4PerSize5: Int = 0,
    val numberOfSize5PerSize6: Int = 0,
    val totalNumberOfSize5: Int = size5.plus((size6.times(numberOfSize5PerSize6))),
    val totalNumberOfSize4: Int = size4.plus(totalNumberOfSize5.times(numberOfSize4PerSize5)),
    val totalNumberOfSize3: Int = size3.plus(totalNumberOfSize4.times(numberOfSize3PerSize4)),
    val totalNumberOfSize2: Int = size2.plus(totalNumberOfSize3.times(numberOfSize2PerSize3)),
    val totalNumberOfSize1: Int = size1.plus(totalNumberOfSize2.times(numberOfSize1PerSize2)),
    val totalNumberOfUnits: Int = unit.plus(totalNumberOfSize1.times(numberOfUnitsPerSize1))
){
    fun addItemQuantityCategorization(quantity: ItemQuantityCategorization?): ItemQuantityCategorization{
        val thisQuantity = quantity?: ItemQuantityCategorization(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
        return ItemQuantityCategorization(
            unit = unit.plus(thisQuantity.unit),
            size1 = if (numberOfUnitsPerSize1 == thisQuantity.numberOfUnitsPerSize1) size1.plus(thisQuantity.size1) else 0,
            size2 = if (numberOfSize1PerSize2 == thisQuantity.numberOfSize1PerSize2) size2.plus(thisQuantity.size2) else 0,
            size3 = if (numberOfSize2PerSize3 == thisQuantity.numberOfSize2PerSize3) size3.plus(thisQuantity.size3) else 0,
            size4 = if (numberOfSize3PerSize4 == thisQuantity.numberOfSize3PerSize4) size4.plus(thisQuantity.size4) else 0,
            size5 = if (numberOfSize4PerSize5 == thisQuantity.numberOfSize4PerSize5) size5.plus(thisQuantity.size5) else 0,
            size6 = if (numberOfSize5PerSize6 == thisQuantity.numberOfSize5PerSize6) size6.plus(thisQuantity.size6) else 0,
            numberOfUnitsPerSize1 = thisQuantity.numberOfUnitsPerSize1,
            numberOfSize1PerSize2 = thisQuantity.numberOfSize1PerSize2,
            numberOfSize2PerSize3 = thisQuantity.numberOfSize2PerSize3,
            numberOfSize3PerSize4 = thisQuantity.numberOfSize3PerSize4,
            numberOfSize4PerSize5 = thisQuantity.numberOfSize4PerSize5,
            numberOfSize5PerSize6 = thisQuantity.numberOfSize5PerSize6,
            totalNumberOfSize5 = totalNumberOfSize5.plus(thisQuantity.totalNumberOfSize5),
            totalNumberOfSize4 = totalNumberOfSize4.plus(thisQuantity.totalNumberOfSize4),
            totalNumberOfSize3 = totalNumberOfSize3.plus(thisQuantity.totalNumberOfSize3),
            totalNumberOfSize2 = totalNumberOfSize2.plus(thisQuantity.totalNumberOfSize2),
            totalNumberOfSize1 = totalNumberOfSize1.plus(thisQuantity.totalNumberOfSize1),
            totalNumberOfUnits = totalNumberOfUnits.plus(thisQuantity.totalNumberOfUnits),
        )
    }

    fun subtractItemQuantityCategorization(quantity: ItemQuantityCategorization?): ItemQuantityCategorization {
        val thisQuantity = quantity ?: ItemQuantityCategorization(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
        return ItemQuantityCategorization(
            unit = unit.minus(thisQuantity.unit),
            size1 = if (numberOfUnitsPerSize1 == thisQuantity.numberOfUnitsPerSize1) size1.minus(thisQuantity.size1) else 0,
            size2 = if (numberOfSize1PerSize2 == thisQuantity.numberOfSize1PerSize2) size2.minus(thisQuantity.size2) else 0,
            size3 = if (numberOfSize2PerSize3 == thisQuantity.numberOfSize2PerSize3) size3.minus(thisQuantity.size3) else 0,
            size4 = if (numberOfSize3PerSize4 == thisQuantity.numberOfSize3PerSize4) size4.minus(thisQuantity.size4) else 0,
            size5 = if (numberOfSize4PerSize5 == thisQuantity.numberOfSize4PerSize5) size5.minus(thisQuantity.size5) else 0,
            size6 = if (numberOfSize5PerSize6 == thisQuantity.numberOfSize5PerSize6) size6.minus(thisQuantity.size6) else 0,
            numberOfUnitsPerSize1 = thisQuantity.numberOfUnitsPerSize1,
            numberOfSize1PerSize2 = thisQuantity.numberOfSize1PerSize2,
            numberOfSize2PerSize3 = thisQuantity.numberOfSize2PerSize3,
            numberOfSize3PerSize4 = thisQuantity.numberOfSize3PerSize4,
            numberOfSize4PerSize5 = thisQuantity.numberOfSize4PerSize5,
            numberOfSize5PerSize6 = thisQuantity.numberOfSize5PerSize6,
            totalNumberOfSize5 = totalNumberOfSize5.minus(thisQuantity.totalNumberOfSize5),
            totalNumberOfSize4 = totalNumberOfSize4.minus(thisQuantity.totalNumberOfSize4),
            totalNumberOfSize3 = totalNumberOfSize3.minus(thisQuantity.totalNumberOfSize3),
            totalNumberOfSize2 = totalNumberOfSize2.minus(thisQuantity.totalNumberOfSize2),
            totalNumberOfSize1 = totalNumberOfSize1.minus(thisQuantity.totalNumberOfSize1),
            totalNumberOfUnits = totalNumberOfUnits.minus(thisQuantity.totalNumberOfUnits),
        )
    }
}
