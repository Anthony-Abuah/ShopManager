package com.example.myshopmanagerapp.feature_app.data.repository

import com.example.myshopmanagerapp.core.Functions.toNotNull
import com.example.myshopmanagerapp.core.Resource
import com.example.myshopmanagerapp.core.SupplierEntities
import com.example.myshopmanagerapp.core.TypeConverters.toPersonnelEntity
import com.example.myshopmanagerapp.core.UserPreferences
import com.example.myshopmanagerapp.feature_app.MyShopManagerApp
import com.example.myshopmanagerapp.feature_app.data.local.AppDatabase
import com.example.myshopmanagerapp.feature_app.data.local.entities.suppliers.SupplierEntity
import com.example.myshopmanagerapp.feature_app.domain.repository.SupplierRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

class SupplierRepositoryImpl(
    private val appDatabase: AppDatabase
): SupplierRepository{
    override fun getAllSuppliers(): Flow<Resource<SupplierEntities?>> = flow{
        emit(Resource.Loading())
        val allSuppliers: List<SupplierEntity>?
        try {
            allSuppliers = appDatabase.supplierDao.getAllSuppliers()
            emit(Resource.Success(allSuppliers))
        }catch (e: Exception){
            emit(Resource.Error(
                message = "Couldn't load all Suppliers from Database",
                data = emptyList()
            ))
        }
    }

    override suspend fun addSupplier(supplier: SupplierEntity): Flow<Resource<String>> = flow{
        try {
            val context = MyShopManagerApp.applicationContext()
            val userPreferences = UserPreferences(context)
            val isLoggedIn = userPreferences.getLoggedInState.first() ?: false
            val personnelIsLoggedIn = userPreferences.getPersonnelLoggedInState.first() ?: false

            val allSupplierNames = appDatabase.supplierDao.getAllSuppliers()?.map { it.supplierName } ?: emptyList()
            when (true) {
                (allSupplierNames.contains(supplier.supplierName)) -> {
                    emit(Resource.Error("Unable to add supplier\nSupplier route already exists"))
                }
                !isLoggedIn->{
                    emit(Resource.Error("Unable to add supplier." +
                            "\nPlease log in into an account to save info" +
                            "\nCreate a new account if you don't have one" +
                            "\nYou can create an account even if offline"))
                }
                !personnelIsLoggedIn->{
                    emit(Resource.Error("Unable to add supplier." +
                            "\nPlease log in into a personnel account to save info" +
                            "\nCreate a new personnel account if you don't have one" +
                            "\nThis helps the app to determine which personnel is entering the information"))
                }
                else -> {
                    appDatabase.supplierDao.addSupplier(supplier)
                    emit(Resource.Success("Supplier add successfully"))
                }
            }
        }catch (e:Exception){
            emit(Resource.Error("Unable to add supplier\nError Message: ${e.message}"))
        }
    }

    override suspend fun addSuppliers(suppliers: SupplierEntities) {
        appDatabase.supplierDao.addSuppliers(suppliers)
    }

    override suspend fun getSupplier(uniqueSupplierId: String): SupplierEntity? {
        return appDatabase.supplierDao.getSupplier(uniqueSupplierId)
    }

    override suspend fun getSupplierByName(supplierName: String): SupplierEntities? {
        return appDatabase.supplierDao.getSupplierByName(supplierName)
    }

    override suspend fun updateSupplier(supplier: SupplierEntity): Flow<Resource<String>> = flow{
        try {
            val context = MyShopManagerApp.applicationContext()
            val userPreferences = UserPreferences(context)
            val isLoggedIn = userPreferences.getLoggedInState.first() ?: false
            val personnelIsLoggedIn = userPreferences.getPersonnelLoggedInState.first() ?: false

            val oldSupplier = appDatabase.supplierDao.getSupplier(supplier.uniqueSupplierId)
            val allSupplierNames = appDatabase.supplierDao.getAllSuppliers()?.map { it.supplierName }?.minus(oldSupplier?.supplierName.toNotNull()) ?: emptyList()
            when(true){
                (oldSupplier == null)->{
                    emit(Resource.Error("Unable to update supplier\nCould not get this supplier's old details"))
                }
                (allSupplierNames.contains(supplier.supplierName))->{
                    emit(Resource.Error("Unable to update supplier\nSupplier route already exists"))
                }
                !isLoggedIn->{
                    emit(Resource.Error("Unable to update supplier." +
                            "\nPlease log in into an account to update info" +
                            "\nCreate a new account if you don't have one" +
                            "\nYou can create an account even if offline"))
                }
                !personnelIsLoggedIn->{
                    emit(Resource.Error("Unable to update supplier." +
                            "\nPlease log in into a personnel account to update info" +
                            "\nCreate a new personnel account if you don't have one" +
                            "\nThis helps the app to determine which personnel is entering the information"))
                }
                else->{
                    appDatabase.supplierDao.updateSupplier(supplier)
                    emit(Resource.Success("Supplier updated successfully"))
                }
            }
        }catch (e: Exception){
            emit(Resource.Error("Unable to update supplier\nError Message: ${e.message}"))
        }
    }


    override suspend fun deleteSupplier(supplierId: Int) {
        appDatabase.supplierDao.deleteSupplier(supplierId)
    }

    override suspend fun deleteSupplier(uniqueSupplierId: String): Flow<Resource<String>> = flow{
        emit(Resource.Loading())
        try {
            val context = MyShopManagerApp.applicationContext()
            val userPreferences = UserPreferences(context)
            val isLoggedIn = userPreferences.getLoggedInState.first() ?: false
            val personnelIsLoggedIn = userPreferences.getPersonnelLoggedInState.first() ?: false
            val personnelIsAdmin = userPreferences.getPersonnelInfo.first()?.toPersonnelEntity()?.hasAdminRights?: false

            when (true){
                !isLoggedIn->{
                    emit(Resource.Error("Unable to delete supplier." +
                            "\nPlease log in into an account to delete info" +
                            "\nCreate a new account if you don't have one" +
                            "\nYou can create an account even if offline"))
                }
                !personnelIsLoggedIn->{
                    emit(Resource.Error("Unable to delete supplier." +
                            "\nPlease log in into a personnel account to delete info" +
                            "\nCreate a new personnel account if you don't have one" +
                            "\nThis helps the app to determine which personnel is deleting the information"))
                }
                !personnelIsAdmin->{
                    emit(Resource.Error("Unable to delete supplier." +
                            "\nThis is because this personnel has not been given privileges to delete information" +
                            "\nTo be able to delete any information, you must have admin rights" +
                            "\nNB: Only an admin can bestow admin rights"))
                }
                else ->{
                    appDatabase.supplierDao.deleteSupplier(uniqueSupplierId)
                    emit(Resource.Success(
                        "Supplier successfully deleted"
                    ))
                }
            }
        }catch (e: Exception){
            emit(Resource.Error("Unable to delete supplier\nError Message: ${e.message}"))
        }
    }

    override suspend fun deleteAllSuppliers() {
        appDatabase.supplierDao.deleteAllSuppliers()
    }

}