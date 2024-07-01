package com.example.myshopmanagerapp.feature_app.presentation.view_models.states.company

import com.example.myshopmanagerapp.core.Constants.emptyString
import com.example.myshopmanagerapp.feature_app.data.local.entities.company.CompanyEntity
import com.example.myshopmanagerapp.feature_app.data.remote.dto.company.CompanyInfoDto
import java.util.*

data class CompanyInfoState (
    val companyInfo: CompanyInfoDto? = CompanyInfoDto( emptyString, emptyString, emptyString, emptyString, emptyString, emptyString, emptyString, emptyString, emptyString, 0, emptyString, emptyString, 0, emptyString),
    val isLoading: Boolean = false,
    val message: String = emptyString,
    val isSuccessful: Boolean = false
)