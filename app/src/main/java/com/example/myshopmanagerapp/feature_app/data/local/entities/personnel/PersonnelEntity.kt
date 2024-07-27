package com.example.myshopmanagerapp.feature_app.data.local.entities.personnel

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.myshopmanagerapp.core.Constants.Password
import com.example.myshopmanagerapp.core.Constants.Personnel_Table
import com.example.myshopmanagerapp.feature_app.data.remote.dto.personnel.PersonnelInfoDto

@Entity(tableName = Personnel_Table)
data class PersonnelEntity(
    @PrimaryKey(autoGenerate = true) val personnelId: Int,
    val uniquePersonnelId: String,
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
    fun toPersonnelInfoDto(uniqueCompanyId: String): PersonnelInfoDto{
        return PersonnelInfoDto(
            uniquePersonnelId = uniquePersonnelId,
            uniqueCompanyId = uniqueCompanyId,
            firstName = firstName,
            lastName = lastName,
            otherNames = otherNames,
            userName = userName,
            contact = contact,
            password = password,
            personnelPhoto = personnelPhoto,
            otherInfo = otherInfo,
            role = role,
            hasAdminRights = hasAdminRights,
            isPrincipalAdmin = isPrincipalAdmin,
            isActive = isActive
        )
    }
}

