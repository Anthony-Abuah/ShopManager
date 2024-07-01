package com.example.myshopmanagerapp.feature_app.domain.repository

import android.content.Context
import com.example.myshopmanagerapp.core.*
import com.example.myshopmanagerapp.feature_app.data.local.entities.receipt.ReceiptEntity
import com.example.myshopmanagerapp.feature_app.domain.model.InventoryQuantityDisplayValues
import com.example.myshopmanagerapp.feature_app.domain.model.ReceiptInfo
import kotlinx.coroutines.flow.Flow


interface GeneratePDFRepository {

    fun getAllReceipts(): Flow<Resource<ReceiptEntities?>>

    suspend fun addReceipt(receipt: ReceiptEntity): Flow<Resource<String?>>

    suspend fun getReceipt(uniqueReceiptId: String): ReceiptEntity?

    suspend fun generateReceipt(context: Context, receiptInfo: ReceiptInfo): Flow<Resource<String?>>

    suspend fun generateCustomerList(context: Context, customers: CustomerEntities): Flow<Resource<String?>>

    suspend fun generateRevenueList(context: Context, revenues: RevenueEntities): Flow<Resource<String?>>

    suspend fun generateSupplierList(context: Context, suppliers: SupplierEntities): Flow<Resource<String?>>

    suspend fun generateInventoryItemList(context: Context, inventoryItems: InventoryItemEntities): Flow<Resource<String?>>

    suspend fun generateInventoryList(context: Context, date: String, inventories: List<InventoryQuantityDisplayValues>,): Flow<Resource<String?>>



}
