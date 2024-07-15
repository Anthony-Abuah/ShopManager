package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.records.company.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.BasicScreenTopBar
import com.example.myshopmanagerapp.feature_app.presentation.view_models.CompanyViewModel


@Composable
fun ViewCompanyScreen(
    companyViewModel: CompanyViewModel = hiltViewModel(),
    uniqueCompanyId: String,
    navigateBack: () -> Unit
) {
    LaunchedEffect(Unit){
        companyViewModel.getCompany(uniqueCompanyId)
    }
    Scaffold(
        topBar = {
            BasicScreenTopBar(topBarTitleText = "View Company") {
                navigateBack()
            }
        }
    ){
        Column(modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {}
    }
}
