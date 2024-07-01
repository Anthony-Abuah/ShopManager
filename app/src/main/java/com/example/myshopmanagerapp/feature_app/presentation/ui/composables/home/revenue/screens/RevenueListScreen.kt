package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.home.revenue.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.FabPosition
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.myshopmanagerapp.core.Constants.emptyString
import com.example.myshopmanagerapp.core.Functions.toNotNull
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.*
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.home.revenue.RevenueListScreenTopBar
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.home.revenue.RevenueListContent
import com.example.myshopmanagerapp.feature_app.presentation.view_models.PersonnelViewModel
import com.example.myshopmanagerapp.feature_app.presentation.view_models.RevenueViewModel


@Composable
fun RevenueListScreen(
    revenueViewModel: RevenueViewModel,
    personnelViewModel: PersonnelViewModel = hiltViewModel(),
    navigateToAddRevenueScreen: () -> Unit,
    navigateToViewRevenueScreen: (String) -> Unit,
    navigateBack: () -> Unit
) {
    val context = LocalContext.current
    LaunchedEffect(Unit){
        revenueViewModel.getAllRevenues()
        personnelViewModel.getAllPersonnel()
    }

    var openPDFDialog by remember {
        mutableStateOf(false)
    }
    var openDialogInfo by remember {
        mutableStateOf(false)
    }
    var dialogMessage by remember {
        mutableStateOf(emptyString)
    }
    var openComparisonBar by remember {
        mutableStateOf(false)
    }
    var openDateRangePickerBar by remember {
        mutableStateOf(false)
    }
    var openSearchBar by remember {
        mutableStateOf(false)
    }
    var allRevenues = revenueViewModel.revenueEntitiesState.value.revenueEntities ?: emptyList()

    Scaffold(
        topBar = {
            val allPersonnel = personnelViewModel.personnelEntitiesState.value.personnelEntities ?: emptyList()
            RevenueListScreenTopBar(
                entireRevenues = revenueViewModel.revenueEntitiesState.value.revenueEntities ?: emptyList(),
                allRevenues = allRevenues,
                showDateRangePickerBar = openDateRangePickerBar,
                showSearchBar = openSearchBar,
                showComparisonBar = openComparisonBar,
                getPersonnelName = { _uniquePersonnelId ->
                    val personnel = allPersonnel.firstOrNull { it.uniquePersonnelId == _uniquePersonnelId }
                    return@RevenueListScreenTopBar "${personnel?.firstName} ${personnel?.lastName} ${personnel?.otherNames}"
                },
                openDialogInfo = { _dialogMessage ->
                    dialogMessage = _dialogMessage
                    openDialogInfo = !openDialogInfo
                },
                openSearchBar = { openSearchBar = true },
                openDateRangePickerBar = { openDateRangePickerBar = true },
                closeSearchBar = { openSearchBar = false },
                closeDateRangePickerBar = { openDateRangePickerBar = false },
                closeComparisonBar = { openComparisonBar = false },
                openComparisonBar = { openComparisonBar = true },
                printRevenues = {
                    revenueViewModel.generateRevenueListPDF(context, allRevenues)
                    openPDFDialog = true
                },
                getRevenues = {
                    allRevenues = it
                }
            ) {
                navigateBack()
            }
        },
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            AddFloatingActionButton {
                navigateToAddRevenueScreen()
            }
        }
    ){
        Column(modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val allPersonnel = personnelViewModel.personnelEntitiesState.value.personnelEntities ?: emptyList()
            val mapOfPersonnelInverse = mutableMapOf<String, String>()
            allPersonnel.forEach {personnel->
                mapOfPersonnelInverse[personnel.uniquePersonnelId] = "${personnel.lastName} ${personnel.otherNames?: emptyString} ${personnel.firstName}"
            }
            RevenueListContent(
                allRevenues = allRevenues,
                isDeletingRevenue = revenueViewModel.deleteRevenueState.value.isLoading,
                revenueDeletingResponse = revenueViewModel.deleteRevenueState.value.message ?: emptyString,
                getPersonnelName = { _uniquePersonnelId ->
                    val thisPersonnel = personnelViewModel.personnelEntitiesState.value.personnelEntities?.firstOrNull{it.uniquePersonnelId == _uniquePersonnelId}
                    return@RevenueListContent "${thisPersonnel?.firstName.toNotNull()} ${thisPersonnel?.lastName.toNotNull()} ${thisPersonnel?.otherNames.toNotNull()}"
                },
                onConfirmDelete = {_uniqueRevenueId->
                    revenueViewModel.deleteRevenue(_uniqueRevenueId)
                },
                navigateToViewRevenueScreen = {_uniqueRevenueId->
                    navigateToViewRevenueScreen(_uniqueRevenueId)
                },
                reloadAllRevenues = {
                    revenueViewModel.getAllRevenues()
                }
            )
        }

        ConfirmationInfoDialog(
            openDialog = openDialogInfo,
            isLoading = false,
            title = null,
            textContent = dialogMessage,
            unconfirmedDeletedToastText = null,
            confirmedDeleteToastText = null
        ) {
            openDialogInfo = false
        }

        ConfirmationInfoDialog(
            openDialog = openPDFDialog,
            isLoading = revenueViewModel.generateRevenueListState.value.isLoading,
            title = null,
            textContent = revenueViewModel.generateRevenueListState.value.message.toNotNull(),
            unconfirmedDeletedToastText = null,
            confirmedDeleteToastText = null
        ) {
            openPDFDialog = false
        }

    }
}
