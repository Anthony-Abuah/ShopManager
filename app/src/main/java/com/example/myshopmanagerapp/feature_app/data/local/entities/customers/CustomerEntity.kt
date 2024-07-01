package com.example.myshopmanagerapp.feature_app.data.local.entities.customers

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.myshopmanagerapp.core.Constants.Customer_Table
import com.example.myshopmanagerapp.feature_app.data.remote.dto.customer.CustomerInfoDto

@Entity(tableName = Customer_Table)
data class CustomerEntity(
    @PrimaryKey(autoGenerate = true) val customerId: Int,
    val uniqueCustomerId: String,
    val customerName: String,
    val customerContact: String,
    val customerLocation: String?,
    val customerPhoto: String?,
    val otherInfo: String?,
    val debtAmount: Double?
){
    fun toCustomerInfoDto(uniqueCompanyId: String): CustomerInfoDto{
        return CustomerInfoDto(
            uniqueCustomerId = uniqueCustomerId,
            customerName = customerName,
            customerContact = customerContact,
            customerLocation = customerLocation,
            customerPhoto = customerPhoto,
            debtAmount = debtAmount,
            uniqueCompanyId = uniqueCompanyId,
            otherInfo = otherInfo
        )
    }
}

