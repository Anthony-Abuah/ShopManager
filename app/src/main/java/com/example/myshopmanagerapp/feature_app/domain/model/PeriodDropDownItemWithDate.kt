package com.example.myshopmanagerapp.feature_app.domain.model

import com.example.myshopmanagerapp.core.Functions.toLocalDate
import java.util.*

data class PeriodDropDownItemWithDate(
    val titleText: String,
    val isAllTime: Boolean = false,
    val firstDate: Date,
    val lastDate: Date,
){
    fun toPeriodDropDownItem(): PeriodDropDownItem{
        return PeriodDropDownItem(
            titleText,
            isAllTime,
            firstDate.toLocalDate(),
            lastDate.toLocalDate()
        )
    }
}
