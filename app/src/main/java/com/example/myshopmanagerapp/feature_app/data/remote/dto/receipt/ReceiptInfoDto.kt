package com.example.myshopmanagerapp.feature_app.data.remote.dto.receipt

import com.example.myshopmanagerapp.core.Constants
import com.example.myshopmanagerapp.core.Functions.toNotNull
import com.example.myshopmanagerapp.core.TypeConverters.toItemQuantityInfoList
import com.example.myshopmanagerapp.feature_app.data.local.entities.receipt.ReceiptEntity

data class ReceiptInfoDto(
    val uniqueReceiptId: String,
    val shopName: String,
    val shopLocation: String,
    val shopContact: String,
    val date: Long,
    val customerName: String,
    val customerContact: String,
    val personnelName: String,
    val personnelRole: String,
    val items: String,
    val totalAmount: Double,
    val uniqueCompanyId: String,
    val paymentMethod: String,
    val transactionId: String
){
    fun toReceiptEntity(): ReceiptEntity{
        return ReceiptEntity(
            0,
            uniqueReceiptId = uniqueReceiptId,
            shopName = shopName,
            shopContact = shopContact,
            shopLocation = shopLocation,
            date = date,
            customerName = customerName,
            customerContact = customerContact,
            personnelName = personnelName,
            personnelRole = personnelRole,
            items = items.toItemQuantityInfoList(),
            totalAmount = totalAmount,
            paymentMethod = paymentMethod.toNotNull(),
            transactionId = transactionId.toNotNull()
        )
    }
}
