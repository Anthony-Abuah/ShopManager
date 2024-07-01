package com.example.myshopmanagerapp.feature_app.data.remote.dto.personnel

import com.example.myshopmanagerapp.feature_app.data.local.entities.personnel.PersonnelEntity


data class PersonnelInfoDto(
    val uniquePersonnelId: String,
    val uniqueCompanyId: String,
    val firstName: String,
    val lastName: String,
    val otherNames: String?,
    val personnelContact: String,
    val personnelPhoto: String?,
    val otherInfo: String?,
    val personnelRole: String?,
    val hasAdminRights: Boolean?
){
    fun toPersonnelEntity(): PersonnelEntity{
        return PersonnelEntity(
            0,
            uniquePersonnelId = uniquePersonnelId,
            firstName = firstName,
            lastName = lastName,
            otherNames = otherNames,
            contact = personnelContact,
            personnelPhoto = personnelPhoto,
            otherInfo = otherInfo,
            role = personnelRole,
            hasAdminRights = hasAdminRights
        )
    }
}
