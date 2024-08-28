package com.example.myshopmanagerapp.core

import com.example.myshopmanagerapp.feature_app.data.local.entities.banks.BankAccountEntity
import com.example.myshopmanagerapp.feature_app.data.local.entities.cash_in.CashInEntity
import com.example.myshopmanagerapp.feature_app.data.local.entities.company.CompanyEntity
import com.example.myshopmanagerapp.feature_app.data.local.entities.customers.CustomerEntity
import com.example.myshopmanagerapp.feature_app.data.local.entities.debt.DebtEntity
import com.example.myshopmanagerapp.feature_app.data.local.entities.debt_repayment.DebtRepaymentEntity
import com.example.myshopmanagerapp.feature_app.data.local.entities.expenses.ExpenseEntity
import com.example.myshopmanagerapp.feature_app.data.local.entities.inventory.InventoryEntity
import com.example.myshopmanagerapp.feature_app.data.local.entities.inventory_items.InventoryItemEntity
import com.example.myshopmanagerapp.feature_app.data.local.entities.inventory_stock.InventoryStockEntity
import com.example.myshopmanagerapp.feature_app.data.local.entities.personnel.PersonnelEntity
import com.example.myshopmanagerapp.feature_app.data.local.entities.receipt.ReceiptEntity
import com.example.myshopmanagerapp.feature_app.data.local.entities.revenue.RevenueEntity
import com.example.myshopmanagerapp.feature_app.data.local.entities.savings.SavingsEntity
import com.example.myshopmanagerapp.feature_app.data.local.entities.stock.StockEntity
import com.example.myshopmanagerapp.feature_app.data.local.entities.suppliers.SupplierEntity
import com.example.myshopmanagerapp.feature_app.data.local.entities.withdrawals.WithdrawalEntity
import com.example.myshopmanagerapp.feature_app.domain.model.*
import com.example.myshopmanagerapp.feature_app.presentation.view_models.states.revenue.ItemValueState

typealias CompanyOwners = List<CompanyOwner>
typealias ListOfShopLoginInfo = List<ShopLoginInfo>
typealias ItemQuantityInfoList = List<ItemQuantityInfo>
typealias UniqueIds = List<UniqueId>
typealias Prices = List<Price>
typealias ReceiptEntities = List<ReceiptEntity>
typealias StockEntities = List<StockEntity>
typealias CustomerEntities = List<CustomerEntity>
typealias ItemQuantities = List<ItemQuantity>
typealias QuantityCategorizations = List<QuantityCategorization>
typealias CompanyEntities = List<CompanyEntity>
typealias BankAccountEntities = List<BankAccountEntity>
typealias DebtEntities = List<DebtEntity>
typealias CashInEntities = List<CashInEntity>
typealias DebtRepaymentEntities = List<DebtRepaymentEntity>
typealias SavingsEntities = List<SavingsEntity>
typealias ExpenseEntities = List<ExpenseEntity>
typealias PersonnelEntities = List<PersonnelEntity>
typealias SupplierEntities = List<SupplierEntity>
typealias WithdrawalEntities = List<WithdrawalEntity>
typealias InventoryEntities = List<InventoryEntity>
typealias InventoryItemEntities = List<InventoryItemEntity>
typealias RevenueEntities = List<RevenueEntity>
typealias InventoryStockEntities = List<InventoryStockEntity>
typealias ExpenseTypes = List<ExpenseType>
typealias ExpenseNames = List<ExpenseName>
typealias SupplierRoles = List<SupplierRole>
typealias RevenueTypes = List<RevenueType>
typealias ItemCategories = List<ItemCategory>
typealias PersonnelRoles = List<PersonnelRole>
typealias BankPersonnelList = List<BankPersonnel>
typealias Manufacturers = List<Manufacturer>


