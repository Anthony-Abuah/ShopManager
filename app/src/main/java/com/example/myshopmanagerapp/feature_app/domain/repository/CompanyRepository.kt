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

    suspend fun registerShopAccount(company: CompanyEntity, confirmedPassword: String)

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

    /*
    suspend fun syncCompanyInfo()

    suspend fun restoreDatabase(context: Context, restart: Boolean = true): Flow<Resource<String>>
    */

}
