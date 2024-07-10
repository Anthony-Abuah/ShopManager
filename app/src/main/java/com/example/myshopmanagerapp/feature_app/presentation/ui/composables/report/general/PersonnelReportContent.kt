package com.example.myshopmanagerapp.feature_app.presentation.ui.composables.report.general

import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import com.example.myshopmanagerapp.core.Constants.NotAvailable
import com.example.myshopmanagerapp.core.Constants.emptyString
import com.example.myshopmanagerapp.core.CustomerEntities
import com.example.myshopmanagerapp.core.FormRelatedString.GHS
import com.example.myshopmanagerapp.core.PersonnelEntities
import com.example.myshopmanagerapp.core.UserPreferences
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.components.BasicScreenColumnWithoutBottomBar
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.records.customer.CustomerCard
import com.example.myshopmanagerapp.feature_app.presentation.ui.composables.records.personnel.PersonnelCard

@Composable
fun PersonnelReportContent(
    allPersonnel: PersonnelEntities
){
    BasicScreenColumnWithoutBottomBar{
        HorizontalDivider()
        allPersonnel.forEachIndexed { index, personnel ->
            PersonnelCard(
                personnel = personnel,
                number = index.plus(1).toString(),
                onDelete = { /*TODO*/ }) {}
            HorizontalDivider()
        }
    }


}