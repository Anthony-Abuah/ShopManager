package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import co.yml.charts.axis.AxisData
import co.yml.charts.axis.DataCategoryOptions
import co.yml.charts.common.model.Point
import co.yml.charts.ui.linechart.LineChart
import co.yml.charts.ui.linechart.model.*
import com.example.myshopmanagerapp.feature_app.presentation.ui.theme.Colors
import com.example.myshopmanagerapp.feature_app.presentation.ui.theme.LocalSpacing

@Composable
fun LineChartCard(
    lineTitle: String,
    pointsData: List<Point>
){
    val maxValue = pointsData.maxOf { it.y }
    val steps = 2
    val xAxisData = AxisData.Builder()
        .axisStepSize(100.dp)
        .startDrawPadding(LocalSpacing.current.noPadding)
        .labelAndAxisLinePadding(LocalSpacing.current.smallMedium)
        .endPadding(LocalSpacing.current.small)
        .backgroundColor(MaterialTheme.colorScheme.background)
        .shouldDrawAxisLineTillEnd(true)
        .axisOffset((0).dp)
        .steps(pointsData.size - 1)
        .bottomPadding(50.dp)
        .axisLabelAngle(20f)
        .labelData { index-> pointsData[index].description }
        .axisLineColor(MaterialTheme.colorScheme.tertiary)
        .axisLabelColor(MaterialTheme.colorScheme.tertiary)
        .build()


    val yAxisData = AxisData.Builder()
        .steps(steps)
        .backgroundColor(MaterialTheme.colorScheme.background)
        .bottomPadding(LocalSpacing.current.noPadding)
        .axisLabelAngle(20f)
        .labelAndAxisLinePadding(LocalSpacing.current.large)
        .axisOffset(0.dp)
        .setDataCategoryOptions(DataCategoryOptions(
            isDataCategoryInYAxis = false,
            isDataCategoryStartFromBottom = false
        ))
        .shouldDrawAxisLineTillEnd(true)
        .labelData { index-> "GHS ${(index * (maxValue.div(steps)))}" }
        .axisLineColor(MaterialTheme.colorScheme.tertiary)
        .axisLabelColor(MaterialTheme.colorScheme.tertiary)
        .build()


    val lineChartData = LineChartData(
        linePlotData = LinePlotData(
            lines = listOf(
                Line(
                    dataPoints = pointsData,
                    LineStyle(
                        color = MaterialTheme.colorScheme.tertiary,
                        lineType = LineType.SmoothCurve(isDotted = false)
                    ),
                    IntersectionPoint(
                        color = MaterialTheme.colorScheme.tertiary
                    ),
                    SelectionHighlightPoint(
                        color = MaterialTheme.colorScheme.primary
                    ),
                    ShadowUnderLine(
                        alpha = 0.5f,
                        brush = Brush.verticalGradient(
                            colors = listOf(MaterialTheme.colorScheme.tertiary, Color.Transparent)
                        )
                    ),
                    SelectionHighlightPopUp()
                ),
            )
        ),
        backgroundColor = MaterialTheme.colorScheme.surface,
        xAxisData = xAxisData,
        yAxisData = yAxisData,
        bottomPadding = LocalSpacing.current.large,
        gridLines = GridLines(color = MaterialTheme.colorScheme.outlineVariant),
    )

    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(LocalSpacing.current.extraSmall),
        colors = CardDefaults.cardColors(
            contentColor = MaterialTheme.colorScheme.background,
            containerColor = MaterialTheme.colorScheme.background
        ),
        //shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(LocalSpacing.current.small)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    modifier = Modifier.padding(LocalSpacing.current.small),
                    text = lineTitle,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.tertiary,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    fontWeight = FontWeight.Bold
                )
            }
            LineChart(
                modifier = Modifier
                    .height(350.dp)
                    .background(MaterialTheme.colorScheme.background),
                lineChartData = lineChartData
            )
        }
    }
}

