package com.example.myshopmanagerapp.feature_app.di


import com.example.myshopmanagerapp.feature_app.data.local.AppDatabase
import com.example.myshopmanagerapp.feature_app.data.remote.ShopManagerDatabaseApi
import com.example.myshopmanagerapp.feature_app.data.repository.*
import com.example.myshopmanagerapp.feature_app.domain.repository.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideGeneratePDFRepository(db: AppDatabase): GeneratePDFRepository{
        return GeneratePDFRepositoryImpl(db.receiptDao)
    }


    @Provides
    @Singleton
    fun provideCustomerRepository(
        db: AppDatabase,
    ): CustomerRepository {
        return CustomerRepositoryImpl(db)
    }

    @Provides
    @Singleton
    fun provideBankRepository(
        db: AppDatabase,
    ): BankAccountRepository {
        return BankAccountRepositoryImpl(db)
    }

    @Provides
    @Singleton
    fun providePersonnelRepository(
        db: AppDatabase,
    ): PersonnelRepository {
        return PersonnelRepositoryImpl(db)
    }

    @Provides
    @Singleton
    fun provideDebtRepository(
        db: AppDatabase,
    ): DebtRepository {
        return DebtRepositoryImpl(db)
    }

    @Provides
    @Singleton
    fun provideDebtRepaymentRepository(
        db: AppDatabase,
    ): DebtRepaymentRepository {
        return DebtRepaymentRepositoryImpl(db)
    }

    @Provides
    @Singleton
    fun provideSupplierRepository(
        db: AppDatabase,
    ): SupplierRepository {
        return SupplierRepositoryImpl(db)
    }

    @Provides
    @Singleton
    fun provideCompanyRepository(
        shopManagerDatabaseApi: ShopManagerDatabaseApi,
        db: AppDatabase,
        customerRepository: CustomerRepository,
        bankAccountRepository: BankAccountRepository,
        cashInRepository: CashInRepository,
        debtRepository: DebtRepository,
        debtRepaymentRepository: DebtRepaymentRepository,
        expenseRepository: ExpenseRepository,
        inventoryRepository: InventoryRepository,
        inventoryItemRepository: InventoryItemRepository,
        personnelRepository: PersonnelRepository,
        revenueRepository: RevenueRepository,
        savingsRepository: SavingsRepository,
        stockRepository: StockRepository,
        supplierRepository: SupplierRepository,
        withdrawalRepository: WithdrawalRepository,
    ): CompanyRepository {
        return CompanyRepositoryImpl(
            db,
            shopManagerDatabaseApi,
            customerRepository,
            bankAccountRepository,
            cashInRepository,
            debtRepository,
            debtRepaymentRepository,
            expenseRepository,
            inventoryRepository,
            inventoryItemRepository,
            personnelRepository,
            revenueRepository,
            savingsRepository,
            stockRepository,
            supplierRepository,
            withdrawalRepository
        )
    }

    @Provides
    @Singleton
    fun provideRevenueRepository(
        db: AppDatabase,
    ): RevenueRepository {
        return RevenueRepositoryImpl(db)
    }

    @Provides
    @Singleton
    fun provideExpenseRepository(
        db: AppDatabase,
    ): ExpenseRepository {
        return ExpenseRepositoryImpl(db)
    }

    @Provides
    @Singleton
    fun provideStockRepository(
        db: AppDatabase,
    ): StockRepository {
        return StockRepositoryImpl(db)
    }


    @Provides
    @Singleton
    fun provideWithdrawalRepository(
        db: AppDatabase,
    ): WithdrawalRepository {
        return WithdrawalRepositoryImpl(db)
    }

    @Provides
    @Singleton
    fun provideSavingsRepository(
        db: AppDatabase,
    ): SavingsRepository {
        return SavingsRepositoryImpl(db)
    }

    @Provides
    @Singleton
    fun provideInventoryRepository(
        db: AppDatabase,
    ): InventoryRepository {
        return InventoryRepositoryImpl(db)
    }
    @Provides
    @Singleton
    fun provideCashInRepository(
        db: AppDatabase,
    ): CashInRepository {
        return CashInRepositoryImpl(db)
    }

    @Provides
    @Singleton
    fun provideInventoryItemRepository(
        db: AppDatabase,
    ): InventoryItemRepository {
        return InventoryItemRepositoryImpl(db)
    }

    @Provides
    @Singleton
    fun provideBackupRepository(
        db: AppDatabase,
        shopManagerDatabaseApi: ShopManagerDatabaseApi
    ): BackupRepository {
        return BackupRepositoryImpl(db, shopManagerDatabaseApi)
    }

}



