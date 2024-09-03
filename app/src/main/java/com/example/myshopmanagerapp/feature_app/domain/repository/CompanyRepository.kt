package com.example.myshopmanagerapp.feature_app.domain.repository

import android.content.Context
import com.example.myshopmanagerapp.core.CompanyEntities
import com.example.myshopmanagerapp.core.Resource
import com.example.myshopmanagerapp.feature_app.data.local.entities.company.CompanyEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow


interface CompanyRepository {

    fun getAllCompanies(): Flow<Resource<CompanyEntities?>>

    suspend fun changePassword(currentPassword: String, newPassword: String, confirmedPassword: String)

    suspend fun changeShopName(currentPassword: String, shopName: String)
    suspend fun changeEmail(currentPassword: String, email: String)
    suspend fun changeContact(currentPassword: String, contact: String)
    suspend fun changeLocation(currentPassword: String, location: String)
    suspend fun changeProductsAndServices(currentPassword: String, productsAndServices: String)
    suspend fun changeOtherInfo(currentPassword: String, otherInfo: String)

    suspend fun registerShopAccount(company: CompanyEntity, confirmedPassword: String)

    suspend fun registerLocalAccountOnline(company: CompanyEntity)

    suspend fun login(email: String, password: String): Flow<Resource<String>>

    suspend fun companyLogin(email: String, password: String)

    suspend fun logout(): Flow<Resource<String>>

    suspend fun companyLogout()

    suspend fun addCompanies(companies: CompanyEntities)

    suspend fun getCompany(uniqueCompanyId: String): CompanyEntity?

    suspend fun getCompanyByName(companyName: String): CompanyEntities?

    suspend fun updateCompany(company: CompanyEntity)

    suspend fun deleteCompany(companyId: Int)

    suspend fun deleteCompany(uniqueCompanyId: String)

    suspend fun deleteAllCompanies()

    fun restartApp()



}
