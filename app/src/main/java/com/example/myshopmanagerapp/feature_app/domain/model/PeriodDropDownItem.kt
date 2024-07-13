package com.example.myshopmanagerapp.feature_app.domain.model

import com.example.myshopmanagerapp.core.Functions.toDate
import com.example.myshopmanagerapp.core.Functions.toLocalDate
import java.time.LocalDate

data class PeriodDropDownItem(
    val titleText: String,
    val isAllTime: Boolean = false,
    val firstDate: LocalDate,
    val lastDate: LocalDate,
){
    fun toPeriodDropDownItemWithDate(): PeriodDropDownItemWithDate{
        return PeriodDropDownItemWithDate(
            titleText,
            isAllTime,
            firstDate.toDate(),
            lastDate.toDate()
        )
    }
}
