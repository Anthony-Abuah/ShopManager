package com.example.myshopmanagerapp.feature_app.data.remote.dto.supplier

import com.example.myshopmanagerapp.feature_app.data.local.entities.suppliers.SupplierEntity


data class SupplierInfoDto(
    val uniqueSupplierId: String,
    val uniqueCompanyId: String,
    val supplierName: String,
    val supplierContact: String,
    val supplierLocation: String,
    val supplierRole: String,
    val otherInfo: String
){
    fun toSupplierEntity(): SupplierEntity{
        return SupplierEntity(
            0,
            uniqueSupplierId = uniqueSupplierId,
            supplierName = supplierName,
            supplierContact = supplierContact,
            supplierLocation = supplierLocation,
            otherInfo = otherInfo,
            supplierRole = supplierRole
        )
    }
}
