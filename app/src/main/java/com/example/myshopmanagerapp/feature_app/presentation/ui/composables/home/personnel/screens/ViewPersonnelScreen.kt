package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.home.personnel.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.myshopmanagerapp.core.Functions.toNotNull
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.BasicScreenTopBar
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.home.personnel.ViewPersonnelContent
import com.example.myshopmanagerapp.feature_app.presentation.view_models.PersonnelViewModel


@Composable
fun ViewPersonnelScreen(
    personnelViewModel: PersonnelViewModel = hiltViewModel(),
    uniquePersonnelId: String,
    navigateBack: () -> Unit,
) {
    LaunchedEffect(Unit){
        personnelViewModel.getPersonnel(uniquePersonnelId)
        personnelViewModel.getAllPersonnel()
    }

    Scaffold(
        topBar = {
            BasicScreenTopBar(topBarTitleText = "View Personnel") {
                navigateBack()
            }
        }
    ){
        Column(modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            ViewPersonnelContent(
                personnel = personnelViewModel.personnelInfo,
                passwordResetMessage = personnelViewModel.resetPasswordState.value.message.toNotNull(),
            ) {uniquePersonnelId->
                personnelViewModel.resetPassword(uniquePersonnelId)
            }
        }
    }
}
