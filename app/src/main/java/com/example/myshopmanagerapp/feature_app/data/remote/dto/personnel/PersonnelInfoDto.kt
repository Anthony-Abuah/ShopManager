package com.example.myshopmanagerapp.feature_app.data.remote.dto.personnel

import com.example.myshopmanagerapp.core.Constants.Password
import com.example.myshopmanagerapp.feature_app.data.local.entities.personnel.PersonnelEntity


data class PersonnelInfoDto(
    val uniquePersonnelId: String,
    val uniqueCompanyId: String,
    val firstName: String,
    val lastName: String,
    val otherNames: String?,
    val userName: String = lastName,
    val contact: String,
    val password: String = Password,
    val personnelPhoto: String?,
    val otherInfo: String?,
    val role: String?,
    val hasAdminRights: Boolean?,
    val isPrincipalAdmin: Boolean = false,
    val isActive: Boolean? = true
){
    fun toPersonnelEntity(): PersonnelEntity{
        return PersonnelEntity(
            0,
            uniquePersonnelId = uniquePersonnelId,
            firstName = firstName,
            lastName = lastName,
            otherNames = otherNames,
            userName = userName,
            contact = contact,
            personnelPhoto = personnelPhoto,
            otherInfo = otherInfo,
            role = role,
            hasAdminRights = hasAdminRights,
            password = password,
            isPrincipalAdmin = isPrincipalAdmin,
            isActive = isActive
        )
    }
}
