package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.records.inventory_item

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.myshopmanagerapp.core.Constants.Delete
import com.example.myshopmanagerapp.core.Constants.ONE
import com.example.myshopmanagerapp.core.Constants.Unit
import com.example.myshopmanagerapp.core.FormRelatedString.DeleteSizeCategory
import com.example.myshopmanagerapp.core.FormRelatedString.DeleteSizeCategoryText
import com.example.myshopmanagerapp.core.QuantityCategorizations
import com.example.myshopmanagerapp.feature_app.domain.model.QuantityCategorization
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.BasicButton
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.BasicScreenColumnWithoutBottomBar
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.DeleteConfirmationDialog
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.QuantityCategorizationCard
import com.example.myshopmanagerapp.feature_app.presentation.ui.theme.LocalSpacing


@Composable
fun QuantityCategorizationContent(
    quantityCategorizations: QuantityCategorizations,
    getQuantityCategorizations: (QuantityCategorizations) -> Unit,
    showButton: Boolean = false,
    buttonName: String = "Update",
    updateQuantityCategorization: ()-> Unit = {}
) {
    val context = LocalContext.current
    var openDeleteDialog by remember {
        mutableStateOf(false)
    }
    var selectedQuantityCategorization by remember {
        mutableStateOf(QuantityCategorization(Unit, ONE))
    }
    Column(modifier = Modifier.fillMaxSize()) {
        HorizontalDivider(thickness = 0.25.dp, color = MaterialTheme.colorScheme.onBackground)
        BasicScreenColumnWithoutBottomBar {
            quantityCategorizations.forEach { quantityCategorization ->
                QuantityCategorizationCard(
                    name = quantityCategorization.sizeName,
                    quantity = quantityCategorization.unitsPerThisSize.toString()
                ) { item, _quantityCategorization ->
                    when (item) {
                        Delete -> {
                            openDeleteDialog = !openDeleteDialog
                            selectedQuantityCategorization = _quantityCategorization
                        }
                        else -> {
                            selectedQuantityCategorization = _quantityCategorization
                        }
                    }
                }
            }
            if (showButton) {
                Box(modifier = Modifier.padding(LocalSpacing.current.medium)) {
                    BasicButton(buttonName = buttonName) {
                        updateQuantityCategorization()
                    }
                }
            }
        }
    }


    DeleteConfirmationDialog(
        openDialog = openDeleteDialog,
        title = DeleteSizeCategory,
        textContent = DeleteSizeCategoryText,
        unconfirmedDeletedToastText = null,
        confirmedDeleteToastText = null,
        confirmDelete = {
            val thisQuantityCategorization = quantityCategorizations.minus(selectedQuantityCategorization)
            getQuantityCategorizations(thisQuantityCategorization)
            Toast.makeText(
                context,
                "Size category successfully removed",
                Toast.LENGTH_LONG
            ).show()
        }) {
        openDeleteDialog = false
    }

}
