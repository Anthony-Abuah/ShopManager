package com.example.myshopmanagerapp.feature_app.data.repository

import android.content.Context
import android.util.Log
import com.example.myshopmanagerapp.core.AdditionEntityMarkers
import com.example.myshopmanagerapp.core.ChangesEntityMarkers
import com.example.myshopmanagerapp.core.Constants.SQLITE_SHMFILE_SUFFIX
import com.example.myshopmanagerapp.core.Constants.SQLITE_WALFILE_SUFFIX
import com.example.myshopmanagerapp.core.Constants.ShopAppDatabase
import com.example.myshopmanagerapp.core.Constants.THEDATABASE_DATABASE_BACKUP_SUFFIX
import com.example.myshopmanagerapp.core.Functions.toCompanyEntity
import com.example.myshopmanagerapp.core.Functions.toNotNull
import com.example.myshopmanagerapp.core.Resource
import com.example.myshopmanagerapp.core.TypeConverters.toUniqueIds
import com.example.myshopmanagerapp.core.UserPreferences
import com.example.myshopmanagerapp.feature_app.MyShopManagerApp
import com.example.myshopmanagerapp.feature_app.data.local.AppDatabase
import com.example.myshopmanagerapp.feature_app.data.local.entities.banks.BankAccountDao
import com.example.myshopmanagerapp.feature_app.data.local.entities.cash_in.CashInDao
import com.example.myshopmanagerapp.feature_app.data.local.entities.customers.CustomerDao
import com.example.myshopmanagerapp.feature_app.data.local.entities.debt.DebtDao
import com.example.myshopmanagerapp.feature_app.data.local.entities.debt_repayment.DebtRepaymentDao
import com.example.myshopmanagerapp.feature_app.data.local.entities.expenses.ExpenseDao
import com.example.myshopmanagerapp.feature_app.data.local.entities.inventory.InventoryDao
import com.example.myshopmanagerapp.feature_app.data.local.entities.inventory_items.InventoryItemDao
import com.example.myshopmanagerapp.feature_app.data.local.entities.inventory_stock.InventoryStockDao
import com.example.myshopmanagerapp.feature_app.data.local.entities.personnel.PersonnelDao
import com.example.myshopmanagerapp.feature_app.data.local.entities.revenue.RevenueDao
import com.example.myshopmanagerapp.feature_app.data.local.entities.savings.SavingsDao
import com.example.myshopmanagerapp.feature_app.data.local.entities.stock.StockDao
import com.example.myshopmanagerapp.feature_app.data.local.entities.suppliers.SupplierDao
import com.example.myshopmanagerapp.feature_app.data.local.entities.withdrawals.WithdrawalDao
import com.example.myshopmanagerapp.feature_app.data.remote.ShopManagerDatabaseApi
import com.example.myshopmanagerapp.feature_app.data.remote.dto.company.CompanyResponseDto
import com.example.myshopmanagerapp.feature_app.domain.model.AddEntitiesResponse
import com.example.myshopmanagerapp.feature_app.domain.repository.BackupRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class BackupRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val customerDao: CustomerDao,
    private val debtDao: DebtDao,
    private val debtRepaymentDao: DebtRepaymentDao,
    private val expenseDao: ExpenseDao,
    private val inventoryDao: InventoryDao,
    private val inventoryItemDao: InventoryItemDao,
    private val personnelDao: PersonnelDao,
    private val supplierDao: SupplierDao,
    private val inventoryStockDao: InventoryStockDao,
    private val revenueDao: RevenueDao,
    private val withdrawalDao: WithdrawalDao,
    private val savingsDao: SavingsDao,
    private val bankAccountDao: BankAccountDao,
    private val stockDao: StockDao,
    private val cashInDao: CashInDao,
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

    override suspend fun backupCompanyInfo(coroutineScope: CoroutineScope): Flow<Resource<String>> = flow{
        try {
            emit(Resource.Loading())
            val context = MyShopManagerApp.applicationContext()
            val userPreferences = UserPreferences(context)
            val isLoggedIn = userPreferences.getLoggedInState.first()
            val shopInfoJson = userPreferences.getShopInfo.first()
            val uniqueCompanyId = shopInfoJson.toCompanyEntity()?.uniqueCompanyId

            //val uniqueCompanyId = "Company_Dedee_51881"
            if (isLoggedIn == true) {
                if (uniqueCompanyId == null){
                    emit(Resource.Error(
                        data = "Could not sync data",
                        message = "Could not get the shop account details\nPlease ensure that you are logged in"
                    ))
                }else {
                    val customers =
                        customerDao.getAllCustomers()?.map { it.toCustomerInfoDto(uniqueCompanyId) }
                    val debts = debtDao.getAllDebt()?.map { it.toDebtInfoDto(uniqueCompanyId) }
                    val debtRepayments = debtRepaymentDao.getAllDebtRepayment()
                        ?.map { it.toDebtRepaymentInfoDto(uniqueCompanyId) }
                    val expenses =
                        expenseDao.getAllExpenses()?.map { it.toExpenseInfoDto(uniqueCompanyId) }
                    val inventories =
                        inventoryDao.getAllInventories()
                            ?.map { it.toInventoryInfoDto(uniqueCompanyId) }
                    val inventoryItems = inventoryItemDao.getAllInventoryItems()
                        ?.map { it.toInventoryItemInfoDto(uniqueCompanyId) }
                    val personnel =
                        personnelDao.getAllPersonnel()
                            ?.map { it.toPersonnelInfoDto(uniqueCompanyId) }
                    val suppliers =
                        supplierDao.getAllSuppliers()?.map { it.toSupplierInfoDto(uniqueCompanyId) }
                    val inventoryStocks = inventoryStockDao.getAllInventoryStock()
                        ?.map { it.toInventoryStockInfoDto(uniqueCompanyId) }
                    val revenues =
                        revenueDao.getAllRevenues()?.map { it.toRevenueInfoDto(uniqueCompanyId) }
                    val withdrawals = withdrawalDao.getAllWithdrawals()
                        ?.map { it.toWithdrawalInfoDto(uniqueCompanyId) }
                    val savings =
                        savingsDao.getAllSavings()?.map { it.toSavingsInfoDto(uniqueCompanyId) }
                    val banks = bankAccountDao.getAllBankAccounts()?.map { it.toBankAccountInfoDto(uniqueCompanyId) }
                    val stocks = stockDao.getAllStocks()?.map { it.toStockInfoDto(uniqueCompanyId) }
                    val cashIns = cashInDao.getAllCashIns()?.map { it.toCashInfoDto(uniqueCompanyId) }

                    if (!customers.isNullOrEmpty()) {
                        Log.d("BackupRepository", "Customer is not empty is called")
                        emit(Resource.Loading("Backing up customers ..."))
                        val call = shopManagerDatabaseApi.addCustomers(uniqueCompanyId, customers)
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
                        Log.d("BackupRepository", "Expense is not empty is called")
                        emit(Resource.Loading("Backing up expenses ..."))
                        val call = shopManagerDatabaseApi.addExpenses(uniqueCompanyId, expenses)
                        call!!.enqueue(object : Callback<AddEntitiesResponse> {
                            override fun onResponse(
                                call: Call<AddEntitiesResponse>,
                                response: Response<AddEntitiesResponse>
                            ) {
                                Log.d("BackupRepository", "Expense data back up is successful")
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
                                    Log.d("BackupRepository", "Expense  data back up failed")
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
                        Log.d("BackupRepository", "Personnel is not empty is called")
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
                                    Log.d(
                                        "BackupRepository",
                                        "Personnel data is emitted successfully"
                                    )
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
                        val call = shopManagerDatabaseApi.addBanks(uniqueCompanyId, banks)
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

                    //emit(Resource.Success("All data successfully backed up"))
                }
            }else{
                emit(Resource.Error(
                    data = "Could not sync data",
                    message = "You are not logged in into any account"
                ))
            }
        }catch (e: Exception){
            emit(Resource.Error(
                message = "Could not back up any/all of the data",
                data = e.message
            ))
        }
    }

    override suspend fun smartBackup(coroutineScope: CoroutineScope): Flow<Resource<String>> = flow{
        try {
            emit(Resource.Loading())
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
                    val allCustomers = customerDao.getAllCustomers()?: emptyList()
                    val addedCustomerIds = AdditionEntityMarkers(context).getAddedCustomerIds.first().toUniqueIds().map { it.uniqueId }
                    val customers = allCustomers.filter { addedCustomerIds.contains(it.uniqueCustomerId) }.map { it.toCustomerInfoDto(uniqueCompanyId) }

                    val allDebts = debtDao.getAllDebt() ?: emptyList()
                    val addedDebtIds = AdditionEntityMarkers(context).getAddedDebtIds.first().toUniqueIds().map { it.uniqueId }
                    val debts = allDebts.filter { addedDebtIds.contains(it.uniqueDebtId) }.map { it.toDebtInfoDto(uniqueCompanyId) }

                    val allDebtRepayments = debtRepaymentDao.getAllDebtRepayment() ?: emptyList()
                    val addedDebtRepaymentsIds = AdditionEntityMarkers(context).getAddedDebtRepaymentIds.first().toUniqueIds().map { it.uniqueId }
                    val debtRepayments = allDebtRepayments.filter { addedDebtRepaymentsIds.contains(it.uniqueDebtRepaymentId) }.map { it.toDebtRepaymentInfoDto(uniqueCompanyId) }

                    val allExpenses = expenseDao.getAllExpenses() ?: emptyList()
                    val addedExpensesIds = AdditionEntityMarkers(context).getAddedExpenseIds.first().toUniqueIds().map { it.uniqueId }
                    val expenses = allExpenses.filter { addedExpensesIds.contains(it.uniqueExpenseId) }.map { it.toExpenseInfoDto(uniqueCompanyId) }

                    val allInventories = inventoryDao.getAllInventories() ?: emptyList()
                    val addedInventoriesIds = AdditionEntityMarkers(context).getAddedExpenseIds.first().toUniqueIds().map { it.uniqueId }
                    val inventories = allInventories.filter { addedInventoriesIds.contains(it.uniqueInventoryId) }.map { it.toInventoryInfoDto(uniqueCompanyId) }

                    val allInventoryItems = inventoryItemDao.getAllInventoryItems() ?: emptyList()
                    val addedInventoryItemsIds = AdditionEntityMarkers(context).getAddedInventoryItemIds.first().toUniqueIds().map { it.uniqueId }
                    val inventoryItems = allInventoryItems.filter { addedInventoryItemsIds.contains(it.uniqueInventoryItemId) }.map { it.toInventoryItemInfoDto(uniqueCompanyId) }

                    val allPersonnel = personnelDao.getAllPersonnel() ?: emptyList()
                    val addedPersonnelIds = AdditionEntityMarkers(context).getAddedInventoryItemIds.first().toUniqueIds().map { it.uniqueId }
                    val personnel = allPersonnel.filter { addedPersonnelIds.contains(it.uniquePersonnelId) }.map { it.toPersonnelInfoDto(uniqueCompanyId) }

                    val allSuppliers = supplierDao.getAllSuppliers() ?: emptyList()
                    val addedSuppliersIds = AdditionEntityMarkers(context).getAddedSupplierIds.first().toUniqueIds().map { it.uniqueId }
                    val suppliers = allSuppliers.filter { addedSuppliersIds.contains(it.uniqueSupplierId) }.map { it.toSupplierInfoDto(uniqueCompanyId) }

                    val allRevenues = revenueDao.getAllRevenues() ?: emptyList()
                    val addedRevenuesIds = AdditionEntityMarkers(context).getAddedRevenueIds.first().toUniqueIds().map { it.uniqueId }
                    val revenues = allRevenues.filter { addedRevenuesIds.contains(it.uniqueRevenueId) }.map { it.toRevenueInfoDto(uniqueCompanyId) }


                    val allWithdrawals = withdrawalDao.getAllWithdrawals() ?: emptyList()
                    val addedWithdrawalsIds = AdditionEntityMarkers(context).getAddedWithdrawalIds.first().toUniqueIds().map { it.uniqueId }
                    val withdrawals = allWithdrawals.filter { addedWithdrawalsIds.contains(it.uniqueWithdrawalId) }.map { it.toWithdrawalInfoDto(uniqueCompanyId) }


                    val allInventoryStocks = inventoryStockDao.getAllInventoryStock() ?: emptyList()
                    val addedInventoryStocksIds = AdditionEntityMarkers(context).getAddedInventoryStockIds.first().toUniqueIds().map { it.uniqueId }
                    val inventoryStocks = allInventoryStocks.filter { addedInventoryStocksIds.contains(it.uniqueInventoryStockId) }.map { it.toInventoryStockInfoDto(uniqueCompanyId) }

                    val allSavings = savingsDao.getAllSavings() ?: emptyList()
                    val addedSavingsIds = AdditionEntityMarkers(context).getAddedSavingsIds.first().toUniqueIds().map { it.uniqueId }
                    val savings = allSavings.filter { addedSavingsIds.contains(it.uniqueSavingsId) }.map { it.toSavingsInfoDto(uniqueCompanyId) }

                    val allStocks = stockDao.getAllStocks() ?: emptyList()
                    val addedStocksIds = AdditionEntityMarkers(context).getAddedStockIds.first().toUniqueIds().map { it.uniqueId }
                    val stocks = allStocks.filter { addedStocksIds.contains(it.uniqueStockId) }.map { it.toStockInfoDto(uniqueCompanyId) }


                    val allCashIns = cashInDao.getAllCashIns() ?: emptyList()
                    val addedCashInsIds = AdditionEntityMarkers(context).getAddedCashInIds.first().toUniqueIds().map { it.uniqueId }
                    val cashIns = allCashIns.filter { addedCashInsIds.contains(it.uniqueCashInId) }.map { it.toCashInfoDto(uniqueCompanyId) }


                    val allBankAccounts = bankAccountDao.getAllBankAccounts() ?: emptyList()
                    val addedBankAccountsIds = AdditionEntityMarkers(context).getAddedBankAccountIds.first().toUniqueIds().map { it.uniqueId }
                    val bankAccounts = allBankAccounts.filter { addedBankAccountsIds.contains(it.uniqueBankAccountId) }.map { it.toBankAccountInfoDto(uniqueCompanyId) }



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
                        val uniqueSavingsIds = ChangesEntityMarkers(context).getChangedCustomerIds.first().toUniqueIds()
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
                        val uniqueBankAccountIds = ChangesEntityMarkers(context).getChangedCustomerIds.first().toUniqueIds()
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
            }else{
                emit(Resource.Error(
                    data = "Could not sync data",
                    message = "You are not logged in into any account"
                ))
            }
        }catch (e: Exception){
            emit(Resource.Error(
                message = "Could not back up any/all of the data",
                data = e.message
            ))
        }
    }

    override suspend fun syncCompanyInfo(coroutineScope: CoroutineScope): Flow<Resource<String>> = flow{
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
                    customerDao.addCustomers(remoteCustomers.map { it.toCustomerEntity() })

                    val remoteSuppliers =
                        shopManagerDatabaseApi.fetchAllCompanySuppliers(uniqueCompanyId)?.data
                            ?: emptyList()
                    supplierDao.addSuppliers(remoteSuppliers.map { it.toSupplierEntity() })

                    val remoteDebts =
                        shopManagerDatabaseApi.fetchAllCompanyDebts(uniqueCompanyId)?.data
                            ?: emptyList()
                    debtDao.addDebts(remoteDebts.map { it.toDebtEntity() })

                    val remoteCashIns =
                        shopManagerDatabaseApi.fetchAllCompanyCashIns(uniqueCompanyId)?.data
                            ?: emptyList()
                    cashInDao.addCashIns(remoteCashIns.map { it.toCashInEntity() })

                    val remoteDebtRepayments =
                        shopManagerDatabaseApi.fetchAllCompanyDebtRepayments(uniqueCompanyId)?.data
                            ?: emptyList()
                    debtRepaymentDao.addDebtRepayments(remoteDebtRepayments.map { it.toDebtRepaymentEntity() })

                    val remoteInventories =
                        shopManagerDatabaseApi.fetchAllCompanyInventories(uniqueCompanyId)?.data
                            ?: emptyList()
                    inventoryDao.addInventories(remoteInventories.map { it.toInventoryEntity() })

                    val remoteInventoryItems =
                        shopManagerDatabaseApi.fetchAllCompanyInventoryItems(uniqueCompanyId)?.data
                            ?: emptyList()
                    inventoryItemDao.addInventoryItems(remoteInventoryItems.map { it.toInventoryItemEntity() })

                    val remoteRevenues =
                        shopManagerDatabaseApi.fetchAllCompanyRevenues(uniqueCompanyId)?.data
                            ?: emptyList()
                    revenueDao.addRevenues(remoteRevenues.map { it.toRevenueEntity() })

                    val remoteExpenses =
                        shopManagerDatabaseApi.fetchAllCompanyExpenses(uniqueCompanyId)?.data
                            ?: emptyList()
                    expenseDao.addExpenses(remoteExpenses.map { it.toExpenseEntity() })

                    val remoteStocks =
                        shopManagerDatabaseApi.fetchAllCompanyStocks(uniqueCompanyId)?.data
                            ?: emptyList()
                    stockDao.addStocks(remoteStocks.map { it.toStockEntity() })

                    val remotePersonnel =
                        shopManagerDatabaseApi.fetchAllCompanyPersonnel(uniqueCompanyId)?.data
                            ?: emptyList()
                    personnelDao.addPersonnel(remotePersonnel.map { it.toPersonnelEntity() })

                    val remoteSavings =
                        shopManagerDatabaseApi.fetchAllCompanySavings(uniqueCompanyId)?.data
                            ?: emptyList()
                    savingsDao.addSavings(remoteSavings.map { it.toSavingsEntity() })

                    val remoteWithdrawals =
                        shopManagerDatabaseApi.fetchAllCompanyWithdrawals(uniqueCompanyId)?.data
                            ?: emptyList()
                    withdrawalDao.addWithdrawals(remoteWithdrawals.map { it.toWithdrawalEntity() })

                    val remoteInventoryStocks =
                        shopManagerDatabaseApi.fetchAllCompanyInventoryStocks(uniqueCompanyId)?.data
                            ?: emptyList()
                    inventoryStockDao.addInventoryStock(remoteInventoryStocks.map { it.toInventoryStock() })

                    val remoteBanks =
                        shopManagerDatabaseApi.fetchAllCompanyBanks(uniqueCompanyId)?.data
                            ?: emptyList()
                    bankAccountDao.addBankAccounts(remoteBanks.map { it.toBankEntity() })
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