package com.example.myshopmanagerapp.feature_app.data.repository

import android.content.Context
import android.util.Log
import com.example.myshopmanagerapp.core.AdditionEntityMarkers
import com.example.myshopmanagerapp.core.ChangesEntityMarkers
import com.example.myshopmanagerapp.core.Constants.SQLITE_SHMFILE_SUFFIX
import com.example.myshopmanagerapp.core.Constants.SQLITE_WALFILE_SUFFIX
import com.example.myshopmanagerapp.core.Constants.ShopAppDatabase
import com.example.myshopmanagerapp.core.Constants.THEDATABASE_DATABASE_BACKUP_SUFFIX
import com.example.myshopmanagerapp.core.Constants.UnknownError
import com.example.myshopmanagerapp.core.Constants.emptyString
import com.example.myshopmanagerapp.core.FormRelatedString.UniqueCompanyId
import com.example.myshopmanagerapp.core.Functions.toCompanyEntity
import com.example.myshopmanagerapp.core.Functions.toNotNull
import com.example.myshopmanagerapp.core.Resource
import com.example.myshopmanagerapp.core.TypeConverters.toUniqueIds
import com.example.myshopmanagerapp.core.UserPreferences
import com.example.myshopmanagerapp.feature_app.MyShopManagerApp
import com.example.myshopmanagerapp.feature_app.data.local.AppDatabase
import com.example.myshopmanagerapp.feature_app.data.local.entities.expenses.ExpenseEntity
import com.example.myshopmanagerapp.feature_app.data.remote.ShopManagerDatabaseApi
import com.example.myshopmanagerapp.feature_app.data.remote.dto.bank.SmartBankAccount
import com.example.myshopmanagerapp.feature_app.data.remote.dto.cash_in.SmartCashIns
import com.example.myshopmanagerapp.feature_app.data.remote.dto.company.CompanyResponseDto
import com.example.myshopmanagerapp.feature_app.data.remote.dto.customer.SmartCustomers
import com.example.myshopmanagerapp.feature_app.data.remote.dto.debt.SmartDebts
import com.example.myshopmanagerapp.feature_app.data.remote.dto.debt_repayment.SmartDebtRepayments
import com.example.myshopmanagerapp.feature_app.data.remote.dto.expense.SmartExpenses
import com.example.myshopmanagerapp.feature_app.data.remote.dto.inventory.SmartInventories
import com.example.myshopmanagerapp.feature_app.data.remote.dto.inventory_stock.SmartInventoryStocks
import com.example.myshopmanagerapp.feature_app.data.remote.dto.inventoy_item.SmartInventoryItems
import com.example.myshopmanagerapp.feature_app.data.remote.dto.personnel.SmartPersonnel
import com.example.myshopmanagerapp.feature_app.data.remote.dto.receipt.SmartReceipts
import com.example.myshopmanagerapp.feature_app.data.remote.dto.revenue.SmartRevenues
import com.example.myshopmanagerapp.feature_app.data.remote.dto.savings.SmartSavings
import com.example.myshopmanagerapp.feature_app.data.remote.dto.stock.SmartStocks
import com.example.myshopmanagerapp.feature_app.data.remote.dto.supplier.SmartSuppliers
import com.example.myshopmanagerapp.feature_app.data.remote.dto.withdrawal.SmartWithdrawals
import com.example.myshopmanagerapp.feature_app.domain.model.AddEntitiesResponse
import com.example.myshopmanagerapp.feature_app.domain.repository.BackupRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import okio.IOException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class BackupRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val shopManagerDatabaseApi: ShopManagerDatabaseApi
): BackupRepository{
    override suspend fun backupDatabase(context: Context): Flow<Resource<String>> = flow {
        emit(Resource.Loading())
        try {
            val dbName = UserPreferences(context).getShopInfo.first().toCompanyEntity()?.companyName ?: ShopAppDatabase
            Log.d("BackupRepository", "back up route = $dbName")
        val databaseFile = context.getDatabasePath(ShopAppDatabase)
        val databaseWALFile = File(databaseFile.path + SQLITE_WALFILE_SUFFIX)
        val databaseSHMFile = File(databaseFile.path + SQLITE_SHMFILE_SUFFIX)
        val backupFile = File(databaseFile.path + dbName +THEDATABASE_DATABASE_BACKUP_SUFFIX)
        val backupWALFile = File(backupFile.path + SQLITE_WALFILE_SUFFIX)
        val backupSHMFile = File(backupFile.path + SQLITE_SHMFILE_SUFFIX)
        if (backupFile.exists()) backupFile.delete()
        if (backupWALFile.exists()) {backupWALFile.delete()}
        if (backupSHMFile.exists()) {backupSHMFile.delete()}
        checkpoint()
        databaseFile.copyTo(backupFile,true)
        if (databaseWALFile.exists()) {databaseWALFile.copyTo(backupWALFile,true)}
        if (databaseSHMFile.exists()) {databaseSHMFile.copyTo(backupSHMFile, true)}
        emit(Resource.Success(
            data = "Data back up successfully"
        ))

        } catch (e: IOException) {
            emit(Resource.Error(
                data = "Couldn't back up data",
                message = e.printStackTrace().toString()
            ))
            e.printStackTrace()
        }
    }

    override suspend fun restoreDatabase(context: Context, restart: Boolean): Flow<Resource<String>> = flow{
        emit(Resource.Loading())
        try {
            val dbName = UserPreferences(context).getShopInfo.first().toCompanyEntity()?.companyName ?: ShopAppDatabase
            Log.d("BackupRepository", "route = $dbName")
        if(!File(context.getDatabasePath(ShopAppDatabase).path+ dbName + THEDATABASE_DATABASE_BACKUP_SUFFIX).exists()) {
            emit(Resource.Error(
                data = "Couldn't retrieve data",
                message = "Could not find any backed up data file"
            ))
        }

        val databasePath = appDatabase.openHelper.readableDatabase.path.toNotNull()
        val databaseFile = File(databasePath)
        val databaseWALFile = File(databaseFile.path + SQLITE_WALFILE_SUFFIX)
        val databaseSHMFile = File(databaseFile.path + SQLITE_SHMFILE_SUFFIX)
        val backupFile = File(databaseFile.path+ dbName + THEDATABASE_DATABASE_BACKUP_SUFFIX)
        val backupWALFile = File(backupFile.path + SQLITE_WALFILE_SUFFIX)
        val backupSHMFile = File(backupFile.path + SQLITE_SHMFILE_SUFFIX)

        backupFile.copyTo(databaseFile, true)
        if (backupWALFile.exists()) backupWALFile.copyTo(databaseWALFile, true)
        if (backupSHMFile.exists()) backupSHMFile.copyTo(databaseSHMFile,true)
        checkpoint()
        emit(Resource.Success("Data successfully restored"))
        } catch (e: IOException) {
            emit(Resource.Error(
                data = "Couldn't back up data",
                message = e.printStackTrace().toString()
            ))
        }
    }

    override suspend fun absoluteBackup(coroutineScope: CoroutineScope): Flow<Resource<String>> = flow{
        try {
            emit(Resource.Loading())
            val context = MyShopManagerApp.applicationContext()
            val userPreferences = UserPreferences(context)
            val isLoggedIn = userPreferences.getLoggedInState.first()
            val shopInfoJson = userPreferences.getShopInfo.first()
            val uniqueCompanyId = shopInfoJson.toCompanyEntity()?.uniqueCompanyId

            when(true) {
                (isLoggedIn != true) -> {
                    userPreferences.saveRepositoryJobSuccessValue(false)
                    userPreferences.saveRepositoryJobMessage("Backing up data...")
                    emit(
                        Resource.Error(
                            data = "Could not sync data",
                            message = "You are not logged in into any account"
                        )
                    )
                }
                (uniqueCompanyId == null) -> {
                    emit(
                        Resource.Error(
                            data = "Could not sync data",
                            message = "Could not get the shop account details\nPlease ensure that you are logged in"
                        )
                    )
                }
                else ->{
                    val customers = appDatabase.customerDao.getAllCustomers()?.map { it.toCustomerInfoDto(uniqueCompanyId) }
                    val debts = appDatabase.debtDao.getAllDebt()?.map { it.toDebtInfoDto(uniqueCompanyId) }
                    val debtRepayments = appDatabase.debtRepaymentDao.getAllDebtRepayment()?.map { it.toDebtRepaymentInfoDto(uniqueCompanyId) }
                    val expenses = appDatabase.expenseDao.getAllExpenses()?.map { it.toExpenseInfoDto(uniqueCompanyId) }
                    val inventories = appDatabase.inventoryDao.getAllInventories()?.map { it.toInventoryInfoDto(uniqueCompanyId) }
                    val inventoryItems = appDatabase.inventoryItemDao.getAllInventoryItems()?.map { it.toInventoryItemInfoDto(uniqueCompanyId) }
                    val personnel = appDatabase.personnelDao.getAllPersonnel()?.map { it.toPersonnelInfoDto(uniqueCompanyId) }
                    val suppliers = appDatabase.supplierDao.getAllSuppliers()?.map { it.toSupplierInfoDto(uniqueCompanyId) }
                    val inventoryStocks = appDatabase.inventoryStockDao.getAllInventoryStock()?.map { it.toInventoryStockInfoDto(uniqueCompanyId) }
                    val revenues = appDatabase.revenueDao.getAllRevenues()?.map { it.toRevenueInfoDto(uniqueCompanyId) }
                    val withdrawals = appDatabase.withdrawalDao.getAllWithdrawals()?.map { it.toWithdrawalInfoDto(uniqueCompanyId) }
                    val savings = appDatabase.savingsDao.getAllSavings()?.map { it.toSavingsInfoDto(uniqueCompanyId) }
                    val banks = appDatabase.bankAccountDao.getAllBankAccounts()?.map { it.toBankAccountInfoDto(uniqueCompanyId) }
                    val stocks = appDatabase.stockDao.getAllStocks()?.map { it.toStockInfoDto(uniqueCompanyId) }
                    val cashIns = appDatabase.cashInDao.getAllCashIns()?.map { it.toCashInfoDto(uniqueCompanyId) }
                    val receipts = appDatabase.receiptDao.getAllReceipts()?.map { it.toReceiptInfoDto(uniqueCompanyId) }


                    if (!customers.isNullOrEmpty()) {
                        Log.d("BackupRepository", "Customer is not empty is called")
                        emit(Resource.Loading("Backing up customers ..."))
                        val call = shopManagerDatabaseApi.addCustomers(uniqueCompanyId, customers)
                        call!!.enqueue(object : Callback<AddEntitiesResponse> {
                            override fun onResponse(
                                call: Call<AddEntitiesResponse>,
                                response: Response<AddEntitiesResponse>
                            ) {
                                Log.d("BackupRepository", "Customer data backup is successful")
                                coroutineScope.launch {
                                    emit(Resource.Success(data = response.body()?.data.toNotNull()))
                                    Log.d(
                                        "BackupRepository",
                                        "Revenue data is emitted successfully"
                                    )
                                }
                            }

                            override fun onFailure(call: Call<AddEntitiesResponse>, t: Throwable) {
                                coroutineScope.launch {
                                    emit(
                                        Resource.Error(
                                            data = t.message,
                                            message = "Unknown error\nUnable to backup customers",
                                        )
                                    )
                                    Log.d(
                                        "BackupRepository",
                                        "Customer data is emitted with failure"
                                    )
                                }
                            }
                        })
                    }

                    if (!suppliers.isNullOrEmpty()) {
                        emit(Resource.Loading("Backing up suppliers ..."))
                        val call = shopManagerDatabaseApi.addSuppliers(uniqueCompanyId, suppliers)
                        call!!.enqueue(object : Callback<AddEntitiesResponse> {
                            override fun onResponse(
                                call: Call<AddEntitiesResponse>,
                                response: Response<AddEntitiesResponse>
                            ) {
                                coroutineScope.launch {
                                    emit(Resource.Success(data = response.body()?.data.toNotNull()))
                                }
                            }

                            override fun onFailure(call: Call<AddEntitiesResponse>, t: Throwable) {
                                coroutineScope.launch {
                                    emit(
                                        Resource.Error(
                                            data = t.message,
                                            message = "Unknown error\nUnable to backup suppliers",
                                        )
                                    )
                                }
                            }
                        })
                    }

                    if (!cashIns.isNullOrEmpty()) {
                        Log.d("BackupRepository", "Debt is not empty is called")
                        emit(Resource.Loading("Backing up debts ..."))
                        val call = shopManagerDatabaseApi.addCashIns(uniqueCompanyId, cashIns)
                        call!!.enqueue(object : Callback<AddEntitiesResponse> {
                            override fun onResponse(
                                call: Call<AddEntitiesResponse>,
                                response: Response<AddEntitiesResponse>
                            ) {
                                Log.d("BackupRepository", "Cash in data backup is successful")
                                coroutineScope.launch {
                                    emit(Resource.Success(data = response.body()?.data.toNotNull()))
                                }
                            }

                            override fun onFailure(call: Call<AddEntitiesResponse>, t: Throwable) {
                                coroutineScope.launch {
                                    emit(
                                        Resource.Error(
                                            data = t.message,
                                            message = "Unknown error\nUnable to backup cash ins",
                                        )
                                    )
                                    Log.d(
                                        "BackupRepository",
                                        "Cash in data backup is emitted with failure"
                                    )
                                }
                                Log.d("BackupRepository", "Cash in data backup failed")
                            }
                        })
                    }

                    if (!receipts.isNullOrEmpty()) {
                        Log.d("BackupRepository", "Receipt is not empty is called")
                        emit(Resource.Loading("Backing up receipts ..."))
                        val call = shopManagerDatabaseApi.addReceipts(uniqueCompanyId, receipts)
                        call!!.enqueue(object : Callback<AddEntitiesResponse> {
                            override fun onResponse(
                                call: Call<AddEntitiesResponse>,
                                response: Response<AddEntitiesResponse>
                            ) {
                                Log.d("BackupRepository", "Receipt data backup is successful")
                                coroutineScope.launch {
                                    emit(Resource.Success(data = response.body()?.data.toNotNull()))
                                }
                            }

                            override fun onFailure(call: Call<AddEntitiesResponse>, t: Throwable) {
                                coroutineScope.launch {
                                    emit(
                                        Resource.Error(
                                            data = t.message,
                                            message = "Unknown error\nUnable to backup receipts",
                                        )
                                    )
                                    Log.d(
                                        "BackupRepository",
                                        "Receipt data backup is emitted with failure"
                                    )
                                }
                                Log.d("BackupRepository", "Receipt data backup failed")
                            }
                        })
                    }

                    if (!debts.isNullOrEmpty()) {
                        Log.d("BackupRepository", "Debt is not empty is called")
                        emit(Resource.Loading("Backing up debts ..."))
                        val call = shopManagerDatabaseApi.addDebts(uniqueCompanyId, debts)
                        call!!.enqueue(object : Callback<AddEntitiesResponse> {
                            override fun onResponse(
                                call: Call<AddEntitiesResponse>,
                                response: Response<AddEntitiesResponse>
                            ) {
                                Log.d("BackupRepository", "Debt data backup is successful")
                                coroutineScope.launch {
                                    emit(Resource.Success(data = response.body()?.data.toNotNull()))
                                }
                            }

                            override fun onFailure(call: Call<AddEntitiesResponse>, t: Throwable) {
                                coroutineScope.launch {
                                    emit(
                                        Resource.Error(
                                            data = t.message,
                                            message = "Unknown error\nUnable to backup debts",
                                        )
                                    )
                                    Log.d(
                                        "BackupRepository",
                                        "Debt data backup is emitted with failure"
                                    )
                                }
                                Log.d("BackupRepository", "Debt data backup failed")
                            }
                        })
                    }

                    if (!debtRepayments.isNullOrEmpty()) {
                        emit(Resource.Loading("Backing up debt repayments ..."))
                        val call =
                            shopManagerDatabaseApi.addDebtRepayments(
                                uniqueCompanyId,
                                debtRepayments
                            )
                        call!!.enqueue(object : Callback<AddEntitiesResponse> {
                            override fun onResponse(
                                call: Call<AddEntitiesResponse>,
                                response: Response<AddEntitiesResponse>
                            ) {
                                coroutineScope.launch {
                                    emit(Resource.Success(data = response.body()?.data.toNotNull()))
                                }
                            }

                            override fun onFailure(call: Call<AddEntitiesResponse>, t: Throwable) {
                                coroutineScope.launch {
                                    emit(
                                        Resource.Error(
                                            data = t.message,
                                            message = "Unknown error\nUnable to backup debt repayments",
                                        )
                                    )
                                }
                            }
                        })
                    }

                    if (!revenues.isNullOrEmpty()) {
                        Log.d("BackupRepository", "Revenue is not empty is called")
                        emit(Resource.Loading("Backing up revenues ..."))
                        val call = shopManagerDatabaseApi.addRevenues(uniqueCompanyId, revenues)
                        call!!.enqueue(object : Callback<AddEntitiesResponse> {
                            override fun onResponse(
                                call: Call<AddEntitiesResponse>,
                                response: Response<AddEntitiesResponse>
                            ) {
                                Log.d("BackupRepository", "Revenue data backup is successful")
                                coroutineScope.launch {
                                    emit(Resource.Success(data = response.body()?.data.toNotNull()))
                                    Log.d(
                                        "BackupRepository",
                                        "Revenue data is emitted successfully"
                                    )
                                }
                            }

                            override fun onFailure(call: Call<AddEntitiesResponse>, t: Throwable) {
                                coroutineScope.launch {
                                    emit(
                                        Resource.Error(
                                            data = t.message,
                                            message = "Unknown error\nUnable to backup revenues",
                                        )
                                    )
                                    Log.d(
                                        "BackupRepository",
                                        "Revenue data is emitted with failure"
                                    )
                                }
                            }
                        })
                    }

                    if (!expenses.isNullOrEmpty()) {
                        emit(Resource.Loading("Backing up expenses ..."))
                        val call = shopManagerDatabaseApi.addExpenses(uniqueCompanyId, expenses)
                        call!!.enqueue(object : Callback<AddEntitiesResponse> {
                            override fun onResponse(
                                call: Call<AddEntitiesResponse>,
                                response: Response<AddEntitiesResponse>
                            ) {
                                coroutineScope.launch {
                                    emit(Resource.Success(data = response.body()?.data.toNotNull()))
                                }
                            }

                            override fun onFailure(call: Call<AddEntitiesResponse>, t: Throwable) {
                                coroutineScope.launch {
                                    emit(
                                        Resource.Error(
                                            data = t.message,
                                            message = "Unknown error\nUnable to backup expenses",
                                        )
                                    )
                                }
                            }
                        })
                    }

                    if (!inventories.isNullOrEmpty()) {
                        emit(Resource.Loading("Backing up inventories ..."))
                        val call =
                            shopManagerDatabaseApi.addInventories(uniqueCompanyId, inventories)
                        call!!.enqueue(object : Callback<AddEntitiesResponse> {
                            override fun onResponse(
                                call: Call<AddEntitiesResponse>,
                                response: Response<AddEntitiesResponse>
                            ) {
                                coroutineScope.launch {
                                    emit(Resource.Success(data = response.body()?.data.toNotNull()))
                                }
                            }

                            override fun onFailure(call: Call<AddEntitiesResponse>, t: Throwable) {
                                coroutineScope.launch {
                                    emit(
                                        Resource.Error(
                                            data = t.message,
                                            message = "Unknown error\nUnable to backup inventories",
                                        )
                                    )
                                }
                            }
                        })
                    }

                    if (!inventoryItems.isNullOrEmpty()) {
                        emit(Resource.Loading("Backing up inventory items ..."))
                        val call =
                            shopManagerDatabaseApi.addInventoryItems(
                                uniqueCompanyId,
                                inventoryItems
                            )
                        call!!.enqueue(object : Callback<AddEntitiesResponse> {
                            override fun onResponse(
                                call: Call<AddEntitiesResponse>,
                                response: Response<AddEntitiesResponse>
                            ) {
                                coroutineScope.launch {
                                    emit(Resource.Success(data = response.body()?.data.toNotNull()))
                                }
                            }

                            override fun onFailure(call: Call<AddEntitiesResponse>, t: Throwable) {
                                coroutineScope.launch {
                                    emit(
                                        Resource.Error(
                                            data = t.message,
                                            message = "Unknown error\nUnable to backup inventory items",
                                        )
                                    )
                                }
                            }
                        })
                    }

                    if (!inventoryStocks.isNullOrEmpty()) {
                        emit(Resource.Loading("Backing up inventory stocks ..."))
                        val call =
                            shopManagerDatabaseApi.addInventoryStocks(
                                uniqueCompanyId,
                                inventoryStocks
                            )
                        call!!.enqueue(object : Callback<AddEntitiesResponse> {
                            override fun onResponse(
                                call: Call<AddEntitiesResponse>,
                                response: Response<AddEntitiesResponse>
                            ) {
                                coroutineScope.launch {
                                    emit(Resource.Success(data = response.body()?.data.toNotNull()))
                                }
                            }

                            override fun onFailure(call: Call<AddEntitiesResponse>, t: Throwable) {
                                coroutineScope.launch {
                                    emit(
                                        Resource.Error(
                                            data = t.message,
                                            message = "Unknown error\nUnable to backup inventory stocks",
                                        )
                                    )
                                }
                            }
                        })
                    }

                    if (!stocks.isNullOrEmpty()) {
                        emit(Resource.Loading("Backing up stocks ..."))
                        val call = shopManagerDatabaseApi.addStocks(uniqueCompanyId, stocks)
                        call!!.enqueue(object : Callback<AddEntitiesResponse> {
                            override fun onResponse(
                                call: Call<AddEntitiesResponse>,
                                response: Response<AddEntitiesResponse>
                            ) {
                                coroutineScope.launch {
                                    emit(Resource.Success(data = response.body()?.data.toNotNull()))
                                }
                            }

                            override fun onFailure(call: Call<AddEntitiesResponse>, t: Throwable) {
                                coroutineScope.launch {
                                    emit(
                                        Resource.Error(
                                            data = t.message,
                                            message = "Unknown error\nUnable to backup stocks",
                                        )
                                    )
                                }
                            }
                        })
                    }

                    if (!personnel.isNullOrEmpty()) {
                        emit(Resource.Loading("Backing up personnel ..."))
                        val call =
                            shopManagerDatabaseApi.addListOfPersonnel(uniqueCompanyId, personnel)
                        call!!.enqueue(object : Callback<AddEntitiesResponse> {
                            override fun onResponse(
                                call: Call<AddEntitiesResponse>,
                                response: Response<AddEntitiesResponse>
                            ) {
                                Log.d("BackupRepository", "Personnel data backup is successful")
                                coroutineScope.launch {
                                    emit(Resource.Success(data = response.body()?.data.toNotNull()))

                                }
                            }

                            override fun onFailure(call: Call<AddEntitiesResponse>, t: Throwable) {
                                coroutineScope.launch {
                                    emit(
                                        Resource.Error(
                                            data = t.message,
                                            message = "Unknown error\nUnable to backup personnel",
                                        )
                                    )
                                    Log.d(
                                        "BackupRepository",
                                        "Personnel data is emitted with failure"
                                    )
                                }
                            }
                        })
                    }

                    if (!savings.isNullOrEmpty()) {
                        Log.d("BackupRepository", "Savings is not empty is called")
                        emit(Resource.Loading("Backing up savings ..."))
                        val call = shopManagerDatabaseApi.addListOfSavings(uniqueCompanyId, savings)
                        call!!.enqueue(object : Callback<AddEntitiesResponse> {
                            override fun onResponse(
                                call: Call<AddEntitiesResponse>,
                                response: Response<AddEntitiesResponse>
                            ) {
                                Log.d("BackupRepository", "Savings data backup is successful")
                                coroutineScope.launch {
                                    emit(Resource.Success(data = response.body()?.data.toNotNull()))
                                }
                            }

                            override fun onFailure(call: Call<AddEntitiesResponse>, t: Throwable) {
                                coroutineScope.launch {
                                    emit(
                                        Resource.Error(
                                            data = t.message,
                                            message = "Unknown error\nUnable to backup savings",
                                        )
                                    )
                                }
                            }
                        })
                    }

                    if (!withdrawals.isNullOrEmpty()) {
                        Log.d("BackupRepository", "Withdrawal is not empty is called")
                        emit(Resource.Loading("Backing up withdrawals ..."))
                        val call =
                            shopManagerDatabaseApi.addWithdrawals(uniqueCompanyId, withdrawals)
                        call!!.enqueue(object : Callback<AddEntitiesResponse> {
                            override fun onResponse(
                                call: Call<AddEntitiesResponse>,
                                response: Response<AddEntitiesResponse>
                            ) {
                                Log.d("BackupRepository", "Withdrawal data backup is successful")
                                coroutineScope.launch {
                                    emit(Resource.Success(data = response.body()?.data.toNotNull()))
                                }
                            }

                            override fun onFailure(call: Call<AddEntitiesResponse>, t: Throwable) {
                                coroutineScope.launch {
                                    emit(
                                        Resource.Error(
                                            data = t.message,
                                            message = "Unknown error\nUnable to backup withdrawals",
                                        )
                                    )
                                }
                            }
                        })
                    }

                    if (!banks.isNullOrEmpty()) {
                        Log.d("BackupRepository", "Bank is not empty is called")
                        val call = shopManagerDatabaseApi.addBankAccounts(uniqueCompanyId, banks)
                        call!!.enqueue(object : Callback<AddEntitiesResponse> {
                            override fun onResponse(
                                call: Call<AddEntitiesResponse>,
                                response: Response<AddEntitiesResponse>
                            ) {
                                Log.d("BackupRepository", "Bank data backup is successful")
                                coroutineScope.launch {
                                    emit(Resource.Success(data = response.body()?.data.toNotNull()))
                                }
                            }

                            override fun onFailure(call: Call<AddEntitiesResponse>, t: Throwable) {
                                coroutineScope.launch {
                                    emit(
                                        Resource.Error(
                                            data = t.message,
                                            message = "Unknown error\nUnable to backup banks",
                                        )
                                    )
                                }
                            }
                        })
                    }
                }
            }
        }catch (e: Exception){
            emit(Resource.Error(
                message = "Could not back up any/all of the data",
                data = e.message
            ))
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    override suspend fun absoluteBackup1(coroutineScope: CoroutineScope) {
        var errorMessage = emptyString
        val context = MyShopManagerApp.applicationContext()
        val userPreferences = UserPreferences(context)

        try {
            val isLoggedIn = userPreferences.getLoggedInState.first()
            val shopInfoJson = userPreferences.getShopInfo.first()
            val uniqueCompanyId = "Company_Dedeeappliances_86681"
            //val uniqueCompanyId = shopInfoJson.toCompanyEntity()?.uniqueCompanyId
            Log.d(
                "BackupRepository",
                "uniqueCompanyId: $uniqueCompanyId"
            )
            when(true){
                (isLoggedIn != true) -> {
                    userPreferences.saveRepositoryJobSuccessValue(false)
                    userPreferences.saveRepositoryJobMessage("You're not logged in into any account.\nPlease register an online account and subscribe in order to continue")
                }
                (uniqueCompanyId == null) -> {
                    userPreferences.saveRepositoryJobSuccessValue(false)
                    userPreferences.saveRepositoryJobMessage("Could not get the shop account details\nPlease ensure that you are logged in")
                }
                else ->{
                    val customers = appDatabase.customerDao.getAllCustomers()?.map { it.toCustomerInfoDto(uniqueCompanyId) }
                    val debts = appDatabase.debtDao.getAllDebt()?.map { it.toDebtInfoDto(uniqueCompanyId) }
                    val debtRepayments = appDatabase.debtRepaymentDao.getAllDebtRepayment()?.map { it.toDebtRepaymentInfoDto(uniqueCompanyId) }
                    val expenses = appDatabase.expenseDao.getAllExpenses()?.map { it.toExpenseInfoDto(uniqueCompanyId) }
                    val inventories = appDatabase.inventoryDao.getAllInventories()?.map { it.toInventoryInfoDto(uniqueCompanyId) }
                    val inventoryItems = appDatabase.inventoryItemDao.getAllInventoryItems()?.map { it.toInventoryItemInfoDto(uniqueCompanyId) }
                    val personnel = appDatabase.personnelDao.getAllPersonnel()?.map { it.toPersonnelInfoDto(uniqueCompanyId) }
                    val suppliers = appDatabase.supplierDao.getAllSuppliers()?.map { it.toSupplierInfoDto(uniqueCompanyId) }
                    val inventoryStocks = appDatabase.inventoryStockDao.getAllInventoryStock()?.map { it.toInventoryStockInfoDto(uniqueCompanyId) }
                    val revenues = appDatabase.revenueDao.getAllRevenues()?.map { it.toRevenueInfoDto(uniqueCompanyId) }
                    val withdrawals = appDatabase.withdrawalDao.getAllWithdrawals()?.map { it.toWithdrawalInfoDto(uniqueCompanyId) }
                    val savings = appDatabase.savingsDao.getAllSavings()?.map { it.toSavingsInfoDto(uniqueCompanyId) }
                    val bankAccounts = appDatabase.bankAccountDao.getAllBankAccounts()?.map { it.toBankAccountInfoDto(uniqueCompanyId) }
                    val stocks = appDatabase.stockDao.getAllStocks()?.map { it.toStockInfoDto(uniqueCompanyId) }
                    val cashIns = appDatabase.cashInDao.getAllCashIns()?.map { it.toCashInfoDto(uniqueCompanyId) }
                    val receipts = appDatabase.receiptDao.getAllReceipts()?.map { it.toReceiptInfoDto(uniqueCompanyId) }

                    userPreferences.saveRepositoryJobMessage("All data has been fetched...")

                    if (!customers.isNullOrEmpty()) {
                        Log.d("BackupRepository", "Customer is not empty is called")
                        userPreferences.saveRepositoryJobMessage("Is backing up customers...")
                        val call = shopManagerDatabaseApi.addCustomers(uniqueCompanyId, customers)
                        call!!.enqueue(object : Callback<AddEntitiesResponse> {
                            override fun onResponse(
                                call: Call<AddEntitiesResponse>,
                                response: Response<AddEntitiesResponse>
                            ) {
                                val customerIsBackedUpSuccessfully = response.body()?.success == true
                                Log.d(
                                    "BackupRepository",
                                    "customer is backed up value = ${response.body()?.success}"
                                )
                                Log.d("BackupRepository", "Customer data backup success value: ${response.body()?.success}")
                                GlobalScope.launch(Dispatchers.IO + Job()) {
                                    if (customerIsBackedUpSuccessfully) {
                                        userPreferences.saveRepositoryJobMessage("${response.body()?.data}")
                                    }else{
                                        errorMessage = "${response.body()?.data}"
                                        userPreferences.saveRepositoryJobMessage(errorMessage)
                                    }
                                }
                            }

                            override fun onFailure(call: Call<AddEntitiesResponse>, t: Throwable) {
                                GlobalScope.launch(Dispatchers.IO + Job()) {
                                    errorMessage = t.message ?: "Unknown error!"
                                    userPreferences.saveRepositoryJobMessage(errorMessage)
                                    Log.d(
                                        "BackupRepository",
                                        "Customer data is emitted with failure"
                                    )
                                }
                            }
                        })
                    }

                    if (!suppliers.isNullOrEmpty()) {
                        userPreferences.saveRepositoryJobMessage("Is backing up suppliers...")
                        val call = shopManagerDatabaseApi.addSuppliers(uniqueCompanyId, suppliers)
                        call!!.enqueue(object : Callback<AddEntitiesResponse> {
                            override fun onResponse(
                                call: Call<AddEntitiesResponse>,
                                response: Response<AddEntitiesResponse>
                            ) {
                                val supplierIsBackedUpSuccessfully = response.body()?.success == true
                                GlobalScope.launch(Dispatchers.IO + Job()) {
                                    if (supplierIsBackedUpSuccessfully) {
                                        userPreferences.saveRepositoryJobMessage("${response.body()?.data}")
                                    }else{
                                        errorMessage = "${response.body()?.data}"
                                        userPreferences.saveRepositoryJobMessage(errorMessage)
                                    }
                                }
                            }

                            override fun onFailure(call: Call<AddEntitiesResponse>, t: Throwable) {
                                GlobalScope.launch(Dispatchers.IO + Job()) {
                                    errorMessage = t.message ?: "Unknown error!"
                                    userPreferences.saveRepositoryJobMessage(errorMessage)
                                }
                            }
                        })
                    }

                    if (!cashIns.isNullOrEmpty()) {
                        userPreferences.saveRepositoryJobMessage("Is backing up cash ins...")
                        val call = shopManagerDatabaseApi.addCashIns(uniqueCompanyId, cashIns)
                        call!!.enqueue(object : Callback<AddEntitiesResponse> {
                            override fun onResponse(
                                call: Call<AddEntitiesResponse>,
                                response: Response<AddEntitiesResponse>
                            ) {
                                val cashInIsBackedUpSuccessfully = response.body()?.success == true
                                GlobalScope.launch(Dispatchers.IO + Job()) {
                                    if (cashInIsBackedUpSuccessfully) {
                                        userPreferences.saveRepositoryJobMessage("${response.body()?.data}")
                                    }else{
                                        errorMessage = "${response.body()?.data}"
                                        userPreferences.saveRepositoryJobMessage(errorMessage)
                                    }
                                }
                            }

                            override fun onFailure(call: Call<AddEntitiesResponse>, t: Throwable) {
                                GlobalScope.launch(Dispatchers.IO + Job()) {

                                    errorMessage = t.message ?: "Unknown error!"
                                    userPreferences.saveRepositoryJobMessage(errorMessage)
                                }
                            }
                        })
                    }

                    if (!receipts.isNullOrEmpty()) {
                        userPreferences.saveRepositoryJobMessage("Is backing up receipts...")
                        val call = shopManagerDatabaseApi.addReceipts(uniqueCompanyId, receipts)
                        call!!.enqueue(object : Callback<AddEntitiesResponse> {
                            override fun onResponse(
                                call: Call<AddEntitiesResponse>,
                                response: Response<AddEntitiesResponse>
                            ) {
                                val receiptIsBackedUpSuccessfully = response.body()?.success == true
                                GlobalScope.launch(Dispatchers.IO + Job()) {
                                    if (receiptIsBackedUpSuccessfully) {
                                        userPreferences.saveRepositoryJobMessage("${response.body()?.data}")
                                    }else{
                                        errorMessage = "${response.body()?.data}"
                                        userPreferences.saveRepositoryJobMessage(errorMessage)
                                    }
                                }
                            }

                            override fun onFailure(call: Call<AddEntitiesResponse>, t: Throwable) {
                                GlobalScope.launch(Dispatchers.IO + Job()) {
                                    errorMessage = t.message ?: "Unknown error!"
                                    userPreferences.saveRepositoryJobMessage(errorMessage)
                                }
                            }
                        })
                    }

                    if (!debts.isNullOrEmpty()) {
                        userPreferences.saveRepositoryJobMessage("Is backing up debts...")
                        val call = shopManagerDatabaseApi.addDebts(uniqueCompanyId, debts)
                        call!!.enqueue(object : Callback<AddEntitiesResponse> {
                            override fun onResponse(
                                call: Call<AddEntitiesResponse>,
                                response: Response<AddEntitiesResponse>
                            ) {
                                val debtIsBackedUpSuccessfully = response.body()?.success == true
                                GlobalScope.launch(Dispatchers.IO + Job()) {
                                    if (debtIsBackedUpSuccessfully) {
                                        userPreferences.saveRepositoryJobMessage("${response.body()?.data}")
                                    }else{
                                        errorMessage = "${response.body()?.data}"
                                        userPreferences.saveRepositoryJobMessage(errorMessage)
                                    }
                                }
                            }

                            override fun onFailure(call: Call<AddEntitiesResponse>, t: Throwable) {
                                GlobalScope.launch(Dispatchers.IO + Job()) {
                                    errorMessage = t.message ?: "Unknown error!"
                                    userPreferences.saveRepositoryJobMessage(errorMessage)
                                }
                            }
                        })
                    }

                    if (!debtRepayments.isNullOrEmpty()) {
                        userPreferences.saveRepositoryJobMessage("Is backing up debtRepayments...")
                        val call = shopManagerDatabaseApi.addDebtRepayments(uniqueCompanyId, debtRepayments)
                        call!!.enqueue(object : Callback<AddEntitiesResponse> {
                            override fun onResponse(
                                call: Call<AddEntitiesResponse>,
                                response: Response<AddEntitiesResponse>
                            ) {
                                val debtRepaymentIsBackedUpSuccessfully = response.body()?.success == true
                                GlobalScope.launch(Dispatchers.IO + Job()) {
                                    if (debtRepaymentIsBackedUpSuccessfully) {
                                        userPreferences.saveRepositoryJobMessage("${response.body()?.data}")
                                    }else{
                                        errorMessage = "${response.body()?.data}"
                                        userPreferences.saveRepositoryJobMessage(errorMessage)
                                    }
                                }
                            }

                            override fun onFailure(call: Call<AddEntitiesResponse>, t: Throwable) {
                                GlobalScope.launch(Dispatchers.IO + Job()) {
                                    errorMessage = t.message ?: "Unknown error!"
                                    userPreferences.saveRepositoryJobMessage(errorMessage)
                                }
                            }
                        })
                    }

                    if (!revenues.isNullOrEmpty()) {
                        userPreferences.saveRepositoryJobMessage("Is backing up revenues...")
                        val call = shopManagerDatabaseApi.addRevenues(uniqueCompanyId, revenues)
                        call!!.enqueue(object : Callback<AddEntitiesResponse> {
                            override fun onResponse(
                                call: Call<AddEntitiesResponse>,
                                response: Response<AddEntitiesResponse>
                            ) {
                                val revenueIsBackedUpSuccessfully = response.body()?.success == true
                                GlobalScope.launch(Dispatchers.IO + Job()) {
                                    if (revenueIsBackedUpSuccessfully) {
                                        userPreferences.saveRepositoryJobMessage("${response.body()?.data}")
                                    }else{
                                        errorMessage = "${response.body()?.data}"
                                        userPreferences.saveRepositoryJobMessage(errorMessage)
                                    }
                                }
                            }

                            override fun onFailure(call: Call<AddEntitiesResponse>, t: Throwable) {
                                GlobalScope.launch(Dispatchers.IO + Job()) {
                                    errorMessage = t.message ?: "Unknown error!"
                                    userPreferences.saveRepositoryJobMessage(errorMessage)
                                }
                            }
                        })
                    }

                    if (!expenses.isNullOrEmpty()) {
                        userPreferences.saveRepositoryJobMessage("Is backing up expenses...")
                        val call = shopManagerDatabaseApi.addExpenses(uniqueCompanyId, expenses)
                        call!!.enqueue(object : Callback<AddEntitiesResponse> {
                            override fun onResponse(
                                call: Call<AddEntitiesResponse>,
                                response: Response<AddEntitiesResponse>
                            ) {
                                val expenseIsBackedUpSuccessfully = response.body()?.success == true
                                GlobalScope.launch(Dispatchers.IO + Job()) {
                                    if (expenseIsBackedUpSuccessfully) {
                                        userPreferences.saveRepositoryJobMessage("${response.body()?.data}")
                                    }else{
                                        errorMessage = "${response.body()?.data}"
                                        userPreferences.saveRepositoryJobMessage(errorMessage)
                                    }
                                }
                            }

                            override fun onFailure(call: Call<AddEntitiesResponse>, t: Throwable) {
                                GlobalScope.launch(Dispatchers.IO + Job()) {
                                    errorMessage = t.message ?: "Unknown error!"
                                    userPreferences.saveRepositoryJobMessage(errorMessage)
                                }
                            }
                        })
                    }

                    if (!inventories.isNullOrEmpty()) {
                        userPreferences.saveRepositoryJobMessage("Is backing up inventories...")
                        val call = shopManagerDatabaseApi.addInventories(uniqueCompanyId, inventories)
                        call!!.enqueue(object : Callback<AddEntitiesResponse> {
                            override fun onResponse(
                                call: Call<AddEntitiesResponse>,
                                response: Response<AddEntitiesResponse>
                            ) {
                                val inventoryIsBackedUpSuccessfully = response.body()?.success == true
                                GlobalScope.launch(Dispatchers.IO + Job()) {
                                    if (inventoryIsBackedUpSuccessfully) {
                                        userPreferences.saveRepositoryJobMessage("${response.body()?.data}")
                                    }else{
                                        errorMessage = "${response.body()?.data}"
                                        userPreferences.saveRepositoryJobMessage(errorMessage)
                                    }
                                }
                            }

                            override fun onFailure(call: Call<AddEntitiesResponse>, t: Throwable) {
                                GlobalScope.launch(Dispatchers.IO + Job()) {
                                    errorMessage = t.message ?: "Unknown error!"
                                    userPreferences.saveRepositoryJobMessage(errorMessage)
                                }
                            }
                        })
                    }

                    if (!inventoryItems.isNullOrEmpty()) {
                        userPreferences.saveRepositoryJobMessage("Is backing up inventoryItems...")
                        val call = shopManagerDatabaseApi.addInventoryItems(uniqueCompanyId, inventoryItems)
                        call!!.enqueue(object : Callback<AddEntitiesResponse> {
                            override fun onResponse(
                                call: Call<AddEntitiesResponse>,
                                response: Response<AddEntitiesResponse>
                            ) {
                                val inventoryItemIsBackedUpSuccessfully = response.body()?.success == true
                                GlobalScope.launch(Dispatchers.IO + Job()) {
                                    if (inventoryItemIsBackedUpSuccessfully) {
                                        userPreferences.saveRepositoryJobMessage("${response.body()?.data}")
                                    }else{
                                        errorMessage = "${response.body()?.data}"
                                        userPreferences.saveRepositoryJobMessage(errorMessage)
                                    }
                                }
                            }

                            override fun onFailure(call: Call<AddEntitiesResponse>, t: Throwable) {
                                GlobalScope.launch(Dispatchers.IO + Job()) {
                                    errorMessage = t.message ?: "Unknown error!"
                                    userPreferences.saveRepositoryJobMessage(errorMessage)
                                }
                            }
                        })
                    }

                    if (!inventoryStocks.isNullOrEmpty()) {
                        userPreferences.saveRepositoryJobMessage("Is backing up inventoryStocks...")
                        val call = shopManagerDatabaseApi.addInventoryStocks(uniqueCompanyId, inventoryStocks)
                        call!!.enqueue(object : Callback<AddEntitiesResponse> {
                            override fun onResponse(
                                call: Call<AddEntitiesResponse>,
                                response: Response<AddEntitiesResponse>
                            ) {
                                val inventoryStockIsBackedUpSuccessfully = response.body()?.success == true
                                GlobalScope.launch(Dispatchers.IO + Job()) {
                                    if (inventoryStockIsBackedUpSuccessfully) {
                                        userPreferences.saveRepositoryJobMessage("${response.body()?.data}")
                                    }else{
                                        errorMessage = "${response.body()?.data}"
                                        userPreferences.saveRepositoryJobMessage(errorMessage)
                                    }
                                }
                            }

                            override fun onFailure(call: Call<AddEntitiesResponse>, t: Throwable) {
                                GlobalScope.launch(Dispatchers.IO + Job()) {
                                    errorMessage = t.message ?: "Unknown error!"
                                    userPreferences.saveRepositoryJobMessage(errorMessage)
                                }
                            }
                        })
                    }

                    if (!stocks.isNullOrEmpty()) {
                        userPreferences.saveRepositoryJobMessage("Is backing up stocks...")
                        val call = shopManagerDatabaseApi.addStocks(uniqueCompanyId, stocks)
                        call!!.enqueue(object : Callback<AddEntitiesResponse> {
                            override fun onResponse(
                                call: Call<AddEntitiesResponse>,
                                response: Response<AddEntitiesResponse>
                            ) {
                                val stockIsBackedUpSuccessfully = response.body()?.success == true
                                GlobalScope.launch(Dispatchers.IO + Job()) {
                                    if (stockIsBackedUpSuccessfully) {
                                        userPreferences.saveRepositoryJobMessage("${response.body()?.data}")
                                    }else{
                                        errorMessage = "${response.body()?.data}"
                                        userPreferences.saveRepositoryJobMessage(errorMessage)
                                    }
                                }
                            }

                            override fun onFailure(call: Call<AddEntitiesResponse>, t: Throwable) {
                                GlobalScope.launch(Dispatchers.IO + Job()) {
                                    errorMessage = t.message ?: "Unknown error!"
                                    userPreferences.saveRepositoryJobMessage(errorMessage)
                                }
                            }
                        })
                    }

                    if (!personnel.isNullOrEmpty()) {
                        userPreferences.saveRepositoryJobMessage("Is backing up personnel...")
                        val call = shopManagerDatabaseApi.addListOfPersonnel(uniqueCompanyId, personnel)
                        call!!.enqueue(object : Callback<AddEntitiesResponse> {
                            override fun onResponse(
                                call: Call<AddEntitiesResponse>,
                                response: Response<AddEntitiesResponse>
                            ) {
                                val customerIsBackedUpSuccessfully = response.body()?.success == true
                                GlobalScope.launch(Dispatchers.IO + Job()) {
                                    if (customerIsBackedUpSuccessfully) {
                                        userPreferences.saveRepositoryJobMessage("${response.body()?.data}")
                                    }else{
                                        errorMessage = "${response.body()?.data}"
                                        userPreferences.saveRepositoryJobMessage(errorMessage)
                                    }
                                }
                            }

                            override fun onFailure(call: Call<AddEntitiesResponse>, t: Throwable) {
                                GlobalScope.launch(Dispatchers.IO + Job()) {
                                    errorMessage = t.message ?: "Unknown error!"
                                    userPreferences.saveRepositoryJobMessage(errorMessage)
                                }
                            }
                        })
                    }

                    if (!savings.isNullOrEmpty()) {
                        userPreferences.saveRepositoryJobMessage("Is backing up savings...")
                        val call = shopManagerDatabaseApi.addListOfSavings(uniqueCompanyId, savings)
                        call!!.enqueue(object : Callback<AddEntitiesResponse> {
                            override fun onResponse(
                                call: Call<AddEntitiesResponse>,
                                response: Response<AddEntitiesResponse>
                            ) {
                                val customerIsBackedUpSuccessfully = response.body()?.success == true
                                GlobalScope.launch(Dispatchers.IO + Job()) {
                                    if (customerIsBackedUpSuccessfully) {
                                        userPreferences.saveRepositoryJobMessage("${response.body()?.data}")
                                    }else{
                                        errorMessage = "${response.body()?.data}"
                                        userPreferences.saveRepositoryJobMessage(errorMessage)
                                    }
                                }
                            }

                            override fun onFailure(call: Call<AddEntitiesResponse>, t: Throwable) {
                                GlobalScope.launch(Dispatchers.IO + Job()) {
                                    errorMessage = t.message ?: "Unknown error!"
                                    userPreferences.saveRepositoryJobMessage(errorMessage)
                                }
                            }
                        })
                    }

                    if (!withdrawals.isNullOrEmpty()) {
                        userPreferences.saveRepositoryJobMessage("Is backing up withdrawals...")
                        val call = shopManagerDatabaseApi.addWithdrawals(uniqueCompanyId, withdrawals)
                        call!!.enqueue(object : Callback<AddEntitiesResponse> {
                            override fun onResponse(
                                call: Call<AddEntitiesResponse>,
                                response: Response<AddEntitiesResponse>
                            ) {
                                val withdrawalIsBackedUpSuccessfully = response.body()?.success == true
                                GlobalScope.launch(Dispatchers.IO + Job()) {
                                    if (withdrawalIsBackedUpSuccessfully) {
                                        userPreferences.saveRepositoryJobMessage("${response.body()?.data}")
                                    }else{
                                        errorMessage = "${response.body()?.data}"
                                        userPreferences.saveRepositoryJobMessage(errorMessage)
                                    }
                                }
                            }

                            override fun onFailure(call: Call<AddEntitiesResponse>, t: Throwable) {
                                GlobalScope.launch(Dispatchers.IO + Job()) {

                                    errorMessage = t.message ?: "Unknown error!"
                                    userPreferences.saveRepositoryJobMessage(errorMessage)
                                }
                            }
                        })
                    }

                    if (!bankAccounts.isNullOrEmpty()) {
                        userPreferences.saveRepositoryJobMessage("Is backing up banks...")
                        val call = shopManagerDatabaseApi.addBankAccounts(uniqueCompanyId, bankAccounts)
                        call!!.enqueue(object : Callback<AddEntitiesResponse> {
                            override fun onResponse(
                                call: Call<AddEntitiesResponse>,
                                response: Response<AddEntitiesResponse>
                            ) {
                                val bankIsBackedUpSuccessfully = response.body()?.success == true
                                GlobalScope.launch(Dispatchers.IO + Job()) {
                                    if (bankIsBackedUpSuccessfully) {
                                        userPreferences.saveRepositoryJobMessage("${response.body()?.data}")
                                    }else{
                                        errorMessage = "${response.body()?.data}"
                                        userPreferences.saveRepositoryJobMessage(errorMessage)
                                    }
                                }
                            }

                            override fun onFailure(call: Call<AddEntitiesResponse>, t: Throwable) {
                                GlobalScope.launch(Dispatchers.IO + Job()) {
                                    errorMessage = t.message ?: "Unknown error!"
                                    userPreferences.saveRepositoryJobMessage(errorMessage)
                                }
                            }
                        })
                    }

                    userPreferences.saveRepositoryJobSuccessValue(true)
                    userPreferences.saveRepositoryJobMessage("All data is successfully backed up")
                }
            }
        }catch (e: Exception){
            userPreferences.saveRepositoryJobSuccessValue(false)
            userPreferences.saveRepositoryJobMessage(errorMessage.plus("\n\n${e.message ?: "Unknown error"}"))
        }
    }

    override suspend fun smartBackup(coroutineScope: CoroutineScope): Flow<Resource<String>> = flow{
        /*
        try {
            emit(Resource.Loading())
            val context = MyShopManagerApp.applicationContext()
            val userPreferences = UserPreferences(context)
            val isLoggedIn = userPreferences.getLoggedInState.first()
            val shopInfoJson = userPreferences.getShopInfo.first()
            //val uniqueCompanyId = shopInfoJson.toCompanyEntity()?.uniqueCompanyId
            val uniqueCompanyId = "Company_Dedeeappliances_86681"

            when(true){
                (isLoggedIn != true)->{
                    emit(Resource.Error(
                        data = "Could not sync data",
                        message = "You are not logged in into any account"
                    ))
                }
                (uniqueCompanyId == null)->{
                    emit(Resource.Error(
                        data = "Could not sync data",
                        message = "Could not get the shop account details\nPlease ensure that you are logged in"
                    ))
                }
                else -> {
                    val allCustomers = appDatabase.customerDao.getAllCustomers()?: emptyList()
                    val addedCustomerIds = AdditionEntityMarkers(context).getAddedCustomerIds.first().toUniqueIds().map { it.uniqueId }
                    val customers = allCustomers.filter { addedCustomerIds.contains(it.uniqueCustomerId) }.map { it.toCustomerInfoDto(uniqueCompanyId) }

                    val allReceipts = appDatabase.receiptDao.getAllReceipts() ?: emptyList()
                    val addedReceiptIds = AdditionEntityMarkers(context).getAddedReceiptIds.first().toUniqueIds().map { it.uniqueId }
                    val receipts = allReceipts.filter { addedReceiptIds.contains(it.uniqueReceiptId) }.map { it.toReceiptInfoDto(uniqueCompanyId) }

                    val allDebts = appDatabase.debtDao.getAllDebt() ?: emptyList()
                    val addedDebtIds = AdditionEntityMarkers(context).getAddedDebtIds.first().toUniqueIds().map { it.uniqueId }
                    val debts = allDebts.filter { addedDebtIds.contains(it.uniqueDebtId) }.map { it.toDebtInfoDto(uniqueCompanyId) }

                    val allDebtRepayments = appDatabase.debtRepaymentDao.getAllDebtRepayment() ?: emptyList()
                    val addedDebtRepaymentsIds = AdditionEntityMarkers(context).getAddedDebtRepaymentIds.first().toUniqueIds().map { it.uniqueId }
                    val debtRepayments = allDebtRepayments.filter { addedDebtRepaymentsIds.contains(it.uniqueDebtRepaymentId) }.map { it.toDebtRepaymentInfoDto(uniqueCompanyId) }

                    val allExpenses = appDatabase.expenseDao.getAllExpenses() ?: emptyList()
                    val addedExpensesIds = AdditionEntityMarkers(context).getAddedExpenseIds.first().toUniqueIds().map { it.uniqueId }
                    val expenses = allExpenses.filter { addedExpensesIds.contains(it.uniqueExpenseId) }.map { it.toExpenseInfoDto(uniqueCompanyId) }

                    val allInventories = appDatabase.inventoryDao.getAllInventories() ?: emptyList()
                    val addedInventoriesIds = AdditionEntityMarkers(context).getAddedExpenseIds.first().toUniqueIds().map { it.uniqueId }
                    val inventories = allInventories.filter { addedInventoriesIds.contains(it.uniqueInventoryId) }.map { it.toInventoryInfoDto(uniqueCompanyId) }

                    val allInventoryItems = appDatabase.inventoryItemDao.getAllInventoryItems() ?: emptyList()
                    val addedInventoryItemsIds = AdditionEntityMarkers(context).getAddedInventoryItemIds.first().toUniqueIds().map { it.uniqueId }
                    val inventoryItems = allInventoryItems.filter { addedInventoryItemsIds.contains(it.uniqueInventoryItemId) }.map { it.toInventoryItemInfoDto(uniqueCompanyId) }

                    val allPersonnel = appDatabase.personnelDao.getAllPersonnel() ?: emptyList()
                    val addedPersonnelIds = AdditionEntityMarkers(context).getAddedInventoryItemIds.first().toUniqueIds().map { it.uniqueId }
                    val personnel = allPersonnel.filter { addedPersonnelIds.contains(it.uniquePersonnelId) }.map { it.toPersonnelInfoDto(uniqueCompanyId) }

                    val allSuppliers = appDatabase.supplierDao.getAllSuppliers() ?: emptyList()
                    val addedSuppliersIds = AdditionEntityMarkers(context).getAddedSupplierIds.first().toUniqueIds().map { it.uniqueId }
                    val suppliers = allSuppliers.filter { addedSuppliersIds.contains(it.uniqueSupplierId) }.map { it.toSupplierInfoDto(uniqueCompanyId) }

                    val allRevenues = appDatabase.revenueDao.getAllRevenues() ?: emptyList()
                    val addedRevenuesIds = AdditionEntityMarkers(context).getAddedRevenueIds.first().toUniqueIds().map { it.uniqueId }
                    val revenues = allRevenues.filter { addedRevenuesIds.contains(it.uniqueRevenueId) }.map { it.toRevenueInfoDto(uniqueCompanyId) }


                    val allWithdrawals = appDatabase.withdrawalDao.getAllWithdrawals() ?: emptyList()
                    val addedWithdrawalsIds = AdditionEntityMarkers(context).getAddedWithdrawalIds.first().toUniqueIds().map { it.uniqueId }
                    val withdrawals = allWithdrawals.filter { addedWithdrawalsIds.contains(it.uniqueWithdrawalId) }.map { it.toWithdrawalInfoDto(uniqueCompanyId) }


                    val allInventoryStocks = appDatabase.inventoryStockDao.getAllInventoryStock() ?: emptyList()
                    val addedInventoryStocksIds = AdditionEntityMarkers(context).getAddedInventoryStockIds.first().toUniqueIds().map { it.uniqueId }
                    val inventoryStocks = allInventoryStocks.filter { addedInventoryStocksIds.contains(it.uniqueInventoryStockId) }.map { it.toInventoryStockInfoDto(uniqueCompanyId) }

                    val allSavings = appDatabase.savingsDao.getAllSavings() ?: emptyList()
                    val addedSavingsIds = AdditionEntityMarkers(context).getAddedSavingsIds.first().toUniqueIds().map { it.uniqueId }
                    val savings = allSavings.filter { addedSavingsIds.contains(it.uniqueSavingsId) }.map { it.toSavingsInfoDto(uniqueCompanyId) }

                    val allStocks = appDatabase.stockDao.getAllStocks() ?: emptyList()
                    val addedStocksIds = AdditionEntityMarkers(context).getAddedStockIds.first().toUniqueIds().map { it.uniqueId }
                    val stocks = allStocks.filter { addedStocksIds.contains(it.uniqueStockId) }.map { it.toStockInfoDto(uniqueCompanyId) }

                    val allCashIns = appDatabase.cashInDao.getAllCashIns() ?: emptyList()
                    val addedCashInsIds = AdditionEntityMarkers(context).getAddedCashInIds.first().toUniqueIds().map { it.uniqueId }
                    val cashIns = allCashIns.filter { addedCashInsIds.contains(it.uniqueCashInId) }.map { it.toCashInfoDto(uniqueCompanyId) }

                    val allBankAccounts = appDatabase.bankAccountDao.getAllBankAccounts() ?: emptyList()
                    val addedBankAccountsIds = AdditionEntityMarkers(context).getAddedBankAccountIds.first().toUniqueIds().map { it.uniqueId }
                    val bankAccounts = allBankAccounts.filter { addedBankAccountsIds.contains(it.uniqueBankAccountId) }.map { it.toBankAccountInfoDto(uniqueCompanyId) }

                    Log.d("BackupRepository", "AddedCustomerIds = $addedCustomerIds \n")
                    Log.d("BackupRepository", "AddedExpensesIds = $addedExpensesIds \n")

                    Log.d("BackupRepository", "CustomerDtos = $customers \n")
                    Log.d("BackupRepository", "ExpenseDtos = $expenses \n")

                    if (expenses.isNotEmpty()) {
                        val uniqueExpenseIds = ChangesEntityMarkers(context).getChangedExpenseIds.first().toUniqueIds()
                        Log.d("BackupRepository", "Expense is not empty is called")
                        emit(Resource.Loading("Backing up expenses ..."))
                        val call = shopManagerDatabaseApi.smartBackUpExpenses(uniqueCompanyId, expenses, uniqueExpenseIds)
                        call!!.enqueue(object : Callback<AddEntitiesResponse> {
                            override fun onResponse(
                                call: Call<AddEntitiesResponse>,
                                response: Response<AddEntitiesResponse>
                            ) {
                                Log.d("BackupRepository", "Expense data back up is successful")
                                coroutineScope.launch {
                                    emit(Resource.Success(data = response.body()?.data.toNotNull()))
                                    AdditionEntityMarkers(context).saveAddedExpenseIds(emptyString)
                                    ChangesEntityMarkers(context).saveChangedExpenseIds(emptyString)
                                }
                            }

                            override fun onFailure(call: Call<AddEntitiesResponse>, t: Throwable) {
                                coroutineScope.launch {
                                    emit(
                                        Resource.Error(
                                            data = t.message,
                                            message = "Unknown error\nUnable to backup expenses",
                                        )
                                    )
                                    Log.d("BackupRepository", "Expense  data back up failed")
                                }
                            }
                        })
                    }

                    if (customers.isNotEmpty()) {
                        val uniqueCustomerIds = ChangesEntityMarkers(context).getChangedCustomerIds.first().toUniqueIds()
                        Log.d("BackupRepository", "Customer is not empty is called")
                        emit(Resource.Loading("Backing up customers ..."))
                        val call = shopManagerDatabaseApi.smartBackUpCustomers(uniqueCompanyId, customers, uniqueCustomerIds)
                        call!!.enqueue(object : Callback<AddEntitiesResponse> {
                            override fun onResponse(
                                call: Call<AddEntitiesResponse>,
                                response: Response<AddEntitiesResponse>
                            ) {
                                Log.d("BackupRepository", "Customer data backup is successful")
                                coroutineScope.launch {
                                    emit(Resource.Success(data = response.body()?.data.toNotNull()))
                                    Log.d(
                                        "BackupRepository",
                                        "Revenue data is emitted successfully"
                                    )
                                    AdditionEntityMarkers(context).saveAddedCustomerIds(emptyString)
                                    ChangesEntityMarkers(context).saveChangedCustomerIds(emptyString)
                                }
                            }

                            override fun onFailure(call: Call<AddEntitiesResponse>, t: Throwable) {
                                coroutineScope.launch {
                                    emit(
                                        Resource.Error(
                                            data = t.message,
                                            message = "Unknown error\nUnable to backup customers",
                                        )
                                    )
                                    Log.d(
                                        "BackupRepository",
                                        "Customer data is emitted with failure"
                                    )
                                }
                            }
                        })
                    }

                    if (suppliers.isNotEmpty()) {
                        val uniqueSupplierIds = ChangesEntityMarkers(context).getChangedSupplierIds.first().toUniqueIds()
                        emit(Resource.Loading("Backing up suppliers ..."))
                        val call = shopManagerDatabaseApi.smartBackUpSuppliers(uniqueCompanyId, suppliers, uniqueSupplierIds)
                        call!!.enqueue(object : Callback<AddEntitiesResponse> {
                            override fun onResponse(
                                call: Call<AddEntitiesResponse>,
                                response: Response<AddEntitiesResponse>
                            ) {
                                coroutineScope.launch {
                                    emit(Resource.Success(data = response.body()?.data.toNotNull()))
                                    AdditionEntityMarkers(context).saveAddedSupplierIds(emptyString)
                                    ChangesEntityMarkers(context).saveChangedSupplierIds(emptyString)
                                }
                            }

                            override fun onFailure(call: Call<AddEntitiesResponse>, t: Throwable) {
                                coroutineScope.launch {
                                    emit(
                                        Resource.Error(
                                            data = t.message,
                                            message = "Unknown error\nUnable to backup suppliers",
                                        )
                                    )
                                }
                            }
                        })
                    }

                    if (cashIns.isNotEmpty()) {
                        val uniqueCashInIds = ChangesEntityMarkers(context).getChangedCashInIds.first().toUniqueIds()
                        Log.d("BackupRepository", "Debt is not empty is called")
                        emit(Resource.Loading("Backing up debts ..."))
                        val call = shopManagerDatabaseApi.smartBackUpCashIns(uniqueCompanyId, cashIns, uniqueCashInIds)
                        call!!.enqueue(object : Callback<AddEntitiesResponse> {
                            override fun onResponse(
                                call: Call<AddEntitiesResponse>,
                                response: Response<AddEntitiesResponse>
                            ) {
                                Log.d("BackupRepository", "Cash in data backup is successful")
                                coroutineScope.launch {
                                    emit(Resource.Success(data = response.body()?.data.toNotNull()))
                                    AdditionEntityMarkers(context).saveAddedCashInIds(emptyString)
                                    ChangesEntityMarkers(context).saveChangedCashInIds(emptyString)
                                }
                            }

                            override fun onFailure(call: Call<AddEntitiesResponse>, t: Throwable) {
                                coroutineScope.launch {
                                    emit(
                                        Resource.Error(
                                            data = t.message,
                                            message = "Unknown error\nUnable to backup cash ins",
                                        )
                                    )
                                    Log.d(
                                        "BackupRepository",
                                        "Cash in data backup is emitted with failure"
                                    )
                                }
                                Log.d("BackupRepository", "Cash in data backup failed")
                            }
                        })
                    }

                    if (receipts.isNotEmpty()) {
                        val uniqueReceiptIds = ChangesEntityMarkers(context).getChangedReceiptIds.first().toUniqueIds()
                        Log.d("BackupRepository", "Receipt is not empty is called")
                        emit(Resource.Loading("Backing up receipts ..."))
                        val call = shopManagerDatabaseApi.smartBackUpReceipts(uniqueCompanyId, receipts, uniqueReceiptIds)
                        call!!.enqueue(object : Callback<AddEntitiesResponse> {
                            override fun onResponse(
                                call: Call<AddEntitiesResponse>,
                                response: Response<AddEntitiesResponse>
                            ) {
                                Log.d("BackupRepository", "Receipt data backup is successful")
                                coroutineScope.launch {
                                    emit(Resource.Success(data = response.body()?.data.toNotNull()))
                                    AdditionEntityMarkers(context).saveAddedReceiptIds(emptyString)
                                    ChangesEntityMarkers(context).saveChangedReceiptIds(emptyString)
                                }
                            }

                            override fun onFailure(call: Call<AddEntitiesResponse>, t: Throwable) {
                                coroutineScope.launch {
                                    emit(
                                        Resource.Error(
                                            data = t.message,
                                            message = "Unknown error\nUnable to backup receipts",
                                        )
                                    )
                                    Log.d(
                                        "BackupRepository",
                                        "Receipt data backup is emitted with failure"
                                    )
                                }
                                Log.d("BackupRepository", "Receipt data backup failed")
                            }
                        })
                    }

                    if (debts.isNotEmpty()) {
                        val uniqueDebtIds = ChangesEntityMarkers(context).getChangedDebtIds.first().toUniqueIds()
                        Log.d("BackupRepository", "Debt is not empty is called")
                        emit(Resource.Loading("Backing up debts ..."))
                        val call = shopManagerDatabaseApi.smartBackUpDebts(uniqueCompanyId, debts, uniqueDebtIds)
                        call!!.enqueue(object : Callback<AddEntitiesResponse> {
                            override fun onResponse(
                                call: Call<AddEntitiesResponse>,
                                response: Response<AddEntitiesResponse>
                            ) {
                                Log.d("BackupRepository", "Debt data backup is successful")
                                coroutineScope.launch {
                                    emit(Resource.Success(data = response.body()?.data.toNotNull()))
                                    AdditionEntityMarkers(context).saveAddedDebtIds(emptyString)
                                    ChangesEntityMarkers(context).saveChangedDebtIds(emptyString)
                                }
                            }

                            override fun onFailure(call: Call<AddEntitiesResponse>, t: Throwable) {
                                coroutineScope.launch {
                                    emit(
                                        Resource.Error(
                                            data = t.message,
                                            message = "Unknown error\nUnable to backup debts",
                                        )
                                    )
                                    Log.d(
                                        "BackupRepository",
                                        "Debt data backup is emitted with failure"
                                    )
                                }
                                Log.d("BackupRepository", "Debt data backup failed")
                            }
                        })
                    }

                    if (debtRepayments.isNotEmpty()) {
                        val uniqueDebtRepaymentIds = ChangesEntityMarkers(context).getChangedDebtRepaymentIds.first().toUniqueIds()
                        emit(Resource.Loading("Backing up debt repayments ..."))
                        val call =
                            shopManagerDatabaseApi.smartBackUpDebtRepayments(
                                uniqueCompanyId,
                                debtRepayments,
                                uniqueDebtRepaymentIds
                            )
                        call!!.enqueue(object : Callback<AddEntitiesResponse> {
                            override fun onResponse(
                                call: Call<AddEntitiesResponse>,
                                response: Response<AddEntitiesResponse>
                            ) {
                                coroutineScope.launch {
                                    emit(Resource.Success(data = response.body()?.data.toNotNull()))
                                    AdditionEntityMarkers(context).saveAddedDebtRepaymentIds(emptyString)
                                    ChangesEntityMarkers(context).saveChangedDebtRepaymentIds(emptyString)
                                }
                            }

                            override fun onFailure(call: Call<AddEntitiesResponse>, t: Throwable) {
                                coroutineScope.launch {
                                    emit(
                                        Resource.Error(
                                            data = t.message,
                                            message = "Unknown error\nUnable to backup debt repayments",
                                        )
                                    )
                                }
                            }
                        })
                    }

                    if (revenues.isNotEmpty()) {
                        val uniqueRevenueIds = ChangesEntityMarkers(context).getChangedRevenueIds.first().toUniqueIds()
                        Log.d("BackupRepository", "Revenue is not empty is called")
                        emit(Resource.Loading("Backing up revenues ..."))
                        val call = shopManagerDatabaseApi.smartBackUpRevenues(uniqueCompanyId, revenues, uniqueRevenueIds)
                        call!!.enqueue(object : Callback<AddEntitiesResponse> {
                            override fun onResponse(
                                call: Call<AddEntitiesResponse>,
                                response: Response<AddEntitiesResponse>
                            ) {
                                Log.d("BackupRepository", "Revenue data backup is successful")
                                coroutineScope.launch {
                                    emit(Resource.Success(data = response.body()?.data.toNotNull()))
                                    Log.d(
                                        "BackupRepository",
                                        "Revenue data is emitted successfully"
                                    )
                                    AdditionEntityMarkers(context).saveAddedRevenueIds(emptyString)
                                    ChangesEntityMarkers(context).saveChangedRevenueIds(emptyString)
                                }
                            }

                            override fun onFailure(call: Call<AddEntitiesResponse>, t: Throwable) {
                                coroutineScope.launch {
                                    emit(
                                        Resource.Error(
                                            data = t.message,
                                            message = "Unknown error\nUnable to backup revenues",
                                        )
                                    )
                                    Log.d(
                                        "BackupRepository",
                                        "Revenue data is emitted with failure"
                                    )
                                }
                            }
                        })
                    }

                    if (inventories.isNotEmpty()) {
                        val uniqueInventoryIds = ChangesEntityMarkers(context).getChangedInventoryIds.first().toUniqueIds()
                        emit(Resource.Loading("Backing up inventories ..."))
                        val call = shopManagerDatabaseApi.smartBackUpInventories(uniqueCompanyId, inventories, uniqueInventoryIds)
                        call!!.enqueue(object : Callback<AddEntitiesResponse> {
                            override fun onResponse(
                                call: Call<AddEntitiesResponse>,
                                response: Response<AddEntitiesResponse>
                            ) {
                                coroutineScope.launch {
                                    emit(Resource.Success(data = response.body()?.data.toNotNull()))
                                    AdditionEntityMarkers(context).saveAddedInventoryIds(emptyString)
                                    ChangesEntityMarkers(context).saveChangedInventoryIds(emptyString)
                                }
                            }

                            override fun onFailure(call: Call<AddEntitiesResponse>, t: Throwable) {
                                coroutineScope.launch {
                                    emit(
                                        Resource.Error(
                                            data = t.message,
                                            message = "Unknown error\nUnable to backup inventories",
                                        )
                                    )
                                }
                            }
                        })
                    }

                    if (inventoryItems.isNotEmpty()) {
                        val uniqueInventoryItemIds = ChangesEntityMarkers(context).getChangedInventoryItemIds.first().toUniqueIds()
                        emit(Resource.Loading("Backing up inventory items ..."))
                        val call =
                            shopManagerDatabaseApi.smartBackUpInventoryItems(
                                uniqueCompanyId,
                                inventoryItems,
                                uniqueInventoryItemIds
                            )
                        call!!.enqueue(object : Callback<AddEntitiesResponse> {
                            override fun onResponse(
                                call: Call<AddEntitiesResponse>,
                                response: Response<AddEntitiesResponse>
                            ) {
                                coroutineScope.launch {
                                    emit(Resource.Success(data = response.body()?.data.toNotNull()))
                                    AdditionEntityMarkers(context).saveAddedInventoryItemIds(emptyString)
                                    ChangesEntityMarkers(context).saveChangedInventoryItemIds(emptyString)
                                }
                            }

                            override fun onFailure(call: Call<AddEntitiesResponse>, t: Throwable) {
                                coroutineScope.launch {
                                    emit(
                                        Resource.Error(
                                            data = t.message,
                                            message = "Unknown error\nUnable to backup inventory items",
                                        )
                                    )
                                }
                            }
                        })
                    }

                    if (inventoryStocks.isNotEmpty()) {
                        val uniqueInventoryStockIds = ChangesEntityMarkers(context).getChangedInventoryStockIds.first().toUniqueIds()
                        emit(Resource.Loading("Backing up inventory stocks ..."))
                        val call =
                            shopManagerDatabaseApi.smartBackUpInventoryStocks(
                                uniqueCompanyId,
                                inventoryStocks,
                                uniqueInventoryStockIds
                            )
                        call!!.enqueue(object : Callback<AddEntitiesResponse> {
                            override fun onResponse(
                                call: Call<AddEntitiesResponse>,
                                response: Response<AddEntitiesResponse>
                            ) {
                                coroutineScope.launch {
                                    emit(Resource.Success(data = response.body()?.data.toNotNull()))
                                    AdditionEntityMarkers(context).saveAddedInventoryStockIds(emptyString)
                                    ChangesEntityMarkers(context).saveChangedInventoryStockIds(emptyString)
                                }
                            }

                            override fun onFailure(call: Call<AddEntitiesResponse>, t: Throwable) {
                                coroutineScope.launch {
                                    emit(
                                        Resource.Error(
                                            data = t.message,
                                            message = "Unknown error\nUnable to backup inventory stocks",
                                        )
                                    )
                                }
                            }
                        })
                    }

                    if (stocks.isNotEmpty()) {
                        val uniqueStockIds = ChangesEntityMarkers(context).getChangedStockIds.first().toUniqueIds()
                        emit(Resource.Loading("Backing up stocks ..."))
                        val call = shopManagerDatabaseApi.smartBackUpStocks(uniqueCompanyId, stocks, uniqueStockIds)
                        call!!.enqueue(object : Callback<AddEntitiesResponse> {
                            override fun onResponse(
                                call: Call<AddEntitiesResponse>,
                                response: Response<AddEntitiesResponse>
                            ) {
                                coroutineScope.launch {
                                    emit(Resource.Success(data = response.body()?.data.toNotNull()))
                                    AdditionEntityMarkers(context).saveAddedStockIds(emptyString)
                                    ChangesEntityMarkers(context).saveChangedStockIds(emptyString)
                                }
                            }

                            override fun onFailure(call: Call<AddEntitiesResponse>, t: Throwable) {
                                coroutineScope.launch {
                                    emit(
                                        Resource.Error(
                                            data = t.message,
                                            message = "Unknown error\nUnable to backup stocks",
                                        )
                                    )
                                }
                            }
                        })
                    }

                    if (personnel.isNotEmpty()) {
                        val uniquePersonnelIds = ChangesEntityMarkers(context).getChangedPersonnelIds.first().toUniqueIds()
                        Log.d("BackupRepository", "Personnel is not empty is called")
                        emit(Resource.Loading("Backing up personnel ..."))
                        val call =
                            shopManagerDatabaseApi.smartBackUpPersonnel(uniqueCompanyId, personnel, uniquePersonnelIds)
                        call!!.enqueue(object : Callback<AddEntitiesResponse> {
                            override fun onResponse(
                                call: Call<AddEntitiesResponse>,
                                response: Response<AddEntitiesResponse>
                            ) {
                                Log.d("BackupRepository", "Personnel data backup is successful")
                                coroutineScope.launch {
                                    emit(Resource.Success(data = response.body()?.data.toNotNull()))
                                    Log.d(
                                        "BackupRepository",
                                        "Personnel data is emitted successfully"
                                    )
                                    AdditionEntityMarkers(context).saveAddedPersonnelIds(emptyString)
                                    ChangesEntityMarkers(context).saveChangedPersonnelIds(emptyString)
                                }
                            }

                            override fun onFailure(call: Call<AddEntitiesResponse>, t: Throwable) {
                                coroutineScope.launch {
                                    emit(
                                        Resource.Error(
                                            data = t.message,
                                            message = "Unknown error\nUnable to backup personnel",
                                        )
                                    )
                                    Log.d(
                                        "BackupRepository",
                                        "Personnel data is emitted with failure"
                                    )
                                }
                            }
                        })
                    }

                    if (savings.isNotEmpty()) {
                        val uniqueSavingsIds = ChangesEntityMarkers(context).getChangedSavingsIds.first().toUniqueIds()
                        Log.d("BackupRepository", "Savings is not empty is called")
                        emit(Resource.Loading("Backing up savings ..."))
                        val call = shopManagerDatabaseApi.smartBackUpSavings(uniqueCompanyId, savings, uniqueSavingsIds)
                        call!!.enqueue(object : Callback<AddEntitiesResponse> {
                            override fun onResponse(
                                call: Call<AddEntitiesResponse>,
                                response: Response<AddEntitiesResponse>
                            ) {
                                Log.d("BackupRepository", "Savings data backup is successful")
                                coroutineScope.launch {
                                    emit(Resource.Success(data = response.body()?.data.toNotNull()))
                                    AdditionEntityMarkers(context).saveAddedSavingsIds(emptyString)
                                    ChangesEntityMarkers(context).saveChangedSavingsIds(emptyString)
                                }
                            }

                            override fun onFailure(call: Call<AddEntitiesResponse>, t: Throwable) {
                                coroutineScope.launch {
                                    emit(
                                        Resource.Error(
                                            data = t.message,
                                            message = "Unknown error\nUnable to backup savings",
                                        )
                                    )
                                }
                            }
                        })
                    }

                    if (withdrawals.isNotEmpty()) {
                        val uniqueWithdrawalIds = ChangesEntityMarkers(context).getChangedWithdrawalIds.first().toUniqueIds()
                        Log.d("BackupRepository", "Withdrawal is not empty is called")
                        emit(Resource.Loading("Backing up withdrawals ..."))
                        val call =
                            shopManagerDatabaseApi.smartBackUpWithdrawals(uniqueCompanyId, withdrawals, uniqueWithdrawalIds)
                        call!!.enqueue(object : Callback<AddEntitiesResponse> {
                            override fun onResponse(
                                call: Call<AddEntitiesResponse>,
                                response: Response<AddEntitiesResponse>
                            ) {
                                Log.d("BackupRepository", "Withdrawal data backup is successful")
                                coroutineScope.launch {
                                    emit(Resource.Success(data = response.body()?.data.toNotNull()))
                                    AdditionEntityMarkers(context).saveAddedWithdrawalIds(emptyString)
                                    ChangesEntityMarkers(context).saveChangedWithdrawalIds(emptyString)
                                }
                            }

                            override fun onFailure(call: Call<AddEntitiesResponse>, t: Throwable) {
                                coroutineScope.launch {
                                    emit(
                                        Resource.Error(
                                            data = t.message,
                                            message = "Unknown error\nUnable to backup withdrawals",
                                        )
                                    )
                                }
                            }
                        })
                    }

                    if (bankAccounts.isNotEmpty()) {
                        val uniqueBankAccountIds = ChangesEntityMarkers(context).getChangedBankAccountIds.first().toUniqueIds()
                        Log.d("BackupRepository", "Bank is not empty is called")
                        val call = shopManagerDatabaseApi.smartBackUpBankAccounts(uniqueCompanyId, bankAccounts, uniqueBankAccountIds)
                        call!!.enqueue(object : Callback<AddEntitiesResponse> {
                            override fun onResponse(
                                call: Call<AddEntitiesResponse>,
                                response: Response<AddEntitiesResponse>
                            ) {
                                Log.d("BackupRepository", "Bank data backup is successful")
                                coroutineScope.launch {
                                    emit(Resource.Success(data = response.body()?.data.toNotNull()))
                                    AdditionEntityMarkers(context).saveAddedBankAccountIds(emptyString)
                                    ChangesEntityMarkers(context).saveChangedBankAccountIds(emptyString)
                                }
                            }

                            override fun onFailure(call: Call<AddEntitiesResponse>, t: Throwable) {
                                coroutineScope.launch {
                                    emit(
                                        Resource.Error(
                                            data = t.message,
                                            message = "Unknown error\nUnable to backup banks",
                                        )
                                    )
                                }
                            }
                        })
                    }

                    //emit(Resource.Success("All data successfully backed up"))

                }
            }

        }
        catch (e: Exception){
            emit(Resource.Error(
                message = "Could not back up any/all of the data",
                data = e.message
            ))
        }
        */
    }

    @OptIn(DelicateCoroutinesApi::class)
    override suspend fun smartBackup1(){
        val context = MyShopManagerApp.applicationContext()
        val userPreferences = UserPreferences(context)
        var loadingValue = 0.0
        try {
            val isLoggedIn = userPreferences.getLoggedInState.first()
            /*val shopInfoJson = userPreferences.getShopInfo.first()
            val uniqueCompanyId = shopInfoJson.toCompanyEntity()?.uniqueCompanyId*/
            var uniqueCompanyId: String? = UniqueCompanyId

            userPreferences.saveRepositoryJobSuccessValue(false)
            userPreferences.saveRepositoryJobMessage(emptyString)
            userPreferences.saveDoubleValue(loadingValue)
            Log.d("BackupRepository", "repository - smartBackup1() - double value = ${userPreferences.getDoubleValue.first().toNotNull()}")

            when(true){
                (isLoggedIn != true)->{
                    userPreferences.saveRepositoryJobSuccessValue(false)
                    userPreferences.saveRepositoryJobMessage("Could not back up data. \nYou are not logged in into any account")
                    loadingValue = 1.0
                    userPreferences.saveDoubleValue(loadingValue)
                }
                (uniqueCompanyId.isNullOrBlank())->{
                    userPreferences.saveRepositoryJobSuccessValue(false)
                    userPreferences.saveRepositoryJobMessage("Could not back up data." +
                            "\nCould not get the shop account details" +
                            "\nPlease ensure that you are logged in")
                    loadingValue = 1.0
                    userPreferences.saveDoubleValue(loadingValue)
                }
                else -> {
                    val additionEntityMarkers  = AdditionEntityMarkers(context)
                    val changesEntityMarkers  = ChangesEntityMarkers(context)

                    val allCustomers = appDatabase.customerDao.getAllCustomers()?: emptyList()
                    val addedCustomerIds = additionEntityMarkers.getAddedCustomerIds.first().toUniqueIds().map { it.uniqueId }
                    val customers = allCustomers.filter { addedCustomerIds.contains(it.uniqueCustomerId) }.map { it.toCustomerInfoDto(uniqueCompanyId) }

                    val allReceipts = appDatabase.receiptDao.getAllReceipts() ?: emptyList()
                    val addedReceiptIds = additionEntityMarkers.getAddedReceiptIds.first().toUniqueIds().map { it.uniqueId }
                    val receipts = allReceipts.filter { addedReceiptIds.contains(it.uniqueReceiptId) }.map { it.toReceiptInfoDto(uniqueCompanyId) }

                    val allDebts = appDatabase.debtDao.getAllDebt() ?: emptyList()
                    val addedDebtIds = additionEntityMarkers.getAddedDebtIds.first().toUniqueIds().map { it.uniqueId }
                    val debts = allDebts.filter { addedDebtIds.contains(it.uniqueDebtId) }.map { it.toDebtInfoDto(uniqueCompanyId) }

                    val allDebtRepayments = appDatabase.debtRepaymentDao.getAllDebtRepayment() ?: emptyList()
                    val addedDebtRepaymentsIds = additionEntityMarkers.getAddedDebtRepaymentIds.first().toUniqueIds().map { it.uniqueId }
                    val debtRepayments = allDebtRepayments.filter { addedDebtRepaymentsIds.contains(it.uniqueDebtRepaymentId) }.map { it.toDebtRepaymentInfoDto(uniqueCompanyId) }

                    val allExpenses = appDatabase.expenseDao.getAllExpenses() ?: emptyList()
                    val addedExpensesIds = additionEntityMarkers.getAddedExpenseIds.first().toUniqueIds().map { it.uniqueId }
                    val expenses = allExpenses.filter { addedExpensesIds.contains(it.uniqueExpenseId) }.map { it.toExpenseInfoDto(uniqueCompanyId) }

                    val allInventories = appDatabase.inventoryDao.getAllInventories() ?: emptyList()
                    val addedInventoriesIds = additionEntityMarkers.getAddedExpenseIds.first().toUniqueIds().map { it.uniqueId }
                    val inventories = allInventories.filter { addedInventoriesIds.contains(it.uniqueInventoryId) }.map { it.toInventoryInfoDto(uniqueCompanyId) }

                    val allInventoryItems = appDatabase.inventoryItemDao.getAllInventoryItems() ?: emptyList()
                    val addedInventoryItemsIds = additionEntityMarkers.getAddedInventoryItemIds.first().toUniqueIds().map { it.uniqueId }
                    val inventoryItems = allInventoryItems.filter { addedInventoryItemsIds.contains(it.uniqueInventoryItemId) }.map { it.toInventoryItemInfoDto(uniqueCompanyId) }

                    val allPersonnel = appDatabase.personnelDao.getAllPersonnel() ?: emptyList()
                    val addedPersonnelIds = additionEntityMarkers.getAddedInventoryItemIds.first().toUniqueIds().map { it.uniqueId }
                    val personnel = allPersonnel.filter { addedPersonnelIds.contains(it.uniquePersonnelId) }.map { it.toPersonnelInfoDto(uniqueCompanyId) }

                    val allSuppliers = appDatabase.supplierDao.getAllSuppliers() ?: emptyList()
                    val addedSuppliersIds = additionEntityMarkers.getAddedSupplierIds.first().toUniqueIds().map { it.uniqueId }
                    val suppliers = allSuppliers.filter { addedSuppliersIds.contains(it.uniqueSupplierId) }.map { it.toSupplierInfoDto(uniqueCompanyId) }

                    val allRevenues = appDatabase.revenueDao.getAllRevenues() ?: emptyList()
                    val addedRevenuesIds = additionEntityMarkers.getAddedRevenueIds.first().toUniqueIds().map { it.uniqueId }
                    val revenues = allRevenues.filter { addedRevenuesIds.contains(it.uniqueRevenueId) }.map { it.toRevenueInfoDto(uniqueCompanyId) }


                    val allWithdrawals = appDatabase.withdrawalDao.getAllWithdrawals() ?: emptyList()
                    val addedWithdrawalsIds = additionEntityMarkers.getAddedWithdrawalIds.first().toUniqueIds().map { it.uniqueId }
                    val withdrawals = allWithdrawals.filter { addedWithdrawalsIds.contains(it.uniqueWithdrawalId) }.map { it.toWithdrawalInfoDto(uniqueCompanyId) }


                    val allInventoryStocks = appDatabase.inventoryStockDao.getAllInventoryStock() ?: emptyList()
                    val addedInventoryStocksIds = additionEntityMarkers.getAddedInventoryStockIds.first().toUniqueIds().map { it.uniqueId }
                    val inventoryStocks = allInventoryStocks.filter { addedInventoryStocksIds.contains(it.uniqueInventoryStockId) }.map { it.toInventoryStockInfoDto(uniqueCompanyId) }

                    val allSavings = appDatabase.savingsDao.getAllSavings() ?: emptyList()
                    val addedSavingsIds = additionEntityMarkers.getAddedSavingsIds.first().toUniqueIds().map { it.uniqueId }
                    val savings = allSavings.filter { addedSavingsIds.contains(it.uniqueSavingsId) }.map { it.toSavingsInfoDto(uniqueCompanyId) }

                    val allStocks = appDatabase.stockDao.getAllStocks() ?: emptyList()
                    val addedStocksIds = additionEntityMarkers.getAddedStockIds.first().toUniqueIds().map { it.uniqueId }
                    val stocks = allStocks.filter { addedStocksIds.contains(it.uniqueStockId) }.map { it.toStockInfoDto(uniqueCompanyId) }

                    val allCashIns = appDatabase.cashInDao.getAllCashIns() ?: emptyList()
                    val addedCashInsIds = additionEntityMarkers.getAddedCashInIds.first().toUniqueIds().map { it.uniqueId }
                    val cashIns = allCashIns.filter { addedCashInsIds.contains(it.uniqueCashInId) }.map { it.toCashInfoDto(uniqueCompanyId) }

                    val allBankAccounts = appDatabase.bankAccountDao.getAllBankAccounts() ?: emptyList()
                    val addedBankAccountsIds = additionEntityMarkers.getAddedBankAccountIds.first().toUniqueIds().map { it.uniqueId }
                    val bankAccounts = allBankAccounts.filter { addedBankAccountsIds.contains(it.uniqueBankAccountId) }.map { it.toBankAccountInfoDto(uniqueCompanyId) }

                    
                    val uniqueExpenseIds = changesEntityMarkers.getChangedExpenseIds.first().toUniqueIds()
                    val uniqueCustomerIds = changesEntityMarkers.getChangedCustomerIds.first().toUniqueIds()
                    val uniqueSupplierIds = changesEntityMarkers.getChangedSupplierIds.first().toUniqueIds()
                    val uniqueBankAccountIds = changesEntityMarkers.getChangedBankAccountIds.first().toUniqueIds()
                    val uniqueWithdrawalIds = changesEntityMarkers.getChangedWithdrawalIds.first().toUniqueIds()
                    val uniqueSavingsIds = changesEntityMarkers.getChangedSavingsIds.first().toUniqueIds()
                    val uniquePersonnelIds = changesEntityMarkers.getChangedPersonnelIds.first().toUniqueIds()
                    val uniqueStockIds = changesEntityMarkers.getChangedStockIds.first().toUniqueIds()
                    val uniqueInventoryStockIds = changesEntityMarkers.getChangedInventoryStockIds.first().toUniqueIds()
                    val uniqueInventoryItemIds = changesEntityMarkers.getChangedInventoryItemIds.first().toUniqueIds()
                    val uniqueInventoryIds = changesEntityMarkers.getChangedInventoryIds.first().toUniqueIds()
                    val uniqueRevenueIds = changesEntityMarkers.getChangedRevenueIds.first().toUniqueIds()
                    val uniqueDebtRepaymentIds = changesEntityMarkers.getChangedDebtRepaymentIds.first().toUniqueIds()
                    val uniqueDebtIds = changesEntityMarkers.getChangedDebtIds.first().toUniqueIds()
                    val uniqueReceiptIds = changesEntityMarkers.getChangedReceiptIds.first().toUniqueIds()
                    val uniqueCashInIds = changesEntityMarkers.getChangedCashInIds.first().toUniqueIds()

                    userPreferences.saveRepositoryJobMessage("All data to be backed up is loaded.\n" + "Is backing up data...")
                    loadingValue = loadingValue.plus(0.52)
                    userPreferences.saveDoubleValue(loadingValue)

                    if (expenses.isNotEmpty() || uniqueExpenseIds.isNotEmpty()) {
                        userPreferences.saveRepositoryJobMessage("Is backing up expenses...")
                        val call = shopManagerDatabaseApi.smartBackUpExpenses(uniqueCompanyId, SmartExpenses( expenses, uniqueExpenseIds))
                        call!!.enqueue(object : Callback<AddEntitiesResponse> {
                            override fun onResponse(
                                call: Call<AddEntitiesResponse>,
                                response: Response<AddEntitiesResponse>
                            ) {
                                GlobalScope.launch(Dispatchers.IO + Job()) {
                                    loadingValue = loadingValue.plus(0.03)
                                    userPreferences.saveDoubleValue(loadingValue)
                                    val data = response.body()?.data.toNotNull()
                                    val message = response.body()?.message.toNotNull()
                                    userPreferences.saveRepositoryJobMessage("$data\n$message")
                                    additionEntityMarkers.saveAddedExpenseIds(emptyString)
                                    changesEntityMarkers.saveChangedExpenseIds(emptyString)
                                }
                            }

                            override fun onFailure(call: Call<AddEntitiesResponse>, t: Throwable) {

                                GlobalScope.launch(Dispatchers.IO + Job()) {
                                    userPreferences.saveRepositoryJobSuccessValue(false)
                                    userPreferences.saveRepositoryJobMessage("${ if(t.message.isNullOrBlank()) UnknownError else t.message} \n" +
                                            "Unable to backup expenses")
                                }
                            }
                        })
                    }
                    else{
                        loadingValue = loadingValue.plus(0.03)
                        userPreferences.saveDoubleValue(loadingValue)
                    }

                    if (customers.isNotEmpty() || uniqueCustomerIds.isNotEmpty()) {
                        userPreferences.saveRepositoryJobMessage("Is backing up customers...")
                        val call = shopManagerDatabaseApi.smartBackUpCustomers(uniqueCompanyId, SmartCustomers( customers, uniqueCustomerIds))
                        call!!.enqueue(object : Callback<AddEntitiesResponse> {
                            override fun onResponse(
                                call: Call<AddEntitiesResponse>,
                                response: Response<AddEntitiesResponse>
                            ) {
                                GlobalScope.launch(Dispatchers.IO + Job()) {
                                    loadingValue = loadingValue.plus(0.03)
                                    userPreferences.saveDoubleValue(loadingValue)
                                                                        loadingValue = loadingValue.plus(0.03)
                                    userPreferences.saveDoubleValue(loadingValue)
                                    val data = response.body()?.data.toNotNull()
                                    val message = response.body()?.message.toNotNull()
                                    userPreferences.saveRepositoryJobMessage("$data\n$message")
                                    additionEntityMarkers.saveAddedCustomerIds(emptyString)
                                    changesEntityMarkers.saveChangedCustomerIds(emptyString)
                                }
                            }

                            override fun onFailure(call: Call<AddEntitiesResponse>, t: Throwable) {
                                GlobalScope.launch(Dispatchers.IO + Job()) {
                                    userPreferences.saveRepositoryJobMessage("${t.message} \nUnknown error\n" +
                                            "Unable to backup customers")
                                }
                            }
                        })
                    }
                    else{
                        loadingValue = loadingValue.plus(0.03)
                        userPreferences.saveDoubleValue(loadingValue)
                    }

                    if (suppliers.isNotEmpty() || uniqueSupplierIds.isNotEmpty()) {
                        val call = shopManagerDatabaseApi.smartBackUpSuppliers(uniqueCompanyId, SmartSuppliers( suppliers, uniqueSupplierIds))
                        call!!.enqueue(object : Callback<AddEntitiesResponse> {
                            override fun onResponse(
                                call: Call<AddEntitiesResponse>,
                                response: Response<AddEntitiesResponse>
                            ) {
                                GlobalScope.launch(Dispatchers.IO + Job()) {
                                    loadingValue = loadingValue.plus(0.03)
                                    userPreferences.saveDoubleValue(loadingValue)
                                    val data = response.body()?.data.toNotNull()
                                    val message = response.body()?.message.toNotNull()
                                    userPreferences.saveRepositoryJobMessage("$data\n$message")
                                    additionEntityMarkers.saveAddedSupplierIds(emptyString)
                                    changesEntityMarkers.saveChangedSupplierIds(emptyString)
                                }
                            }

                            override fun onFailure(call: Call<AddEntitiesResponse>, t: Throwable) {
                                GlobalScope.launch(Dispatchers.IO + Job()) {
                                    userPreferences.saveRepositoryJobMessage("${t.message} \nUnknown error\n" +
                                            "Unable to backup suppliers")
                                }
                            }
                        })
                    }
                    else{
                        loadingValue = loadingValue.plus(0.03)
                        userPreferences.saveDoubleValue(loadingValue)
                    }

                    if (cashIns.isNotEmpty() || uniqueCashInIds.isNotEmpty()) {
                        Log.d("BackupRepository", "Debt is not empty is called")
                        userPreferences.saveRepositoryJobMessage("Is backing up cash ins...")
                        val call = shopManagerDatabaseApi.smartBackUpCashIns(uniqueCompanyId, SmartCashIns(cashIns, uniqueCashInIds))
                        call!!.enqueue(object : Callback<AddEntitiesResponse> {
                            override fun onResponse(
                                call: Call<AddEntitiesResponse>,
                                response: Response<AddEntitiesResponse>
                            ) {
                                Log.d("BackupRepository", "Cash in data backup is successful")
                                GlobalScope.launch(Dispatchers.IO + Job()) {
                                                                        loadingValue = loadingValue.plus(0.03)
                                    userPreferences.saveDoubleValue(loadingValue)
                                    val data = response.body()?.data.toNotNull()
                                    val message = response.body()?.message.toNotNull()
                                    userPreferences.saveRepositoryJobMessage("$data\n$message")
                                    additionEntityMarkers.saveAddedCashInIds(emptyString)
                                    changesEntityMarkers.saveChangedCashInIds(emptyString)
                                }
                            }

                            override fun onFailure(call: Call<AddEntitiesResponse>, t: Throwable) {
                                GlobalScope.launch(Dispatchers.IO + Job()) {
                                    userPreferences.saveRepositoryJobMessage("${t.message} \nUnknown error\n" +
                                            "Unable to backup cash ins")
                                    Log.d(
                                        "BackupRepository",
                                        "Cash in data backup is emitted with failure"
                                    )
                                }
                                Log.d("BackupRepository", "Cash in data backup failed")
                            }
                        })
                    }
                    else{
                        loadingValue = loadingValue.plus(0.03)
                        userPreferences.saveDoubleValue(loadingValue)
                    }

                    if (receipts.isNotEmpty() || uniqueReceiptIds.isNotEmpty()) {
                        Log.d("BackupRepository", "Receipt is not empty is called")
                        userPreferences.saveRepositoryJobMessage("Is backing up receipts...")
                        val call = shopManagerDatabaseApi.smartBackUpReceipts(uniqueCompanyId, SmartReceipts(receipts, uniqueReceiptIds))
                        call!!.enqueue(object : Callback<AddEntitiesResponse> {
                            override fun onResponse(
                                call: Call<AddEntitiesResponse>,
                                response: Response<AddEntitiesResponse>
                            ) {
                                Log.d("BackupRepository", "Receipt data backup is successful")
                                GlobalScope.launch(Dispatchers.IO + Job()) {
                                                                        loadingValue = loadingValue.plus(0.03)
                                    userPreferences.saveDoubleValue(loadingValue)
                                    val data = response.body()?.data.toNotNull()
                                    val message = response.body()?.message.toNotNull()
                                    userPreferences.saveRepositoryJobMessage("$data\n$message")
                                    additionEntityMarkers.saveAddedReceiptIds(emptyString)
                                    changesEntityMarkers.saveChangedReceiptIds(emptyString)
                                }
                            }

                            override fun onFailure(call: Call<AddEntitiesResponse>, t: Throwable) {
                                GlobalScope.launch(Dispatchers.IO + Job()) {
                                    userPreferences.saveRepositoryJobMessage("${t.message} \nUnknown error\n" +
                                            "Unable to backup receipts")
                                    Log.d(
                                        "BackupRepository",
                                        "Receipt data backup is emitted with failure"
                                    )
                                }
                                Log.d("BackupRepository", "Receipt data backup failed")
                            }
                        })
                    }
                    else{
                        loadingValue = loadingValue.plus(0.03)
                        userPreferences.saveDoubleValue(loadingValue)
                    }

                    if (debts.isNotEmpty() || uniqueDebtIds.isNotEmpty()) {
                        userPreferences.saveRepositoryJobMessage("Is backing up debts...")
                        val call = shopManagerDatabaseApi.smartBackUpDebts(uniqueCompanyId, SmartDebts(debts, uniqueDebtIds))
                        call!!.enqueue(object : Callback<AddEntitiesResponse> {
                            override fun onResponse(
                                call: Call<AddEntitiesResponse>,
                                response: Response<AddEntitiesResponse>
                            ) {
                                GlobalScope.launch(Dispatchers.IO + Job()) {
                                    loadingValue = loadingValue.plus(0.03)
                                    userPreferences.saveDoubleValue(loadingValue)
                                    val data = response.body()?.data.toNotNull()
                                    val message = response.body()?.message.toNotNull()
                                    userPreferences.saveRepositoryJobMessage("$data\n$message")
                                    additionEntityMarkers.saveAddedDebtIds(emptyString)
                                    changesEntityMarkers.saveChangedDebtIds(emptyString)
                                }
                            }

                            override fun onFailure(call: Call<AddEntitiesResponse>, t: Throwable) {
                                GlobalScope.launch(Dispatchers.IO + Job()) {
                                    userPreferences.saveRepositoryJobMessage("${t.message} \nUnknown error\n" +
                                            "Unable to backup debts")
                                }
                            }
                        })
                    }
                    else{
                        loadingValue = loadingValue.plus(0.03)
                        userPreferences.saveDoubleValue(loadingValue)
                    }

                    if (debtRepayments.isNotEmpty() || uniqueDebtRepaymentIds.isNotEmpty()) {
                        userPreferences.saveRepositoryJobMessage("Is backing up debt repayments...")
                        val call =
                            shopManagerDatabaseApi.smartBackUpDebtRepayments(
                                uniqueCompanyId,
                                SmartDebtRepayments(
                                debtRepayments,
                                uniqueDebtRepaymentIds)
                            )
                        call!!.enqueue(object : Callback<AddEntitiesResponse> {
                            override fun onResponse(
                                call: Call<AddEntitiesResponse>,
                                response: Response<AddEntitiesResponse>
                            ) {
                                GlobalScope.launch(Dispatchers.IO + Job()) {
                                    loadingValue = loadingValue.plus(0.03)
                                    userPreferences.saveDoubleValue(loadingValue)
                                    val data = response.body()?.data.toNotNull()
                                    val message = response.body()?.message.toNotNull()
                                    userPreferences.saveRepositoryJobMessage("$data\n$message")
                                    additionEntityMarkers.saveAddedDebtRepaymentIds(emptyString)
                                    changesEntityMarkers.saveChangedDebtRepaymentIds(emptyString)
                                }
                            }

                            override fun onFailure(call: Call<AddEntitiesResponse>, t: Throwable) {
                                GlobalScope.launch(Dispatchers.IO + Job()) {
                                    userPreferences.saveRepositoryJobMessage("${t.message} \nUnknown error\n" +
                                            "Unable to backup debt repayments")
                                }
                            }
                        })
                    }
                    else{
                        loadingValue = loadingValue.plus(0.03)
                        userPreferences.saveDoubleValue(loadingValue)
                    }

                    if (revenues.isNotEmpty() || uniqueRevenueIds.isNotEmpty()) {
                        userPreferences.saveRepositoryJobMessage("Is backing up revenues...")
                        val call = shopManagerDatabaseApi.smartBackUpRevenues(uniqueCompanyId, SmartRevenues(revenues, uniqueRevenueIds))
                        call!!.enqueue(object : Callback<AddEntitiesResponse> {
                            override fun onResponse(
                                call: Call<AddEntitiesResponse>,
                                response: Response<AddEntitiesResponse>
                            ) {
                                GlobalScope.launch(Dispatchers.IO + Job()) {
                                    loadingValue = loadingValue.plus(0.03)
                                    userPreferences.saveDoubleValue(loadingValue)
                                    val data = response.body()?.data.toNotNull()
                                    val message = response.body()?.message.toNotNull()
                                    userPreferences.saveRepositoryJobMessage("$data\n$message")
                                    additionEntityMarkers.saveAddedRevenueIds(emptyString)
                                    changesEntityMarkers.saveChangedRevenueIds(emptyString)
                                }
                            }

                            override fun onFailure(call: Call<AddEntitiesResponse>, t: Throwable) {
                                GlobalScope.launch(Dispatchers.IO + Job()) {
                                    userPreferences.saveRepositoryJobMessage("${t.message} \nUnknown error\n" +
                                            "Unable to backup revenues")
                                }
                            }
                        })
                    }
                    else{
                        loadingValue = loadingValue.plus(0.03)
                        userPreferences.saveDoubleValue(loadingValue)
                    }

                    if (inventories.isNotEmpty() || uniqueInventoryIds.isNotEmpty()) {
                        userPreferences.saveRepositoryJobMessage("Is backing up inventories...")
                        val call = shopManagerDatabaseApi.smartBackUpInventories(uniqueCompanyId, SmartInventories(inventories, uniqueInventoryIds))
                        call!!.enqueue(object : Callback<AddEntitiesResponse> {
                            override fun onResponse(
                                call: Call<AddEntitiesResponse>,
                                response: Response<AddEntitiesResponse>
                            ) {
                                GlobalScope.launch(Dispatchers.IO + Job()) {
                                                                        loadingValue = loadingValue.plus(0.03)
                                    userPreferences.saveDoubleValue(loadingValue)
                                    val data = response.body()?.data.toNotNull()
                                    val message = response.body()?.message.toNotNull()
                                    userPreferences.saveRepositoryJobMessage("$data\n$message")
                                    additionEntityMarkers.saveAddedInventoryIds(emptyString)
                                    changesEntityMarkers.saveChangedInventoryIds(emptyString)
                                }
                            }

                            override fun onFailure(call: Call<AddEntitiesResponse>, t: Throwable) {
                                GlobalScope.launch(Dispatchers.IO + Job()) {
                                    userPreferences.saveRepositoryJobMessage("${t.message} \nUnknown error\n" +
                                            "Unable to backup inventories")
                                }
                            }
                        })
                    }
                    else{
                        loadingValue = loadingValue.plus(0.03)
                        userPreferences.saveDoubleValue(loadingValue)
                    }

                    if (inventoryItems.isNotEmpty() || uniqueInventoryItemIds.isNotEmpty()) {
                        userPreferences.saveRepositoryJobMessage("Is backing up inventory items...")
                        val call =
                            shopManagerDatabaseApi.smartBackUpInventoryItems(
                                uniqueCompanyId,
                                SmartInventoryItems(
                                inventoryItems,
                                uniqueInventoryItemIds
                                )
                            )
                        call!!.enqueue(object : Callback<AddEntitiesResponse> {
                            override fun onResponse(
                                call: Call<AddEntitiesResponse>,
                                response: Response<AddEntitiesResponse>
                            ) {
                                GlobalScope.launch(Dispatchers.IO + Job()) {
                                                                        loadingValue = loadingValue.plus(0.03)
                                    userPreferences.saveDoubleValue(loadingValue)
                                    val data = response.body()?.data.toNotNull()
                                    val message = response.body()?.message.toNotNull()
                                    userPreferences.saveRepositoryJobMessage("$data\n$message")
                                    additionEntityMarkers.saveAddedInventoryItemIds(emptyString)
                                    changesEntityMarkers.saveChangedInventoryItemIds(emptyString)
                                }
                            }

                            override fun onFailure(call: Call<AddEntitiesResponse>, t: Throwable) {
                                GlobalScope.launch(Dispatchers.IO + Job()) {
                                    userPreferences.saveRepositoryJobMessage("${t.message} \nUnknown error\n" +
                                            "Unable to backup inventory items")
                                }
                            }
                        })
                    }
                    else{
                        loadingValue = loadingValue.plus(0.03)
                        userPreferences.saveDoubleValue(loadingValue)
                    }

                    if (inventoryStocks.isNotEmpty() || uniqueInventoryStockIds.isNotEmpty()) {
                        userPreferences.saveRepositoryJobMessage("Is backing up inventory stocks...")
                        val call =
                            shopManagerDatabaseApi.smartBackUpInventoryStocks(
                                uniqueCompanyId,
                                SmartInventoryStocks(
                                inventoryStocks,
                                uniqueInventoryStockIds)
                            )
                        call!!.enqueue(object : Callback<AddEntitiesResponse> {
                            override fun onResponse(
                                call: Call<AddEntitiesResponse>,
                                response: Response<AddEntitiesResponse>
                            ) {
                                GlobalScope.launch(Dispatchers.IO + Job()) {
                                                                        loadingValue = loadingValue.plus(0.03)
                                    userPreferences.saveDoubleValue(loadingValue)
                                    val data = response.body()?.data.toNotNull()
                                    val message = response.body()?.message.toNotNull()
                                    userPreferences.saveRepositoryJobMessage("$data\n$message")
                                    additionEntityMarkers.saveAddedInventoryStockIds(emptyString)
                                    changesEntityMarkers.saveChangedInventoryStockIds(emptyString)
                                }
                            }

                            override fun onFailure(call: Call<AddEntitiesResponse>, t: Throwable) {
                                GlobalScope.launch(Dispatchers.IO + Job()) {
                                    userPreferences.saveRepositoryJobMessage("${t.message} \nUnknown error\n" +
                                            "Unable to backup inventory stocks")
                                }
                            }
                        })
                    }
                    else{
                        loadingValue = loadingValue.plus(0.03)
                        userPreferences.saveDoubleValue(loadingValue)
                    }

                    if (stocks.isNotEmpty() || uniqueStockIds.isNotEmpty()) {
                        userPreferences.saveRepositoryJobMessage("Is backing up stocks...")
                        val call = shopManagerDatabaseApi.smartBackUpStocks(uniqueCompanyId, SmartStocks(stocks, uniqueStockIds))
                        call!!.enqueue(object : Callback<AddEntitiesResponse> {
                            override fun onResponse(
                                call: Call<AddEntitiesResponse>,
                                response: Response<AddEntitiesResponse>
                            ) {
                                GlobalScope.launch(Dispatchers.IO + Job()) {
                                                                        loadingValue = loadingValue.plus(0.03)
                                    userPreferences.saveDoubleValue(loadingValue)
                                    val data = response.body()?.data.toNotNull()
                                    val message = response.body()?.message.toNotNull()
                                    userPreferences.saveRepositoryJobMessage("$data\n$message")
                                    additionEntityMarkers.saveAddedStockIds(emptyString)
                                    changesEntityMarkers.saveChangedStockIds(emptyString)
                                }
                            }

                            override fun onFailure(call: Call<AddEntitiesResponse>, t: Throwable) {
                                GlobalScope.launch(Dispatchers.IO + Job()) {
                                    userPreferences.saveRepositoryJobMessage("${t.message} \nUnknown error\n" +
                                            "Unable to backup stocks")
                                }
                            }
                        })
                    }
                    else{
                        loadingValue = loadingValue.plus(0.03)
                        userPreferences.saveDoubleValue(loadingValue)
                    }

                    if (personnel.isNotEmpty() || uniquePersonnelIds.isNotEmpty()) {
                        Log.d("BackupRepository", "Personnel is not empty is called")
                        userPreferences.saveRepositoryJobMessage("Is backing up personnel...")
                        val call =
                            shopManagerDatabaseApi.smartBackUpPersonnel(uniqueCompanyId, SmartPersonnel(personnel, uniquePersonnelIds))
                        call!!.enqueue(object : Callback<AddEntitiesResponse> {
                            override fun onResponse(
                                call: Call<AddEntitiesResponse>,
                                response: Response<AddEntitiesResponse>
                            ) {
                                Log.d("BackupRepository", "Personnel data backup is successful")
                                GlobalScope.launch(Dispatchers.IO + Job()) {
                                                                        loadingValue = loadingValue.plus(0.03)
                                    userPreferences.saveDoubleValue(loadingValue)
                                    val data = response.body()?.data.toNotNull()
                                    val message = response.body()?.message.toNotNull()
                                    userPreferences.saveRepositoryJobMessage("$data\n$message")
                                    Log.d(
                                        "BackupRepository",
                                        "Personnel data is emitted successfully"
                                    )
                                    additionEntityMarkers.saveAddedPersonnelIds(emptyString)
                                    changesEntityMarkers.saveChangedPersonnelIds(emptyString)
                                }
                            }

                            override fun onFailure(call: Call<AddEntitiesResponse>, t: Throwable) {
                                GlobalScope.launch(Dispatchers.IO + Job()) {
                                    userPreferences.saveRepositoryJobMessage("${t.message}Unknown error\n" +
                                            "Unable to backup personnel")
                                    Log.d(
                                        "BackupRepository",
                                        "Personnel data is emitted with failure"
                                    )
                                }
                            }
                        })
                    }
                    else{
                        loadingValue = loadingValue.plus(0.03)
                        userPreferences.saveDoubleValue(loadingValue)
                    }

                    if (savings.isNotEmpty() || uniqueSavingsIds.isNotEmpty()) {
                        Log.d("BackupRepository", "Savings is not empty is called")
                        userPreferences.saveRepositoryJobMessage("Is backing up savings...")
                        val call = shopManagerDatabaseApi.smartBackUpSavings(uniqueCompanyId, SmartSavings(savings, uniqueSavingsIds))
                        call!!.enqueue(object : Callback<AddEntitiesResponse> {
                            override fun onResponse(
                                call: Call<AddEntitiesResponse>,
                                response: Response<AddEntitiesResponse>
                            ) {
                                GlobalScope.launch(Dispatchers.IO + Job()) {
                                                                        loadingValue = loadingValue.plus(0.03)
                                    userPreferences.saveDoubleValue(loadingValue)
                                    val data = response.body()?.data.toNotNull()
                                    val message = response.body()?.message.toNotNull()
                                    userPreferences.saveRepositoryJobMessage("$data\n$message")
                                    additionEntityMarkers.saveAddedSavingsIds(emptyString)
                                    changesEntityMarkers.saveChangedSavingsIds(emptyString)
                                }
                            }

                            override fun onFailure(call: Call<AddEntitiesResponse>, t: Throwable) {
                                GlobalScope.launch(Dispatchers.IO + Job()) {
                                    userPreferences.saveRepositoryJobMessage("${t.message}Unknown error\n" +
                                            "Unable to backup savings")

                                }
                            }
                        })
                    }
                    else{
                        loadingValue = loadingValue.plus(0.03)
                        userPreferences.saveDoubleValue(loadingValue)
                    }

                    if (withdrawals.isNotEmpty() || uniqueWithdrawalIds.isNotEmpty()) {
                        Log.d("BackupRepository", "Withdrawal is not empty is called")
                        userPreferences.saveRepositoryJobMessage("Is backing up withdrawals...")
                        val call =
                            shopManagerDatabaseApi.smartBackUpWithdrawals(uniqueCompanyId, SmartWithdrawals(withdrawals, uniqueWithdrawalIds))
                        call!!.enqueue(object : Callback<AddEntitiesResponse> {
                            override fun onResponse(
                                call: Call<AddEntitiesResponse>,
                                response: Response<AddEntitiesResponse>
                            ) {
                                GlobalScope.launch(Dispatchers.IO + Job()) {
                                                                        loadingValue = loadingValue.plus(0.03)
                                    userPreferences.saveDoubleValue(loadingValue)
                                    val data = response.body()?.data.toNotNull()
                                    val message = response.body()?.message.toNotNull()
                                    userPreferences.saveRepositoryJobMessage("$data\n$message")
                                    additionEntityMarkers.saveAddedWithdrawalIds(emptyString)
                                    changesEntityMarkers.saveChangedWithdrawalIds(emptyString)
                                }
                            }

                            override fun onFailure(call: Call<AddEntitiesResponse>, t: Throwable) {
                                GlobalScope.launch(Dispatchers.IO + Job()) {
                                    userPreferences.saveRepositoryJobMessage("${t.message}Unknown error\n" +
                                            "Unable to backup withdrawals")
                                }
                            }
                        })
                    }
                    else{
                        loadingValue = loadingValue.plus(0.03)
                        userPreferences.saveDoubleValue(loadingValue)
                    }

                    if (bankAccounts.isNotEmpty() || uniqueBankAccountIds.isNotEmpty()) {
                        Log.d("BackupRepository", "Bank is not empty is called")
                        val call = shopManagerDatabaseApi.smartBackUpBankAccounts(uniqueCompanyId, SmartBankAccount(bankAccounts, uniqueBankAccountIds))
                        call!!.enqueue(object : Callback<AddEntitiesResponse> {
                            override fun onResponse(
                                call: Call<AddEntitiesResponse>,
                                response: Response<AddEntitiesResponse>
                            ) {
                                GlobalScope.launch(Dispatchers.IO + Job()) {
                                    loadingValue = loadingValue.plus(0.03)
                                    userPreferences.saveDoubleValue(loadingValue)
                                    val data = response.body()?.data.toNotNull()
                                    val message = response.body()?.message.toNotNull()
                                    userPreferences.saveRepositoryJobMessage("$data\n$message")
                                    additionEntityMarkers.saveAddedBankAccountIds(emptyString)
                                    changesEntityMarkers.saveChangedBankAccountIds(emptyString)
                                }
                            }

                            override fun onFailure(call: Call<AddEntitiesResponse>, t: Throwable) {
                                GlobalScope.launch(Dispatchers.IO + Job()) {
                                    userPreferences.saveRepositoryJobMessage("${t.message}Unknown error\n" +
                                            "Unable to backup withdrawals")
                                }
                            }
                        })
                    }
                    else{
                        loadingValue = loadingValue.plus(0.03)
                        userPreferences.saveDoubleValue(loadingValue)
                    }

                    userPreferences.saveRepositoryJobMessage("Smart back up complete")
                    Log.d("BackupRepository", "repository - smartBackup1() - double value = ${userPreferences.getDoubleValue.first().toNotNull()}")
                }
            }
        }catch (e: Exception){
            Log.d("BackupRepository", "${e.message}")
            userPreferences.saveRepositoryJobMessage("${e.message}Unknown error\n" +
                    "Could not back up any/all of the data")
            userPreferences.saveDoubleValue(1.0)
        }
    }

    override suspend fun absoluteSyncCompanyInfo(){
        val context = MyShopManagerApp.applicationContext()
        val userPreferences = UserPreferences(context)
        try {
            userPreferences.saveRepositoryJobMessage(emptyString)
            userPreferences.saveDoubleValue(0.0)
            val isLoggedIn = userPreferences.getLoggedInState.first()
            val shopInfoJson = userPreferences.getShopInfo.first()
            val uniqueCompanyId = shopInfoJson.toCompanyEntity()?.uniqueCompanyId
            if (isLoggedIn == true) {
                if (uniqueCompanyId == null){
                    userPreferences.saveRepositoryJobMessage("Could not sync data\nCould not get the shop account details\n" +
                            "Please ensure that you are logged in")
                    userPreferences.saveDoubleValue(1.0)
                }else {

                    val remoteCustomerDto = shopManagerDatabaseApi.fetchAllCompanyCustomers(uniqueCompanyId)
                    val remoteCustomers =
                        shopManagerDatabaseApi.fetchAllCompanyCustomers(uniqueCompanyId)?.data
                            ?: emptyList()

                    val remoteSupplierDto = shopManagerDatabaseApi.fetchAllCompanySuppliers(uniqueCompanyId)
                    val remoteSuppliers =
                        shopManagerDatabaseApi.fetchAllCompanySuppliers(uniqueCompanyId)?.data
                            ?: emptyList()

                    val remoteReceiptDto = shopManagerDatabaseApi.fetchAllCompanyReceipts(uniqueCompanyId)
                    val remoteReceipts =
                        shopManagerDatabaseApi.fetchAllCompanyReceipts(uniqueCompanyId)?.data
                            ?: emptyList()

                    val remoteDebtDto = shopManagerDatabaseApi.fetchAllCompanyDebts(uniqueCompanyId)
                    val remoteDebts =
                        shopManagerDatabaseApi.fetchAllCompanyDebts(uniqueCompanyId)?.data
                            ?: emptyList()

                    val remoteCashInDto = shopManagerDatabaseApi.fetchAllCompanyCashIns(uniqueCompanyId)
                    val remoteCashIns =
                        shopManagerDatabaseApi.fetchAllCompanyCashIns(uniqueCompanyId)?.data
                            ?: emptyList()

                    val remoteDebtRepaymentDto = shopManagerDatabaseApi.fetchAllCompanyCustomers(uniqueCompanyId)
                    val remoteDebtRepayments =
                        shopManagerDatabaseApi.fetchAllCompanyDebtRepayments(uniqueCompanyId)?.data
                            ?: emptyList()

                    val remoteInventoryDto = shopManagerDatabaseApi.fetchAllCompanyInventories(uniqueCompanyId)
                    val remoteInventories =
                        shopManagerDatabaseApi.fetchAllCompanyInventories(uniqueCompanyId)?.data
                            ?: emptyList()

                    val remoteInventoryItemDto = shopManagerDatabaseApi.fetchAllCompanyInventoryItems(uniqueCompanyId)
                    val remoteInventoryItems =
                        shopManagerDatabaseApi.fetchAllCompanyInventoryItems(uniqueCompanyId)?.data
                            ?: emptyList()

                    val remoteRevenuesDto = shopManagerDatabaseApi.fetchAllCompanyRevenues(uniqueCompanyId)
                    val remoteRevenues =
                        shopManagerDatabaseApi.fetchAllCompanyRevenues(uniqueCompanyId)?.data
                            ?: emptyList()

                    val remoteExpenseDto = shopManagerDatabaseApi.fetchAllCompanyExpenses(uniqueCompanyId)
                    val remoteExpenses =
                        shopManagerDatabaseApi.fetchAllCompanyExpenses(uniqueCompanyId)?.data
                            ?: emptyList()

                    val remoteStockDto = shopManagerDatabaseApi.fetchAllCompanyStocks(uniqueCompanyId)
                    val remoteStocks =
                        shopManagerDatabaseApi.fetchAllCompanyStocks(uniqueCompanyId)?.data
                            ?: emptyList()

                    val remotePersonnelDto = shopManagerDatabaseApi.fetchAllCompanyPersonnel(uniqueCompanyId)
                    val remotePersonnel =
                        shopManagerDatabaseApi.fetchAllCompanyPersonnel(uniqueCompanyId)?.data
                            ?: emptyList()

                    val remoteSavingsDto = shopManagerDatabaseApi.fetchAllCompanySavings(uniqueCompanyId)
                    val remoteSavings =
                        shopManagerDatabaseApi.fetchAllCompanySavings(uniqueCompanyId)?.data
                            ?: emptyList()

                    val remoteWithdrawalDto = shopManagerDatabaseApi.fetchAllCompanyWithdrawals(uniqueCompanyId)
                    val remoteWithdrawals =
                        shopManagerDatabaseApi.fetchAllCompanyWithdrawals(uniqueCompanyId)?.data
                            ?: emptyList()

                    val remoteInventoryStockDto = shopManagerDatabaseApi.fetchAllCompanyInventoryStocks(uniqueCompanyId)
                    val remoteInventoryStocks =
                        shopManagerDatabaseApi.fetchAllCompanyInventoryStocks(uniqueCompanyId)?.data
                            ?: emptyList()

                    val remoteBankDto = shopManagerDatabaseApi.fetchAllCompanyBanks(uniqueCompanyId)
                    val remoteBanks =
                        shopManagerDatabaseApi.fetchAllCompanyBanks(uniqueCompanyId)?.data
                            ?: emptyList()
                    userPreferences.saveDoubleValue(0.50)


                    userPreferences.saveRepositoryJobMessage("Fetching data from cloud complete...\n" +
                            "Saving data to database...")

                    if (remoteCustomerDto?.success.toNotNull()) {
                        appDatabase.customerDao.deleteAllCustomers()
                        appDatabase.customerDao.addCustomers(remoteCustomers.map { it.toCustomerEntity() })
                    }

                    if (remoteSupplierDto?.success.toNotNull()) {
                        appDatabase.supplierDao.deleteAllSuppliers()
                        appDatabase.supplierDao.addSuppliers(remoteSuppliers.map { it.toSupplierEntity() })
                    }

                    if (remoteReceiptDto?.success.toNotNull()) {
                        appDatabase.receiptDao.deleteAllReceipts()
                        appDatabase.receiptDao.addReceipts(remoteReceipts.map { it.toReceiptEntity() })
                    }

                    if (remoteDebtDto?.success.toNotNull()) {
                        appDatabase.debtDao.deleteAllDebts()
                        appDatabase.debtDao.addDebts(remoteDebts.map { it.toDebtEntity() })
                    }

                    if (remoteCashInDto?.success.toNotNull()) {
                        appDatabase.cashInDao.deleteAllCashIns()
                        appDatabase.cashInDao.addCashIns(remoteCashIns.map { it.toCashInEntity() })
                    }

                    if (remoteDebtRepaymentDto?.success.toNotNull()) {
                        appDatabase.debtRepaymentDao.deleteAllDebtRepayments()
                        appDatabase.debtRepaymentDao.addDebtRepayments(remoteDebtRepayments.map { it.toDebtRepaymentEntity() })
                    }

                    if (remoteInventoryDto?.success.toNotNull()) {
                        appDatabase.inventoryDao.deleteAllInventories()
                        appDatabase.inventoryDao.addInventories(remoteInventories.map { it.toInventoryEntity() })
                    }

                    if (remoteInventoryItemDto?.success.toNotNull()) {
                        appDatabase.inventoryItemDao.deleteAllInventoryItems()
                        appDatabase.inventoryItemDao.addInventoryItems(remoteInventoryItems.map { it.toInventoryItemEntity() })
                    }

                    if (remoteRevenuesDto?.success.toNotNull()) {
                        appDatabase.revenueDao.deleteAllRevenues()
                        appDatabase.revenueDao.addRevenues(remoteRevenues.map { it.toRevenueEntity() })
                    }

                    if (remoteExpenseDto?.success.toNotNull()) {
                        appDatabase.expenseDao.deleteAllExpenses()
                        appDatabase.expenseDao.addExpenses(remoteExpenses.map { it.toExpenseEntity() })
                    }

                    if (remoteStockDto?.success.toNotNull()) {
                        appDatabase.stockDao.deleteAllStocks()
                        appDatabase.stockDao.addStocks(remoteStocks.map { it.toStockEntity() })
                    }

                    if (remotePersonnelDto?.success.toNotNull()) {
                        appDatabase.personnelDao.deleteAllPersonnel()
                        appDatabase.personnelDao.addPersonnel(remotePersonnel.map { it.toPersonnelEntity() })
                    }

                    if (remoteSavingsDto?.success.toNotNull()) {
                        appDatabase.savingsDao.deleteAllSavings()
                        appDatabase.savingsDao.addSavings(remoteSavings.map { it.toSavingsEntity() })
                    }

                    if (remoteWithdrawalDto?.success.toNotNull()) {
                        appDatabase.withdrawalDao.deleteAllWithdrawals()
                        appDatabase.withdrawalDao.addWithdrawals(remoteWithdrawals.map { it.toWithdrawalEntity() })
                    }

                    if (remoteInventoryStockDto?.success.toNotNull()) {
                        appDatabase.inventoryStockDao.deleteAllInventoryStock()
                        appDatabase.inventoryStockDao.addInventoryStock(remoteInventoryStocks.map { it.toInventoryStock() })
                    }

                    if (remoteBankDto?.success.toNotNull()) {
                        appDatabase.bankAccountDao.deleteAllBanks()
                        appDatabase.bankAccountDao.addBankAccounts(remoteBanks.map { it.toBankEntity() })
                    }
                    userPreferences.saveDoubleValue(1.0)
                    userPreferences.saveRepositoryJobMessage("Data sync complete")
                }
            }
            else{
                Log.d("BackupRepository", "It is not logged in")
                userPreferences.saveDoubleValue(1.0)
                userPreferences.saveRepositoryJobMessage("Could not sync data\nYou are not logged in into any account")
            }

        }catch (e: Exception){
            Log.d("BackupRepository", "Exception is called")
            userPreferences.saveDoubleValue(1.0)
            userPreferences.saveRepositoryJobMessage("Could not sync data\n${e.message}")
        }
    }

    override suspend fun smartSyncCompanyInfo(coroutineScope: CoroutineScope): Flow<Resource<String>> = flow{
        emit(Resource.Loading())
        try {
            val context = MyShopManagerApp.applicationContext()
            val userPreferences = UserPreferences(context)
            val isLoggedIn = userPreferences.getLoggedInState.first()
            val shopInfoJson = userPreferences.getShopInfo.first()
            val uniqueCompanyId = shopInfoJson.toCompanyEntity()?.uniqueCompanyId
            if (isLoggedIn == true) {
                if (uniqueCompanyId == null){
                    emit(Resource.Error(
                        data = "Could not sync data",
                        message = "Could not get the shop account details\nPlease ensure that you are logged in"
                    ))
                }else {
                    val remoteCustomers =
                        shopManagerDatabaseApi.fetchAllCompanyCustomers(uniqueCompanyId)?.data
                            ?: emptyList()
                    val allCustomerIds = appDatabase.customerDao.getAllCustomers()?.map { it.uniqueCustomerId } ?: emptyList()
                    appDatabase.customerDao.addCustomers(remoteCustomers.map { it.toCustomerEntity() }.filter { !allCustomerIds.contains(it.uniqueCustomerId) })

                    val remoteSuppliers =
                        shopManagerDatabaseApi.fetchAllCompanySuppliers(uniqueCompanyId)?.data
                            ?: emptyList()
                    val allSupplierIds = appDatabase.supplierDao.getAllSuppliers()?.map { it.uniqueSupplierId } ?: emptyList()
                    appDatabase.supplierDao.addSuppliers(remoteSuppliers.map { it.toSupplierEntity() }.filter { !allSupplierIds.contains(it.uniqueSupplierId) })

                    val remoteReceipts =
                        shopManagerDatabaseApi.fetchAllCompanyReceipts(uniqueCompanyId)?.data
                            ?: emptyList()
                    val allReceiptIds = appDatabase.receiptDao.getAllReceipts()?.map { it.uniqueReceiptId } ?: emptyList()
                    appDatabase.receiptDao.addReceipts(remoteReceipts.map { it.toReceiptEntity() }.filter { !allReceiptIds.contains(it.uniqueReceiptId) })

                    val remoteDebts =
                        shopManagerDatabaseApi.fetchAllCompanyDebts(uniqueCompanyId)?.data
                            ?: emptyList()
                    val allDebtIds = appDatabase.debtDao.getAllDebt()?.map { it.uniqueDebtId } ?: emptyList()
                    appDatabase.debtDao.addDebts(remoteDebts.map { it.toDebtEntity() }.filter { !allDebtIds.contains(it.uniqueDebtId) })

                    val remoteCashIns =
                        shopManagerDatabaseApi.fetchAllCompanyCashIns(uniqueCompanyId)?.data
                            ?: emptyList()
                    val allCashIns = appDatabase.cashInDao.getAllCashIns()?.map { it.uniqueCashInId } ?: emptyList()
                    appDatabase.cashInDao.addCashIns(remoteCashIns.map { it.toCashInEntity() }.filter { !allCashIns.contains(it.uniqueCashInId) })

                    val remoteDebtRepayments =
                        shopManagerDatabaseApi.fetchAllCompanyDebtRepayments(uniqueCompanyId)?.data
                            ?: emptyList()
                    val allDebtRepaymentIds = appDatabase.debtRepaymentDao.getAllDebtRepayment()?.map { it.uniqueDebtRepaymentId } ?: emptyList()
                    appDatabase.debtRepaymentDao.addDebtRepayments(remoteDebtRepayments.map { it.toDebtRepaymentEntity() }.filter { !allDebtRepaymentIds.contains(it.uniqueDebtRepaymentId) })

                    val remoteInventories =
                        shopManagerDatabaseApi.fetchAllCompanyInventories(uniqueCompanyId)?.data
                            ?: emptyList()
                    val allInventoryIds = appDatabase.inventoryDao.getAllInventories()?.map { it.uniqueInventoryId } ?: emptyList()
                    appDatabase.inventoryDao.addInventories(remoteInventories.map { it.toInventoryEntity() }.filter { !allInventoryIds.contains(it.uniqueInventoryId) })

                    val remoteInventoryItems =
                        shopManagerDatabaseApi.fetchAllCompanyInventoryItems(uniqueCompanyId)?.data
                            ?: emptyList()
                    val allInventoryItemIds = appDatabase.inventoryItemDao.getAllInventoryItems()?.map { it.uniqueInventoryItemId } ?: emptyList()
                    appDatabase.inventoryItemDao.addInventoryItems(remoteInventoryItems.map { it.toInventoryItemEntity() }.filter { !allInventoryItemIds.contains(it.uniqueInventoryItemId) })

                    val remoteRevenues =
                        shopManagerDatabaseApi.fetchAllCompanyRevenues(uniqueCompanyId)?.data
                            ?: emptyList()
                    val allRevenueIds = appDatabase.revenueDao.getAllRevenues()?.map { it.uniqueRevenueId } ?: emptyList()
                    appDatabase.revenueDao.addRevenues(remoteRevenues.map { it.toRevenueEntity() }.filter { !allRevenueIds.contains(it.uniqueRevenueId) })

                    val remoteExpenses =
                        shopManagerDatabaseApi.fetchAllCompanyExpenses(uniqueCompanyId)?.data
                            ?: emptyList()
                    val allExpenseIds = appDatabase.expenseDao.getAllExpenses()?.map { it.uniqueExpenseId } ?: emptyList()
                    appDatabase.expenseDao.addExpenses(remoteExpenses.map { it.toExpenseEntity() }.filter { !allExpenseIds.contains(it.uniqueExpenseId) })

                    val remoteStocks =
                        shopManagerDatabaseApi.fetchAllCompanyStocks(uniqueCompanyId)?.data
                            ?: emptyList()
                    val allStockIds = appDatabase.stockDao.getAllStocks()?.map { it.uniqueStockId } ?: emptyList()
                    appDatabase.stockDao.addStocks(remoteStocks.map { it.toStockEntity() }.filter { !allStockIds.contains(it.uniqueStockId) })

                    val remotePersonnel =
                        shopManagerDatabaseApi.fetchAllCompanyPersonnel(uniqueCompanyId)?.data
                            ?: emptyList()
                    val allPersonnelIds = appDatabase.personnelDao.getAllPersonnel()?.map { it.uniquePersonnelId } ?: emptyList()
                    appDatabase.personnelDao.addPersonnel(remotePersonnel.map { it.toPersonnelEntity() }.filter { !allPersonnelIds.contains(it.uniquePersonnelId) })

                    val remoteSavings =
                        shopManagerDatabaseApi.fetchAllCompanySavings(uniqueCompanyId)?.data
                            ?: emptyList()
                    val allSavingsIds = appDatabase.savingsDao.getAllSavings()?.map { it.uniqueSavingsId } ?: emptyList()
                    appDatabase.savingsDao.addSavings(remoteSavings.map { it.toSavingsEntity() }.filter { !allSavingsIds.contains(it.uniqueSavingsId) })

                    val remoteWithdrawals =
                        shopManagerDatabaseApi.fetchAllCompanyWithdrawals(uniqueCompanyId)?.data
                            ?: emptyList()
                    val allWithdrawalIds = appDatabase.withdrawalDao.getAllWithdrawals()?.map { it.uniqueWithdrawalId } ?: emptyList()
                    appDatabase.withdrawalDao.addWithdrawals(remoteWithdrawals.map { it.toWithdrawalEntity() }.filter { !allWithdrawalIds.contains(it.uniqueWithdrawalId) })

                    val remoteInventoryStocks =
                        shopManagerDatabaseApi.fetchAllCompanyInventoryStocks(uniqueCompanyId)?.data
                            ?: emptyList()
                    val allInventoryStockIds = appDatabase.inventoryStockDao.getAllInventoryStock()?.map { it.uniqueInventoryStockId } ?: emptyList()
                    appDatabase.inventoryStockDao.addInventoryStock(remoteInventoryStocks.map { it.toInventoryStock() }.filter { !allInventoryStockIds.contains(it.uniqueInventoryStockId) })

                    val remoteBanks =
                        shopManagerDatabaseApi.fetchAllCompanyBanks(uniqueCompanyId)?.data
                            ?: emptyList()
                    val allBankAccountIds = appDatabase.bankAccountDao.getAllBankAccounts()?.map { it.uniqueBankAccountId } ?: emptyList()
                    appDatabase.bankAccountDao.addBankAccounts(remoteBanks.map { it.toBankEntity() }.filter { !allBankAccountIds.contains(it.uniqueBankAccountId) })
                }
            }else{
                emit(Resource.Error(
                    data = "Could not sync data",
                    message = "You are not logged in into any account"
                ))
            }

        }catch (e: Exception){
            emit(Resource.Error(
                data = "Could not sync data",
                message = e.message
            ))
        }
    }

    override suspend fun changePassword(currentPassword: String, newPassword: String): Flow<Resource<String>> = flow{
        try {
            val context = MyShopManagerApp.applicationContext()
            val userPreferences = UserPreferences(context)
            val isLoggedIn = userPreferences.getLoggedInState.first()
            val shopInfoJson = userPreferences.getShopInfo.first()
            val uniqueCompanyId = shopInfoJson.toCompanyEntity()?.uniqueCompanyId
            if (isLoggedIn == true) {
                if (uniqueCompanyId == null){
                    emit(Resource.Error(
                        data = "Could not sync data",
                        message = "Could not get the shop account details\nPlease ensure that you are logged in"
                    ))
                }else {
                    val call = shopManagerDatabaseApi.changePassword(uniqueCompanyId, currentPassword, newPassword)
                    call!!.enqueue(object : Callback<CompanyResponseDto> {
                        override fun onResponse(
                            call: Call<CompanyResponseDto>,
                            response: Response<CompanyResponseDto>
                        ) {
                            TODO("Not yet implemented")
                        }
                        override fun onFailure(call: Call<CompanyResponseDto>, t: Throwable) {
                            TODO("Not yet implemented")
                        }
                    })
                }
            } else{
                emit(Resource.Error(
                    data = "Could not change password",
                    message = "You are not logged in into any account"
                ))
            }

            }catch (e: Exception){
            emit(Resource.Error(
                data = "Could not sync data",
                message = e.message
            ))
        }
    }

    override suspend fun deleteAccount(): Flow<Resource<String>> = flow{
        try {
            val context = MyShopManagerApp.applicationContext()
            val userPreferences = UserPreferences(context)
            val isLoggedIn = userPreferences.getLoggedInState.first()
            val shopInfoJson = userPreferences.getShopInfo.first()
            val uniqueCompanyId = shopInfoJson.toCompanyEntity()?.uniqueCompanyId
            if (isLoggedIn == true) {
                if (uniqueCompanyId == null){
                    emit(Resource.Error(
                        data = "Could not sync data",
                        message = "Could not get the shop account details\nPlease ensure that you are logged in"
                    ))
                }else {
                    val call = shopManagerDatabaseApi.deleteCompany(uniqueCompanyId)
                    call!!.enqueue(object : Callback<CompanyResponseDto> {
                        override fun onResponse(
                            call: Call<CompanyResponseDto>,
                            response: Response<CompanyResponseDto>
                        ) {
                            TODO("Not yet implemented")
                        }
                        override fun onFailure(call: Call<CompanyResponseDto>, t: Throwable) {
                            TODO("Not yet implemented")
                        }
                    })
                }
            } else{
                emit(Resource.Error(
                    data = "Could not change password",
                    message = "You are not logged in into any account"
                ))
            }

        }catch (e: Exception){
            emit(Resource.Error(
                data = "Could not sync data",
                message = e.message
            ))
        }
    }

    override suspend fun clearAllTables(): Flow<Resource<String>> = flow {
        try {
            emit(Resource.Loading())
            appDatabase.clearAllTables()
            emit(Resource.Success("All tables cleared successfully"))
        }catch (e: Exception){
            emit(Resource.Error(
                data = "Could not clear all tables",
                message = e.message ?: "Unknown Error",
            ))
        }
    }

    private fun checkpoint() {
        val db = appDatabase.openHelper.writableDatabase
        db.query("PRAGMA wal_checkpoint(FULL);", emptyArray())
        db.query("PRAGMA wal_checkpoint(TRUNCATE);", emptyArray())
    }
}