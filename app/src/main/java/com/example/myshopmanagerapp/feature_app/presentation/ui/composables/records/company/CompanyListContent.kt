package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.records.company

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.myshopmanagerapp.core.CompanyEntities
import com.example.myshopmanagerapp.core.Constants.emptyString
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.BasicScreenColumnWithoutBottomBar
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.DeleteConfirmationDialog
import com.example.myshopmanagerapp.feature_app.presentation.ui.theme.LocalSpacing


@Composable
fun CompanyListContent(
    allCompanies: CompanyEntities?,
    onConfirmDelete: (String)-> Unit,
    navigateToViewCompanyScreen: (String)-> Unit
) {
    var openDeleteConfirmation by remember {
        mutableStateOf(false)
    }
    var uniqueCompanyId by remember {
        mutableStateOf(emptyString)
    }
    var companyName by remember {
        mutableStateOf(emptyString)
    }

    if (allCompanies.isNullOrEmpty()){
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "No companies have been added yet!",
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
    else{
        BasicScreenColumnWithoutBottomBar {
            allCompanies.forEach { company ->
                Box(
                    modifier = Modifier.padding(LocalSpacing.current.extraSmall),
                    contentAlignment = Alignment.Center
                ) {
                    CompanyCard(
                        companyName = company.companyName,
                        products = company.companyProductsAndServices,
                        companyLocation = company.companyLocation ?: emptyString,
                        onDelete = {
                            companyName = company.companyName
                            uniqueCompanyId = company.uniqueCompanyId
                            openDeleteConfirmation = !openDeleteConfirmation
                        },
                        onOpenCompany = {
                            navigateToViewCompanyScreen(company.uniqueCompanyId)
                        }
                    )
                }
            }
        }
        DeleteConfirmationDialog(
            openDialog = openDeleteConfirmation,
            title = "Delete Company",
            textContent = "Are your sure you want to permanently delete this company",
            unconfirmedDeletedToastText = "Company not deleted",
            confirmedDeleteToastText = "Company $companyName has been successfully removed",
            confirmDelete = {
                onConfirmDelete(uniqueCompanyId)
            }
        ) {
            openDeleteConfirmation = false
        }
    }

}
