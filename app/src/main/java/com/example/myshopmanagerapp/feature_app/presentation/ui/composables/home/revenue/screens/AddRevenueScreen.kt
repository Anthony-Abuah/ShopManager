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
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.BasicScreenTopBar
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.home.revenue.AddRevenueContent
import com.example.myshopmanagerapp.feature_app.presentation.view_models.RevenueViewModel
import java.util.*


@Composable
fun AddRevenueScreen(
    revenueViewModel: RevenueViewModel = hiltViewModel(),
    navigateBack: () -> Unit
) {
    LaunchedEffect(Unit){
        revenueViewModel.getAllRevenues()
    }
    Scaffold(
        topBar = {
            BasicScreenTopBar(topBarTitleText = "Add Revenue") {
                navigateBack()
            }
        }
    ){
        Column(modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val revenue = revenueViewModel.addRevenueInfo
            AddRevenueContent(
                revenue = revenue,
                isSavingRevenue = revenueViewModel.insertRevenueState.value.isLoading,
                savingRevenueResponse = revenueViewModel.insertRevenueState.value.message ?: emptyString,
                revenueIsSaved = revenueViewModel.insertRevenueState.value.isSuccessful,
                addRevenueDate = {_date->
                    val date = _date.toLocalDate().toDate().time
                    val dayOfWeek = _date.toLocalDate().dayOfWeek.toString().lowercase()
                        .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT)
                        else it.toString() }
                    revenueViewModel.addRevenueDate(date, dayOfWeek)
                },
                addRevenueType = { _revenueType->
                    revenueViewModel.addRevenueType(_revenueType)
                },
                addRevenueNumberOfHours = {_hours->
                    revenueViewModel.addRevenueNumberOfHours(_hours)
                },
                addRevenueAmount = {_amount->
                    revenueViewModel.addRevenueAmount(convertToDouble(_amount))
                },
                addOtherInfo = { _otherInfo->
                    revenueViewModel.addRevenueOtherInfo(_otherInfo)
                },
                addRevenue = {_revenue->
                    revenueViewModel.insertRevenue(_revenue)
                }
            ) {
                navigateBack()
            }
        }
    }
}
