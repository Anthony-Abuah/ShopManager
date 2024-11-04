package com.example.myshopmanagerapp.feature_app.data.repository

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import com.example.myshopmanagerapp.core.CompanyEntities
import com.example.myshopmanagerapp.core.Constants
import com.example.myshopmanagerapp.core.Constants.InternetConnectionMessage
import com.example.myshopmanagerapp.core.Constants.ServerErrorMessage
import com.example.myshopmanagerapp.core.Constants.ShopAppDatabase
import com.example.myshopmanagerapp.core.Constants.UnknownError
import com.example.myshopmanagerapp.core.Constants.emptyString
import com.example.myshopmanagerapp.core.Functions.generateUniqueCompanyId
import com.example.myshopmanagerapp.core.Functions.toCompanyEntity
import com.example.myshopmanagerapp.core.Functions.toCompanyEntityJson
import com.example.myshopmanagerapp.core.Functions.toCompanyInfoDto
import com.example.myshopmanagerapp.core.Functions.toNotNull
import com.example.myshopmanagerapp.core.Resource
import com.example.myshopmanagerapp.core.TypeConverters.toListOfCompanyEntities
import com.example.myshopmanagerapp.core.TypeConverters.toListOfCompanyEntitiesJson
import com.example.myshopmanagerapp.core.TypeConverters.toPersonnelEntity
import com.example.myshopmanagerapp.core.UserPreferences
import com.example.myshopmanagerapp.feature_app.MyShopManagerApp
import com.example.myshopmanagerapp.feature_app.data.local.AppDatabase
import com.example.myshopmanagerapp.feature_app.data.local.entities.company.CompanyEntity
import com.example.myshopmanagerapp.feature_app.data.remote.ShopManagerDatabaseApi
import com.example.myshopmanagerapp.feature_app.data.remote.dto.company.CompanyResponseDto
import com.example.myshopmanagerapp.feature_app.domain.model.AddCompanyResponse
import com.example.myshopmanagerapp.feature_app.domain.repository.*
import com.example.myshopmanagerapp.feature_app.security.HashService
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import java.io.File
import java.io.IOException


class CompanyRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val shopManagerDatabaseApi: ShopManagerDatabaseApi,
    private val customerRepository: CustomerRepository,
    private val bankAccountRepository: BankAccountRepository,
    private val cashInRepository: CashInRepository,
    private val debtRepository: DebtRepository,
    private val debtRepaymentRepository: DebtRepaymentRepository,
    private val expenseRepository: ExpenseRepository,
    private val inventoryRepository: InventoryRepository,
    private val inventoryItemRepository: InventoryItemRepository,
    private val personnelRepository: PersonnelRepository,
    private val revenueRepository: RevenueRepository,
    private val savingsRepository: SavingsRepository,
    private val stockRepository: StockRepository,
    private val supplierRepository: SupplierRepository,
    private val withdrawalRepository: WithdrawalRepository,
    private val hashService: HashService,
): CompanyRepository{
    override fun getAllCompanies(): Flow<Resource<CompanyEntities?>> = flow{
        emit(Resource.Loading())
        try {
            val companyResponse = shopManagerDatabaseApi.fetchAllCompanies()
            val theCompanies = companyResponse.data?.map { it.toCompanyEntity() }
            emit(Resource.Success(theCompanies))
        }catch (e: Exception){
            emit(Resource.Error(
                message = "Couldn't load all Companies from Database",
                data = emptyList()
            ))
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    override suspend fun changePassword(
        currentPassword: String,
        newPassword: String,
        confirmedPassword: String
    ) {
        val context = MyShopManagerApp.applicationContext()
        val userPreferences = UserPreferences(context)
        try {
            val isLoggedIn = userPreferences.getLoggedInState.first()
            userPreferences.saveRepositoryJobMessage(emptyString)
            userPreferences.saveRepositoryJobSuccessValue(false)
            val uniqueCompanyId = userPreferences.getShopInfo.first().toCompanyEntity()?.uniqueCompanyId
            val shopAccountResponse = shopManagerDatabaseApi.getCompany(uniqueCompanyId.toNotNull())
            val onlineCompanyAccountNotExists = shopAccountResponse?.data.toCompanyInfoDto() == null
            val isPrincipalAdmin = userPreferences.getPersonnelInfo.first().toPersonnelEntity()?.isPrincipalAdmin == true
            when(true){
                (isLoggedIn != true)->{
                    userPreferences.saveRepositoryJobSuccessValue(false)
                    userPreferences.saveRepositoryJobMessage("Please ensure that you're logged in to change password")
                }
                (newPassword != confirmedPassword)->{
                    userPreferences.saveRepositoryJobSuccessValue(false)
                    userPreferences.saveRepositoryJobMessage("Passwords do not match")
                }
                (!isPrincipalAdmin)->{
                    userPreferences.saveRepositoryJobSuccessValue(false)
                    userPreferences.saveRepositoryJobMessage("Only the principal admin has the privileges to make this change")
                }
                (uniqueCompanyId == null)->{
                    userPreferences.saveRepositoryJobSuccessValue(false)
                    userPreferences.saveRepositoryJobMessage("Could not get this account's info\nPlease ensure that you're logged in")
                }
                onlineCompanyAccountNotExists->{
                    val shopInfo = userPreferences.getShopInfo.first().toCompanyEntity()
                    if (shopInfo != null) {
                        val saltedHash = hashService.generateSaltedHash(confirmedPassword)
                        val salt = saltedHash.salt
                        val hashedPassword = saltedHash.hash
                        appDatabase.companyDao.updateCompany(shopInfo.copy(password = hashedPassword, salt = salt))
                        userPreferences.saveShopInfo(shopInfo.copy(password = hashedPassword, salt = salt).toCompanyEntityJson().toNotNull())
                        userPreferences.saveRepositoryJobSuccessValue(true)
                        userPreferences.saveRepositoryJobMessage("Password changed successfully")
                    }else{
                        userPreferences.saveRepositoryJobSuccessValue(false)
                        userPreferences.saveRepositoryJobMessage( "Couldn't change password")
                    }
                }
                else->{
                    val call = shopManagerDatabaseApi.changePassword(uniqueCompanyId,currentPassword, newPassword)
                    call!!.enqueue(object : Callback<CompanyResponseDto>{
                        override fun onResponse(
                            call: Call<CompanyResponseDto>,
                            response: Response<CompanyResponseDto>
                        ) {
                            val responseInfo = response.body()?.data.toCompanyInfoDto()
                            GlobalScope.launch(Dispatchers.IO + Job()) {
                                if (responseInfo != null) {
                                    val updatedAccount = responseInfo.toCompanyEntity()
                                    appDatabase.companyDao.deleteAllCompanies()
                                    appDatabase.companyDao.addCompany(updatedAccount)
                                    
                                    userPreferences.saveShopInfo(updatedAccount.toCompanyEntityJson().toNotNull())
                                    userPreferences.saveLoggedInState(true)
                                    userPreferences.saveRepositoryJobMessage(response.body()?.message.toNotNull())
                                    userPreferences.saveRepositoryJobSuccessValue(true)
                                } else {
                                    userPreferences.saveRepositoryJobSuccessValue(false)
                                    userPreferences.saveRepositoryJobMessage(response.body()?.message ?: "Unable to change password")
                                }
                            }
                        }
                        override fun onFailure(call: Call<CompanyResponseDto>, t: Throwable) {
                            GlobalScope.launch(Dispatchers.IO + Job()) {
                                userPreferences.saveRepositoryJobSuccessValue(false)
                                userPreferences.saveRepositoryJobMessage(t.message ?: ServerErrorMessage)
                            }
                        }
                    })
                }
            }
        }
        catch (e: Exception){
            userPreferences.saveRepositoryJobSuccessValue(false)
            userPreferences.saveRepositoryJobMessage("$ServerErrorMessage\n$InternetConnectionMessage")
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    override suspend fun resetPassword(
        email: String,
        newPassword: String,
        confirmedPassword: String,
        personnelPassword: String
    ) {
        val context = MyShopManagerApp.applicationContext()
        val userPreferences = UserPreferences(context)
        try {
            val isLoggedIn = userPreferences.getLoggedInState.first()
            userPreferences.saveRepositoryJobMessage(emptyString)
            userPreferences.saveRepositoryJobSuccessValue(false)
            val uniqueCompanyId = userPreferences.getShopInfo.first().toCompanyEntity()?.uniqueCompanyId
            val shopAccountResponse = shopManagerDatabaseApi.getCompany(uniqueCompanyId.toNotNull())
            val onlineCompanyAccountExists = shopAccountResponse?.data.toCompanyInfoDto() != null
            val isPrincipalAdmin = userPreferences.getPersonnelInfo.first().toPersonnelEntity()?.isPrincipalAdmin == true
            when(true){
                (isLoggedIn != true)->{
                    userPreferences.saveRepositoryJobSuccessValue(false)
                    userPreferences.saveRepositoryJobMessage("Please ensure that you're logged in to change password")
                }
                (newPassword != confirmedPassword)->{
                    userPreferences.saveRepositoryJobSuccessValue(false)
                    userPreferences.saveRepositoryJobMessage("Passwords do not match")
                }
                (!isPrincipalAdmin)->{
                    userPreferences.saveRepositoryJobSuccessValue(false)
                    userPreferences.saveRepositoryJobMessage("Only the principal admin has the privileges to make this change")
                }
                (uniqueCompanyId == null)->{
                    userPreferences.saveRepositoryJobSuccessValue(false)
                    userPreferences.saveRepositoryJobMessage("Could not get this account's info\nPlease ensure that you're logged in")
                }
                !onlineCompanyAccountExists->{
                    val shopInfo = userPreferences.getShopInfo.first().toCompanyEntity()
                    val emailIsVerified = email.trim() == shopInfo?.email?.trim()
                    when(true){
                        (shopInfo == null)-> {
                            userPreferences.saveRepositoryJobSuccessValue(false)
                            userPreferences.saveRepositoryJobMessage( "Couldn't change password")
                        }
                        (!emailIsVerified)-> {
                            userPreferences.saveRepositoryJobSuccessValue(false)
                            userPreferences.saveRepositoryJobMessage( "Couldn't verify email.\nPlease enter the correct email that was used to register this account")
                        }
                        else->{
                            val saltedHash = hashService.generateSaltedHash(confirmedPassword)
                            val salt = saltedHash.salt
                            val hashedPassword = saltedHash.hash
                            appDatabase.companyDao.updateCompany(shopInfo.copy(password = hashedPassword, salt = salt))
                            userPreferences.saveShopInfo(shopInfo.copy(password = hashedPassword, salt = salt).toCompanyEntityJson().toNotNull())
                            userPreferences.saveRepositoryJobSuccessValue(true)
                            userPreferences.saveRepositoryJobMessage("Password reset successful")
                        }
                    }

                }
                else->{
                    val call = shopManagerDatabaseApi.resetPassword(uniqueCompanyId,email, newPassword, personnelPassword)
                    call!!.enqueue(object : Callback<CompanyResponseDto>{
                        override fun onResponse(
                            call: Call<CompanyResponseDto>,
                            response: Response<CompanyResponseDto>
                        ) {
                            val responseInfo = response.body()?.data.toCompanyInfoDto()
                            GlobalScope.launch(Dispatchers.IO + Job()) {
                                if (responseInfo != null) {
                                    val updatedAccount = responseInfo.toCompanyEntity()
                                    appDatabase.companyDao.deleteAllCompanies()
                                    appDatabase.companyDao.addCompany(updatedAccount)

                                    userPreferences.saveShopInfo(updatedAccount.toCompanyEntityJson().toNotNull())
                                    userPreferences.saveLoggedInState(true)
                                    userPreferences.saveRepositoryJobMessage(response.body()?.message.toNotNull())
                                    userPreferences.saveRepositoryJobSuccessValue(true)
                                    
                                } else {
                                    userPreferences.saveRepositoryJobSuccessValue(false)
                                    userPreferences.saveRepositoryJobMessage(response.body()?.message ?: UnknownError)
                                }
                            }
                        }
                        override fun onFailure(call: Call<CompanyResponseDto>, t: Throwable) {
                            GlobalScope.launch(Dispatchers.IO + Job()) {
                                userPreferences.saveRepositoryJobSuccessValue(false)
                                userPreferences.saveRepositoryJobMessage(t.message ?: ServerErrorMessage)
                            }
                        }
                    })
                }
            }
        }
        catch (e: Exception){
            userPreferences.saveRepositoryJobSuccessValue(false)
            userPreferences.saveRepositoryJobMessage("${e.message}\n$ServerErrorMessage\n$InternetConnectionMessage")
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    override suspend fun changeShopName(currentPassword: String, shopName: String) {
        val context = MyShopManagerApp.applicationContext()
        val userPreferences = UserPreferences(context)
        try {
            val isLoggedIn = userPreferences.getLoggedInState.first()
            userPreferences.saveRepositoryJobMessage(emptyString)
            userPreferences.saveRepositoryJobSuccessValue(false)
            val uniqueCompanyId = userPreferences.getShopInfo.first().toCompanyEntity()?.uniqueCompanyId
            val shopAccountResponse = shopManagerDatabaseApi.getCompany(uniqueCompanyId.toNotNull())
            val onlineCompanyAccountExists = shopAccountResponse?.data.toCompanyInfoDto() != null
            val isPrincipalAdmin = userPreferences.getPersonnelInfo.first().toPersonnelEntity()?.isPrincipalAdmin == true
            when(true){
                (isLoggedIn != true)->{
                    userPreferences.saveRepositoryJobSuccessValue(false)
                    userPreferences.saveRepositoryJobMessage("Please ensure that you're logged in to change shop name")
                }
                (!isPrincipalAdmin)->{
                    userPreferences.saveRepositoryJobSuccessValue(false)
                    userPreferences.saveRepositoryJobMessage("Only the principal admin has the privileges to make this change")
                }
                (uniqueCompanyId == null)->{
                    userPreferences.saveRepositoryJobSuccessValue(false)
                    userPreferences.saveRepositoryJobMessage("Could not get this account's info\nPlease ensure that you're logged in")
                }
                !onlineCompanyAccountExists->{
                    val shopInfo = userPreferences.getShopInfo.first().toCompanyEntity()
                    if (shopInfo != null) {
                        appDatabase.companyDao.updateCompany(shopInfo.copy(companyName = shopName))
                        userPreferences.saveShopInfo(shopInfo.copy(companyName = shopName).toCompanyEntityJson().toNotNull())
                        userPreferences.saveRepositoryJobSuccessValue(true)
                        userPreferences.saveRepositoryJobMessage("Shop name changed successfully")
                    }else{
                        userPreferences.saveRepositoryJobSuccessValue(false)
                        userPreferences.saveRepositoryJobMessage( "Couldn't change shop name")
                    }
                }
                else->{
                    val call = shopManagerDatabaseApi.changeShopName(uniqueCompanyId,currentPassword, shopName)
                    call!!.enqueue(object : Callback<CompanyResponseDto>{
                        override fun onResponse(
                            call: Call<CompanyResponseDto>,
                            response: Response<CompanyResponseDto>
                        ) {
                            val shopNameChangedSuccessfully = response.body()?.data.toCompanyInfoDto() != null
                            GlobalScope.launch(Dispatchers.IO + Job()) {
                                if (shopNameChangedSuccessfully) {
                                    val responseInfo = response.body()?.data?.toCompanyInfoDto()
                                    val updatedAccountInfo = responseInfo?.toCompanyEntity()
                                    updatedAccountInfo?.let { _companyEntity->
                                        appDatabase.companyDao.deleteAllCompanies()
                                        appDatabase.companyDao.addCompany(_companyEntity)

                                        userPreferences.saveShopInfo(_companyEntity.toCompanyEntityJson().toNotNull())
                                        userPreferences.saveLoggedInState(true)
                                        userPreferences.saveRepositoryJobMessage(response.body()?.message.toNotNull())
                                        userPreferences.saveRepositoryJobSuccessValue(true)
                                    }
                                } else {
                                    userPreferences.saveRepositoryJobSuccessValue(false)
                                    userPreferences.saveRepositoryJobMessage(response.body()?.message ?: UnknownError)
                                }

                            }
                        }
                        override fun onFailure(call: Call<CompanyResponseDto>, t: Throwable) {
                            GlobalScope.launch(Dispatchers.IO + Job()) {
                                userPreferences.saveRepositoryJobSuccessValue(false)
                                userPreferences.saveRepositoryJobMessage(t.message ?: ServerErrorMessage)
                            }
                        }
                    })
                }
            }
        }catch (e: Exception){
            userPreferences.saveRepositoryJobSuccessValue(false)
            userPreferences.saveRepositoryJobMessage("$ServerErrorMessage\n$InternetConnectionMessage")
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    override suspend fun changeEmail(currentPassword: String, email: String) {
        val context = MyShopManagerApp.applicationContext()
        val userPreferences = UserPreferences(context)
        try {
            val isLoggedIn = userPreferences.getLoggedInState.first()
            userPreferences.saveRepositoryJobMessage(emptyString)
            userPreferences.saveRepositoryJobSuccessValue(false)
            val uniqueCompanyId = userPreferences.getShopInfo.first().toCompanyEntity()?.uniqueCompanyId
            val shopAccountResponse = shopManagerDatabaseApi.getCompany(uniqueCompanyId.toNotNull())
            val onlineCompanyAccountExists = shopAccountResponse?.data.toCompanyInfoDto() != null
            val isPrincipalAdmin = userPreferences.getPersonnelInfo.first().toPersonnelEntity()?.isPrincipalAdmin == true
            when(true){
                (isLoggedIn != true)->{
                    userPreferences.saveRepositoryJobSuccessValue(false)
                    userPreferences.saveRepositoryJobMessage("Please ensure that you're logged in to change email")
                }
                (!isPrincipalAdmin)->{
                    userPreferences.saveRepositoryJobSuccessValue(false)
                    userPreferences.saveRepositoryJobMessage("Only the principal admin has the privileges to make this change")
                }
                (uniqueCompanyId == null)->{
                    userPreferences.saveRepositoryJobSuccessValue(false)
                    userPreferences.saveRepositoryJobMessage("Could not get this account's info\nPlease ensure that you're logged in")
                }
                !onlineCompanyAccountExists->{
                    val shopInfo = userPreferences.getShopInfo.first().toCompanyEntity()
                    if (shopInfo != null) {
                        appDatabase.companyDao.updateCompany(shopInfo.copy(email = email))
                        userPreferences.saveShopInfo(shopInfo.copy(email = email).toCompanyEntityJson().toNotNull())
                        userPreferences.saveRepositoryJobSuccessValue(true)
                        userPreferences.saveRepositoryJobMessage("Email changed successfully")
                    }else{
                        userPreferences.saveRepositoryJobSuccessValue(false)
                        userPreferences.saveRepositoryJobMessage( "Couldn't change email")
                    }
                }
                else->{
                    val call = shopManagerDatabaseApi.changeEmail(uniqueCompanyId,currentPassword, email)
                    call!!.enqueue(object : Callback<CompanyResponseDto>{
                        override fun onResponse(
                            call: Call<CompanyResponseDto>,
                            response: Response<CompanyResponseDto>
                        ) {
                            val emailChangedSuccessfully = response.body()?.data.toCompanyInfoDto() != null
                            GlobalScope.launch(Dispatchers.IO + Job()) {
                                if (emailChangedSuccessfully) {
                                    val responseInfo = response.body()?.data?.toCompanyInfoDto()
                                    val updatedAccountInfo = responseInfo?.toCompanyEntity()
                                    updatedAccountInfo?.let { _companyEntity->
                                        appDatabase.companyDao.deleteAllCompanies()
                                        appDatabase.companyDao.addCompany(_companyEntity)

                                        userPreferences.saveShopInfo(_companyEntity.toCompanyEntityJson().toNotNull())
                                        userPreferences.saveLoggedInState(true)
                                        userPreferences.saveRepositoryJobMessage(response.body()?.message.toNotNull())
                                        userPreferences.saveRepositoryJobSuccessValue(true)
                                    }
                                } else {
                                    userPreferences.saveRepositoryJobSuccessValue(false)
                                    userPreferences.saveRepositoryJobMessage(response.body()?.message ?: UnknownError)
                                }

                            }
                        }
                        override fun onFailure(call: Call<CompanyResponseDto>, t: Throwable) {
                            GlobalScope.launch(Dispatchers.IO + Job()) {
                                userPreferences.saveRepositoryJobSuccessValue(false)
                                userPreferences.saveRepositoryJobMessage(t.message ?: ServerErrorMessage)
                            }
                        }
                    })
                }
            }
        }catch (e: Exception){
            userPreferences.saveRepositoryJobSuccessValue(false)
            userPreferences.saveRepositoryJobMessage("$ServerErrorMessage\n$InternetConnectionMessage")
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    override suspend fun changeContact(currentPassword: String, contact: String) {
        val context = MyShopManagerApp.applicationContext()
        val userPreferences = UserPreferences(context)
        try {
            val isLoggedIn = userPreferences.getLoggedInState.first()
            userPreferences.saveRepositoryJobMessage(emptyString)
            userPreferences.saveRepositoryJobSuccessValue(false)
            val uniqueCompanyId = userPreferences.getShopInfo.first().toCompanyEntity()?.uniqueCompanyId
            val shopAccountResponse = shopManagerDatabaseApi.getCompany(uniqueCompanyId.toNotNull())
            val onlineCompanyAccountNotExists = shopAccountResponse?.data.toCompanyInfoDto() == null
            val isPrincipalAdmin = userPreferences.getPersonnelInfo.first().toPersonnelEntity()?.isPrincipalAdmin == true
            when(true){
                (isLoggedIn != true)->{
                    userPreferences.saveRepositoryJobSuccessValue(false)
                    userPreferences.saveRepositoryJobMessage("Please ensure that you're logged in to change contact")
                }
                (!isPrincipalAdmin)->{
                    userPreferences.saveRepositoryJobSuccessValue(false)
                    userPreferences.saveRepositoryJobMessage("Only the principal admin has the privileges to make this change")
                }
                (uniqueCompanyId == null)->{
                    userPreferences.saveRepositoryJobSuccessValue(false)
                    userPreferences.saveRepositoryJobMessage("Could not get this account's info\nPlease ensure that you're logged in")
                }
                onlineCompanyAccountNotExists->{
                    val shopInfo = userPreferences.getShopInfo.first().toCompanyEntity()
                    if (shopInfo != null) {
                        appDatabase.companyDao.updateCompany(shopInfo.copy(companyContact = contact))
                        userPreferences.saveShopInfo(shopInfo.copy(companyContact = contact).toCompanyEntityJson().toNotNull())
                        userPreferences.saveRepositoryJobSuccessValue(true)
                        userPreferences.saveRepositoryJobMessage("Contact changed successfully")
                    }else{
                        userPreferences.saveRepositoryJobSuccessValue(false)
                        userPreferences.saveRepositoryJobMessage( "Couldn't change contact")
                    }
                }
                else->{
                    val call = shopManagerDatabaseApi.changeCompanyContact(uniqueCompanyId,currentPassword, contact)
                    call!!.enqueue(object : Callback<CompanyResponseDto>{
                        override fun onResponse(
                            call: Call<CompanyResponseDto>,
                            response: Response<CompanyResponseDto>
                        ) {
                            val contactChangedSuccessfully = response.body()?.data.toCompanyInfoDto() != null
                            GlobalScope.launch(Dispatchers.IO + Job()) {
                                if (contactChangedSuccessfully) {
                                    val responseInfo = response.body()?.data?.toCompanyInfoDto()
                                    val updatedAccountInfo = responseInfo?.toCompanyEntity()
                                    updatedAccountInfo?.let { _companyEntity->
                                        appDatabase.companyDao.deleteAllCompanies()
                                        appDatabase.companyDao.addCompany(_companyEntity)

                                        userPreferences.saveShopInfo(_companyEntity.toCompanyEntityJson().toNotNull())
                                        userPreferences.saveLoggedInState(true)
                                        userPreferences.saveRepositoryJobMessage(response.body()?.message.toNotNull())
                                        userPreferences.saveRepositoryJobSuccessValue(true)
                                    }
                                } else {
                                    userPreferences.saveRepositoryJobSuccessValue(false)
                                    userPreferences.saveRepositoryJobMessage(response.body()?.message ?: UnknownError)
                                }

                            }
                        }
                        override fun onFailure(call: Call<CompanyResponseDto>, t: Throwable) {
                            GlobalScope.launch(Dispatchers.IO + Job()) {
                                userPreferences.saveRepositoryJobSuccessValue(false)
                                userPreferences.saveRepositoryJobMessage(t.message ?: ServerErrorMessage)
                            }
                        }
                    })
                }
            }
        }catch (e: Exception){
            userPreferences.saveRepositoryJobSuccessValue(false)
            userPreferences.saveRepositoryJobMessage("$ServerErrorMessage\n$InternetConnectionMessage")
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    override suspend fun changeLocation(currentPassword: String, location: String) {
        val context = MyShopManagerApp.applicationContext()
        val userPreferences = UserPreferences(context)
        try {
            val isLoggedIn = userPreferences.getLoggedInState.first()
            userPreferences.saveRepositoryJobMessage(emptyString)
            userPreferences.saveRepositoryJobSuccessValue(false)
            val uniqueCompanyId = userPreferences.getShopInfo.first().toCompanyEntity()?.uniqueCompanyId
            val shopAccountResponse = shopManagerDatabaseApi.getCompany(uniqueCompanyId.toNotNull())
            val onlineCompanyAccountNotExists = shopAccountResponse?.data.toCompanyInfoDto() == null
            val isPrincipalAdmin = userPreferences.getPersonnelInfo.first().toPersonnelEntity()?.isPrincipalAdmin == true
            when(true){
                (isLoggedIn != true)->{
                    userPreferences.saveRepositoryJobSuccessValue(false)
                    userPreferences.saveRepositoryJobMessage("Please ensure that you're logged in to change location")
                }
                (!isPrincipalAdmin)->{
                    userPreferences.saveRepositoryJobSuccessValue(false)
                    userPreferences.saveRepositoryJobMessage("Only the principal admin has the privileges to make this change")
                }
                (uniqueCompanyId == null)->{
                    userPreferences.saveRepositoryJobSuccessValue(false)
                    userPreferences.saveRepositoryJobMessage("Could not get this account's info\nPlease ensure that you're logged in")
                }
                onlineCompanyAccountNotExists->{
                    val shopInfo = userPreferences.getShopInfo.first().toCompanyEntity()
                    if (shopInfo != null) {
                        appDatabase.companyDao.updateCompany(shopInfo.copy(companyLocation = location))
                        userPreferences.saveShopInfo(shopInfo.copy(companyLocation = location).toCompanyEntityJson().toNotNull())
                        userPreferences.saveRepositoryJobSuccessValue(true)
                        userPreferences.saveRepositoryJobMessage("Location changed successfully")
                    }else{
                        userPreferences.saveRepositoryJobSuccessValue(false)
                        userPreferences.saveRepositoryJobMessage( "Couldn't change location")
                    }
                }
                else->{
                    val call = shopManagerDatabaseApi.changeCompanyLocation(uniqueCompanyId,currentPassword, location)
                    call!!.enqueue(object : Callback<CompanyResponseDto>{
                        override fun onResponse(
                            call: Call<CompanyResponseDto>,
                            response: Response<CompanyResponseDto>
                        ) {
                            val locationChangedSuccessfully = response.body()?.data.toCompanyInfoDto() != null
                            GlobalScope.launch(Dispatchers.IO + Job()) {
                                if (locationChangedSuccessfully) {
                                    val responseInfo = response.body()?.data?.toCompanyInfoDto()
                                    val updatedAccountInfo = responseInfo?.toCompanyEntity()
                                    updatedAccountInfo?.let { _companyEntity->
                                        appDatabase.companyDao.deleteAllCompanies()
                                        appDatabase.companyDao.addCompany(_companyEntity)

                                        userPreferences.saveShopInfo(_companyEntity.toCompanyEntityJson().toNotNull())
                                        userPreferences.saveLoggedInState(true)
                                        userPreferences.saveRepositoryJobMessage(response.body()?.message.toNotNull())
                                        userPreferences.saveRepositoryJobSuccessValue(true)
                                    }
                                } else {
                                    userPreferences.saveRepositoryJobSuccessValue(false)
                                    userPreferences.saveRepositoryJobMessage(response.body()?.message ?: UnknownError)
                                }

                            }
                        }
                        override fun onFailure(call: Call<CompanyResponseDto>, t: Throwable) {
                            GlobalScope.launch(Dispatchers.IO + Job()) {
                                userPreferences.saveRepositoryJobSuccessValue(false)
                                userPreferences.saveRepositoryJobMessage(t.message ?: ServerErrorMessage)
                            }
                        }
                    })
                }
            }
        }catch (e: Exception){
            userPreferences.saveRepositoryJobSuccessValue(false)
            userPreferences.saveRepositoryJobMessage("$ServerErrorMessage\n$InternetConnectionMessage")
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    override suspend fun changeProductsAndServices(
        currentPassword: String,
        productsAndServices: String
    ) {
        val context = MyShopManagerApp.applicationContext()
        val userPreferences = UserPreferences(context)
        try {
            val isLoggedIn = userPreferences.getLoggedInState.first()
            userPreferences.saveRepositoryJobMessage(emptyString)
            userPreferences.saveRepositoryJobSuccessValue(false)
            val uniqueCompanyId = userPreferences.getShopInfo.first().toCompanyEntity()?.uniqueCompanyId
            val shopAccountResponse = shopManagerDatabaseApi.getCompany(uniqueCompanyId.toNotNull())
            val onlineCompanyAccountNotExists = shopAccountResponse?.data.toCompanyInfoDto() == null
            val isPrincipalAdmin = userPreferences.getPersonnelInfo.first().toPersonnelEntity()?.isPrincipalAdmin == true
            when(true){
                (isLoggedIn != true)->{
                    userPreferences.saveRepositoryJobSuccessValue(false)
                    userPreferences.saveRepositoryJobMessage("Please ensure that you're logged in to change company products")
                }
                (!isPrincipalAdmin)->{
                    userPreferences.saveRepositoryJobSuccessValue(false)
                    userPreferences.saveRepositoryJobMessage("Only the principal admin has the privileges to make this change")
                }
                (uniqueCompanyId == null)->{
                    userPreferences.saveRepositoryJobSuccessValue(false)
                    userPreferences.saveRepositoryJobMessage("Could not get this account's info\nPlease ensure that you're logged in")
                }
                onlineCompanyAccountNotExists->{
                    val shopInfo = userPreferences.getShopInfo.first().toCompanyEntity()
                    if (shopInfo != null) {
                        appDatabase.companyDao.updateCompany(shopInfo.copy(companyProductsAndServices = productsAndServices))
                        userPreferences.saveShopInfo(shopInfo.copy(companyProductsAndServices = productsAndServices).toCompanyEntityJson().toNotNull())
                        userPreferences.saveRepositoryJobSuccessValue(true)
                        userPreferences.saveRepositoryJobMessage("Company products changed successfully")
                    }else{
                        userPreferences.saveRepositoryJobSuccessValue(false)
                        userPreferences.saveRepositoryJobMessage( "Couldn't change company products")
                    }
                }
                else->{
                    val call = shopManagerDatabaseApi.changeCompanyProductsAndServices(uniqueCompanyId,currentPassword, productsAndServices)
                    call!!.enqueue(object : Callback<CompanyResponseDto>{
                        override fun onResponse(
                            call: Call<CompanyResponseDto>,
                            response: Response<CompanyResponseDto>
                        ) {
                            val productAndServicesChangedSuccessfully = response.body()?.data.toCompanyInfoDto() != null
                            GlobalScope.launch(Dispatchers.IO + Job()) {
                                if (productAndServicesChangedSuccessfully) {
                                    val responseInfo = response.body()?.data?.toCompanyInfoDto()
                                    val updatedAccountInfo = responseInfo?.toCompanyEntity()
                                    updatedAccountInfo?.let { _companyEntity->
                                        appDatabase.companyDao.deleteAllCompanies()
                                        appDatabase.companyDao.addCompany(_companyEntity)

                                        userPreferences.saveShopInfo(_companyEntity.toCompanyEntityJson().toNotNull())
                                        userPreferences.saveLoggedInState(true)
                                        userPreferences.saveRepositoryJobMessage(response.body()?.message.toNotNull())
                                        userPreferences.saveRepositoryJobSuccessValue(true)
                                    }
                                } else {
                                    userPreferences.saveRepositoryJobSuccessValue(false)
                                    userPreferences.saveRepositoryJobMessage(response.body()?.message ?: UnknownError)
                                }

                            }
                        }
                        override fun onFailure(call: Call<CompanyResponseDto>, t: Throwable) {
                            GlobalScope.launch(Dispatchers.IO + Job()) {
                                userPreferences.saveRepositoryJobSuccessValue(false)
                                userPreferences.saveRepositoryJobMessage(t.message ?: ServerErrorMessage)
                            }
                        }
                    })
                }
            }
        }catch (e: Exception){
            userPreferences.saveRepositoryJobSuccessValue(false)
            userPreferences.saveRepositoryJobMessage("$ServerErrorMessage\n$InternetConnectionMessage")
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    override suspend fun changeOtherInfo(currentPassword: String, otherInfo: String) {
        val context = MyShopManagerApp.applicationContext()
        val userPreferences = UserPreferences(context)
        try {
            val isLoggedIn = userPreferences.getLoggedInState.first()
            userPreferences.saveRepositoryJobMessage(emptyString)
            userPreferences.saveRepositoryJobSuccessValue(false)
            val uniqueCompanyId = userPreferences.getShopInfo.first().toCompanyEntity()?.uniqueCompanyId
            val shopAccountResponse = shopManagerDatabaseApi.getCompany(uniqueCompanyId.toNotNull())
            val onlineCompanyAccountNotExists = shopAccountResponse?.data.toCompanyInfoDto() == null
            val isPrincipalAdmin = userPreferences.getPersonnelInfo.first().toPersonnelEntity()?.isPrincipalAdmin == true
            when(true){
                (isLoggedIn != true)->{
                    userPreferences.saveRepositoryJobSuccessValue(false)
                    userPreferences.saveRepositoryJobMessage("Please ensure that you're logged in to change other info")
                }
                (!isPrincipalAdmin)->{
                    userPreferences.saveRepositoryJobSuccessValue(false)
                    userPreferences.saveRepositoryJobMessage("Only the principal admin has the privileges to make this change")
                }
                (uniqueCompanyId == null)->{
                    userPreferences.saveRepositoryJobSuccessValue(false)
                    userPreferences.saveRepositoryJobMessage("Could not get this account's info\nPlease ensure that you're logged in")
                }
                onlineCompanyAccountNotExists->{
                    val shopInfo = userPreferences.getShopInfo.first().toCompanyEntity()
                    if (shopInfo != null) {
                        appDatabase.companyDao.updateCompany(shopInfo.copy(otherInfo = otherInfo))
                        userPreferences.saveShopInfo(shopInfo.copy(otherInfo = otherInfo).toCompanyEntityJson().toNotNull())
                        userPreferences.saveRepositoryJobSuccessValue(true)
                        userPreferences.saveRepositoryJobMessage("Other Info changed successfully")
                    }else{
                        userPreferences.saveRepositoryJobSuccessValue(false)
                        userPreferences.saveRepositoryJobMessage( "Couldn't change other info")
                    }
                }
                else->{
                    val call = shopManagerDatabaseApi.changeCompanyOtherInfo(uniqueCompanyId,currentPassword, otherInfo)
                    call!!.enqueue(object : Callback<CompanyResponseDto>{
                        override fun onResponse(
                            call: Call<CompanyResponseDto>,
                            response: Response<CompanyResponseDto>
                        ) {
                            val otherInfoChangedSuccessfully = response.body()?.data.toCompanyInfoDto() != null
                            GlobalScope.launch(Dispatchers.IO + Job()) {
                                if (otherInfoChangedSuccessfully) {
                                    val responseInfo = response.body()?.data?.toCompanyInfoDto()
                                    val updatedAccountInfo = responseInfo?.toCompanyEntity()
                                    updatedAccountInfo?.let { _companyEntity->
                                        appDatabase.companyDao.deleteAllCompanies()
                                        appDatabase.companyDao.addCompany(_companyEntity)

                                        userPreferences.saveShopInfo(_companyEntity.toCompanyEntityJson().toNotNull())
                                        userPreferences.saveLoggedInState(true)
                                        userPreferences.saveRepositoryJobMessage(response.body()?.message.toNotNull())
                                        userPreferences.saveRepositoryJobSuccessValue(true)
                                    }
                                } else {
                                    userPreferences.saveRepositoryJobSuccessValue(false)
                                    userPreferences.saveRepositoryJobMessage(response.body()?.message ?: UnknownError)
                                }

                            }
                        }
                        override fun onFailure(call: Call<CompanyResponseDto>, t: Throwable) {
                            GlobalScope.launch(Dispatchers.IO + Job()) {
                                userPreferences.saveRepositoryJobSuccessValue(false)
                                userPreferences.saveRepositoryJobMessage(t.message ?: ServerErrorMessage)
                            }
                        }
                    })
                }
            }
        }catch (e: Exception){
            userPreferences.saveRepositoryJobSuccessValue(false)
            userPreferences.saveRepositoryJobMessage("${e.message}\n$ServerErrorMessage\n$InternetConnectionMessage")
        }
    }


    @OptIn(DelicateCoroutinesApi::class)
    override suspend fun registerShopAccount(
        company: CompanyEntity,
        confirmedPassword: String
    ){
        val context = MyShopManagerApp.applicationContext()
        val userPreferences = UserPreferences(context)
        try {
            val isLoggedIn = userPreferences.getLoggedInState.first()
            userPreferences.saveIOExceptionOrHttpExceptionState(false)
            userPreferences.saveExceptionOrErrorMessage(emptyString)
            userPreferences.saveRepositoryJobMessage(emptyString)
            userPreferences.saveRepositoryJobSuccessValue(false)
            if (isLoggedIn == true){
                userPreferences.saveRepositoryJobSuccessValue(false)
                userPreferences.saveRepositoryJobMessage("You are already logged in")
            }
            else {
                val invalidParameters = company.companyName.isBlank() || company.email.isBlank() || company.password.isBlank()
                val passwordIsNotValid = !passwordIsValid(company.password, company.companyName, company.email)
                when(true){
                    (company.password != confirmedPassword)->{
                        userPreferences.saveRepositoryJobSuccessValue(false)
                        userPreferences.saveRepositoryJobMessage("Passwords do not match")
                    }
                    (invalidParameters)->{
                        userPreferences.saveRepositoryJobSuccessValue(false)
                        userPreferences.saveRepositoryJobMessage("Please make sure that all required fields are filled appropriately")
                    }
                    passwordIsNotValid ->{
                        userPreferences.saveRepositoryJobSuccessValue(false)
                        userPreferences.saveRepositoryJobMessage("Password is not valid.\nPlease make sure that the password does not contain email or company name and password length is more than 7 characters")
                    }
                    else->{
                        val call = shopManagerDatabaseApi.addCompany(company.toCompanyDto())
                        call!!.enqueue(object : Callback<AddCompanyResponse> {
                            override fun onResponse(
                                call: Call<AddCompanyResponse>,
                                response: Response<AddCompanyResponse>
                            ) {
                                val isRegisteredSuccessfully = response.body()?.data.toCompanyInfoDto() != null
                                GlobalScope.launch(Dispatchers.IO + Job()) {
                                    if (isRegisteredSuccessfully) {
                                        val responseInfo = response.body()?.data?.toCompanyInfoDto()
                                        val registeredAccount = responseInfo!!.toCompanyEntity()
                                        appDatabase.companyDao.deleteAllCompanies()
                                        appDatabase.companyDao.addCompany(registeredAccount)
                                        userPreferences.saveShopInfo(registeredAccount.toCompanyEntityJson().toNotNull())
                                        userPreferences.saveLoggedInState(true)
                                        userPreferences.saveRepositoryJobMessage(response.body()?.message.toNotNull())
                                        userPreferences.saveRepositoryJobSuccessValue(true)
                                        
                                    }
                                    else {
                                        userPreferences.saveRepositoryJobSuccessValue(false)
                                        userPreferences.saveRepositoryJobMessage(response.body()?.message.toNotNull())

                                    }
                                }
                            }
                            override fun onFailure(
                                call: Call<AddCompanyResponse>,
                                t: Throwable
                            ) {
                                GlobalScope.launch(Dispatchers.IO + Job()) {
                                    userPreferences.saveRepositoryJobSuccessValue(false)
                                    userPreferences.saveRepositoryJobMessage(t.message.toNotNull())
                                    try {
                                        userPreferences.saveIOExceptionOrHttpExceptionState(true)
                                        userPreferences.saveExceptionOrErrorMessage(t.message ?: ServerErrorMessage)

                                        if (isLoggedIn == true) {
                                            userPreferences.saveRepositoryJobSuccessValue(false)
                                            userPreferences.saveRepositoryJobMessage("You are already logged in")
                                        }
                                        else {
                                            when (true) {
                                                (company.password != confirmedPassword) -> {
                                                    userPreferences.saveRepositoryJobSuccessValue(false)
                                                    userPreferences.saveRepositoryJobMessage("Passwords do not match")
                                                }
                                                (invalidParameters) -> {
                                                    userPreferences.saveRepositoryJobSuccessValue(false)
                                                    userPreferences.saveRepositoryJobMessage("Please make sure that all required fields are filled appropriately")
                                                }
                                                else -> {
                                                    val uniqueCompanyId = generateUniqueCompanyId(company.companyName)
                                                    val saltedHash = hashService.generateSaltedHash(confirmedPassword)
                                                    val salt = saltedHash.salt
                                                    val hashedPassword = saltedHash.hash
                                                    val newCompany = company.copy(uniqueCompanyId = uniqueCompanyId, password = hashedPassword, salt = salt)
                                                    appDatabase.companyDao.addCompany(newCompany)
                                                    userPreferences.saveLoggedInState(true)
                                                    userPreferences.saveRepositoryJobSuccessValue(true)
                                                    userPreferences.saveRepositoryJobMessage("Company registered successfully\nNB: Your company has only been registered locally on this device")
                                                    newCompany.toCompanyEntityJson()?.let { userPreferences.saveShopInfo(it) }
                                                }
                                            }
                                        }
                                    }
                                    catch (e: Exception){
                                        userPreferences.saveRepositoryJobSuccessValue(false)
                                        userPreferences.saveRepositoryJobMessage(e.message ?: UnknownError)
                                    }
                                }
                            }
                        })
                    }
                }
            }
        }
        catch (e:Exception){
            userPreferences.saveRepositoryJobSuccessValue(false)
            userPreferences.saveRepositoryJobMessage(e.message ?: UnknownError)
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    override suspend fun registerLocalAccountOnline(company: CompanyEntity) {
        val context = MyShopManagerApp.applicationContext()
        val userPreferences = UserPreferences(context)
        try {
            userPreferences.saveRepositoryJobMessage(emptyString)
            val isLoggedIn = userPreferences.getLoggedInState.first() == true
            val uniqueCompanyId = userPreferences.getShopInfo.first().toCompanyEntity()?.uniqueCompanyId
            val shopAccountResponse = shopManagerDatabaseApi.getCompany(uniqueCompanyId.toNotNull())
            val onlineCompanyAccountNotExists = shopAccountResponse?.data.toCompanyInfoDto() == null

            when(true) {
                !isLoggedIn -> {
                    userPreferences.saveRepositoryJobSuccessValue(false)
                    userPreferences.saveRepositoryJobMessage("You need to log in to perform this operation")
                }
                (onlineCompanyAccountNotExists) -> {
                    val call = shopManagerDatabaseApi.createOnlineCompany(company.toCompanyDto())
                    call!!.enqueue(object : Callback<AddCompanyResponse> {
                        override fun onResponse(
                            call: Call<AddCompanyResponse>,
                            response: Response<AddCompanyResponse>
                        ) {
                            val isRegisteredSuccessfully = response.body()?.data.toCompanyInfoDto() != null
                            GlobalScope.launch(Dispatchers.IO + Job()) {
                                if (isRegisteredSuccessfully) {
                                    val responseInfo = response.body()?.data?.toCompanyInfoDto()
                                    val registeredAccountInfo =
                                        responseInfo?.toCompanyEntity()?.toCompanyEntityJson()
                                    registeredAccountInfo?.let {
                                        appDatabase.companyDao.deleteAllCompanies()
                                        appDatabase.companyDao.addCompany(it.toCompanyEntity()!!)
                                        userPreferences.saveShopInfo(it)
                                        userPreferences.saveLoggedInState(true)
                                        userPreferences.saveRepositoryJobMessage(response.body()?.message.toNotNull())

                                        userPreferences.saveRepositoryJobSuccessValue(true)
                                        userPreferences.saveRepositoryJobMessage("Account created successfully")
                                    }
                                } else {
                                    userPreferences.saveRepositoryJobSuccessValue(false)
                                    userPreferences.saveRepositoryJobMessage(response.body()?.message.toNotNull())

                                }
                            }
                        }

                        override fun onFailure(
                            call: Call<AddCompanyResponse>,
                            t: Throwable
                        ) {
                            GlobalScope.launch(Dispatchers.IO + Job()) {
                                userPreferences.saveRepositoryJobSuccessValue(false)
                                userPreferences.saveRepositoryJobMessage(t.message.toNotNull())
                            }
                        }
                    })
                }
                else -> {
                    userPreferences.saveRepositoryJobSuccessValue(false)
                    userPreferences.saveRepositoryJobMessage("Account already exists")
                }
            }
        }catch (e: Exception){
            userPreferences.saveRepositoryJobSuccessValue(false)
            userPreferences.saveRepositoryJobMessage(ServerErrorMessage)
        }
    }

    override suspend fun login(email: String, password: String): Flow<Resource<String>> = flow{
        val context = MyShopManagerApp.applicationContext()
        val userPreferences = UserPreferences(context)
        try {
            emit(Resource.Loading())
            val thisEmail = email.trim()
            val invalidParameters = thisEmail.isBlank() || password.isBlank()
            val loginResult = shopManagerDatabaseApi.login(thisEmail, password)
            when(true){
                invalidParameters-> {
                    emit(Resource.Error(
                        data = "Failed to log in",
                        message = "email or password field is empty")
                    )
                }
                (loginResult == null)-> {
                    emit(Resource.Error(
                        data = "Failed to log in",
                        message = "Could not load account from the server"
                    ))
                }
                else ->{
                    val shopInfo = loginResult.data
                    val loginStatus = loginResult.success
                    val loginMessage = loginResult.message

                    if (loginStatus != true){
                        emit(Resource.Error(
                            data = "Failed to log in",
                            message = loginMessage.toNotNull()
                        ))
                    }else if (shopInfo == null){
                        emit(Resource.Error(
                            data = "Failed to log in",
                            message = loginMessage.toNotNull()
                        ))
                    }else{
                        shopInfo.toCompanyInfoDto()?.toCompanyEntity()?.toCompanyEntityJson()?.let {
                            appDatabase.companyDao.deleteAllCompanies()
                            appDatabase.companyDao.addCompany(it.toCompanyEntity()!!)
                            userPreferences.saveShopInfo(it)
                            userPreferences.saveLoggedInState(true)
                            emit(Resource.Success(
                                data = "You have been successfully logged in",
                            ))
                            syncCompanyInfo()
                        }
                    }
                }
            }
        }
        catch (e: IOException){
            emit(Resource.Error(
                data = "Could not log in to cloud account. Trying to log in to local account",
                message = e.message
            ))
            try {
                val thisEmail = email.trim()
                val invalidParameters = thisEmail.isBlank() || password.isBlank()
                val company = appDatabase.companyDao.getAllCompanies()?.firstOrNull()
                when(true) {
                    invalidParameters -> {
                        emit(
                            Resource.Error(
                                data = "Failed to log in",
                                message = "email or password field is empty"
                            )
                        )
                    }
                    (company == null) -> {
                        emit(
                            Resource.Error(
                                data = "Failed to log in",
                                message = "Could not fetch account from local device"
                            )
                        )
                    }
                    else -> {
                        val emailMatches = company.email.trim() == thisEmail
                        val passwordMatches = company.password.trim() == password
                        if (emailMatches && passwordMatches) {
                            UserPreferences(MyShopManagerApp.applicationContext()).saveShopInfo(
                                company.toCompanyEntityJson().toNotNull()
                            )
                            UserPreferences(MyShopManagerApp.applicationContext()).saveLoggedInState(true)
                            emit(
                                Resource.Success(
                                    data = "You have been successfully logged in",
                                )
                            )

                        } else {
                            emit(
                                Resource.Error(
                                    data = "Failed to log in",
                                    message = "Email or password do not match"
                                )
                            )
                        }
                    }
                }
            }catch (e: Exception){
                emit(Resource.Error(
                    data = "Failed to log in",
                    message = e.message ?: "Unknown Error"
                ))
            }
        }
        catch (e: Exception){
            emit(Resource.Error(
                data = "Failed to log in",
                message = e.message
            ))
        }
    }

    override suspend fun companyLogin(email: String, password: String) {
        try {
            val context = MyShopManagerApp.applicationContext()
            val userPreferences = UserPreferences(context)

            userPreferences.saveRepositoryJobMessage(emptyString)
            userPreferences.saveRepositoryJobSuccessValue(false)

            val thisEmail = email.trim()
            val invalidParameters = thisEmail.isBlank() || password.isBlank()
            val loginResult = shopManagerDatabaseApi.login(thisEmail, password)
            when(true){
                invalidParameters-> {
                    userPreferences.saveRepositoryJobMessage("Failed to log in\nEmail or password field is empty")
                    userPreferences.saveRepositoryJobSuccessValue(false)

                }
                (loginResult == null)-> {
                    userPreferences.saveRepositoryJobMessage("Failed to log in\nCould not load account from the server")
                    userPreferences.saveRepositoryJobSuccessValue(false)
                }
                else ->{
                    val shopInfo = loginResult.data
                    val loginStatus = loginResult.success
                    val loginMessage = loginResult.message

                    if (loginStatus != true){
                        userPreferences.saveRepositoryJobMessage("Failed to log in\n${loginMessage.toNotNull()}")
                        userPreferences.saveRepositoryJobSuccessValue(false)
                    }
                    else if (shopInfo == null){
                        userPreferences.saveRepositoryJobMessage("Failed to log in\n${loginMessage.toNotNull()}")
                        userPreferences.saveRepositoryJobSuccessValue(false)
                    }
                    else{
                        val isLoggedIn = userPreferences.getLoggedInState.first()
                        val thisShop = userPreferences.getShopInfo.first()
                        Log.d("CompanyRepository", "1st is logged in = $isLoggedIn")
                        Log.d("CompanyRepository", "1st Shop info json = $thisShop")

                        shopInfo.toCompanyInfoDto()?.toCompanyEntity()?.toCompanyEntityJson()?.let {
                            userPreferences.saveShopInfo(it)
                            userPreferences.saveLoggedInState(true)

                            syncCompanyInfo()

                            val isLoggedIn1 = userPreferences.getLoggedInState.first()
                            val thisShop1 = userPreferences.getShopInfo.first()
                            Log.d("CompanyRepository", "2nd is logged in = $isLoggedIn1")
                            Log.d("CompanyRepository", "2nd Shop info json = $thisShop1")
                        }
                    }
                }
            }
        }
        catch (e: IOException){
            val context = MyShopManagerApp.applicationContext()
            val userPreferences = UserPreferences(context)

            userPreferences.saveRepositoryJobMessage("Could not log in to cloud account.\nTrying to log in to local account\n${e.message ?: UnknownError}")
            userPreferences.saveRepositoryJobSuccessValue(false)
            try {
                val listOfCompanyEntitiesInfo = userPreferences.getListOfShopLogInInfo.first().toListOfCompanyEntities()
                val thisEmail = email.trim()
                val invalidParameters = thisEmail.isBlank() || password.isBlank()
                val companyEntityInfo = listOfCompanyEntitiesInfo.firstOrNull{(it.email.trim() == thisEmail) && (it.password.trim() == password)}
                when(true) {
                    invalidParameters -> {
                        userPreferences.saveRepositoryJobMessage("Failed to log in.\nEmail or password field is empty")
                        userPreferences.saveRepositoryJobSuccessValue(false)
                    }
                    (companyEntityInfo == null) -> {
                        userPreferences.saveRepositoryJobMessage("Failed to log in.\nEmail and password do not match any account created locally on this device")
                        userPreferences.saveRepositoryJobSuccessValue(false)
                    }
                    else -> {
                        UserPreferences(MyShopManagerApp.applicationContext()).saveShopInfo(
                            companyEntityInfo.toCompanyEntityJson().toNotNull()
                        )
                        restoreDatabase(context, companyEntityInfo.companyName, userPreferences)
                        UserPreferences(MyShopManagerApp.applicationContext()).saveLoggedInState(true)
                        userPreferences.saveRepositoryJobMessage("You have been successfully logged in" +
                                "\nClick OK to complete the restoration of backed up database. \nNB: The app will be restarted. If for some reason it doesn't restart, open the app again")
                        userPreferences.saveRepositoryJobSuccessValue(true)

                    }
                }
            }
            catch (e: Exception){
                userPreferences.saveRepositoryJobMessage("Failed to log in.\n${e.message ?: "Unknown Error"}")
                userPreferences.saveRepositoryJobSuccessValue(false)
            }
        }
        catch (e: HttpException){
            val context = MyShopManagerApp.applicationContext()
            val userPreferences = UserPreferences(context)

            userPreferences.saveRepositoryJobMessage("Could not log in to cloud account.\nTrying to log in to local account\n${e.message ?: UnknownError}")
            userPreferences.saveRepositoryJobSuccessValue(false)
            try {
                val listOfCompanyEntitiesInfo = userPreferences.getListOfShopLogInInfo.first().toListOfCompanyEntities()
                val thisEmail = email.trim()
                val invalidParameters = thisEmail.isBlank() || password.isBlank()
                val companyEntityInfo = listOfCompanyEntitiesInfo.firstOrNull{(it.email.trim() == thisEmail) && (it.password.trim() == password)}
                when(true) {
                    invalidParameters -> {
                        userPreferences.saveRepositoryJobMessage("Failed to log in.\nEmail or password field is empty")
                        userPreferences.saveRepositoryJobSuccessValue(false)
                    }
                    (companyEntityInfo == null) -> {
                        userPreferences.saveRepositoryJobMessage("Failed to log in.\nEmail and password do not match any account created locally on this device")
                        userPreferences.saveRepositoryJobSuccessValue(false)
                    }
                    else -> {
                        UserPreferences(MyShopManagerApp.applicationContext()).saveShopInfo(
                            companyEntityInfo.toCompanyEntityJson().toNotNull()
                        )
                        restoreDatabase(context, companyEntityInfo.companyName, userPreferences)
                        UserPreferences(MyShopManagerApp.applicationContext()).saveLoggedInState(true)
                        userPreferences.saveRepositoryJobMessage("You have been successfully logged in" +
                                "\nClick OK to complete the restoration of backed up database. \nNB: The app will be restarted. If for some reason it doesn't restart, open the app again")
                        userPreferences.saveRepositoryJobSuccessValue(true)


                        /*
                        val i = context.packageManager.getLaunchIntentForPackage(context.packageName)
                        i!!.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        context.startActivity(i)
                        exitProcess(0)
                        */

                    }
                }
            }
            catch (e: Exception){
                userPreferences.saveRepositoryJobMessage("Failed to log in.\n${e.message ?: "Unknown Error"}")
                userPreferences.saveRepositoryJobSuccessValue(false)
            }
        }
        catch (e: Exception){
            UserPreferences(MyShopManagerApp.applicationContext()).saveRepositoryJobMessage("Failed to log in.\n${e.message ?: "Unknown Error"}")
            UserPreferences(MyShopManagerApp.applicationContext()).saveRepositoryJobSuccessValue(false)
        }
    }

    override suspend fun logout(): Flow<Resource<String>> = flow{
        try {
            val context = MyShopManagerApp.applicationContext()
            val userPreferences = UserPreferences(context)
            val companyName = userPreferences.getShopInfo.first().toCompanyEntity()?.companyName ?: ShopAppDatabase
            backupDatabase(context, companyName, userPreferences)
            userPreferences.saveLoggedInState(false)
            userPreferences.saveShopInfo(emptyString)
            userPreferences.savePersonnelLoggedInState(false)
            userPreferences.savePersonnelInfo(emptyString)
            appDatabase.companyDao.deleteAllTables()
            emit(Resource.Success(
                data = "Log out successful"
            ))
        }catch (e: Exception){
            emit(Resource.Error(
                data = "Failed to log out",
                message = e.message.toNotNull()
            ))
        }
    }

    override suspend fun companyLogout(){
        try {
            val context = MyShopManagerApp.applicationContext()
            val userPreferences = UserPreferences(context)
            val company = userPreferences.getShopInfo.first().toCompanyEntity()
            val companyName = userPreferences.getShopInfo.first().toCompanyEntity()?.companyName ?: ShopAppDatabase
            val listOfShopLoginInfo = userPreferences.getListOfShopLogInInfo.first().toListOfCompanyEntities()
            val newListOfShopLoginInfo = listOfShopLoginInfo.plus(company).filterNotNull()
            backupDatabase(context, companyName, userPreferences)
            userPreferences.saveLoggedInState(false)
            userPreferences.saveShopInfo(emptyString)
            userPreferences.savePersonnelLoggedInState(false)
            userPreferences.savePersonnelInfo(emptyString)
            userPreferences.saveListOfShopLogInInfo(newListOfShopLoginInfo.toListOfCompanyEntitiesJson())
            appDatabase.companyDao.deleteAllTables()
            userPreferences.saveRepositoryJobMessage("Log out successful")
            userPreferences.saveRepositoryJobSuccessValue(true)
        }catch (e: Exception){
            UserPreferences(MyShopManagerApp.applicationContext()).saveRepositoryJobMessage("Could not log out.\n${e.message ?: "Unknown Error"}")
            UserPreferences(MyShopManagerApp.applicationContext()).saveRepositoryJobSuccessValue(false)
        }
    }

    override suspend fun addCompanies(companies: CompanyEntities) {
        appDatabase.companyDao.addCompanies(companies)
    }

    override suspend fun getCompany(uniqueCompanyId: String): CompanyEntity? {
        return appDatabase.companyDao.getCompany(uniqueCompanyId)
    }

    override suspend fun getCompanyByName(companyName: String): CompanyEntities? {
        return appDatabase.companyDao.getCompanyByName(companyName)
    }

    override suspend fun updateCompany(company: CompanyEntity) {
        appDatabase.companyDao.updateCompany(company)
    }

    override suspend fun deleteCompany(companyId: Int) {
        appDatabase.companyDao.deleteCompany(companyId)
    }

    override suspend fun deleteCompany(uniqueCompanyId: String) {
        appDatabase.companyDao.deleteCompany(uniqueCompanyId)
    }

    override suspend fun deleteAllCompanies() {
        appDatabase.companyDao.deleteAllCompanies()
    }

    private suspend fun syncCompanyInfo(){
        try {
            val context = MyShopManagerApp.applicationContext()
            val userPreferences = UserPreferences(context)

            userPreferences.saveRepositoryJobMessage(emptyString)
            userPreferences.saveRepositoryJobSuccessValue(false)

            val isLoggedIn = userPreferences.getLoggedInState.first()
            val shopInfoJson = userPreferences.getShopInfo.first()
            val uniqueCompanyId = shopInfoJson.toCompanyEntity()?.uniqueCompanyId
            if (isLoggedIn == true) {
                if (uniqueCompanyId == null){
                    userPreferences.saveRepositoryJobMessage("Could not get the shop account details\nPlease ensure that you are logged in")
                    userPreferences.saveRepositoryJobSuccessValue(false)
                }else {
                    val remoteCustomers = shopManagerDatabaseApi.fetchAllCompanyCustomers(uniqueCompanyId)?.data ?: emptyList()
                    //appDatabase.customerDao.addCustomers(remoteCustomers.map { it.toCustomerEntity() })
                    customerRepository.addCustomers(remoteCustomers.map { it.toCustomerEntity() })

                    val remoteSuppliers =
                        shopManagerDatabaseApi.fetchAllCompanySuppliers(uniqueCompanyId)?.data
                            ?: emptyList()
                    //appDatabase.supplierDao.addSuppliers(remoteSuppliers.map { it.toSupplierEntity() })
                    supplierRepository.addSuppliers(remoteSuppliers.map { it.toSupplierEntity() })

                    val remoteDebts =
                        shopManagerDatabaseApi.fetchAllCompanyDebts(uniqueCompanyId)?.data
                            ?: emptyList()
                    //appDatabase.debtDao.addDebts(remoteDebts.map { it.toDebtEntity() })
                    debtRepository.addDebts(remoteDebts.map { it.toDebtEntity() })

                    val remoteCashIns =
                        shopManagerDatabaseApi.fetchAllCompanyCashIns(uniqueCompanyId)?.data
                            ?: emptyList()
                    //appDatabase.cashInDao.addCashIns(remoteCashIns.map { it.toCashInEntity() })
                    cashInRepository.addCashIns(remoteCashIns.map { it.toCashInEntity() })

                    val remoteDebtRepayments =
                        shopManagerDatabaseApi.fetchAllCompanyDebtRepayments(uniqueCompanyId)?.data
                            ?: emptyList()
                    //appDatabase.debtRepaymentDao.addDebtRepayments(remoteDebtRepayments.map { it.toDebtRepaymentEntity() })
                    debtRepaymentRepository.addDebtRepayments(remoteDebtRepayments.map { it.toDebtRepaymentEntity() })

                    val remoteInventories =
                        shopManagerDatabaseApi.fetchAllCompanyInventories(uniqueCompanyId)?.data
                            ?: emptyList()
                    //appDatabase.inventoryDao.addInventories(remoteInventories.map { it.toInventoryEntity() })
                    inventoryRepository.addInventories(remoteInventories.map { it.toInventoryEntity() })

                    val remoteInventoryItems =
                        shopManagerDatabaseApi.fetchAllCompanyInventoryItems(uniqueCompanyId)?.data
                            ?: emptyList()
                    //appDatabase.inventoryItemDao.addInventoryItems(remoteInventoryItems.map { it.toInventoryItemEntity() })
                    inventoryItemRepository.addInventoryItems(remoteInventoryItems.map { it.toInventoryItemEntity() })

                    val remoteRevenues =
                        shopManagerDatabaseApi.fetchAllCompanyRevenues(uniqueCompanyId)?.data
                            ?: emptyList()
                    appDatabase.revenueDao.addRevenues(remoteRevenues.map { it.toRevenueEntity() })
                    revenueRepository.addRevenues(remoteRevenues.map { it.toRevenueEntity() })

                    val remoteExpenses =
                        shopManagerDatabaseApi.fetchAllCompanyExpenses(uniqueCompanyId)?.data
                            ?: emptyList()
                    //appDatabase.expenseDao.addExpenses(remoteExpenses.map { it.toExpenseEntity() })
                    expenseRepository.addExpenses(remoteExpenses.map { it.toExpenseEntity() })

                    val remoteStocks =
                        shopManagerDatabaseApi.fetchAllCompanyStocks(uniqueCompanyId)?.data
                            ?: emptyList()
                    //appDatabase.stockDao.addStocks(remoteStocks.map { it.toStockEntity() })
                    stockRepository.addStocks(remoteStocks.map { it.toStockEntity() })

                    val remotePersonnel =
                        shopManagerDatabaseApi.fetchAllCompanyPersonnel(uniqueCompanyId)?.data
                            ?: emptyList()
                    //appDatabase.personnelDao.addPersonnel(remotePersonnel.map { it.toPersonnelEntity() })
                    personnelRepository.addPersonnel(remotePersonnel.map { it.toPersonnelEntity() })

                    val remoteSavings =
                        shopManagerDatabaseApi.fetchAllCompanySavings(uniqueCompanyId)?.data
                            ?: emptyList()
                    //appDatabase.savingsDao.addSavings(remoteSavings.map { it.toSavingsEntity() })
                    savingsRepository.addSavings(remoteSavings.map { it.toSavingsEntity() })

                    val remoteWithdrawals =
                        shopManagerDatabaseApi.fetchAllCompanyWithdrawals(uniqueCompanyId)?.data
                            ?: emptyList()
                    //appDatabase.withdrawalDao.addWithdrawals(remoteWithdrawals.map { it.toWithdrawalEntity() })
                    withdrawalRepository.addWithdrawals(remoteWithdrawals.map { it.toWithdrawalEntity() })

                    val remoteInventoryStocks =
                        shopManagerDatabaseApi.fetchAllCompanyInventoryStocks(uniqueCompanyId)?.data
                            ?: emptyList()
                    appDatabase.inventoryStockDao.addInventoryStock(remoteInventoryStocks.map { it.toInventoryStock() })

                    val remoteBanks =
                        shopManagerDatabaseApi.fetchAllCompanyBanks(uniqueCompanyId)?.data
                            ?: emptyList()
                    //appDatabase.bankAccountDao.addBankAccounts(remoteBanks.map { it.toBankEntity() })
                    bankAccountRepository.addBankAccounts(remoteBanks.map { it.toBankEntity() })

                    userPreferences.saveRepositoryJobMessage("Data successfully loaded and synced")
                    userPreferences.saveRepositoryJobSuccessValue(true)
                }
            }else{
                userPreferences.saveRepositoryJobMessage("Could not sync data\nYou are not logged in into any account")
                userPreferences.saveRepositoryJobSuccessValue(false)
            }

        }catch (e: Exception){
            UserPreferences(MyShopManagerApp.applicationContext()).saveRepositoryJobMessage("Could not sync data\n${e.message ?: UnknownError}")
            UserPreferences(MyShopManagerApp.applicationContext()).saveRepositoryJobSuccessValue(false)
        }
    }

    private suspend fun backupDatabase(context: Context, companyName: String, userPreferences: UserPreferences){
        try {
            val databaseFile = context.getDatabasePath(ShopAppDatabase)
            val databaseWALFile = File(databaseFile.path + Constants.SQLITE_WALFILE_SUFFIX)
            val databaseSHMFile = File(databaseFile.path + Constants.SQLITE_SHMFILE_SUFFIX)
            val backupFile = File(databaseFile.path + companyName + Constants.THEDATABASE_DATABASE_BACKUP_SUFFIX)
            val backupWALFile = File(backupFile.path + Constants.SQLITE_WALFILE_SUFFIX)
            val backupSHMFile = File(backupFile.path + Constants.SQLITE_SHMFILE_SUFFIX)
            if (backupFile.exists()) backupFile.delete()
            if (backupWALFile.exists()) {backupWALFile.delete()}
            if (backupSHMFile.exists()) {backupSHMFile.delete()}
            checkpoint()
            databaseFile.copyTo(backupFile,true)
            if (databaseWALFile.exists()) {databaseWALFile.copyTo(backupWALFile,true)}
            if (databaseSHMFile.exists()) {databaseSHMFile.copyTo(backupSHMFile, true)}
            userPreferences.saveRepositoryJobMessage("Company data successfully backed up")
        } catch (e: IOException) {
            userPreferences.saveRepositoryJobMessage("Could not retrieve data.\n${e.message ?: "Unknown Error"}")
            userPreferences.saveRepositoryJobSuccessValue(false)
        }
    }

    private suspend fun restoreDatabase(context: Context, companyName: String, userPreferences: UserPreferences){
        try {
            if(!File(context.getDatabasePath(companyName).path + companyName  + Constants.THEDATABASE_DATABASE_BACKUP_SUFFIX).exists()) {
                userPreferences.saveRepositoryJobMessage("Could not retrieve data.\nCould not find any backed up data file")
                userPreferences.saveRepositoryJobSuccessValue(false)
            }

            val databasePath = appDatabase.openHelper.readableDatabase.path.toNotNull()
            val databaseFile = File(databasePath)
            val databaseWALFile = File(databaseFile.path + Constants.SQLITE_WALFILE_SUFFIX)
            val databaseSHMFile = File(databaseFile.path + Constants.SQLITE_SHMFILE_SUFFIX)
            val backupFile = File(databaseFile.path + companyName  + Constants.THEDATABASE_DATABASE_BACKUP_SUFFIX)
            val backupWALFile = File(backupFile.path + Constants.SQLITE_WALFILE_SUFFIX)
            val backupSHMFile = File(backupFile.path + Constants.SQLITE_SHMFILE_SUFFIX)

            backupFile.copyTo(databaseFile, true)
            if (backupWALFile.exists()) backupWALFile.copyTo(databaseWALFile, true)
            if (backupSHMFile.exists()) backupSHMFile.copyTo(databaseSHMFile,true)
            checkpoint()
            userPreferences.saveRepositoryJobMessage("Data successfully restored")
            userPreferences.saveRepositoryJobSuccessValue(true)

        } catch (e: IOException) {
            userPreferences.saveRepositoryJobMessage("Could not retrieve data.\n${e.message ?: "Unknown Error"}")
            userPreferences.saveRepositoryJobSuccessValue(false)
        }
    }

    private fun checkpoint() {
        val db = appDatabase.openHelper.writableDatabase
        db.query("PRAGMA wal_checkpoint(FULL);", emptyArray())
        db.query("PRAGMA wal_checkpoint(TRUNCATE);", emptyArray())
    }

    private fun passwordIsValid(password: String, companyName: String, email: String): Boolean {
        val containsCompanyName = password.contains(companyName.take(6), true)
        val containsEmail = password.contains(email.take(6), true)
        val shortPassword = password.length < 7
        return (containsCompanyName.not() && containsEmail.not() && shortPassword.not())
    }


    override fun restartApp() {
        val context = MyShopManagerApp.applicationContext()
        val packageManager: PackageManager = context.packageManager
        val intent: Intent = packageManager.getLaunchIntentForPackage(context.packageName)!!
        val componentName: ComponentName = intent.component!!
        val restartIntent: Intent = Intent.makeRestartActivityTask(componentName)
        context.startActivity(restartIntent)
        Runtime.getRuntime().exit(0)
    }

}