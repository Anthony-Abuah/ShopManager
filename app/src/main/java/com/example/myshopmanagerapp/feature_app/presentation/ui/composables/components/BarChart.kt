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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import co.yml.charts.axis.AxisData
import co.yml.charts.ui.barchart.BarChart
import co.yml.charts.ui.barchart.models.BarChartData
import co.yml.charts.ui.barchart.models.BarChartType
import co.yml.charts.ui.barchart.models.BarData
import co.yml.charts.ui.barchart.models.BarStyle
import com.example.myshopmanagerapp.feature_app.presentation.ui.theme.LocalSpacing

@Composable
fun BarChartCard(
    barTitle: String,
    barsData: List<BarData>
){
    val maxValue = barsData.first().point.description.toDouble()
    val stepSize = 10

    val xAxisData = AxisData.Builder()
        .axisStepSize(100.dp)
        .startDrawPadding(LocalSpacing.current.large)
        .labelAndAxisLinePadding(LocalSpacing.current.smallMedium)
        .endPadding(LocalSpacing.current.small)
        .backgroundColor(MaterialTheme.colorScheme.background)
        .shouldDrawAxisLineTillEnd(true)
        .axisOffset((32).dp)
        .steps(barsData.size - 1)
        .bottomPadding(LocalSpacing.current.large)
        .axisLabelAngle(20f)
        .labelData { index-> barsData[index].label }
        .axisLineColor(MaterialTheme.colorScheme.tertiary)
        .axisLabelColor(MaterialTheme.colorScheme.tertiary)
        .build()


    val yAxisData = AxisData.Builder()
        .steps(stepSize)
        .startDrawPadding(LocalSpacing.current.large)
        .axisLabelAngle(20f)
        .labelAndAxisLinePadding(LocalSpacing.current.medium)
        .axisOffset(0.dp)
        .shouldDrawAxisLineTillEnd(true)
        .labelData { index-> ( index * (maxValue.div(10))).toInt().toString() }
        .axisLineColor(MaterialTheme.colorScheme.tertiary)
        .axisLabelColor(MaterialTheme.colorScheme.tertiary)
        .build()

    val barChartData = BarChartData(
        chartData = barsData,
        xAxisData = xAxisData,
        yAxisData = yAxisData,
        backgroundColor = MaterialTheme.colorScheme.background,
        barChartType = BarChartType.VERTICAL,
        horizontalExtraSpace = LocalSpacing.current.smallMedium,
        paddingEnd = LocalSpacing.current.noPadding,
        paddingTop = LocalSpacing.current.noPadding,
        barStyle = BarStyle(),
    )

    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(LocalSpacing.current.extraSmall),
        colors = CardDefaults.cardColors(
            contentColor = MaterialTheme.colorScheme.background,
            containerColor = MaterialTheme.colorScheme.background
        ),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(LocalSpacing.current.small)
    ) {
        Column(modifier = Modifier.fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(LocalSpacing.current.noPadding)
                    .background(MaterialTheme.colorScheme.background),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = barTitle,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.tertiary,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    fontWeight = FontWeight.Bold
                )
            }
            BarChart(
                modifier = Modifier
                    .height(400.dp)
                    .background(MaterialTheme.colorScheme.background),
                barChartData = barChartData
            )
        }
    }
}

