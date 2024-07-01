package com.example.myshopmanagerapp.feature_app.presentation.view_models.states.personnel

import com.example.myshopmanagerapp.core.PersonnelEntities

data class PersonnelEntitiesState (
    val personnelEntities: PersonnelEntities? = emptyList(),
    val isLoading: Boolean = false
)