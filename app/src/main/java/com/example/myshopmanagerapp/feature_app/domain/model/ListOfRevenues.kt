package com.example.myshopmanagerapp.feature_app.domain.model

import co.yml.charts.common.model.Point
import com.example.myshopmanagerapp.core.Constants.emptyString
import com.example.myshopmanagerapp.core.Functions.toDateString
import com.example.myshopmanagerapp.core.Functions.toLocalDate
import com.example.myshopmanagerapp.core.Functions.toNotNull
import com.example.myshopmanagerapp.core.RevenueEntities
import java.util.*

data class ListOfRevenues(
    val revenues: RevenueEntities,
){
    fun toLinePoints(): List<Point>{
        val sortedRevenues = revenues.sortedBy { it.date }
        val mutableListOfPoints = mutableListOf<Point>()
        mutableListOfPoints.add(Point(1f, 0f, emptyString))
        sortedRevenues.forEachIndexed {index, revenue ->
            val dayOfWeek = revenue.dayOfWeek?.take(3).toNotNull()
                .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }
            val date = revenue.date.toLocalDate().toDateString()
            val theDate = "$dayOfWeek, $date"
            val point = Point(index.plus(2f), revenue.revenueAmount.toFloat(), theDate)
            mutableListOfPoints.add(point)
        }
        return mutableListOfPoints
    }
}
