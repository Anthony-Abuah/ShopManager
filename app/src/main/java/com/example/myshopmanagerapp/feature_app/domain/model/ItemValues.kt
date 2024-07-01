package com.example.myshopmanagerapp.feature_app.domain.model

import android.util.Log
import androidx.compose.ui.graphics.Color
import co.yml.charts.common.extensions.roundTwoDecimal
import co.yml.charts.common.model.Point
import co.yml.charts.ui.barchart.models.BarData
import com.example.myshopmanagerapp.core.Constants.emptyString
import com.example.myshopmanagerapp.core.Functions.toNearestCeiling
import com.example.myshopmanagerapp.core.Functions.toRoundedInt
import kotlin.random.Random

data class ItemValues(
    val itemValues: List<ItemValue>
){
    fun toBarData(): List<BarData>{
        val maxValue = if (itemValues.isNotEmpty()) itemValues.maxOf { it.value }.toNearestCeiling() else 0.0
        return itemValues.mapIndexed { index, itemValue ->
            val point = Point(
                x = index.toFloat(),
                y = if (maxValue < 10) itemValue.value.times(10).toFloat() else itemValue.value.div(maxValue.div(10)).toFloat(),
                description = maxValue.toString()
                )
            BarData(
                point = point,
                label = itemValue.itemName,
                color = Color(Random.nextInt(256), Random.nextInt(256), Random.nextInt(256)),
                description = itemValue.value.toString()
            )
        }
    }
}
