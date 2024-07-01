package com.example.myshopmanagerapp.feature_app.data.local.entities.suppliers

import androidx.room.*
import com.example.myshopmanagerapp.core.Constants.Supplier_Table
import com.example.myshopmanagerapp.core.SupplierEntities
import java.util.*

@Dao
interface SupplierDao {

    @Query ("SELECT * FROM $Supplier_Table")
    suspend fun getAllSuppliers(): SupplierEntities?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addSupplier(supplier: SupplierEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addSuppliers(suppliers: SupplierEntities)

    @Query ("SELECT * FROM $Supplier_Table WHERE uniqueSupplierId LIKE :uniqueSupplierId")
    suspend fun getSupplier(uniqueSupplierId: String): SupplierEntity?

    @Query ("SELECT * FROM $Supplier_Table WHERE supplierName LIKE :supplierName")
    suspend fun getSupplierByName(supplierName: String): List<SupplierEntity>?
    
    @Query ("DELETE FROM $Supplier_Table")
    suspend fun deleteAllSuppliers()

    @Query ("DELETE FROM $Supplier_Table WHERE uniqueSupplierId LIKE :uniqueSupplierId")
    suspend fun deleteSupplier(uniqueSupplierId: String)

    @Query ("DELETE FROM $Supplier_Table WHERE supplierId LIKE :supplierId")
    suspend fun deleteSupplier(supplierId: Int)

    @Query ("DELETE FROM $Supplier_Table WHERE supplierId NOT IN (SELECT MIN(supplierId) FROM $Supplier_Table GROUP BY uniqueSupplierId)")
    suspend fun deleteDuplicateSuppliers()
    
    @Update
    suspend fun updateSupplier(supplier: SupplierEntity)


}