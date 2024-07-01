package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.myshopmanagerapp.core.Constants.emptyString
import com.example.myshopmanagerapp.feature_app.presentation.ui.theme.LocalSpacing

@Composable
fun StockReportFilterBar(
    contentColor: Color,
    cardContainerColor: Color
){
    var expandPeriod by remember {
        mutableStateOf(false)
    }
    var expandCategory by remember {
        mutableStateOf(false)
    }
    Card(modifier = Modifier
        .fillMaxWidth()
        .height(LocalSpacing.current.topAppBarSize),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = cardContainerColor
        ),
        elevation = CardDefaults.cardElevation(LocalSpacing.current.small)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .background(Color.Transparent),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (!expandCategory) {
                Box(modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clickable {
                        expandPeriod = !expandPeriod
                        expandCategory = false
                    }
                ) {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Box(
                            modifier = Modifier
                                .weight(3f)
                                .fillMaxHeight()
                                .padding(LocalSpacing.current.small),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            Text(
                                modifier = Modifier.padding(horizontal = LocalSpacing.current.small),
                                text = "Period",
                                style = MaterialTheme.typography.bodyLarge,
                                color = contentColor
                            )
                        }
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .padding(LocalSpacing.current.small),
                            contentAlignment = Alignment.CenterEnd
                        ) {
                            IconButton(onClick = {
                                expandPeriod = !expandPeriod
                                expandCategory = false
                            }) {
                                Icon(
                                    imageVector = if (expandPeriod) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                                    contentDescription = emptyString,
                                    tint = contentColor
                                )
                            }
                        }
                    }
                }
            }
            if (!expandPeriod) {
                VerticalDivider(
                    modifier = Modifier.fillMaxHeight(),
                    color = contentColor,
                    thickness = 0.25.dp
                )
                Box(modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clickable {
                        expandCategory = !expandCategory
                        expandPeriod = false
                    }
                ) {
                    Row(modifier = Modifier.fillMaxSize()) {
                        Box(
                            modifier = Modifier
                                .weight(3f)
                                .fillMaxHeight()
                                .padding(LocalSpacing.current.small),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            Text(
                                modifier = Modifier.padding(horizontal = LocalSpacing.current.small),
                                text = "ItemCategory",
                                style = MaterialTheme.typography.bodyLarge,
                                color = contentColor
                            )
                        }
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .padding(LocalSpacing.current.small),
                            contentAlignment = Alignment.CenterEnd
                        ) {
                            IconButton(onClick = {
                                expandCategory = !expandCategory
                                expandPeriod = false
                            }) {
                                Icon(
                                    imageVector = if (expandCategory) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                                    contentDescription = emptyString,
                                    tint = contentColor
                                )
                            }

                        }
                    }
                }
            }
        }
    }
}

