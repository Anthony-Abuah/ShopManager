package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.records.inventory_item.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.FabPosition
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.myshopmanagerapp.core.Constants.ONE
import com.example.myshopmanagerapp.core.Constants.Unit
import com.example.myshopmanagerapp.core.FormRelatedString.SizeNameInfo
import com.example.myshopmanagerapp.core.FormRelatedString.SizeQuantityInfo
import com.example.myshopmanagerapp.core.Functions.toNotNull
import com.example.myshopmanagerapp.feature_app.domain.model.QuantityCategorization
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.AddFloatingActionButton
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.BasicScreenTopBar
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.ConfirmationInfoDialog
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.QuantityCategorizationAlertDialog
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.records.inventory_item.QuantityCategorizationContent
import com.example.myshopmanagerapp.feature_app.presentation.view_models.InventoryItemViewModel
import java.util.*


@Composable
fun AddQuantityCategorizationScreen(
    inventoryItemViewModel: InventoryItemViewModel,
    navigateBack: () -> Unit
) {
    var openQuantityCategorizations by remember {
        mutableStateOf(false)
    }
    var openSizeNameInfoDialog by remember {
        mutableStateOf(false)
    }
    var openSizeQuantityInfoDialog by remember {
        mutableStateOf(false)
    }
    Scaffold(
        topBar = {
            BasicScreenTopBar(topBarTitleText = "Quantity Categorizations") {
                navigateBack()
            }
        },
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            AddFloatingActionButton {
                openQuantityCategorizations = !openQuantityCategorizations
            }
        }
    ){
        Column(modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            QuantityCategorizationContent(
                quantityCategorizations = inventoryItemViewModel.addInventoryItemInfo.quantityCategorizations,
                getQuantityCategorizations = {_quantityCategorizations->
                    inventoryItemViewModel.addQuantityCategorizations(_quantityCategorizations)
                }
            )
        }


        QuantityCategorizationAlertDialog(
            openDialog = openQuantityCategorizations,
            unconfirmedUpdatedToastText = null,
            confirmedUpdatedToastText = null,
            getValue = {_sizeName, _unitQuantity ->
                val unitCategory = QuantityCategorization(Unit, ONE)
                inventoryItemViewModel.addQuantityCategorizations(
                    inventoryItemViewModel.addInventoryItemInfo.quantityCategorizations
                        .asSequence()
                        .plus(unitCategory)
                        .plus(QuantityCategorization(_sizeName, _unitQuantity))
                        .toSet().sortedBy { it.unitsPerThisSize }
                        .distinctBy { it.unitsPerThisSize }
                        .distinctBy { it.sizeName.lowercase(Locale.ROOT) }
                )
            },
            openSizeNameInfoDialog = { openSizeNameInfoDialog = !openSizeNameInfoDialog },
            openSizeQuantityInfoDialog = { openSizeQuantityInfoDialog = !openSizeQuantityInfoDialog }) {
            openQuantityCategorizations = false
        }


        ConfirmationInfoDialog(
            openDialog = openSizeNameInfoDialog,
            isLoading = false,
            title = null,
            textContent = SizeNameInfo,
            unconfirmedDeletedToastText = null,
            confirmedDeleteToastText = null
        ) {
            openSizeNameInfoDialog = false
        }
        ConfirmationInfoDialog(
            openDialog = openSizeQuantityInfoDialog,
            isLoading = false,
            title = null,
            textContent = SizeQuantityInfo,
            unconfirmedDeletedToastText = null,
            confirmedDeleteToastText = null
        ) {
            openSizeQuantityInfoDialog = false
        }
    }
}


@Composable
fun UpdateQuantityCategorizationScreen(
    //uniqueInventoryId: String,
    inventoryItemViewModel: InventoryItemViewModel,
    navigateBack: () -> Unit
) {
    var openQuantityCategorizations by remember {
        mutableStateOf(false)
    }
    var openSizeNameInfoDialog by remember {
        mutableStateOf(false)
    }
    var openSizeQuantityInfoDialog by remember {
        mutableStateOf(false)
    }
    var openUpdateConfirmation by remember {
        mutableStateOf(false)
    }
    Scaffold(
        topBar = {
            BasicScreenTopBar(topBarTitleText = "Quantity Categorizations") {
                navigateBack()
            }
        },
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            AddFloatingActionButton {
                openQuantityCategorizations = !openQuantityCategorizations
            }
        }
    ){
        Column(modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            QuantityCategorizationContent(
                quantityCategorizations = inventoryItemViewModel.inventoryItemInfo.quantityCategorizations,
                getQuantityCategorizations = {_quantityCategorizations->
                    inventoryItemViewModel.updateQuantityCategorizations(_quantityCategorizations)
                },
                updateQuantityCategorization = {
                    inventoryItemViewModel.updateInventoryItem(inventoryItemViewModel.inventoryItemInfo)
                    openUpdateConfirmation = !openUpdateConfirmation
                },
                showButton = inventoryItemViewModel.inventoryItemInfo.quantityCategorizations.isNotEmpty()
            )
        }
        QuantityCategorizationAlertDialog(
            openDialog = openQuantityCategorizations,
            unconfirmedUpdatedToastText = null,
            confirmedUpdatedToastText = null,
            getValue = {_sizeName, _unitQuantity ->
                val unitCategory = QuantityCategorization(Unit, ONE)
                inventoryItemViewModel.updateQuantityCategorizations(
                    inventoryItemViewModel.inventoryItemInfo.quantityCategorizations
                        .asSequence()
                        .plus(unitCategory)
                        .plus(QuantityCategorization(_sizeName, _unitQuantity))
                        .toSet().sortedBy { it.unitsPerThisSize }
                        .distinctBy { it.unitsPerThisSize }
                        .distinctBy { it.sizeName.lowercase(Locale.ROOT) }
                )
            },
            openSizeNameInfoDialog = { openSizeNameInfoDialog = !openSizeNameInfoDialog },
            openSizeQuantityInfoDialog = { openSizeQuantityInfoDialog = !openSizeQuantityInfoDialog }) {
            openQuantityCategorizations = false
        }
        ConfirmationInfoDialog(
            openDialog = openSizeNameInfoDialog,
            isLoading = false,
            title = null,
            textContent = SizeNameInfo,
            unconfirmedDeletedToastText = null,
            confirmedDeleteToastText = null
        ) {
            openSizeNameInfoDialog = false
        }
        ConfirmationInfoDialog(
            openDialog = openSizeQuantityInfoDialog,
            isLoading = false,
            title = null,
            textContent = SizeQuantityInfo,
            unconfirmedDeletedToastText = null,
            confirmedDeleteToastText = null
        ) {
            openSizeQuantityInfoDialog = false
        }
        ConfirmationInfoDialog(
            openDialog = openUpdateConfirmation,
            isLoading = inventoryItemViewModel.updateInventoryItemState.value.isLoading,
            title = null,
            textContent = inventoryItemViewModel.updateInventoryItemState.value.message.toNotNull(),
            unconfirmedDeletedToastText = null,
            confirmedDeleteToastText = null
        ) {
            openUpdateConfirmation = false
            if (inventoryItemViewModel.updateInventoryItemState.value.isSuccessful) {
                navigateBack()
            }
        }
    }
}
