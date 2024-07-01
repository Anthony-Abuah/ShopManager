package com.example.myshopmanagerapp.feature_app.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.myshopmanagerapp.feature_app.data.local.entities.banks.BankAccountDao
import com.example.myshopmanagerapp.feature_app.data.local.entities.banks.BankAccountEntity
import com.example.myshopmanagerapp.feature_app.data.local.entities.cash_in.CashInDao
import com.example.myshopmanagerapp.feature_app.data.local.entities.cash_in.CashInEntity
import com.example.myshopmanagerapp.feature_app.data.local.entities.company.CompanyDao
import com.example.myshopmanagerapp.feature_app.data.local.entities.company.CompanyEntity
import com.example.myshopmanagerapp.feature_app.data.local.entities.customers.CustomerDao
import com.example.myshopmanagerapp.feature_app.data.local.entities.customers.CustomerEntity
import com.example.myshopmanagerapp.feature_app.data.local.entities.debt.DebtDao
import com.example.myshopmanagerapp.feature_app.data.local.entities.debt.DebtEntity
import com.example.myshopmanagerapp.feature_app.data.local.entities.debt_repayment.DebtRepaymentDao
import com.example.myshopmanagerapp.feature_app.data.local.entities.debt_repayment.DebtRepaymentEntity
import com.example.myshopmanagerapp.feature_app.data.local.entities.expenses.ExpenseDao
import com.example.myshopmanagerapp.feature_app.data.local.entities.expenses.ExpenseEntity
import com.example.myshopmanagerapp.feature_app.data.local.entities.inventory.InventoryDao
import com.example.myshopmanagerapp.feature_app.data.local.entities.inventory.InventoryEntity
import com.example.myshopmanagerapp.feature_app.data.local.entities.inventory_items.InventoryItemDao
import com.example.myshopmanagerapp.feature_app.data.local.entities.inventory_items.InventoryItemEntity
import com.example.myshopmanagerapp.feature_app.data.local.entities.inventory_stock.InventoryStockDao
import com.example.myshopmanagerapp.feature_app.data.local.entities.inventory_stock.InventoryStockEntity
import com.example.myshopmanagerapp.feature_app.data.local.entities.personnel.PersonnelDao
import com.example.myshopmanagerapp.feature_app.data.local.entities.personnel.PersonnelEntity
import com.example.myshopmanagerapp.feature_app.data.local.entities.receipt.ReceiptDao
import com.example.myshopmanagerapp.feature_app.data.local.entities.receipt.ReceiptEntity
import com.example.myshopmanagerapp.feature_app.data.local.entities.revenue.RevenueDao
import com.example.myshopmanagerapp.feature_app.data.local.entities.revenue.RevenueEntity
import com.example.myshopmanagerapp.feature_app.data.local.entities.savings.SavingsDao
import com.example.myshopmanagerapp.feature_app.data.local.entities.savings.SavingsEntity
import com.example.myshopmanagerapp.feature_app.data.local.entities.stock.StockDao
import com.example.myshopmanagerapp.feature_app.data.local.entities.stock.StockEntity
import com.example.myshopmanagerapp.feature_app.data.local.entities.suppliers.SupplierDao
import com.example.myshopmanagerapp.feature_app.data.local.entities.suppliers.SupplierEntity
import com.example.myshopmanagerapp.feature_app.data.local.entities.withdrawals.WithdrawalDao
import com.example.myshopmanagerapp.feature_app.data.local.entities.withdrawals.WithdrawalEntity

@Database(
    entities = [
        CustomerEntity::class,
        BankAccountEntity::class,
        CompanyEntity::class,
        DebtEntity::class,
        DebtRepaymentEntity::class,
        ExpenseEntity::class,
        InventoryEntity::class,
        InventoryItemEntity::class,
        PersonnelEntity::class,
        RevenueEntity::class,
        SavingsEntity::class,
        StockEntity::class,
        SupplierEntity::class,
        WithdrawalEntity::class,
        InventoryStockEntity::class,
        ReceiptEntity::class,
        CashInEntity::class
    ],
    version = 1
)
@TypeConverters(Converters::class, DateConverter::class)
abstract class AppDatabase: RoomDatabase() {

