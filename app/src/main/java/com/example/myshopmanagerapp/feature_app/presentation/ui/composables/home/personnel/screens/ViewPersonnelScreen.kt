package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.home.personnel.screens

import android.util.Log
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
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.BasicScreenTopBar
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.home.personnel.ViewPersonnelContent
import com.example.myshopmanagerapp.feature_app.presentation.view_models.PersonnelViewModel
import java.util.*


@Composable
fun ViewPersonnelScreen(
    personnelViewModel: PersonnelViewModel = hiltViewModel(),
    uniquePersonnelId: String,
    navigateToTakePhotoScreen: () -> Unit,
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
            val personnel = personnelViewModel.personnelInfo
            val photo = personnel?.personnelPhoto
            val personnelId = personnel?.personnelId ?: 0
            val firstName = personnel?.firstName ?: emptyString
            val lastName = personnel?.lastName ?: emptyString
            val otherNames = personnel?.otherNames
            val contact = personnel?.contact ?: emptyString
            val role = personnel?.role ?: emptyString
            val adminRights = personnel?.hasAdminRights
            val anyOtherInfo = personnel?.otherInfo

            Log.d("ViewPersonnel", "personnel firstName = $firstName ...")
            Log.d("ViewPersonnel", "personnel lastName = $lastName ...")

            val allPersonnel = personnelViewModel.personnelEntitiesState.value.personnelEntities ?: emptyList()
            val allValidNames = mutableListOf<String>()
            val personnelNames = allPersonnel.map { _personnel->
                val firstName1 = _personnel.firstName.lowercase(Locale.ROOT)
                val lastName1 = _personnel.lastName.lowercase(Locale.ROOT)
                val otherNames1 = _personnel.otherNames?.lowercase(Locale.ROOT)
                "$firstName1$lastName1$otherNames1"
            }
            allValidNames.addAll(personnelNames)
            allValidNames.remove("$firstName$lastName$otherNames")
            val personnelRoles = listOf("Teacher", "Driver", "Owner", "Manager", "Store Keeper")

            ViewPersonnelContent(
                personnelPhoto = photo,
                personnelId = personnelId,
                uniquePersonnelId = uniquePersonnelId,
                personnelFirstName = firstName,
                personnelLastName = lastName,
                personnelOtherNames = otherNames,
                personnelContact = contact,
                personnelRole = role,
                personnelHasAdminRight = adminRights ?: false,
                anyOtherInfo = anyOtherInfo,
                listOfPersonnelNames = allValidNames,
                getUpdatedPersonnelFirstName = { value-> personnelViewModel.updatePersonnelFirstName(value) },
                getUpdatedPersonnelLastName = { value-> personnelViewModel.updatePersonnelLastName(value) },
                getUpdatedPersonnelOtherNames = { value-> personnelViewModel.updatePersonnelOtherName(value) },
                getUpdatedPersonnelContact = { value-> personnelViewModel.updatePersonnelContact(value) },
                getUpdatedPersonnelRole = { value-> personnelViewModel.updatePersonnelRole(value) },
                getUpdatedPersonnelAdminRight = { value-> personnelViewModel.updatePersonnelAdminRights(value) },
                getUpdatePersonnelOtherInfo = { value-> personnelViewModel.updatePersonnelOtherInfo(value) },
                onTakePhoto = {_firstName, _lastName, _otherNames, _contact, _role, _anyOtherInfo, _adminRight ->
                    navigateToTakePhotoScreen()
                },
                updatePersonnel = {_personnel->
                    personnelViewModel.updatePersonnel(_personnel)
                }
            ) {
                navigateBack()
            }
        }
    }
}
