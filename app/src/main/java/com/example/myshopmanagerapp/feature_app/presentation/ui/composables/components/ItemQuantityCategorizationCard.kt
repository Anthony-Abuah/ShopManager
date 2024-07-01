package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import com.example.myshopmanagerapp.core.Constants.emptyString
import com.example.myshopmanagerapp.core.FormRelatedString.Cancel
import com.example.myshopmanagerapp.core.FormRelatedString.EnterNumberOfBigBoxes
import com.example.myshopmanagerapp.core.FormRelatedString.EnterNumberOfBiggerBoxes
import com.example.myshopmanagerapp.core.FormRelatedString.EnterNumberOfBiggestBoxes
import com.example.myshopmanagerapp.core.FormRelatedString.EnterNumberOfBoxes
import com.example.myshopmanagerapp.core.FormRelatedString.EnterNumberOfCartons
import com.example.myshopmanagerapp.core.FormRelatedString.EnterNumberOf
import com.example.myshopmanagerapp.core.FormRelatedString.EnterNumberOfUnits
import com.example.myshopmanagerapp.core.FormRelatedString.NumberOfBigBoxesPerBiggerBox
import com.example.myshopmanagerapp.core.FormRelatedString.NumberOfBiggerBoxesPerBiggestBox
import com.example.myshopmanagerapp.core.FormRelatedString.NumberOfBoxesPerBigBox
import com.example.myshopmanagerapp.core.FormRelatedString.NumberOfCartonsPerBox
import com.example.myshopmanagerapp.core.FormRelatedString.NumberOfPacksPerCarton
import com.example.myshopmanagerapp.core.FormRelatedString.NumberOfUnitsPerPack
import com.example.myshopmanagerapp.core.FormRelatedString.QuantityInSizeNamePlaceholder
import com.example.myshopmanagerapp.core.FormRelatedString.QuantityInUnitPlaceholder
import com.example.myshopmanagerapp.core.FormRelatedString.SaveQuantity
import com.example.myshopmanagerapp.core.FormRelatedString.UnitsPerPackPlaceholder
import com.example.myshopmanagerapp.core.Functions.convertToInt
import com.example.myshopmanagerapp.feature_app.domain.model.ItemQuantityCategorization
import com.example.myshopmanagerapp.feature_app.presentation.ui.theme.LocalSpacing