    abstract val receiptDao: ReceiptDao
    abstract val customerDao: CustomerDao
    abstract val bankAccountDao: BankAccountDao
    abstract val companyDao: CompanyDao
    abstract val debtDao: DebtDao
    abstract val debtRepaymentDao: DebtRepaymentDao
    abstract val expenseDao: ExpenseDao
    abstract val inventoryDao: InventoryDao
    abstract val inventoryItemDao: InventoryItemDao
    abstract val personnelDao: PersonnelDao
    abstract val revenueDao: RevenueDao
    abstract val savingsDao: SavingsDao
    abstract val stockDao: StockDao
    abstract val supplierDao: SupplierDao
    abstract val withdrawalDao: WithdrawalDao
    abstract val inventoryStockDao: InventoryStockDao
    abstract val cashInDao: CashInDao

    /*

    fun backupDatabase(context: Context): Int {
        var result = -99

        val databaseFile = context.getDatabasePath(ShopAppDatabase)
        val databaseWALFile = File(databaseFile.path + SQLITE_WALFILE_SUFFIX)
        val databaseSHMFile = File(databaseFile.path + SQLITE_SHMFILE_SUFFIX)
        val backupFile = File(databaseFile.path + THEDATABASE_DATABASE_BACKUP_SUFFIX)
        val backupWALFile = File(backupFile.path + SQLITE_WALFILE_SUFFIX)
        val backupSHMFile = File(backupFile.path + SQLITE_SHMFILE_SUFFIX)
        if (backupFile.exists()) backupFile.delete()
        if (backupWALFile.exists()) backupWALFile.delete()
        if (backupSHMFile.exists()) backupSHMFile.delete()
        checkpoint()
        try {
            databaseFile.copyTo(backupFile,true)
            if (databaseWALFile.exists()) databaseWALFile.copyTo(backupWALFile,true)
            if (databaseSHMFile.exists()) databaseSHMFile.copyTo(backupSHMFile, true)
            result = 0
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return result
    }

    fun restoreDatabase(context: Context,restart: Boolean = true) {
        if(!File(context.getDatabasePath(ShopAppDatabase).path + THEDATABASE_DATABASE_BACKUP_SUFFIX).exists()) {
            return
        }
        val databasePath = this.openHelper.readableDatabase.path.toNotNull()
        val databaseFile = File(databasePath)
        val databaseWALFile = File(databaseFile.path + SQLITE_WALFILE_SUFFIX)
        val databaseSHMFile = File(databaseFile.path + SQLITE_SHMFILE_SUFFIX)
        val backupFile = File(databaseFile.path + THEDATABASE_DATABASE_BACKUP_SUFFIX)
        val backupWALFile = File(backupFile.path + SQLITE_WALFILE_SUFFIX)
        val backupSHMFile = File(backupFile.path + SQLITE_SHMFILE_SUFFIX)
        try {
            backupFile.copyTo(databaseFile, true)
            if (backupWALFile.exists()) backupWALFile.copyTo(databaseWALFile, true)
            if (backupSHMFile.exists()) backupSHMFile.copyTo(databaseSHMFile,true)
            checkpoint()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        if (restart) {
            val i = context.packageManager.getLaunchIntentForPackage(context.packageName)
            i!!.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            context.startActivity(i)
            exitProcess(0)
        }
    }

    */


    private fun checkpoint() {
        val db = this.openHelper.writableDatabase
        db.query("PRAGMA wal_checkpoint(FULL);", emptyArray())
        db.query("PRAGMA wal_checkpoint(TRUNCATE);", emptyArray())
    }
}