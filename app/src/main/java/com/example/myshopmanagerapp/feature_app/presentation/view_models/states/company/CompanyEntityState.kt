package com.example.myshopmanagerapp.feature_app.presentation.view_models.states.company

import com.example.myshopmanagerapp.core.Constants.emptyString
import com.example.myshopmanagerapp.feature_app.data.local.entities.company.CompanyEntity
import java.util.*

data class CompanyEntityState (
    val companyEntity: CompanyEntity? = CompanyEntity(0, emptyString, emptyString, emptyString, emptyString, emptyString, emptyString, emptyString, emptyString, emptyString, Date(), emptyString, emptyString, 0, emptyString),
    val isLoading: Boolean = false
)