package com.example.myshopmanagerapp.feature_app.data.remote.dto.supplier

import com.example.myshopmanagerapp.feature_app.domain.model.UniqueId

data class SmartSuppliers(
    val suppliers: List<SupplierInfoDto>,
    val uniqueSupplierIds: List<UniqueId>
)