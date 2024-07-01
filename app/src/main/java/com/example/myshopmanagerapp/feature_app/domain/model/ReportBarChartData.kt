package com.example.myshopmanagerapp.feature_app.domain.model

import androidx.compose.ui.graphics.Color
import co.yml.charts.axis.DataCategoryOptions
import co.yml.charts.common.model.Point
import co.yml.charts.ui.barchart.models.BarData
import com.example.myshopmanagerapp.feature_app.presentation.ui.theme.Colors

data class ReportBarChartData(
    val point: Point,
    val color: Color = Colors.random(),
    val label: String,
    val gradientColorList: List<Color> = Colors,
    val dataCategoryOptions: DataCategoryOptions = DataCategoryOptions(),
    val description: String
){
    fun toBarData(): BarData{
        return BarData(
            point, color, label, gradientColorList, dataCategoryOptions, description
        )
    }
}
