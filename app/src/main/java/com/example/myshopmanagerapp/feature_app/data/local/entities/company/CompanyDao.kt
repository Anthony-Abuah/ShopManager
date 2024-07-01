package com.example.myshopmanagerapp.feature_app.data.local.entities.company

import androidx.room.*
import com.example.myshopmanagerapp.core.CompanyEntities
import com.example.myshopmanagerapp.core.Constants.BankAccount_Table
import com.example.myshopmanagerapp.core.Constants.CashIn_Table
import com.example.myshopmanagerapp.core.Constants.Company_Table
import com.example.myshopmanagerapp.core.Constants.Customer_Table
import com.example.myshopmanagerapp.core.Constants.DebtRepayment_Table
import com.example.myshopmanagerapp.core.Constants.Debt_Table
import com.example.myshopmanagerapp.core.Constants.Expense_Table
import com.example.myshopmanagerapp.core.Constants.InventoryItem_Table
import com.example.myshopmanagerapp.core.Constants.InventoryStock_Table
import com.example.myshopmanagerapp.core.Constants.Inventory_Table
import com.example.myshopmanagerapp.core.Constants.Personnel_Table
import com.example.myshopmanagerapp.core.Constants.Receipt_Table
import com.example.myshopmanagerapp.core.Constants.Revenue_Table
import com.example.myshopmanagerapp.core.Constants.Savings_Table
import com.example.myshopmanagerapp.core.Constants.Stock_Table
import com.example.myshopmanagerapp.core.Constants.Supplier_Table
import com.example.myshopmanagerapp.core.Constants.Withdrawal_Table
import com.example.myshopmanagerapp.feature_app.data.local.entities.inventory.InventoryEntity
import com.example.myshopmanagerapp.feature_app.data.local.entities.inventory_items.InventoryItemEntity
import com.example.myshopmanagerapp.feature_app.data.local.entities.stock.StockEntity
import java.util.*

@Dao
interface CompanyDao {

    @Query ("SELECT * FROM $Company_Table")
    suspend fun getAllCompanies(): CompanyEntities?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addCompany(company: CompanyEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addCompanies(companies: CompanyEntities)

    @Query ("SELECT * FROM $Company_Table WHERE uniqueCompanyId LIKE :uniqueCompanyId")
    suspend fun getCompany(uniqueCompanyId: String): CompanyEntity?

    @Query ("SELECT * FROM $Company_Table WHERE companyName LIKE :companyName")
    suspend fun getCompanyByName(companyName: String): List<CompanyEntity>?
    
    @Query ("DELETE FROM $Company_Table")
    suspend fun deleteAllCompanies()

    @Query ("DELETE FROM $Company_Table WHERE uniqueCompanyId LIKE :uniqueCompanyId")
    suspend fun deleteCompany(uniqueCompanyId: String)

    @Query ("DELETE FROM $Company_Table WHERE companyId LIKE :companyId")
    suspend fun deleteCompany(companyId: Int)

    @Query ("DELETE FROM $Company_Table WHERE companyId NOT IN (SELECT MIN(companyId) FROM $Company_Table GROUP BY uniqueCompanyId)")
    suspend fun deleteDuplicateCompanies()

    @Update
    suspend fun updateCompany(company: CompanyEntity)

    @Transaction
    suspend fun registerNewCompany(company: CompanyEntity){
        deleteAllCompanies()
        addCompany(company)
    }

    @Query ("DELETE FROM $BankAccount_Table")
    suspend fun deleteAllBankAccounts()
    @Query ("DELETE FROM $CashIn_Table")
    suspend fun deleteAllCashIns()
    @Query ("DELETE FROM $Customer_Table")
    suspend fun deleteAllCustomers()
    @Query ("DELETE FROM $Debt_Table")
    suspend fun deleteAllDebts()
    @Query ("DELETE FROM $DebtRepayment_Table")
    suspend fun deleteAllDebtRepayments()
    @Query ("DELETE FROM $Expense_Table")
    suspend fun deleteAllExpenses()
    @Query ("DELETE FROM $Inventory_Table")
    suspend fun deleteAllInventories()
    @Query ("DELETE FROM $InventoryItem_Table")
    suspend fun deleteAllInventoryItems()
    @Query ("DELETE FROM $InventoryStock_Table")
    suspend fun deleteAllInventoryStocks()
    @Query ("DELETE FROM $Personnel_Table")
    suspend fun deleteAllPersonnel()
    @Query ("DELETE FROM $Receipt_Table")
    suspend fun deleteAllReceipts()
    @Query ("DELETE FROM $Revenue_Table")
    suspend fun deleteAllRevenues()
    @Query ("DELETE FROM $Savings_Table")
    suspend fun deleteAllSavings()
    @Query ("DELETE FROM $Stock_Table")
    suspend fun deleteAllStocks()
    @Query ("DELETE FROM $Supplier_Table")
    suspend fun deleteAllSuppliers()
    @Query ("DELETE FROM $Withdrawal_Table")
    suspend fun deleteAllWithdrawals()

    @Transaction
    suspend fun deleteAllTables(){
        deleteAllBankAccounts()
        deleteAllCashIns()
        deleteAllCustomers()
        deleteAllDebts()
        deleteAllDebtRepayments()
        deleteAllExpenses()
        deleteAllInventories()
        deleteAllInventoryItems()
        deleteAllInventoryStocks()
        deleteAllPersonnel()
        deleteAllReceipts()
        deleteAllRevenues()
        deleteAllSavings()
        deleteAllStocks()
        deleteAllSuppliers()
        deleteAllWithdrawals()
    }
}