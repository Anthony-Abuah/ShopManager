package com.example.myshopmanagerapp.feature_app.domain.repository

import com.example.myshopmanagerapp.core.SupplierEntities
import com.example.myshopmanagerapp.core.Resource
import com.example.myshopmanagerapp.feature_app.data.local.entities.suppliers.SupplierEntity
import kotlinx.coroutines.flow.Flow


interface SupplierRepository {

    fun getAllSuppliers(): Flow<Resource<SupplierEntities?>>

    suspend fun addSupplier(supplier: SupplierEntity): Flow<Resource<String>>

    suspend fun addSuppliers(suppliers: SupplierEntities)

    suspend fun getSupplier(uniqueSupplierId: String): SupplierEntity?

    suspend fun getSupplierByName(supplierName: String): SupplierEntities?

    suspend fun updateSupplier(supplier: SupplierEntity): Flow<Resource<String>>

    suspend fun deleteSupplier(supplierId: Int)

    suspend fun deleteSupplier(uniqueSupplierId: String): Flow<Resource<String>>

    suspend fun deleteAllSuppliers()

}
