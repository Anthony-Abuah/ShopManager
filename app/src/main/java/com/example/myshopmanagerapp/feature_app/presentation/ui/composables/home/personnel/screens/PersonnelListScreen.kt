package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.home.personnel.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.FabPosition
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.myshopmanagerapp.core.Constants
import com.example.myshopmanagerapp.core.Constants.listOfListNumbers
import com.example.myshopmanagerapp.core.Constants.listOfPersonnelSortItems
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.AddFloatingActionButton
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.ConfirmationInfoDialog
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.CustomerScreenTopBar
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.SearchTopBar
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.home.personnel.PersonnelListContent
import com.example.myshopmanagerapp.feature_app.presentation.view_models.PersonnelViewModel

@Composable
fun PersonnelListScreen(
    personnelViewModel: PersonnelViewModel,
    navigateToAddPersonnelScreen: () -> Unit,
    navigateToViewPersonnelScreen: (String) -> Unit,
    navigateBack: () -> Unit
) {
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        personnelViewModel.getAllPersonnel()
    }
    var openDialogInfo by remember {
        mutableStateOf(false)
    }
    var dialogMessage by remember {
        mutableStateOf(Constants.emptyString)
    }

    var openSearchBar by remember {
        mutableStateOf(false)
    }
    var selectedNumber by remember {
        mutableStateOf(0)
    }
    var allPersonnel = personnelViewModel.personnelEntitiesState.value.personnelEntities ?: emptyList()

    Scaffold(
        topBar = {
            if (openSearchBar){
                SearchTopBar(placeholder = "Search personnel...",
                    getSearchValue = {_searchValue->
                        if (_searchValue.isBlank()){
                            openSearchBar = false
                        }else{
                            allPersonnel = allPersonnel.filter { it.firstName.contains(_searchValue) || it.lastName.contains(_searchValue) || it.otherNames?.contains(_searchValue) == true }
                            openSearchBar = false
                            openDialogInfo = true
                            openDialogInfo = false
                        }
                    }
                )
            }
            else {
                CustomerScreenTopBar(
                    topBarTitleText = "Personnel",
                    onSort = { _value ->
                        when (_value.number) {
                            1 -> {
                                allPersonnel = allPersonnel.sortedBy { it.lastName.take(1) }
                                dialogMessage = "Last names are sorted in alphabetical order"
                                Toast.makeText(context, dialogMessage, Toast.LENGTH_LONG).show()
                            }
                            2 -> {
                                allPersonnel = allPersonnel.sortedByDescending { it.lastName.take(1) }
                                dialogMessage = "Last names are sorted in inverse alphabetical order"
                                Toast.makeText(context, dialogMessage, Toast.LENGTH_LONG).show()
                            }
                            3 -> {
                                allPersonnel = allPersonnel.sortedBy { it.firstName.take(1) }
                                dialogMessage = "First names are sorted in alphabetical order"
                                Toast.makeText(context, dialogMessage, Toast.LENGTH_LONG).show()
                            }
                            4 -> {
                                allPersonnel = allPersonnel.sortedByDescending { it.firstName.take(1) }
                                dialogMessage = "First names are sorted in inverse alphabetical order"
                                Toast.makeText(context, dialogMessage, Toast.LENGTH_LONG).show()
                            }


                        }
                    },
                    listDropDownItems = listOfListNumbers,
                    onClickListItem = { _listNumber ->
                        selectedNumber = _listNumber.number
                        val number = _listNumber.number
                        when (_listNumber.number) {
                            0 -> {
                                allPersonnel = personnelViewModel.personnelEntitiesState.value.personnelEntities
                                    ?: emptyList()
                                dialogMessage = "All personnel are selected"
                                Toast.makeText(context, dialogMessage, Toast.LENGTH_LONG).show()
                            }
                            1 -> {
                                Toast.makeText(context, "Search", Toast.LENGTH_LONG).show()
                                openSearchBar = !openSearchBar
                            }
                            2 -> {
                                dialogMessage = "Total number of personnel on this list are ${allPersonnel.size}"
                                openDialogInfo = !openDialogInfo
                            }
                            else -> {
                                allPersonnel = allPersonnel.take(number)
                                dialogMessage = "First $number personnel selected"
                                Toast.makeText(context, dialogMessage, Toast.LENGTH_LONG).show()
                            }
                        }
                    },
                    listOfSortItems = listOfPersonnelSortItems,
                ) {
                    navigateBack()
                }


            }
        },
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            AddFloatingActionButton {
                navigateToAddPersonnelScreen()
            }
        }
    ){
        Column(modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            PersonnelListContent(
                personnel = allPersonnel,
                personnelDeletingMessage = personnelViewModel.deletePersonnelState.value.message,
                isDeletingPersonnel = personnelViewModel.deletePersonnelState.value.isLoading,
                personnelDeletionIsSuccessful = personnelViewModel.deletePersonnelState.value.isSuccessful,
                reloadAllPersonnel = { personnelViewModel.getAllPersonnel() },
                onConfirmDelete = {_uniquePersonnelId->
                    personnelViewModel.deletePersonnel(_uniquePersonnelId)
                }
            ) {_uniquePersonnelId->
                personnelViewModel.getPersonnel(_uniquePersonnelId)
                navigateToViewPersonnelScreen(_uniquePersonnelId)
            }
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
    }
}
