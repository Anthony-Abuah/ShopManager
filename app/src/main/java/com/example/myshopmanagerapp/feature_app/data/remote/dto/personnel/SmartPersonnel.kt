package com.example.myshopmanagerapp.feature_app.data.remote.dto.personnel

import com.example.myshopmanagerapp.feature_app.domain.model.UniqueId

data class SmartPersonnel(
    val personnel: List<PersonnelInfoDto>,
    val uniquePersonnelIds: List<UniqueId>
)