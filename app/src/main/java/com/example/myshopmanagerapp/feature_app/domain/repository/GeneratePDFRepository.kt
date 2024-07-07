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
    suspend fun updateReceipt(receipt: ReceiptEntity): Flow<Resource<String?>>
    suspend fun deleteReceipt(uniqueReceiptId: String): Flow<Resource<String?>>

    suspend fun getReceipt(uniqueReceiptId: String): ReceiptEntity?

    suspend fun generateReceipt(context: Context, receiptInfo: ReceiptInfo): Flow<Resource<String?>>

}