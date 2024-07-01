package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.home.company.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.FabPosition
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.AddFloatingActionButton
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.BasicScreenTopBar
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.home.company.CompanyListContent
import com.example.myshopmanagerapp.feature_app.presentation.view_models.CompanyViewModel


@Composable
fun CompanyListScreen(
    companyViewModel: CompanyViewModel = hiltViewModel(),
    navigateToAddCompanyScreen: () -> Unit,
    navigateToViewCompanyScreen: (String) -> Unit,
    navigateBack: () -> Unit
) {
    LaunchedEffect(Unit){
        companyViewModel.getAllCompanies()
    }
    Scaffold(
        topBar = {
            BasicScreenTopBar(topBarTitleText = "Company List") {
                navigateBack()
            }
        },
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            AddFloatingActionButton {
                navigateToAddCompanyScreen()
            }
        }
    ){it
        Column(modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val allCompanies = companyViewModel.companyEntitiesState.value.companyEntities  ?: emptyList()
            CompanyListContent(
                allCompanies = allCompanies,
                onConfirmDelete = {_uniqueCompanyId->
                    companyViewModel.deleteCompany(_uniqueCompanyId)
                    companyViewModel.getAllCompanies()
                },
                navigateToViewCompanyScreen = {_uniqueCompanyId->
                    navigateToViewCompanyScreen(_uniqueCompanyId)
                }
            )
        }
    }
}
