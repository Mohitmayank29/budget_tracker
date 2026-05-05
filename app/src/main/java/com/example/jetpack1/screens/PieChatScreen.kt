package com.example.jetpack1.screens

import android.R.attr.description
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.example.jetpack1.screens.Dashboard.UiState
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry


@Composable
fun PieChartView(
    dataEntries: List<PieEntry>,
    colors: List<Int>,
    centerText: String
) {
    AndroidView(
        factory = { context -> PieChart(context) },

        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(Color.LightGray.copy(alpha = 0.1f)),
        update = { chart ->
            chart.apply {
                holeRadius = 50f
                transparentCircleRadius = 50f
                isRotationEnabled = true
                isHighlightPerTapEnabled = true
                dragDecelerationFrictionCoef = 0.95f
                setUsePercentValues(false)
                setDrawEntryLabels(false)
                setTouchEnabled(true)
                description.isEnabled = false
                legend.isEnabled = true
                setCenterText(centerText)
                setCenterTextSize(12f)
                setCenterTextColor(android.graphics.Color.WHITE)
                setHoleColor(Color.Gray.copy(alpha = 0.1f).toArgb())
                legend.form = Legend.LegendForm.CIRCLE
                animateXY(1000, 1000)

                val dataSet = PieDataSet(dataEntries, "")
                dataSet.colors = colors
                dataSet.sliceSpace = 1f
                dataSet.valueTextColor = android.graphics.Color.WHITE
                dataSet.valueTextSize = 11f

                legend.apply {
                    isEnabled = true
                    textColor = android.graphics.Color.WHITE
                    textSize = 12f
                    formSize = 10f
                }

                val data = PieData(dataSet)
                setData(data)
                invalidate()
            }
        }
    )
}

@Composable
fun MyPieChartScreen(state: UiState) {

    val entries = listOf(
        PieEntry(state.income.toFloat(), "Income"),
        PieEntry(state.expenses.toFloat(), "Expense")
    )

    val colors = listOf(
        Color.Magenta,
        Color.Red
    ).map { it.toArgb() }

    PieChartView(
        dataEntries = entries,
        colors = colors,
        centerText = " Total Balance: \n ${state.balance}"
    )
}