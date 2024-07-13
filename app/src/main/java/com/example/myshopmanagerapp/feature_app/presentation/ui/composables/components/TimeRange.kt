package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.example.myshopmanagerapp.feature_app.presentation.ui.theme.*

@Composable
fun TimeRange(
    listOfTimes: List<String>,
    getSelectedItem: (String)-> Unit,
) {
    var selectedItemIndex by remember {
        mutableStateOf(0)
    }
    val mainRowBackgroundColor = if (isSystemInDarkTheme()) Grey20 else BlueGrey90

    Row(
        modifier = Modifier
            .background(mainRowBackgroundColor, MaterialTheme.shapes.large)
            .fillMaxWidth()
            .height(LocalSpacing.current.large)
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        for ((index, item) in listOfTimes.withIndex()) {
            val cardBackground = if (isSystemInDarkTheme()) {
                if (selectedItemIndex == index) BlueGrey30 else Color.Transparent
            } else {
                if (selectedItemIndex == index) BlueGrey80 else Color.Transparent
            }
            val cardContentColor = if (isSystemInDarkTheme()) {
                 BlueGrey90
            } else {
                BlueGrey20
            }

            Box(
                modifier = Modifier
                    .background(
                        cardBackground,
                        MaterialTheme.shapes.large
                    )
                    .wrapContentWidth()
                    .fillMaxHeight()
                    .clickable {
                        selectedItemIndex = index
                        getSelectedItem(item)
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    modifier = Modifier.padding(horizontal = LocalSpacing.current.smallMedium),
                    text = item,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = if (selectedItemIndex == index) FontWeight.ExtraBold else FontWeight.SemiBold,
                    color = cardContentColor
                )
            }
        }
    }
}

