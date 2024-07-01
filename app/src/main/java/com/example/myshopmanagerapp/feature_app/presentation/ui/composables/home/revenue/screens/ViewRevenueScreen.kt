package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.home.revenue.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.myshopmanagerapp.core.Constants.emptyString
import com.example.myshopmanagerapp.core.Functions.convertToDouble
import com.example.myshopmanagerapp.core.Functions.toDate
import com.example.myshopmanagerapp.core.Functions.toLocalDate
import com.example.myshopmanagerapp.core.Functions.toNotNull
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.BasicScreenTopBar
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.home.revenue.ViewRevenueContent
import com.example.myshopmanagerapp.feature_app.presentation.view_models.PersonnelViewModel
import com.example.myshopmanagerapp.feature_app.presentation.view_models.RevenueViewModel
import java.util.*


@Composable
fun ViewRevenueScreen(
    revenueViewModel: RevenueViewModel,
    personnelViewModel: PersonnelViewModel = hiltViewModel(),
    uniqueRevenueId: String,
    navigateBack: () -> Unit
) {
    LaunchedEffect(Unit){
        personnelViewModel.getAllPersonnel()
        revenueViewModel.getAllRevenues()
        revenueViewModel.getRevenue(uniqueRevenueId)
    }
    Scaffold(
        topBar = {
            BasicScreenTopBar(topBarTitleText = "View Revenue") {
                navigateBack()
            }
        }
    ){
        Column(modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val revenueInfo = revenueViewModel.revenueInfo
            val allPersonnel = personnelViewModel.personnelEntitiesState.value.personnelEntities ?: emptyList()
            val personnel = allPersonnel.firstOrNull { it.uniquePersonnelId == revenueInfo.uniquePersonnelId }
            val personnelName = "${personnel?.firstName.toNotNull()} ${personnel?.lastName.toNotNull()} ${personnel?.otherNames.toNotNull()}"

            ViewRevenueContent(
                revenue = revenueInfo,
                personnelName = personnelName,
                isUpdatingRevenue = revenueViewModel.updateRevenueState.value.isLoading,
                updatingRevenueIsSuccessful = revenueViewModel.updateRevenueState.value.isSuccessful,
                updatingRevenueResponse = revenueViewModel.updateRevenueState.value.message ?: emptyString,
                currency = "GHS",
                getUpdatedRevenueDate = {_dateString->
                    val date = _dateString.toLocalDate().toDate().time
                    val dayOfWeek = _dateString.toLocalDate().dayOfWeek.toString().lowercase()
                        .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }
                    revenueViewModel.updateRevenueDate(date, dayOfWeek)
                },
                getUpdatedRevenueHours = {_hours->
                    revenueViewModel.updateRevenueNumberOfHours(_hours)
                },
                getUpdatedRevenueAmount = {_revenueAmount->
                    revenueViewModel.updateRevenueAmount(convertToDouble(_revenueAmount))
                },
                getUpdatedRevenueOtherInfo = {_otherInfo->
                    revenueViewModel.updateRevenueOtherInfo(_otherInfo)
                },
                getUpdatedRevenueType = {_revenueType->
                    revenueViewModel.updateRevenueType(_revenueType)
                },
                updateRevenue = {_updatedRevenueEntity->
                    revenueViewModel.updateRevenue(_updatedRevenueEntity!!)
                }
            ) {
                navigateBack()
            }
        }
    }
}
