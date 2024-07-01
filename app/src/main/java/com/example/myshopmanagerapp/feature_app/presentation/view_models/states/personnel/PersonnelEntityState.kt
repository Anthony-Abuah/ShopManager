package com.example.myshopmanagerapp.feature_app.presentation.view_models.states.personnel

import com.example.myshopmanagerapp.core.Constants.emptyString
import com.example.myshopmanagerapp.feature_app.data.local.entities.personnel.PersonnelEntity

data class PersonnelEntityState (
    val personnelEntity: PersonnelEntity? = PersonnelEntity(0, emptyString, emptyString, emptyString, emptyString, emptyString,"1234", emptyString, emptyString, emptyString, emptyString, false),
    val isLoading: Boolean = false
)