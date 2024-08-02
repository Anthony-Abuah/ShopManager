package com.example.myshopmanagerapp.feature_app.data.remote.dto.customer

import com.example.myshopmanagerapp.core.Functions.toNotNull
import com.example.myshopmanagerapp.feature_app.data.local.entities.customers.CustomerEntity

data class CustomerInfoDto(
    val uniqueCustomerId: String,
    val customerName: String?,
    val customerContact: String?,
    val customerLocation: String?,
    val customerPhoto: String?,
    val debtAmount: Double?,
    val uniqueCompanyId: String,
    val otherInfo: String?
){
    fun toCustomerEntity(): CustomerEntity{
        return CustomerEntity(
            0,
            uniqueCustomerId = uniqueCustomerId.toNotNull(),
            customerName = customerName.toNotNull(),
            customerContact = customerContact.toNotNull(),
            customerLocation = customerLocation,
            customerPhoto = customerPhoto,
            otherInfo = otherInfo,
            debtAmount = debtAmount.toNotNull()
        )
    }
}
