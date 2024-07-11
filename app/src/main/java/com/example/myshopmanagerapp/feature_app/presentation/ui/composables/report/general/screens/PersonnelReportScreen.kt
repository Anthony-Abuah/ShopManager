package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.report.general.screens

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
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.report.general.PersonnelReportContent
import com.example.myshopmanagerapp.feature_app.presentation.view_models.PersonnelViewModel

@Composable
fun PersonnelReportScreen(
    personnelViewModel: PersonnelViewModel = hiltViewModel(),
    navigateBack: () -> Unit,
) {

    LaunchedEffect(Unit) {
        personnelViewModel.getAllPersonnel()
    }

    Scaffold(
        topBar = {
            BasicScreenTopBar(topBarTitleText = "All Personnel") {
                navigateBack()
            }
        },
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            PersonnelReportContent(
                allPersonnel = personnelViewModel.personnelEntitiesState.value.personnelEntities?.filter { it.isActive== true } ?: emptyList()
            )
        }
    }
}
