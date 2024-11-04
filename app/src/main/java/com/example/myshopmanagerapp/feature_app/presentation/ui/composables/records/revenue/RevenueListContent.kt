package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.records.revenue

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.myshopmanagerapp.core.Constants.emptyString
import com.example.myshopmanagerapp.core.RevenueEntities
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.BasicScreenColumnWithoutBottomBar
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.ConfirmationInfoDialog
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.DeleteConfirmationDialog
import com.example.myshopmanagerapp.feature_app.presentation.ui.theme.LocalSpacing


@Composable
fun RevenueListContent(
    allRevenues: RevenueEntities?,
    revenueDeletingResponse: String,
    isDeletingRevenue: Boolean,
    getPersonnelName: (String)-> String,
    onConfirmDelete: (String)-> Unit,
    reloadAllRevenues: ()-> Unit,
    navigateToViewRevenueScreen: (String)-> Unit
) {

    var confirmationInfo by remember {
        mutableStateOf(false)
    }
    var openDeleteConfirmation by remember {
        mutableStateOf(false)
    }
    var uniqueRevenueId by remember {
        mutableStateOf(emptyString)
    }

    if (allRevenues.isNullOrEmpty()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "No revenues to show!",
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    } else {
        BasicScreenColumnWithoutBottomBar {
            allRevenues.forEachIndexed { index, revenue ->
                if (index == 0) {
                    HorizontalDivider(
                    thickness = LocalSpacing.current.divider,
                    color = MaterialTheme.colorScheme.onBackground)
                }
                Box(
                    modifier = Modifier.padding(
                        horizontal = LocalSpacing.current.small,
                        vertical = LocalSpacing.current.default,
                    ),
                    contentAlignment = Alignment.Center
                ) {
                    RevenueCard(
                        revenue = revenue,
                        personnel = getPersonnelName(revenue.uniquePersonnelId),
                        number = index.plus(1).toString(),
                        currency = "GHS",
                        onDelete = {
                            uniqueRevenueId = revenue.uniqueRevenueId
                            openDeleteConfirmation = !openDeleteConfirmation
                        }
                    ) {
                        navigateToViewRevenueScreen(revenue.uniqueRevenueId)
                    }
                }

            }
        }
        DeleteConfirmationDialog(
            openDialog = openDeleteConfirmation,
            title = "Delete Revenue",
            textContent = "Are your sure you want to permanently delete this revenue",
            unconfirmedDeletedToastText = null,
            confirmedDeleteToastText = null,
            confirmDelete = {
                onConfirmDelete(uniqueRevenueId)
                confirmationInfo = !confirmationInfo
            }
        ) {
            openDeleteConfirmation = false
        }
        ConfirmationInfoDialog(
            openDialog = confirmationInfo,
            isLoading = isDeletingRevenue,
            title = null,
            textContent = revenueDeletingResponse,
            unconfirmedDeletedToastText = null,
            confirmedDeleteToastText = null
        ) {
            reloadAllRevenues()
            confirmationInfo = false
        }
    }
}
