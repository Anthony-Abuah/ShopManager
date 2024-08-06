package com.example.myshopmanagerapp.feature_app.data.remote.dto.customer

import com.example.myshopmanagerapp.feature_app.domain.model.UniqueId


data class SmartCustomers(
    val customers: List<CustomerInfoDto>,
    val uniqueCustomerIds: List<UniqueId>
)