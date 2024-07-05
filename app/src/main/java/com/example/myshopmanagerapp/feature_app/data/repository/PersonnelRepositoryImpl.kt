package com.example.myshopmanagerapp.feature_app.data.repository

import com.example.myshopmanagerapp.core.Constants.emptyString
import com.example.myshopmanagerapp.core.Functions.generateUniquePersonnelId
import com.example.myshopmanagerapp.core.Functions.toCompanyEntityJson
import com.example.myshopmanagerapp.core.Functions.toNotNull
import com.example.myshopmanagerapp.core.PersonnelEntities
import com.example.myshopmanagerapp.core.Resource
import com.example.myshopmanagerapp.core.TypeConverters.toPersonnelEntities
import com.example.myshopmanagerapp.core.TypeConverters.toPersonnelEntitiesJson
import com.example.myshopmanagerapp.core.TypeConverters.toPersonnelEntity
import com.example.myshopmanagerapp.core.TypeConverters.toPersonnelEntityJson
import com.example.myshopmanagerapp.core.UserPreferences
import com.example.myshopmanagerapp.feature_app.MyShopManagerApp
import com.example.myshopmanagerapp.feature_app.data.local.entities.company.CompanyDao
import com.example.myshopmanagerapp.feature_app.data.local.entities.debt.DebtDao
import com.example.myshopmanagerapp.feature_app.data.local.entities.debt_repayment.DebtRepaymentDao
import com.example.myshopmanagerapp.feature_app.data.local.entities.expenses.ExpenseDao
import com.example.myshopmanagerapp.feature_app.data.local.entities.personnel.PersonnelDao
import com.example.myshopmanagerapp.feature_app.data.local.entities.personnel.PersonnelEntity
import com.example.myshopmanagerapp.feature_app.data.local.entities.revenue.RevenueDao
import com.example.myshopmanagerapp.feature_app.data.local.entities.savings.SavingsDao
import com.example.myshopmanagerapp.feature_app.data.local.entities.withdrawals.WithdrawalDao
import com.example.myshopmanagerapp.feature_app.domain.repository.PersonnelRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import java.util.*

