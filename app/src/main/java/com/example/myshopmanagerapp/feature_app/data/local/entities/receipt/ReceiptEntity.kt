package com.example.myshopmanagerapp.feature_app.data.local.entities.receipt

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.myshopmanagerapp.core.Constants.Cash
import com.example.myshopmanagerapp.core.Constants.Receipt_Table
import com.example.myshopmanagerapp.core.Constants.emptyString
import com.example.myshopmanagerapp.core.Functions.toNotNull
import com.example.myshopmanagerapp.core.TypeConverters.toItemQuantityInfoListJson
import com.example.myshopmanagerapp.feature_app.data.remote.dto.receipt.ReceiptInfoDto
import com.example.myshopmanagerapp.feature_app.domain.model.ItemQuantityInfo
import com.example.myshopmanagerapp.feature_app.domain.model.ReceiptInfo


@Entity(tableName = Receipt_Table)
data class ReceiptEntity(
   @PrimaryKey(autoGenerate = true) val receiptId: Int,
   val uniqueReceiptId: String,
   val shopName: String,
   val shopLocation: String,
   val shopContact: String,
   val date: Long,
   val customerName: String,
   val customerContact: String?,
   val personnelName: String?,
   val personnelRole: String?,
   val items: List<ItemQuantityInfo>,
   val totalAmount: Double = items.sumOf { it.amount },
   val paymentMethod: String? = Cash,
   val transactionId: String? = emptyString
){
   fun toReceiptInfo(): ReceiptInfo{
      return ReceiptInfo(
         uniqueReceiptId, shopName, shopLocation, shopContact, date, customerName, customerContact, personnelName, personnelRole, items
      )
   }
   fun toReceiptInfoDto(uniqueCompanyId: String): ReceiptInfoDto{
      return ReceiptInfoDto(
         uniqueReceiptId = uniqueReceiptId,
         shopName = shopName,
         shopLocation = shopLocation,
         shopContact = shopContact,
         date = date,
         customerName = customerName,
         customerContact = customerContact.toNotNull(),
         personnelName = personnelName.toNotNull(),
         personnelRole = personnelRole.toNotNull(),
         items = items.toItemQuantityInfoListJson(),
         totalAmount = totalAmount,
         uniqueCompanyId = uniqueCompanyId,
         paymentMethod.toNotNull(),
         transactionId.toNotNull()
      )
   }

}