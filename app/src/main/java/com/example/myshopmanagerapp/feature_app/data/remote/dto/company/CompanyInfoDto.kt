package com.example.myshopmanagerapp.feature_app.data.remote.dto.company

import com.example.myshopmanagerapp.core.Functions.toDate
import com.example.myshopmanagerapp.feature_app.data.local.entities.company.CompanyEntity

data class CompanyInfoDto(
    val uniqueCompanyId: String,
    val companyName: String,
    val companyContact: String,
    val companyLocation: String?,
    val companyOwners: String?,
    val companyProductsAndServices: String,
    val email: String,
    val password: String,
    val salt: String,
    val dateCreated: Long,
    val companyPersonnel: String?,
    val subscriptionPackage: String?,
    val subscriptionEndDate: Long?,
    val otherInfo: String?
){
    fun toCompanyEntity(): CompanyEntity{
        return CompanyEntity(
            0,
            uniqueCompanyId,
            companyName,
            companyContact,
            companyLocation,
            companyOwners,
            companyProductsAndServices,
            email,
            password,
            salt,
            dateCreated.toDate(),
            companyPersonnel,
            subscriptionPackage,
            subscriptionEndDate,
            otherInfo
        )
    }
}