class PersonnelRepositoryImpl(
    private val personnelDao: PersonnelDao,
    private val revenueDao: RevenueDao,
    private val expenseDao: ExpenseDao,
    private val savingsDao: SavingsDao,
    private val withdrawalDao: WithdrawalDao,
    private val debtDao: DebtDao,
    private val debtRepaymentDao: DebtRepaymentDao,
    private val companyDao: CompanyDao,
): PersonnelRepository{
    override fun getAllPersonnel(): Flow<Resource<PersonnelEntities?>> = flow{
        emit(Resource.Loading())
        val allPersonnel: List<PersonnelEntity>?
        try {
            allPersonnel = personnelDao.getAllPersonnel()?.sortedBy { it.lastName }
            emit(Resource.Success(allPersonnel))
        }catch (e: Exception){
            emit(Resource.Error(
                message = "Couldn't load all Personnel from Database",
                data = emptyList()
            ))
        }
    }

    override suspend fun registerAndLogIn(personnel: PersonnelEntity): Flow<Resource<String?>> = flow{
        emit(Resource.Loading())
        try {
            val context = MyShopManagerApp.applicationContext()
            val userPreferences = UserPreferences(context)
            val allCompanies = companyDao.getAllCompanies() ?: emptyList()
            val company = allCompanies.firstOrNull()
            val invalidParameters = (personnel.firstName.isEmpty() || personnel.lastName.isEmpty() || personnel.contact.isEmpty() || personnel.password.isBlank())
            val allPersonnel = personnelDao.getAllPersonnel() ?: emptyList()
            val allPersonnelNames = allPersonnel.map { it.firstName.trim().lowercase(Locale.ROOT) + it.lastName.trim().lowercase(Locale.ROOT) + it.otherNames?.trim()?.lowercase(Locale.ROOT) }
            val name = personnel.firstName.trim().lowercase(Locale.ROOT) + personnel.lastName.trim().lowercase(Locale.ROOT) + personnel.otherNames?.trim()?.lowercase(Locale.ROOT)

            when(true){
                invalidParameters->{
                    emit(Resource.Error("Unable to add personnel \nPlease ensure that the first route, last route and contact are provided"))
                }
                (company == null)->{
                    emit(Resource.Error("Unable to add personnel \nThere is no company to add personnel to"))
                }
                (allPersonnelNames.contains(name))->{
                    emit(Resource.Error("Unable to add personnel \nPersonnel with provided names already exists"))
                }
                else->{
                    val uniquePersonnelId = generateUniquePersonnelId(name)
                    val newPersonnel = personnel.copy(uniquePersonnelId = uniquePersonnelId, hasAdminRights = true)
                    val existingPersonnel = company.companyPersonnel.toPersonnelEntities()
                    val currentPersonnel = existingPersonnel.plus(newPersonnel)
                    val newCompany = company.copy(companyPersonnel = currentPersonnel.toPersonnelEntitiesJson())
                    personnelDao.registerPersonnel(newPersonnel, newCompany)
                    userPreferences.savePersonnelInfo(newPersonnel.toPersonnelEntityJson().toNotNull())
                    userPreferences.savePersonnelLoggedInState(true)
                    userPreferences.saveShopInfo(newCompany.toCompanyEntityJson().toNotNull())
                    emit(Resource.Success("Personnel successfully added"))
                }
            }
        }
        catch (e: Exception){
            emit(Resource.Error("Unknown error!! \nCould not add personnel\n${e.message}"))
        }
    }

    override suspend fun addPersonnel(personnel: PersonnelEntity): Flow<Resource<String?>> = flow {
        emit(Resource.Loading())
        try {
            val context = MyShopManagerApp.applicationContext()
            val userPreferences = UserPreferences(context)
            val personnelHasAdminRights = userPreferences.getPersonnelInfo.first().toPersonnelEntity()?.hasAdminRights ?: false
            val allCompanies = companyDao.getAllCompanies() ?: emptyList()
            val company = allCompanies.firstOrNull()
            val invalidParameters = (personnel.firstName.isEmpty() || personnel.lastName.isEmpty() || personnel.contact.isEmpty() || personnel.password.isBlank())
            val allPersonnel = personnelDao.getAllPersonnel() ?: emptyList()
            val allPersonnelNames = allPersonnel.map { it.firstName.trim().lowercase(Locale.ROOT) + it.lastName.trim().lowercase(Locale.ROOT) + it.otherNames?.trim()?.lowercase(Locale.ROOT) }
            val name = personnel.firstName.trim().lowercase(Locale.ROOT) + personnel.lastName.trim().lowercase(Locale.ROOT) + personnel.otherNames?.trim()?.lowercase(Locale.ROOT)

            when(true){
                invalidParameters->{
                    emit(Resource.Error("Unable to add personnel \nPlease ensure that the first route, last route and contact are provided"))
                }
                (company == null)->{
                    emit(Resource.Error("Unable to add personnel \nThere is no company to add personnel to"))
                }
                (allPersonnelNames.contains(name))->{
                    emit(Resource.Error("Unable to add personnel \nPersonnel with provided names already exists"))
                }
                else->{
                    val uniquePersonnelId = generateUniquePersonnelId(name)
                    val newPersonnel = if (personnelHasAdminRights) { personnel.copy(uniquePersonnelId = uniquePersonnelId) }
                    else {
                        if (allPersonnel.isEmpty()) personnel.copy(uniquePersonnelId = uniquePersonnelId, hasAdminRights = true)
                        else personnel.copy(uniquePersonnelId = uniquePersonnelId, hasAdminRights = false)
                    }
                    val existingPersonnel = company.companyPersonnel.toPersonnelEntities()
                    val currentPersonnel = existingPersonnel.plus(newPersonnel)
                    val newCompany = company.copy(companyPersonnel = currentPersonnel.toPersonnelEntitiesJson())
                    personnelDao.registerPersonnel(newPersonnel, newCompany)
                    emit(Resource.Success("Personnel successfully added"))
                    userPreferences.saveShopInfo(newCompany.toCompanyEntityJson().toNotNull())
                }
            }
        }catch (e: Exception){
            emit(Resource.Error("Unknown error!! \nCould not add personnel\n${e.message}"))
        }
    }

    override suspend fun addPersonnel(personnel: PersonnelEntities) {
        personnelDao.addPersonnel(personnel)
    }

    override suspend fun getPersonnel(uniquePersonnelId: String): PersonnelEntity? {
        return personnelDao.getPersonnel(uniquePersonnelId)
    }

    override suspend fun getPersonnelByName(personnelName: String): PersonnelEntities? {
        return personnelDao.getPersonnelByName(personnelName)
    }

    override suspend fun updatePersonnel(personnel: PersonnelEntity): Flow<Resource<String?>> = flow  {
        emit(Resource.Loading())
        try {
            if (personnel.firstName.isEmpty() || personnel.lastName.isEmpty() || personnel.contact.isEmpty()){
                emit(Resource.Error("Unable to update personnel \nPlease ensure that the first route, last route and contact are provided"))
            }else{
                val unUpdatedPersonnel = personnelDao.getPersonnel(personnel.uniquePersonnelId)
                if (unUpdatedPersonnel == null){
                    emit(Resource.Error("Unable to update personnel \nCannot get the personnel you are trying to update"))
                }else {
                    val allPersonnel = personnelDao.getAllPersonnel() ?: emptyList()
                    val filteredPersonnelNames = allPersonnel.filter { !(it.firstName == unUpdatedPersonnel.firstName && it.lastName == unUpdatedPersonnel.lastName && it.otherNames == unUpdatedPersonnel.otherNames) }
                    val allPersonnelNames = filteredPersonnelNames.map { it.firstName.trim().lowercase(Locale.ROOT) + it.lastName.trim().lowercase(Locale.ROOT) + it.otherNames?.trim()?.lowercase(Locale.ROOT) }
                    val name = personnel.firstName.trim().lowercase(Locale.ROOT) + personnel.lastName.trim().lowercase(Locale.ROOT) + personnel.otherNames?.trim()?.lowercase(Locale.ROOT)
                    if (allPersonnelNames.contains(name)) {
                        emit(Resource.Error("Unable to update personnel \nPersonnel with provided names already exists"))
                    } else {
                        personnelDao.updatePersonnel(personnel)
                        emit(Resource.Success("Personnel successfully updated"))
                    }
                }
            }
        }catch (e: Exception){
            emit(Resource.Error("Unknown error!! \nCould not update personnel"))
        }
    }

    override suspend fun deletePersonnel(uniquePersonnelId: String): Flow<Resource<String?>> = flow  {
        emit(Resource.Loading())
        try {
            val personnel = personnelDao.getPersonnel(uniquePersonnelId)
            if (personnel == null){
                emit(Resource.Error("Could not delete personnel \nCould not get the personnel"))
            }else {
                val allRevenues = revenueDao.getAllRevenues()?.map { it.uniquePersonnelId } ?: emptyList()
                val allExpenses = expenseDao.getAllExpenses()?.map { it.uniquePersonnelId } ?: emptyList()
                val allSavings = savingsDao.getAllSavings()?.map { it.uniquePersonnelId } ?: emptyList()
                val allWithdrawals = withdrawalDao.getAllWithdrawals()?.map { it.uniquePersonnelId } ?: emptyList()
                val allDebts = debtDao.getAllDebt()?.map { it.uniquePersonnelId } ?: emptyList()
                val allDebtRepayments = debtRepaymentDao.getAllDebtRepayment()?.map { it.uniquePersonnelId } ?: emptyList()

                if (allRevenues.contains(uniquePersonnelId) || allExpenses.contains(uniquePersonnelId) || allSavings.contains(uniquePersonnelId) || allWithdrawals.contains(uniquePersonnelId) || allDebts.contains(uniquePersonnelId) || allDebtRepayments.contains(uniquePersonnelId)){
                    emit(Resource.Error("Could not delete personnel \nThis personnel is already involved with other activities such as savings, revenue, expenses, etc." +
                            "\nTo delete personnel, you have to delete all the records this personnel is associated with"))
                }else{
                    personnelDao.deletePersonnel(uniquePersonnelId)
                    emit(Resource.Success("Personnel successfully deleted"))
                }
            }
        }catch (e: Exception){
            emit(Resource.Error("Unknown error!! \nCould not delete personnel"))
        }
    }

    override suspend fun deleteAllPersonnel() {
        personnelDao.deleteAllPersonnel()
    }

    override suspend fun loginPersonnel(userName: String, password: String): Flow<Resource<String?>> = flow{
        emit(Resource.Loading())
        try {
            val context = MyShopManagerApp.applicationContext()
            val userPreferences = UserPreferences(context)
            val passwordOrUserNameEmpty = userName.isBlank() || password.isBlank()
            val allPersonnel = personnelDao.getAllPersonnel() ?: emptyList()
            val personnelUserName = userName.trim().lowercase(Locale.ROOT)
            val selectedPersonnel = allPersonnel.firstOrNull { it.userName.lowercase(Locale.getDefault()).trim() == personnelUserName }
            val passwordDoesNotMatch = selectedPersonnel?.password == password

            when(true){
                (passwordOrUserNameEmpty)->{
                    emit(Resource.Error("Could not log in personnel\nPassword or username fields are blank. Please fill those fields appropriately"))
                }
                (selectedPersonnel ==  null)->{
                    emit(Resource.Error("Could not log in personnel\nCould not find personnel with username: $userName"))
                }
                (passwordDoesNotMatch)->{
                    emit(Resource.Error("Could not log in personnel\nThe password for selected personnel does not match. Please enter valid password"))
                }
                else->{
                    userPreferences.savePersonnelLoggedInState(true)
                    userPreferences.savePersonnelInfo(selectedPersonnel.toPersonnelEntityJson())
                    emit(Resource.Success("Personnel: $userName successfully logged in"))
                }
            }

        }catch (e: Exception){
            emit(Resource.Error("Could not log in personnel\n${e.message ?: "Unknown Error"}"))
        }
    }

    override suspend fun logoutPersonnel(): Flow<Resource<String?>> = flow{
        try {
            emit(Resource.Loading())
            val context = MyShopManagerApp.applicationContext()
            val userPreferences = UserPreferences(context)
            userPreferences.savePersonnelLoggedInState(false)
            userPreferences.savePersonnelInfo(emptyString)
            emit(Resource.Success("Personnel successfully logged out"))
        }catch (e: Exception){
            emit(Resource.Error(
                data = "Unable to log out\n${e.message ?: "Unknown error!"}",
                message = e.message
            ))
        }
    }

    override suspend fun changePersonnelPassword(
        currentPassword: String,
        newPassword: String
    ): Flow<Resource<String?>> {
        TODO("Not yet implemented")
    }

    override suspend fun resetPersonnelPassword(uniquePersonnelId: String): Flow<Resource<String?>> = flow{
        try {
            emit(Resource.Loading())
            val context = MyShopManagerApp.applicationContext()
            val userPreferences = UserPreferences(context)
            val personnelIsLoggedIn = userPreferences.getPersonnelLoggedInState.first() ?: false
            val personnelHasAdminRights = userPreferences.getPersonnelInfo.first().toPersonnelEntity()?.hasAdminRights ?: false
            val personnel = personnelDao.getPersonnel(uniquePersonnelId)
            when(true){
                !personnelIsLoggedIn->{
                    emit(Resource.Error("Could not reset password because you are not logged in"))
                }
                !personnelHasAdminRights->{
                    emit(Resource.Error("Could not reset password because you do not have admin rights"))
                }
                (personnel == null)->{
                    emit(Resource.Error("Could not reset password because this personnel's details could not be loaded"))
                }
                else->{
                    val updatedPersonnel = personnel.copy(password = "1234")
                    personnelDao.updatePersonnel(updatedPersonnel)
                    emit(Resource.Success("Password successfully changed to 1234"))
                }
            }
        }catch (e: Exception){
            emit(Resource.Error("Unable to reset password\nError Message: ${e.message}"))
        }
    }

}