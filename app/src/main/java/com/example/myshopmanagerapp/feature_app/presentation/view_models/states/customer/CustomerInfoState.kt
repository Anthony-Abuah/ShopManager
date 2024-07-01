package com.example.myshopmanagerapp.feature_app.presentation.view_models.states.customer

import com.example.myshopmanagerapp.core.Constants.emptyString

data class CustomerInfoState (
    val customerName: String = emptyString,
    val customerContact: String = emptyString,
    val customerLocation: String? = null,
    val customerPhoto: String? = null,
    val anyOtherInfo: String? = null,
)