@Composable
fun ItemQuantityCategorizationCard(
    itemQuantityCategorization: ItemQuantityCategorization?,
    discardChanges: () -> Unit,
    getQuantities: (ItemQuantityCategorization) -> Unit,
){
    val size1 = itemQuantityCategorization?.size1?.toString() ?: emptyString
    val unitsPerPacks = itemQuantityCategorization?.numberOfUnitsPerSize1?.toString() ?: emptyString

    val size2 = itemQuantityCategorization?.size2?.toString() ?: emptyString
    val packsPerCarton = itemQuantityCategorization?.numberOfSize1PerSize2?.toString() ?: emptyString

    val size3 = itemQuantityCategorization?.size3?.toString() ?: emptyString
    val cartonsPerBoxes = itemQuantityCategorization?.numberOfSize2PerSize3?.toString() ?: emptyString

    val size4 = itemQuantityCategorization?.size4?.toString() ?: emptyString
    val boxesPerBigBoxes = itemQuantityCategorization?.numberOfSize3PerSize4?.toString() ?: emptyString

    val size5 = itemQuantityCategorization?.size5?.toString() ?: emptyString
    val bigBoxesPerBiggerBoxes = itemQuantityCategorization?.numberOfSize4PerSize5?.toString() ?: emptyString

    val size6 = itemQuantityCategorization?.size6?.toString() ?: emptyString
    val biggerBoxesPerBiggestBoxes = itemQuantityCategorization?.numberOfSize5PerSize6?.toString() ?: emptyString

    var numberOfUnits by remember {
        mutableStateOf(itemQuantityCategorization?.unit?.toString() ?: emptyString)
    }
    var numberOfSize1 by remember {
        mutableStateOf(size1)
    }
    var numberOfUnitsPerSize1 by remember {
        mutableStateOf(unitsPerPacks)
    }
    var numberOfSize2 by remember {
        mutableStateOf(size2)
    }
    var numberOfSize1PerSize2 by remember {
        mutableStateOf(packsPerCarton)
    }
    var numberOfSize3 by remember {
        mutableStateOf(size3)
    }
    var numberOfSize2PerSize3 by remember {
        mutableStateOf(cartonsPerBoxes)
    }
    var numberOfSize4 by remember {
        mutableStateOf(size4)
    }
    var numberOfSize3PerSize4 by remember {
        mutableStateOf(boxesPerBigBoxes)
    }
    var numberOfSize5 by remember {
        mutableStateOf(size5)
    }
    var numberOfSize4PerSize5 by remember {
        mutableStateOf(bigBoxesPerBiggerBoxes)
    }
    var numberOfSize6 by remember {
        mutableStateOf(size6)
    }
    var numberOfSize5PerSize6 by remember {
        mutableStateOf(biggerBoxesPerBiggestBoxes)
    }
    Card(modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()
        .padding(LocalSpacing.current.small),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            contentColor = MaterialTheme.colorScheme.onSurface,
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(LocalSpacing.current.noElevation)
    ){
        Column(modifier = Modifier.fillMaxWidth()) {
            // Units
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(LocalSpacing.current.borderStroke),
                    contentAlignment = Alignment.Center
                ) {
                    ItemQuantityCategorizationTextField(
                        value = numberOfUnits,
                        onValueChange = { numberOfUnits = it },
                        placeholder = QuantityInUnitPlaceholder,
                        label = EnterNumberOfUnits,
                        keyboardType = KeyboardType.Number
                    )
                }
            }

            // Packs
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = LocalSpacing.current.extraSmall),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(LocalSpacing.current.borderStroke),
                    contentAlignment = Alignment.Center
                ) {
                    ItemQuantityCategorizationTextField(
                        value = numberOfSize1,
                        onValueChange = { numberOfSize1 = it },
                        placeholder = QuantityInSizeNamePlaceholder,
                        label = EnterNumberOf,
                        keyboardType = KeyboardType.Number
                    )
                }
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(LocalSpacing.current.borderStroke),
                    contentAlignment = Alignment.Center
                ) {
                    ItemQuantityCategorizationTextField(
                        value = numberOfUnitsPerSize1,
                        onValueChange = { numberOfUnitsPerSize1 = it },
                        placeholder = UnitsPerPackPlaceholder,
                        label = NumberOfUnitsPerPack,
                        keyboardType = KeyboardType.Number
                    )
                }
            }

            // Cartons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = LocalSpacing.current.extraSmall),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(LocalSpacing.current.borderStroke),
                    contentAlignment = Alignment.Center
                ) {
                    ItemQuantityCategorizationTextField(
                        value = numberOfSize2,
                        onValueChange = { numberOfSize2 = it },
                        placeholder = QuantityInSizeNamePlaceholder,
                        label = EnterNumberOfCartons,
                        keyboardType = KeyboardType.Number
                    )
                }
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(LocalSpacing.current.borderStroke),
                    contentAlignment = Alignment.Center
                ) {
                    ItemQuantityCategorizationTextField(
                        value = numberOfSize1PerSize2,
                        onValueChange = { numberOfSize1PerSize2 = it },
                        placeholder = UnitsPerPackPlaceholder,
                        label = NumberOfPacksPerCarton,
                        keyboardType = KeyboardType.Number
                    )
                }
            }

            // Boxes
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = LocalSpacing.current.extraSmall),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(LocalSpacing.current.borderStroke),
                    contentAlignment = Alignment.Center
                ) {
                    ItemQuantityCategorizationTextField(
                        value = numberOfSize3,
                        onValueChange = { numberOfSize3 = it },
                        placeholder = QuantityInSizeNamePlaceholder,
                        label = EnterNumberOfBoxes,
                        keyboardType = KeyboardType.Number
                    )
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(LocalSpacing.current.borderStroke),
                    contentAlignment = Alignment.Center
                ) {
                    ItemQuantityCategorizationTextField(
                        value = numberOfSize2PerSize3,
                        onValueChange = { numberOfSize2PerSize3 = it },
                        placeholder = UnitsPerPackPlaceholder,
                        label = NumberOfCartonsPerBox,
                        keyboardType = KeyboardType.Number
                    )
                }
            }

            // Big Boxes
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = LocalSpacing.current.extraSmall),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(LocalSpacing.current.borderStroke),
                    contentAlignment = Alignment.Center
                ) {
                    ItemQuantityCategorizationTextField(
                        value = numberOfSize4,
                        onValueChange = { numberOfSize4 = it },
                        placeholder = QuantityInSizeNamePlaceholder,
                        label = EnterNumberOfBigBoxes,
                        keyboardType = KeyboardType.Number
                    )
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(LocalSpacing.current.borderStroke),
                    contentAlignment = Alignment.Center
                ) {
                    ItemQuantityCategorizationTextField(
                        value = numberOfSize3PerSize4,
                        onValueChange = { numberOfSize3PerSize4 = it },
                        placeholder = UnitsPerPackPlaceholder,
                        label = NumberOfBoxesPerBigBox,
                        keyboardType = KeyboardType.Number
                    )
                }
            }

            // Bigger Boxes
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = LocalSpacing.current.extraSmall),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(LocalSpacing.current.borderStroke),
                    contentAlignment = Alignment.Center
                ) {
                    ItemQuantityCategorizationTextField(
                        value = numberOfSize5,
                        onValueChange = { numberOfSize5 = it },
                        placeholder = QuantityInSizeNamePlaceholder,
                        label = EnterNumberOfBiggerBoxes,
                        keyboardType = KeyboardType.Number
                    )
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(LocalSpacing.current.borderStroke),
                    contentAlignment = Alignment.Center
                ) {
                    ItemQuantityCategorizationTextField(
                        value = numberOfSize4PerSize5,
                        onValueChange = { numberOfSize4PerSize5 = it },
                        placeholder = UnitsPerPackPlaceholder,
                        label = NumberOfBigBoxesPerBiggerBox,
                        keyboardType = KeyboardType.Number
                    )
                }
            }

            // Biggest Boxes
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = LocalSpacing.current.extraSmall),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(LocalSpacing.current.borderStroke),
                    contentAlignment = Alignment.Center
                ) {
                    ItemQuantityCategorizationTextField(
                        value = numberOfSize6,
                        onValueChange = { numberOfSize6 = it },
                        placeholder = QuantityInSizeNamePlaceholder,
                        label = EnterNumberOfBiggestBoxes,
                        keyboardType = KeyboardType.Number
                    )
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(LocalSpacing.current.borderStroke),
                    contentAlignment = Alignment.Center
                ) {
                    ItemQuantityCategorizationTextField(
                        value = numberOfSize5PerSize6,
                        onValueChange = { numberOfSize5PerSize6 = it },
                        placeholder = UnitsPerPackPlaceholder,
                        label = NumberOfBiggerBoxesPerBiggestBox,
                        keyboardType = KeyboardType.Number
                    )
                }
            }

            Row(modifier = Modifier.fillMaxWidth()
                .padding(vertical = LocalSpacing.current.smallMedium),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Discard
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(LocalSpacing.current.small),
                    contentAlignment = Alignment.Center
                ) {
                    CustomizeButton(
                        buttonName = Cancel,
                        contentColor = MaterialTheme.colorScheme.onErrorContainer,
                        containerColor = MaterialTheme.colorScheme.errorContainer,
                        buttonHeight = LocalSpacing.current.topAppBarSize
                    ) {
                        discardChanges()
                    }
                }

                // Save
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(LocalSpacing.current.small),
                    contentAlignment = Alignment.Center
                ) {
                    CustomizeButton(
                        buttonName = SaveQuantity,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        buttonHeight = LocalSpacing.current.topAppBarSize
                    ) {
                        getQuantities(
                            ItemQuantityCategorization(
                                unit = convertToInt(numberOfUnits),
                                size1 = convertToInt(numberOfSize1),
                                size2 = convertToInt(numberOfSize2),
                                size3 = convertToInt(numberOfSize3),
                                size4 = convertToInt(numberOfSize4),
                                size5 = convertToInt(numberOfSize5),
                                size6 = convertToInt(numberOfSize6),
                                numberOfUnitsPerSize1 = convertToInt(numberOfUnitsPerSize1),
                                numberOfSize1PerSize2 = convertToInt(numberOfSize1PerSize2),
                                numberOfSize2PerSize3 = convertToInt(numberOfSize2PerSize3),
                                numberOfSize3PerSize4 = convertToInt(numberOfSize3PerSize4),
                                numberOfSize4PerSize5 = convertToInt(numberOfSize4PerSize5),
                                numberOfSize5PerSize6 = convertToInt(numberOfSize5PerSize6)
                            )
                        )
                    }
                }
            }
        }
    }
}

