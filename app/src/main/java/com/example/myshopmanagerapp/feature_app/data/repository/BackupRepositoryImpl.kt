package com.example.myshopmanagerapp.feature_app.data.repository

import android.content.Context
import android.util.Log
import com.example.myshopmanagerapp.core.Constants.SQLITE_SHMFILE_SUFFIX
import com.example.myshopmanagerapp.core.Constants.SQLITE_WALFILE_SUFFIX
import com.example.myshopmanagerapp.core.Constants.ShopAppDatabase
import com.example.myshopmanagerapp.core.Constants.THEDATABASE_DATABASE_BACKUP_SUFFIX
import com.example.myshopmanagerapp.core.Functions.toCompanyEntity
import com.example.myshopmanagerapp.core.Functions.toNotNull
import com.example.myshopmanagerapp.core.Resource
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
        /*if (restart) {
            val i = context.packageManager.getLaunchIntentForPackage(context.packageName)
            i!!.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            context.startActivity(i)
            exitProcess(0)
        }*/
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
                    val banks = bankAccountDao.getAllBanks()?.map { it.toBankInfoDto(uniqueCompanyId) }
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