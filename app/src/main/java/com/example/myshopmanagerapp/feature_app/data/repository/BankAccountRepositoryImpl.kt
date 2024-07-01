package com.example.myshopmanagerapp.feature_app.data.repository

import com.example.myshopmanagerapp.core.BankAccountEntities
import com.example.myshopmanagerapp.core.Resource
import com.example.myshopmanagerapp.core.TypeConverters.toPersonnelEntity
import com.example.myshopmanagerapp.core.UserPreferences
import com.example.myshopmanagerapp.feature_app.MyShopManagerApp
import com.example.myshopmanagerapp.feature_app.data.local.entities.banks.BankAccountDao
import com.example.myshopmanagerapp.feature_app.data.local.entities.banks.BankAccountEntity
import com.example.myshopmanagerapp.feature_app.domain.repository.BankAccountRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

class BankAccountRepositoryImpl(
    private val bankAccountDao: BankAccountDao
): BankAccountRepository{
    override fun getAllBanks(): Flow<Resource<BankAccountEntities?>> = flow{
        emit(Resource.Loading())
        val allBanks: List<BankAccountEntity>?
        try {
            allBanks = bankAccountDao.getAllBanks()?.sortedBy { it.bankAccountName }
            emit(Resource.Success(allBanks))
        }catch (e: Exception){
            emit(Resource.Error(
                message = "Couldn't load all Banks from Database",
                data = emptyList()
            ))
        }
    }

    override suspend fun addBankAccount(bankAccount: BankAccountEntity): Flow<Resource<String?>> = flow {
        emit(Resource.Loading())
        try {
            val context = MyShopManagerApp.applicationContext()
            val userPreferences = UserPreferences(context)
            val isLoggedIn = userPreferences.getLoggedInState.first() ?: false
            val personnelIsLoggedIn = userPreferences.getPersonnelLoggedInState.first() ?: false
            when(true){
                bankAccount.bankName.isEmpty()->{
                    emit(Resource.Error("Unable to add bank account.\nPlease enter the route of the bank"))
                }
                bankAccount.bankAccountName.isEmpty()->{
                    emit(Resource.Error("Unable to add bank account.\nPlease enter the bank account route"))
                }
                bankAccount.bankContact.isEmpty()->{
                    emit(Resource.Error("Unable to add bank contact.\nPlease enter the bank contact"))
                }
                !isLoggedIn->{
                    emit(Resource.Error("Unable to add bank contact." +
                            "\nPlease log in into an account to save info" +
                            "\nCreate a new account if you don't have one" +
                            "\nYou can create an account even if offline"))
                }
                !personnelIsLoggedIn->{
                    emit(Resource.Error("Unable to add bank contact." +
                            "\nPlease log in into a personnel account to save info" +
                            "\nCreate a new personnel account if you don't have one" +
                            "\nThis helps the app to determine which personnel is entering the information"))
                }
                else -> {
                    bankAccountDao.addBank(bankAccount)
                    emit(Resource.Success("${bankAccount.bankAccountName} has been successfully added"))
                }
            }
        }catch (e: Exception){
            emit(Resource.Error(
                message = "Could not add bank account\nError message: ${e.message}",
            ))
        }
    }

    override suspend fun getBankAccount(uniqueBankAccountId: String): BankAccountEntity? {
        return bankAccountDao.getBankAccount(uniqueBankAccountId)
    }

    override suspend fun updateBankAccount(bankAccount: BankAccountEntity): Flow<Resource<String?>> = flow  {
        emit(Resource.Loading())
        try{
            val context = MyShopManagerApp.applicationContext()
            val userPreferences = UserPreferences(context)
            val isLoggedIn = userPreferences.getLoggedInState.first() ?: false
            val personnelIsLoggedIn = userPreferences.getPersonnelLoggedInState.first() ?: false
            when(true){
                bankAccount.bankName.isEmpty()->{
                    emit(Resource.Error("Unable to update bank account.\nPlease enter the route of the bank"))
                }
                bankAccount.bankAccountName.isEmpty()->{
                    emit(Resource.Error("Unable to update bank account.\nPlease enter the bank account route"))
                }
                bankAccount.bankContact.isEmpty()->{
                    emit(Resource.Error("Unable to update bank contact.\nPlease enter the bank contact"))
                }
                !isLoggedIn->{
                    emit(Resource.Error("Unable to update bank account." +
                            "\nPlease log in into an account to save info" +
                            "\nCreate a new account if you don't have one" +
                            "\nYou can create an account even if offline"))
                }
                !personnelIsLoggedIn->{
                    emit(Resource.Error("Unable to update bank account." +
                            "\nPlease log in into a personnel account to save info" +
                            "\nCreate a new personnel account if you don't have one" +
                            "\nThis helps the app to determine which personnel is updating the information"))
                }
                else -> {
                    bankAccountDao.updateBank(bankAccount)
                    emit(Resource.Success("${bankAccount.bankAccountName} has been successfully updated"))
                }
            }
        }catch (e: Exception){
            emit(Resource.Error(
                message = "Could not update bank Account\nError message: ${e.message}",
            ))
        }
    }

    override suspend fun deleteBankAccount(uniqueBankAccountId: String): Flow<Resource<String?>> = flow  {
        emit(Resource.Loading())
        try{
            val context = MyShopManagerApp.applicationContext()
            val userPreferences = UserPreferences(context)
            val isLoggedIn = userPreferences.getLoggedInState.first() ?: false
            val personnelIsLoggedIn = userPreferences.getPersonnelLoggedInState.first() ?: false
            val personnelIsAdmin = userPreferences.getPersonnelInfo.first()?.toPersonnelEntity()?.hasAdminRights?: false
            val bankAccount = bankAccountDao.getBankAccount(uniqueBankAccountId)
            when(true){
                (bankAccount == null) ->{
                    emit(Resource.Error("Unable to delete bank account.\nCould not load the particular bank account you want to delete"))
                }
                (((bankAccount.accountBalance ?: 0.0) > 0))->{
                    emit(Resource.Error("Unable to delete bank account.\nThis account has some savings hence can't be deleted" +
                            "\nYou need to delete all associated savings and withdrawals"))
                }
                bankAccount.bankContact.isEmpty()->{
                    emit(Resource.Error("Unable to update bank contact.\nPlease enter the bank contact"))
                }
                !isLoggedIn->{
                    emit(Resource.Error("Unable to delete bank account." +
                            "\nPlease log in into an account to save info" +
                            "\nCreate a new account if you don't have one" +
                            "\nYou can create an account even if offline"))
                }
                !personnelIsLoggedIn->{
                    emit(Resource.Error("Unable to delete bank account." +
                            "\nPlease log in into a personnel account to save info" +
                            "\nCreate a new personnel account if you don't have one" +
                            "\nThis helps the app to determine which personnel is updating the information"))
                }
                !personnelIsAdmin->{
                    emit(Resource.Error("Unable to delete bank account." +
                            "\nThis is because this personnel has not been given privileges to delete information" +
                            "\nTo be able to delete any information, you must have admin rights" +
                            "\nOnly an admin can bestow admin rights"))
                }
                else -> {
                    bankAccountDao.deleteBank(bankAccount.uniqueBankAccountId)
                    emit(Resource.Success("${bankAccount.bankAccountName} has been successfully deleted"))
                }
            }
        }catch (e: Exception){
            emit(Resource.Error(
                message = "Could not delete bank account\nError message: ${e.message}",
            ))
        }
    }
}