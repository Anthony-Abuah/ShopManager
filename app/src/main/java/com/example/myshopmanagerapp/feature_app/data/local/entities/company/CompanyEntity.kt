package com.example.myshopmanagerapp.feature_app.data.local.entities.company

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.myshopmanagerapp.core.Constants.Company_Table
import com.example.myshopmanagerapp.core.Functions.toTimestamp
import com.example.myshopmanagerapp.feature_app.data.remote.dto.company.CompanyInfoDto
import java.util.Date

@Entity(tableName = Company_Table)
data class CompanyEntity(
    @PrimaryKey(autoGenerate = true) val companyId: Int,
    val uniqueCompanyId: String,
    val companyName: String,
    val companyContact: String,
    val companyLocation: String?,
    val companyOwners: String?,
    val companyProductsAndServices: String,
    val email: String,
    val password: String,
    val salt: String,
    val dateCreated: Date,
    val companyPersonnel: String?,
    val subscriptionPackage: String?,
    val subscriptionEndDate: Long?,
    val otherInfo: String?
){
    fun toCompanyDto(): CompanyInfoDto{
        return CompanyInfoDto(
            uniqueCompanyId = uniqueCompanyId,
            companyName = companyName,
            companyContact = companyContact,
            companyLocation = companyLocation,
            companyOwners = companyOwners,
            companyProductsAndServices = companyProductsAndServices,
            email = email,
            password = password,
            salt = salt,
            dateCreated = dateCreated.toTimestamp(),
            companyPersonnel = companyPersonnel ,
            subscriptionPackage = subscriptionPackage ,
            subscriptionEndDate = subscriptionEndDate,
            otherInfo = otherInfo
        )
    }
}

