package com.example.myshopmanagerapp.feature_app.domain.model

import java.time.LocalDate

data class PeriodDropDownItem(
    val titleText: String,
    val isAllTime: Boolean = false,
    val firstDate: LocalDate,
    val lastDate: LocalDate,
)
