package com.example.myshopmanagerapp.feature_app.data.repository

import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.myshopmanagerapp.core.CompanyEntities
import com.example.myshopmanagerapp.core.Constants
import com.example.myshopmanagerapp.core.Constants.ShopAppDatabase
import com.example.myshopmanagerapp.core.Constants.emptyString
import com.example.myshopmanagerapp.core.Functions.generateUniqueCompanyId
import com.example.myshopmanagerapp.core.Functions.toCompanyEntity
import com.example.myshopmanagerapp.core.Functions.toCompanyEntityJson
import com.example.myshopmanagerapp.core.Functions.toCompanyInfoDto
import com.example.myshopmanagerapp.core.Functions.toNotNull
import com.example.myshopmanagerapp.core.Resource
import com.example.myshopmanagerapp.core.TypeConverters.toPersonnelEntities
import com.example.myshopmanagerapp.core.UserPreferences
import com.example.myshopmanagerapp.feature_app.MyShopManagerApp
import com.example.myshopmanagerapp.feature_app.data.local.AppDatabase
import com.example.myshopmanagerapp.feature_app.data.local.entities.company.CompanyDao
import com.example.myshopmanagerapp.feature_app.data.local.entities.company.CompanyEntity
import com.example.myshopmanagerapp.feature_app.data.remote.ShopManagerDatabaseApi
import com.example.myshopmanagerapp.feature_app.data.remote.dto.company.CompanyResponseDto
import com.example.myshopmanagerapp.feature_app.domain.model.AddCompanyResponse
import com.example.myshopmanagerapp.feature_app.domain.repository.CompanyRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import java.io.File
import java.io.IOException
import kotlin.system.exitProcess


class CompanyRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val shopManagerDatabaseApi: ShopManagerDatabaseApi,
    private val companyDao: CompanyDao,
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

            when(true){
                (isLoggedIn != true)->{
                    userPreferences.saveRepositoryJobSuccessValue(false)
                    userPreferences.saveRepositoryJobMessage("Please ensure that you're logged in to change password")
                }
                (newPassword != confirmedPassword)->{
                    userPreferences.saveRepositoryJobSuccessValue(false)
                    userPreferences.saveRepositoryJobMessage("Passwords do not match")
                }
                (uniqueCompanyId == null)->{
                    userPreferences.saveRepositoryJobSuccessValue(false)
                    userPreferences.saveRepositoryJobMessage("Could not get this account's info\nPlease ensure that you're logged in")
                }
                else->{
                    Log.d("CompanyRepository", "Api call enqueue is called")
                    Log.d("CompanyRepository", "uniqueCompanyId = $uniqueCompanyId")
                    Log.d("CompanyRepository", "currentPassword = $currentPassword")
                    Log.d("CompanyRepository", "newPassword = $newPassword")
                    val call = shopManagerDatabaseApi.changePassword(uniqueCompanyId,currentPassword, newPassword)
                    call!!.enqueue(object : Callback<CompanyResponseDto>{
                        override fun onResponse(
                            call: Call<CompanyResponseDto>,
                            response: Response<CompanyResponseDto>
                        ) {
                            val passwordChangedSuccessfully = response.body()?.data != null
                            GlobalScope.launch(Dispatchers.IO + Job()) {
                                if (passwordChangedSuccessfully) {
                                    val responseInfo = response.body()?.data?.toCompanyInfoDto()
                                    val registeredAccountInfo = responseInfo?.toCompanyEntity()?.toCompanyEntityJson()
                                    registeredAccountInfo?.let {
                                        it.toCompanyEntity()?.let { companyEntity ->
                                            companyDao.deleteAllCompanies()
                                            companyDao.addCompany(companyEntity)
                                        }
                                        userPreferences.saveShopInfo(it)
                                        userPreferences.saveLoggedInState(true)
                                        userPreferences.saveRepositoryJobMessage(response.body()?.message.toNotNull())

                                        userPreferences.saveRepositoryJobSuccessValue(true)
                                        userPreferences.saveRepositoryJobMessage("Password changed successfully")
                                    }
                                } else {
                                    userPreferences.saveRepositoryJobSuccessValue(false)
                                    userPreferences.saveRepositoryJobMessage(response.body()?.message ?: "Unable to change password")
                                }

                            }
                        }
                        override fun onFailure(call: Call<CompanyResponseDto>, t: Throwable) {
                            GlobalScope.launch(Dispatchers.IO + Job()) {
                                userPreferences.saveRepositoryJobSuccessValue(false)
                                userPreferences.saveRepositoryJobMessage(t.message ?: "Couldn't connect to server")
                                val shopInfo = userPreferences.getShopInfo.first().toCompanyEntity()
                                if (shopInfo != null) {
                                    companyDao.updateCompany(shopInfo.copy(password = newPassword))
                                    userPreferences.saveRepositoryJobSuccessValue(true)
                                    userPreferences.saveRepositoryJobMessage("Password changed successfully")
                                }else{
                                    userPreferences.saveRepositoryJobSuccessValue(false)
                                    userPreferences.saveRepositoryJobMessage( "Couldn't change password")
                                }
                            }
                        }
                    })
                }
            }
        }catch (e: Exception){
            Log.d(
                "CompanyRepository",
                "General exception is called"
            )
            userPreferences.saveRepositoryJobSuccessValue(false)
            userPreferences.saveRepositoryJobMessage(e.message ?: "Unknown error!")
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
            userPreferences.saveIO_ExceptionOrHttpExceptionMessage(emptyString)
            userPreferences.saveRepositoryJobMessage(emptyString)
            userPreferences.saveRepositoryJobSuccessValue(false)
            if (isLoggedIn == true){
                userPreferences.saveRepositoryJobSuccessValue(false)
                userPreferences.saveRepositoryJobMessage("You are already logged in")
            }
            else {
                val invalidParameters = company.companyName.isBlank() || company.email.isBlank() || company.password.isBlank()
                when(true){
                    (company.password != confirmedPassword)->{
                        Log.d("CompanyRepository", "Unmatched password is called")
                        Log.d("CompanyRepository", "flow of unmatched password is called")
                        userPreferences.saveRepositoryJobSuccessValue(false)
                        userPreferences.saveRepositoryJobMessage("Passwords do not match")
                    }
                    (invalidParameters)->{
                        Log.d("CompanyRepository", "Invalid parameter is called")
                        Log.d("CompanyRepository", "flow of invalid parameters is called")
                        userPreferences.saveRepositoryJobSuccessValue(false)
                        userPreferences.saveRepositoryJobMessage("Please make sure that all required fields are filled appropriately")
                    }
                    else->{
                        Log.d("CompanyRepository", "Api call enqueue is called")
                        val call = shopManagerDatabaseApi.addCompany(company.toCompanyDto())
                        call!!.enqueue(object : Callback<AddCompanyResponse> {
                            override fun onResponse(
                                call: Call<AddCompanyResponse>,
                                response: Response<AddCompanyResponse>
                            ) {
                                Log.d("CompanyRepository", "Is successfully registered shop is called")
                                val isRegisteredSuccessfully = response.body()?.data != null
                                Log.d("CompanyRepository", "the api registered value: $isRegisteredSuccessfully")
                                Log.d("CompanyRepository", "Emits on response success")
                                GlobalScope.launch(Dispatchers.IO + Job()) {
                                    if (isRegisteredSuccessfully) {
                                        Log.d(
                                            "CompanyRepository",
                                            "This global scope success coroutine block gets called"
                                        )
                                        val responseInfo = response.body()?.data?.toCompanyInfoDto()
                                        val registeredAccountInfo = responseInfo?.toCompanyEntity()?.toCompanyEntityJson()
                                        registeredAccountInfo?.let {
                                            userPreferences.saveShopInfo(it)
                                            userPreferences.saveLoggedInState(true)
                                            userPreferences.saveRepositoryJobMessage(response.body()?.message.toNotNull())

                                            userPreferences.saveRepositoryJobSuccessValue(true)
                                            userPreferences.saveRepositoryJobMessage("Account created successfully")
                                        }
                                    } else {
                                        userPreferences.saveRepositoryJobSuccessValue(false)
                                        userPreferences.saveRepositoryJobMessage(response.body()?.message.toNotNull())

                                        Log.d(
                                            "CompanyRepository",
                                            "Emits server failure in global scope"
                                        )
                                        Log.d(
                                            "CompanyRepository",
                                            "Unable to register message is : ${response.body()?.message.toNotNull()}"
                                        )
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
                                    Log.d("CompanyRepository", "Emits on failure in post enqueue method")
                                    Log.d("CompanyRepository", "PostResponseMessage in on failure = ${t.message}")
                                    try {
                                        userPreferences.saveIOExceptionOrHttpExceptionState(true)
                                        userPreferences.saveIO_ExceptionOrHttpExceptionMessage(t.message ?: "IO Exception")
                                        userPreferences.saveRepositoryJobMessage(emptyString)
                                        userPreferences.saveRepositoryJobSuccessValue(false)

                                        if (isLoggedIn == true) {
                                            userPreferences.saveRepositoryJobSuccessValue(false)
                                            userPreferences.saveRepositoryJobMessage("You are already logged in")
                                        } else {
                                            when (true) {
                                                (company.password != confirmedPassword) -> {
                                                    Log.d("CompanyRepository", "Unmatched password is called")
                                                    Log.d("CompanyRepository", "flow of unmatched password is called")
                                                    userPreferences.saveRepositoryJobSuccessValue(false)
                                                    userPreferences.saveRepositoryJobMessage("Passwords do not match")
                                                }
                                                (invalidParameters) -> {
                                                    Log.d("CompanyRepository", "Invalid parameter is called")
                                                    Log.d("CompanyRepository", "flow of invalid parameters is called")
                                                    userPreferences.saveRepositoryJobSuccessValue(false)
                                                    userPreferences.saveRepositoryJobMessage("Please make sure that all required fields are filled appropriately")
                                                }
                                                else -> {
                                                    val uniqueCompanyId = generateUniqueCompanyId(company.companyName)
                                                    val newCompany = company.copy(uniqueCompanyId = uniqueCompanyId)
                                                    companyDao.addCompany(newCompany)
                                                    userPreferences.saveLoggedInState(true)
                                                    userPreferences.saveRepositoryJobSuccessValue(true)
                                                    userPreferences.saveRepositoryJobMessage("Company registered successfully\nNB: Your company has only been registered locally on this device")
                                                    newCompany.toCompanyEntityJson()?.let { userPreferences.saveShopInfo(it) }
                                                }
                                            }
                                        }
                                    }
                                    catch (e: Exception){
                                        Log.d(
                                            "CompanyRepository",
                                            "General exception is called under IO Exception"
                                        )
                                        userPreferences.saveRepositoryJobSuccessValue(false)
                                        userPreferences.saveRepositoryJobMessage(e.message ?: "Unknown error")
                                    }
                                }
                            }
                        })
                    }
                }
            }
        }
        catch (e:Exception){
            Log.d(
                "CompanyRepository",
                "General exception is called"
            )
            userPreferences.saveRepositoryJobSuccessValue(false)
            userPreferences.saveRepositoryJobMessage(e.message ?: "Unknown error")
        }
    }

    override suspend fun login(email: String, password: String): Flow<Resource<String>> = flow{
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
                        val context = MyShopManagerApp.applicationContext()
                        val userPreferences = UserPreferences(context)
                        val isLoggedIn = userPreferences.getLoggedInState.first()
                        val thisShop = userPreferences.getShopInfo.first()
                        Log.d("CompanyRepository", "1st is logged in = $isLoggedIn")
                        Log.d("CompanyRepository", "1st Shop info json = $thisShop")

                        shopInfo.toCompanyInfoDto()?.toCompanyEntity()?.toCompanyEntityJson()?.let {
                            userPreferences.saveShopInfo(it)
                            userPreferences.saveLoggedInState(true)
                            emit(Resource.Success(
                                data = "You have been successfully logged in",
                            ))

                            syncCompanyInfo()

                            val isLoggedIn1 = userPreferences.getLoggedInState.first()
                            val thisShop1 = userPreferences.getShopInfo.first()
                            Log.d("CompanyRepository", "2nd is logged in = $isLoggedIn1")
                            Log.d("CompanyRepository", "2nd Shop info json = $thisShop1")
                        }
                    }

                }
            }

        }catch (e: IOException){
            emit(Resource.Error(
                data = "Could not log in to cloud account. Trying to log in to local account",
                message = e.message
            ))
            try {
                val thisEmail = email.trim()
                val invalidParameters = thisEmail.isBlank() || password.isBlank()
                val company = companyDao.getAllCompanies()?.firstOrNull()
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
                            UserPreferences(MyShopManagerApp.applicationContext()).saveLoggedInState(
                                true
                            )
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
        }catch (e: Exception){
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
                    }else if (shopInfo == null){
                        userPreferences.saveRepositoryJobMessage("Failed to log in\n${loginMessage.toNotNull()}")
                        userPreferences.saveRepositoryJobSuccessValue(false)
                    }else{
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

            userPreferences.saveRepositoryJobMessage("Could not log in to cloud account.\nTrying to log in to local account\n${e.message ?: "Unknown error"}")
            userPreferences.saveRepositoryJobSuccessValue(false)
            try {
                val thisEmail = email.trim()
                val invalidParameters = thisEmail.isBlank() || password.isBlank()
                val company = companyDao.getAllCompanies()?.firstOrNull{(it.email.trim() == thisEmail) && (it.password.trim() == password)}
                when(true) {
                    invalidParameters -> {
                        userPreferences.saveRepositoryJobMessage("Failed to log in.\nEmail or password field is empty")
                        userPreferences.saveRepositoryJobSuccessValue(false)
                    }
                    (company == null) -> {
                        userPreferences.saveRepositoryJobMessage("Failed to log in.\nEmail and password do not match any account created locally on this device")
                        userPreferences.saveRepositoryJobSuccessValue(false)
                    }
                    else -> {
                        UserPreferences(MyShopManagerApp.applicationContext()).saveShopInfo(
                            company.toCompanyEntityJson().toNotNull()
                        )
                        restoreDatabase(context, company.companyName, userPreferences)
                        UserPreferences(MyShopManagerApp.applicationContext()).saveLoggedInState(true)
                        userPreferences.saveRepositoryJobMessage("You have been successfully logged in")
                        userPreferences.saveRepositoryJobSuccessValue(true)
                        if (userPreferences.getLoggedInState.first() == true) {
                            val i = context.packageManager.getLaunchIntentForPackage(context.packageName)
                            i!!.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            context.startActivity(i)
                            exitProcess(0)
                        }
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

            userPreferences.saveRepositoryJobMessage("Could not log in to cloud account.\nTrying to log in to local account\n${e.message ?: "Unknown error"}")
            userPreferences.saveRepositoryJobSuccessValue(false)
            try {
                val thisEmail = email.trim()
                val invalidParameters = thisEmail.isBlank() || password.isBlank()
                val company = companyDao.getAllCompanies()?.firstOrNull{(it.email.trim() == thisEmail) && (it.password.trim() == password)}
                when(true) {
                    invalidParameters -> {
                        userPreferences.saveRepositoryJobMessage("Failed to log in.\nEmail or password field is empty")
                        userPreferences.saveRepositoryJobSuccessValue(false)
                    }
                    (company == null) -> {
                        userPreferences.saveRepositoryJobMessage("Failed to log in.\nEmail and password do not match any account created locally on this device")
                        userPreferences.saveRepositoryJobSuccessValue(false)
                    }
                    else -> {
                        UserPreferences(MyShopManagerApp.applicationContext()).saveShopInfo(
                            company.toCompanyEntityJson().toNotNull()
                        )
                        val personnel = company.companyPersonnel.toPersonnelEntities()
                        appDatabase.personnelDao.addPersonnel(personnel)
                        UserPreferences(MyShopManagerApp.applicationContext()).saveLoggedInState(true)
                        userPreferences.saveRepositoryJobMessage("You have been successfully logged in")
                        userPreferences.saveRepositoryJobSuccessValue(true)
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
            companyDao.deleteAllTables()
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
            val companyName = userPreferences.getShopInfo.first().toCompanyEntity()?.companyName ?: ShopAppDatabase
            backupDatabase(context, companyName, userPreferences)
            userPreferences.saveLoggedInState(false)
            userPreferences.saveShopInfo(emptyString)
            userPreferences.savePersonnelLoggedInState(false)
            userPreferences.savePersonnelInfo(emptyString)
            companyDao.deleteAllTables()
            userPreferences.saveRepositoryJobMessage("Log out successful")
            userPreferences.saveRepositoryJobSuccessValue(false)
        }catch (e: Exception){
            UserPreferences(MyShopManagerApp.applicationContext()).saveRepositoryJobMessage("Could not retrieve data.\n${e.message ?: "Unknown Error"}")
            UserPreferences(MyShopManagerApp.applicationContext()).saveRepositoryJobSuccessValue(false)
        }
    }

    override suspend fun addCompanies(companies: CompanyEntities) {
        companyDao.addCompanies(companies)
    }

    override suspend fun getCompany(uniqueCompanyId: String): CompanyEntity? {
        return companyDao.getCompany(uniqueCompanyId)
    }

    override suspend fun getCompanyByName(companyName: String): CompanyEntities? {
        return companyDao.getCompanyByName(companyName)
    }

    override suspend fun updateCompany(company: CompanyEntity) {
        companyDao.updateCompany(company)
    }

    override suspend fun deleteCompany(companyId: Int) {
        companyDao.deleteCompany(companyId)
    }

    override suspend fun deleteCompany(uniqueCompanyId: String) {
        companyDao.deleteCompany(uniqueCompanyId)
    }

    override suspend fun deleteAllCompanies() {
        companyDao.deleteAllCompanies()
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
                    val remoteCustomers =
                        shopManagerDatabaseApi.fetchAllCompanyCustomers(uniqueCompanyId)?.data
                            ?: emptyList()
                    appDatabase.customerDao.addCustomers(remoteCustomers.map { it.toCustomerEntity() })

                    val remoteSuppliers =
                        shopManagerDatabaseApi.fetchAllCompanySuppliers(uniqueCompanyId)?.data
                            ?: emptyList()
                    appDatabase.supplierDao.addSuppliers(remoteSuppliers.map { it.toSupplierEntity() })

                    val remoteDebts =
                        shopManagerDatabaseApi.fetchAllCompanyDebts(uniqueCompanyId)?.data
                            ?: emptyList()
                    appDatabase.debtDao.addDebts(remoteDebts.map { it.toDebtEntity() })

                    val remoteCashIns =
                        shopManagerDatabaseApi.fetchAllCompanyCashIns(uniqueCompanyId)?.data
                            ?: emptyList()
                    appDatabase.cashInDao.addCashIns(remoteCashIns.map { it.toCashInEntity() })

                    val remoteDebtRepayments =
                        shopManagerDatabaseApi.fetchAllCompanyDebtRepayments(uniqueCompanyId)?.data
                            ?: emptyList()
                    appDatabase.debtRepaymentDao.addDebtRepayments(remoteDebtRepayments.map { it.toDebtRepaymentEntity() })

                    val remoteInventories =
                        shopManagerDatabaseApi.fetchAllCompanyInventories(uniqueCompanyId)?.data
                            ?: emptyList()
                    appDatabase.inventoryDao.addInventories(remoteInventories.map { it.toInventoryEntity() })

                    val remoteInventoryItems =
                        shopManagerDatabaseApi.fetchAllCompanyInventoryItems(uniqueCompanyId)?.data
                            ?: emptyList()
                    appDatabase.inventoryItemDao.addInventoryItems(remoteInventoryItems.map { it.toInventoryItemEntity() })

                    val remoteRevenues =
                        shopManagerDatabaseApi.fetchAllCompanyRevenues(uniqueCompanyId)?.data
                            ?: emptyList()
                    appDatabase.revenueDao.addRevenues(remoteRevenues.map { it.toRevenueEntity() })

                    val remoteExpenses =
                        shopManagerDatabaseApi.fetchAllCompanyExpenses(uniqueCompanyId)?.data
                            ?: emptyList()
                    appDatabase.expenseDao.addExpenses(remoteExpenses.map { it.toExpenseEntity() })

                    val remoteStocks =
                        shopManagerDatabaseApi.fetchAllCompanyStocks(uniqueCompanyId)?.data
                            ?: emptyList()
                    appDatabase.stockDao.addStocks(remoteStocks.map { it.toStockEntity() })

                    val remotePersonnel =
                        shopManagerDatabaseApi.fetchAllCompanyPersonnel(uniqueCompanyId)?.data
                            ?: emptyList()
                    appDatabase.personnelDao.addPersonnel(remotePersonnel.map { it.toPersonnelEntity() })

                    val remoteSavings =
                        shopManagerDatabaseApi.fetchAllCompanySavings(uniqueCompanyId)?.data
                            ?: emptyList()
                    appDatabase.savingsDao.addSavings(remoteSavings.map { it.toSavingsEntity() })

                    val remoteWithdrawals =
                        shopManagerDatabaseApi.fetchAllCompanyWithdrawals(uniqueCompanyId)?.data
                            ?: emptyList()
                    appDatabase.withdrawalDao.addWithdrawals(remoteWithdrawals.map { it.toWithdrawalEntity() })

                    val remoteInventoryStocks =
                        shopManagerDatabaseApi.fetchAllCompanyInventoryStocks(uniqueCompanyId)?.data
                            ?: emptyList()
                    appDatabase.inventoryStockDao.addInventoryStock(remoteInventoryStocks.map { it.toInventoryStock() })

                    val remoteBanks =
                        shopManagerDatabaseApi.fetchAllCompanyBanks(uniqueCompanyId)?.data
                            ?: emptyList()
                    appDatabase.bankAccountDao.addBanks(remoteBanks.map { it.toBankEntity() })

                    userPreferences.saveRepositoryJobMessage("Data successfully loaded and synced")
                    userPreferences.saveRepositoryJobSuccessValue(true)
                }
            }else{
                userPreferences.saveRepositoryJobMessage("Could not sync data\nYou are not logged in into any account")
                userPreferences.saveRepositoryJobSuccessValue(false)
            }

        }catch (e: Exception){
            UserPreferences(MyShopManagerApp.applicationContext()).saveRepositoryJobMessage("Could not sync data\n${e.message ?: "Unknown error"}")
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
            userPreferences.saveRepositoryJobSuccessValue(false)
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

}