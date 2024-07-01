package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.home.inventory

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.myshopmanagerapp.feature_app.domain.model.InventoryDisplayCardValue
import com.example.myshopmanagerapp.feature_app.domain.model.InventoryQuantityDisplayValues
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.InventoryDisplayCardOnList
import com.example.myshopmanagerapp.feature_app.presentation.ui.theme.LocalSpacing

@Composable
fun InventoryCard(
    date: String,
    listOfDisplayValues: List<InventoryQuantityDisplayValues>,
    printInventoryValues: (List<InventoryQuantityDisplayValues>) -> Unit,
    getSelectedDisplayCardValues: (InventoryQuantityDisplayValues?) -> Unit,
    deleteSelectedDisplayCardValues: (InventoryQuantityDisplayValues?) -> Unit
){
    val contentColor = MaterialTheme.colorScheme.onPrimaryContainer
    val cardContainerColor = MaterialTheme.colorScheme.primaryContainer

    Card(modifier = Modifier
        .fillMaxWidth()
        .heightIn(max = 400.dp),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = cardContainerColor
        ),
        elevation = CardDefaults.cardElevation(LocalSpacing.current.small)
    ) {
        Column(
            modifier = Modifier
                .wrapContentHeight()
                .background(Color.Transparent),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(LocalSpacing.current.small),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    modifier = Modifier.padding(LocalSpacing.current.small),
                    text = "Date: $date",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = contentColor,
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(LocalSpacing.current.small),
                contentAlignment = Alignment.CenterStart
            ) {
                InventoryDisplayCardOnList(
                    inventoryCardDisplayValues = listOfDisplayValues,
                    onPrintInventory = { inventoryDisplayValues -> printInventoryValues(inventoryDisplayValues) },
                    getInventoryValue = { selectedValue -> getSelectedDisplayCardValues(selectedValue) },
                    deleteInventoryValue = { selectedValue -> deleteSelectedDisplayCardValues(selectedValue) },
                )
            }

        }
    }
}

