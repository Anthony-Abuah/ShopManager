package com.example.myshopmanagerapp.feature_app.data.local.entities.suppliers

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.myshopmanagerapp.core.Constants.Supplier_Table
import com.example.myshopmanagerapp.core.Functions.toNotNull
import com.example.myshopmanagerapp.feature_app.data.remote.dto.supplier.SupplierInfoDto

@Entity(tableName = Supplier_Table)
data class SupplierEntity(
    @PrimaryKey(autoGenerate = true) val supplierId: Int,
    val uniqueSupplierId: String,
    val supplierName: String,
    val supplierContact: String,
    val supplierLocation: String?,
    val otherInfo: String?,
    val supplierRole: String?
){
    fun toSupplierInfoDto(uniqueCompanyId: String): SupplierInfoDto{
        return SupplierInfoDto(
            uniqueCompanyId = uniqueCompanyId,
            uniqueSupplierId = uniqueSupplierId,
            supplierName = supplierName,
            supplierContact = supplierContact,
            supplierLocation = supplierLocation.toNotNull(),
            supplierRole = supplierRole.toNotNull(),
            otherInfo = otherInfo.toNotNull()
        )
    }
}

