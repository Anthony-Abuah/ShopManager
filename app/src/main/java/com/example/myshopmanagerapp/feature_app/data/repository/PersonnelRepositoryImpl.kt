package com.example.myshopmanagerapp.feature_app.data.repository

import com.example.myshopmanagerapp.core.*
import com.example.myshopmanagerapp.core.Constants.emptyString
import com.example.myshopmanagerapp.core.Functions.generateUniquePersonnelId
import com.example.myshopmanagerapp.core.Functions.toCompanyEntity
import com.example.myshopmanagerapp.core.Functions.toCompanyEntityJson
import com.example.myshopmanagerapp.core.Functions.toNotNull
import com.example.myshopmanagerapp.core.TypeConverters.toPersonnelEntities
import com.example.myshopmanagerapp.core.TypeConverters.toPersonnelEntitiesJson
import com.example.myshopmanagerapp.core.TypeConverters.toPersonnelEntity
import com.example.myshopmanagerapp.core.TypeConverters.toPersonnelEntityJson
import com.example.myshopmanagerapp.core.TypeConverters.toUniqueIds
import com.example.myshopmanagerapp.core.TypeConverters.toUniqueIdsJson
import com.example.myshopmanagerapp.feature_app.MyShopManagerApp
import com.example.myshopmanagerapp.feature_app.data.local.AppDatabase
import com.example.myshopmanagerapp.feature_app.data.local.entities.personnel.PersonnelEntity
import com.example.myshopmanagerapp.feature_app.domain.model.UniqueId
import com.example.myshopmanagerapp.feature_app.domain.repository.PersonnelRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import java.util.*

class PersonnelRepositoryImpl(
    private val appDatabase: AppDatabase,
): PersonnelRepository{
    override fun getAllPersonnel(): Flow<Resource<PersonnelEntities?>> = flow{
        emit(Resource.Loading())
        val allPersonnel: List<PersonnelEntity>?
        try {
            allPersonnel = appDatabase.personnelDao.getAllPersonnel()?.sortedBy { it.lastName }
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
            val allCompanies = appDatabase.companyDao.getAllCompanies() ?: emptyList()
            val company = allCompanies.firstOrNull()
            val invalidParameters = (personnel.firstName.isEmpty() || personnel.lastName.isEmpty() || personnel.contact.isEmpty() || personnel.password.isBlank())
            val allPersonnel = appDatabase.personnelDao.getAllPersonnel() ?: emptyList()
            val allPersonnelNames = allPersonnel.map { it.firstName.trim().lowercase(Locale.ROOT) + it.lastName.trim().lowercase(Locale.ROOT) + it.otherNames?.trim()?.lowercase(Locale.ROOT) }
            val name = personnel.firstName.trim().lowercase(Locale.ROOT) + personnel.lastName.trim().lowercase(Locale.ROOT) + personnel.otherNames?.trim()?.lowercase(Locale.ROOT)

            when(true){
                invalidParameters->{
                    emit(Resource.Error("Unable to add personnel \nPlease ensure that the first name, last name and contact are provided"))
                }
                (company == null)->{
                    emit(Resource.Error("Unable to add personnel \nThere is no company to add personnel to"))
                }
                (allPersonnelNames.contains(name))->{
                    emit(Resource.Error("Unable to add personnel \nPersonnel with provided names already exists"))
                }
                else->{
                    val personnelContainsPrincipalAdmin = allPersonnel.map { it.isPrincipalAdmin }.contains(true)
                    val uniquePersonnelId = generateUniquePersonnelId(name)
                    val newPersonnel = personnel.copy(uniquePersonnelId = uniquePersonnelId, hasAdminRights = true, isPrincipalAdmin = !personnelContainsPrincipalAdmin)
                    val existingPersonnel = company.companyPersonnel.toPersonnelEntities()
                    val currentPersonnel = existingPersonnel.plus(newPersonnel)
                    val newCompany = company.copy(companyPersonnel = currentPersonnel.toPersonnelEntitiesJson())
                    appDatabase.personnelDao.registerPersonnel(newPersonnel, newCompany)

                    val addedPersonnelIdsJson = AdditionEntityMarkers(context).getAddedPersonnelIds.first().toNotNull()
                    val addedPersonnelIds = addedPersonnelIdsJson.toUniqueIds().plus(UniqueId(personnel.uniquePersonnelId)).toSet().toList()
                    AdditionEntityMarkers(context).saveAddedPersonnelIds(addedPersonnelIds.toUniqueIdsJson())

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
            val isLoggedIn = userPreferences.getLoggedInState.first() == true
            val personnelIsLoggedIn = userPreferences.getPersonnelLoggedInState.first() == true
            val companyEntity = userPreferences.getShopInfo.first()?.toCompanyEntity()
            val hasAdminRights = userPreferences.getPersonnelInfo.first().toPersonnelEntity()?.hasAdminRights == true
            val invalidParameters = (personnel.firstName.isEmpty() || personnel.lastName.isEmpty() || personnel.contact.isEmpty() || personnel.userName.isBlank() || personnel.password.isBlank())
            val allPersonnel = appDatabase.personnelDao.getAllPersonnel() ?: emptyList()
            val allPersonnelNames = allPersonnel.map { it.firstName.trim().lowercase(Locale.ROOT) + it.lastName.trim().lowercase(Locale.ROOT) + it.otherNames?.trim()?.lowercase(Locale.ROOT) }
            val name = personnel.firstName.trim().lowercase(Locale.ROOT) + personnel.lastName.trim().lowercase(Locale.ROOT) + personnel.otherNames?.trim()?.lowercase(Locale.ROOT)

            when(true){
                invalidParameters->{
                    emit(Resource.Error("Unable to add personnel \nPlease ensure that the first name, last name and contact are provided"))
                }
                (!isLoggedIn)->{
                    emit(Resource.Error("Unable to add personnel \nYou're not logged in to any shop account"))
                }
                (!personnelIsLoggedIn)->{
                    emit(Resource.Error("Unable to add personnel \nYou're not logged as a personnel or employee"))
                }
                (allPersonnelNames.contains(name))->{
                    emit(Resource.Error("Unable to add personnel \nPersonnel with provided names already exists"))
                }
                (companyEntity == null)->{
                    emit(Resource.Error("Unable to add personnel \nCould not load the logged in shop info\nPlease try again later"))
                }
                (!hasAdminRights)->{
                    emit(Resource.Error("Unable to add personnel \nOnly personnel with admin rights can add another personnel"))
                }
                else->{
                    val uniquePersonnelId = generateUniquePersonnelId(name)
                    val newPersonnel = personnel.copy(uniquePersonnelId = uniquePersonnelId)

                    val allCurrentPersonnel = allPersonnel.plus(newPersonnel)
                    val newShopInfo = companyEntity.copy(companyPersonnel = allCurrentPersonnel.toPersonnelEntitiesJson())
                    appDatabase.personnelDao.registerPersonnel(newPersonnel, newShopInfo)

                    val addedPersonnelIdsJson = AdditionEntityMarkers(context).getAddedPersonnelIds.first().toNotNull()
                    val addedPersonnelIds = addedPersonnelIdsJson.toUniqueIds().plus(UniqueId(personnel.uniquePersonnelId)).toSet().toList()
                    AdditionEntityMarkers(context).saveAddedPersonnelIds(addedPersonnelIds.toUniqueIdsJson())

                    emit(Resource.Success("Personnel successfully added"))
                    userPreferences.saveShopInfo(newShopInfo.toCompanyEntityJson().toNotNull())
                }
            }
        }catch (e: Exception){
            emit(Resource.Error("Unknown error!! \nCould not add personnel\n${e.message}"))
        }
    }

    override suspend fun addPersonnel(personnel: PersonnelEntities) {
        try {
            val allPersonnel = appDatabase.personnelDao.getAllPersonnel() ?: emptyList()
            val allUniquePersonnelIds = allPersonnel.map { it.uniquePersonnelId }
            val newPersonnel = personnel.filter { !allUniquePersonnelIds.contains(it.uniquePersonnelId) }
            appDatabase.personnelDao.addPersonnel(newPersonnel)
        }catch (_: Exception){}
    }

    override suspend fun getPersonnel(uniquePersonnelId: String): PersonnelEntity? {
        return appDatabase.personnelDao.getPersonnel(uniquePersonnelId)
    }

    override suspend fun getPersonnelByName(personnelName: String): PersonnelEntities? {
        return appDatabase.personnelDao.getPersonnelByName(personnelName)
    }

    override suspend fun updatePersonnel(personnel: PersonnelEntity): Flow<Resource<String?>> = flow  {
        emit(Resource.Loading())
        try {
            val context = MyShopManagerApp.applicationContext()
            val userPreferences = UserPreferences(context)
            val isLoggedIn = userPreferences.getLoggedInState.first() == true
            val personnelIsLoggedIn = userPreferences.getPersonnelLoggedInState.first() == true
            val loggedInPersonnel = userPreferences.getPersonnelInfo.first()?.toPersonnelEntity()
            val personnelHimself = loggedInPersonnel?.uniquePersonnelId == personnel.uniquePersonnelId
            val companyEntity = userPreferences.getShopInfo.first()?.toCompanyEntity()
            val hasAdminRights = userPreferences.getPersonnelInfo.first().toPersonnelEntity()?.hasAdminRights == true
            val invalidParameters = (personnel.firstName.isEmpty() || personnel.lastName.isEmpty() || personnel.contact.isEmpty() || personnel.password.isBlank())
            val unUpdatedPersonnel = appDatabase.personnelDao.getPersonnel(personnel.uniquePersonnelId)
            val allPersonnel = appDatabase.personnelDao.getAllPersonnel() ?: emptyList()
            val filteredPersonnelNames = allPersonnel.filter { !(it.firstName == unUpdatedPersonnel?.firstName && it.lastName == unUpdatedPersonnel.lastName && it.otherNames == unUpdatedPersonnel.otherNames) }
            val allPersonnelNames = filteredPersonnelNames.map { it.firstName.trim().lowercase(Locale.ROOT) + it.lastName.trim().lowercase(Locale.ROOT) + it.otherNames?.trim()?.lowercase(Locale.ROOT) }
            val name = personnel.firstName.trim().lowercase(Locale.ROOT) + personnel.lastName.trim().lowercase(Locale.ROOT) + personnel.otherNames?.trim()?.lowercase(Locale.ROOT)

            when(true){
                invalidParameters->{
                    emit(Resource.Error("Unable to update personnel \nPlease ensure that the first name, last name and contact are provided"))
                }
                (!isLoggedIn)->{
                    emit(Resource.Error("Unable to update personnel \nYou're not logged in to any shop account"))
                }
                (!personnelIsLoggedIn)->{
                    emit(Resource.Error("Unable to update personnel \nYou're not logged in as a personnel"))
                }
                (companyEntity == null)->{
                    emit(Resource.Error("Unable to update personnel \nCould not load the logged in shop info\nPlease try again later"))
                }
                (unUpdatedPersonnel == null)->{
                    emit(Resource.Error("Unable to update personnel \nCould not load the info of the personnel you're trying to update.\nPlease try again later"))
                }
                (!(personnelHimself || hasAdminRights))->{
                    emit(Resource.Error("Unable to update personnel \nOnly personnel with admin rights or the personnel himself can update another personnel"))
                }
                (allPersonnelNames.contains(name)) ->{
                    emit(Resource.Error("Unable to update personnel \nPersonnel with provided names already exists"))
                }
                else->{
                    appDatabase.personnelDao.updatePersonnel(personnel)
                    val allCurrentPersonnel = allPersonnel.filter { it.uniquePersonnelId != personnel.uniquePersonnelId }.plus(personnel)
                    userPreferences.saveShopInfo(companyEntity.copy(companyPersonnel = allCurrentPersonnel.toPersonnelEntitiesJson()).toCompanyEntityJson().toNotNull())

                    val addedPersonnelIdsJson = AdditionEntityMarkers(context).getAddedPersonnelIds.first().toNotNull()
                    val addedPersonnelIds = addedPersonnelIdsJson.toUniqueIds().plus(UniqueId(personnel.uniquePersonnelId)).toSet().toList()
                    AdditionEntityMarkers(context).saveAddedPersonnelIds(addedPersonnelIds.toUniqueIdsJson())

                    val updatedPersonnelIdsJson = ChangesEntityMarkers(context).getChangedPersonnelIds.first().toNotNull()
                    val updatedPersonnelIds = updatedPersonnelIdsJson.toUniqueIds().plus(UniqueId(personnel.uniquePersonnelId)).toSet().toList()
                    ChangesEntityMarkers(context).saveChangedPersonnelIds(updatedPersonnelIds.toUniqueIdsJson())
                    emit(Resource.Success("Personnel successfully updated"))
                }
            }
        }catch (e: Exception){
            emit(Resource.Error("Unknown error!! \nCould not update personnel"))
        }
    }

    override suspend fun deletePersonnel(uniquePersonnelId: String): Flow<Resource<String?>> = flow  {
        emit(Resource.Loading())
        try {
            val context = MyShopManagerApp.applicationContext()
            val userPreferences = UserPreferences(context)
            val isPrincipalAdmin = userPreferences.getPersonnelInfo.first()?.toPersonnelEntity()?.isPrincipalAdmin == true
            val isLoggedIn = userPreferences.getLoggedInState.first() == true
            val companyEntity = userPreferences.getShopInfo.first()?.toCompanyEntity()

            val personnel = appDatabase.personnelDao.getPersonnel(uniquePersonnelId)
            val allRevenues = appDatabase.revenueDao.getAllRevenues()?.map { it.uniquePersonnelId } ?: emptyList()
            val allExpenses = appDatabase.expenseDao.getAllExpenses()?.map { it.uniquePersonnelId } ?: emptyList()
            val allSavings = appDatabase.savingsDao.getAllSavings()?.map { it.uniquePersonnelId } ?: emptyList()
            val allWithdrawals = appDatabase.withdrawalDao.getAllWithdrawals()?.map { it.uniquePersonnelId } ?: emptyList()
            val allDebts = appDatabase.debtDao.getAllDebt()?.map { it.uniquePersonnelId } ?: emptyList()
            val allDebtRepayments = appDatabase.debtRepaymentDao.getAllDebtRepayment()?.map { it.uniquePersonnelId } ?: emptyList()
            val personnelIsPrincipalAdmin = personnel?.isPrincipalAdmin == true
            when(true){
                !isPrincipalAdmin ->{
                    emit(Resource.Error("Could not delete personnel \nOnly the principal admin can delete other admins"))
                }
                personnelIsPrincipalAdmin ->{
                    emit(Resource.Error("Could not delete personnel \nYou cannot delete the principal admin"))
                }
                (personnel == null)->{
                    emit(Resource.Error("Could not delete personnel \nCould not get the personnel details\nPlease try again later"))
                }
                (!isLoggedIn)->{
                    emit(Resource.Error("Unable to delete personnel \nYou're not logged in to any shop account"))
                }
                (companyEntity == null)->{
                    emit(Resource.Error("Unable to delete personnel \nCould not load the logged in shop info\nPlease try again later"))
                }
                (allRevenues.contains(uniquePersonnelId) || allExpenses.contains(uniquePersonnelId) || allSavings.contains(uniquePersonnelId) || allWithdrawals.contains(uniquePersonnelId) || allDebts.contains(uniquePersonnelId) || allDebtRepayments.contains(uniquePersonnelId))->{
                    emit(Resource.Error("Could not delete personnel \nThis personnel is already involved with other activities such as savings, revenue, expenses, etc." +
                            "\nTo delete personnel, you have to delete all the records this personnel is associated with"))
                }
                else->{
                    appDatabase.personnelDao.deletePersonnel(uniquePersonnelId)
                    val allPersonnel = appDatabase.personnelDao.getAllPersonnel() ?: emptyList()
                    val allCurrentPersonnel = allPersonnel.filter { it.uniquePersonnelId != personnel.uniquePersonnelId }
                    userPreferences.saveShopInfo(companyEntity.copy(companyPersonnel = allCurrentPersonnel.toPersonnelEntitiesJson()).toCompanyEntityJson().toNotNull())


                    val deletedPersonnelIdsJson = ChangesEntityMarkers(context).getChangedPersonnelIds.first().toNotNull()
                    val deletedPersonnelIds = deletedPersonnelIdsJson.toUniqueIds().plus(UniqueId(personnel.uniquePersonnelId)).toSet().toList()
                    ChangesEntityMarkers(context).saveChangedPersonnelIds(deletedPersonnelIds.toUniqueIdsJson())
                    emit(Resource.Success("Personnel successfully deleted"))
                }
            }
        }catch (e: Exception){
            emit(Resource.Error("Unknown error!! \nCould not delete personnel"))
        }
    }

    override suspend fun deleteAllPersonnel() {
        appDatabase.personnelDao.deleteAllPersonnel()
    }

    override suspend fun loginPersonnel(userName: String, password: String): Flow<Resource<String?>> = flow{
        emit(Resource.Loading())
        try {
            val context = MyShopManagerApp.applicationContext()
            val userPreferences = UserPreferences(context)
            val passwordOrUserNameEmpty = userName.isBlank() || password.isBlank()
            val allPersonnel = appDatabase.personnelDao.getAllPersonnel() ?: emptyList()
            val personnelUserName = userName.trim().lowercase(Locale.ROOT)
            val selectedPersonnelList = allPersonnel.filter { it.userName.lowercase(Locale.getDefault()).trim() == personnelUserName }
            val selectedPersonnel = selectedPersonnelList.firstOrNull { it.password == password }
            val selectedPersonnelIsActive = selectedPersonnel?.isActive == true

            when(true){
                (passwordOrUserNameEmpty)->{
                    emit(Resource.Error("Could not log in personnel\nPassword or username fields are blank. Please fill those fields appropriately"))
                }
                (selectedPersonnelList.isEmpty())->{
                    emit(Resource.Error("Could not log in personnel\nCould not find personnel with username: $userName"))
                }
                (selectedPersonnel == null)->{
                    emit(Resource.Error("Could not log in personnel\nThe password for selected personnel does not match. Please enter valid password"))
                }
                (!selectedPersonnelIsActive)->{
                    emit(Resource.Error("Could not log in personnel\nThe personnel with these details has been made inactive by the shop's admin"))
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
    ): Flow<Resource<String?>> = flow{
        try {
            emit(Resource.Loading())
            val context = MyShopManagerApp.applicationContext()
            val userPreferences = UserPreferences(context)
            val personnelIsLoggedIn = userPreferences.getPersonnelLoggedInState.first() == true
            val personnel = userPreferences.getPersonnelInfo.first().toPersonnelEntity()
            val currentPasswordMatches = personnel?.password == currentPassword
            when(true){
                (newPassword.length < 4)->{
                    emit(Resource.Error("Password length is too short"))
                }
                !personnelIsLoggedIn->{
                    emit(Resource.Error("Could not change password because you are not logged in"))
                }

                (personnel == null)->{
                    emit(Resource.Error("Could not change password because personnel's details could not be loaded"))
                }
                (!currentPasswordMatches)->{
                    emit(Resource.Error("Password does not match.\nPlease try again"))
                }
                else->{
                    val updatedPersonnel = personnel.copy(password = newPassword)
                    appDatabase.personnelDao.updatePersonnel(updatedPersonnel)
                    userPreferences.savePersonnelInfo(updatedPersonnel.toPersonnelEntityJson())

                    val addedPersonnelIdsJson = AdditionEntityMarkers(context).getAddedPersonnelIds.first().toNotNull()
                    val addedPersonnelIds = addedPersonnelIdsJson.toUniqueIds().plus(UniqueId(personnel.uniquePersonnelId)).toSet().toList()
                    AdditionEntityMarkers(context).saveAddedPersonnelIds(addedPersonnelIds.toUniqueIdsJson())

                    val updatedPersonnelIdsJson = ChangesEntityMarkers(context).getChangedPersonnelIds.first().toNotNull()
                    val updatedPersonnelIds = updatedPersonnelIdsJson.toUniqueIds().plus(UniqueId(personnel.uniquePersonnelId)).toSet().toList()
                    ChangesEntityMarkers(context).saveChangedPersonnelIds(updatedPersonnelIds.toUniqueIdsJson())

                    emit(Resource.Success("Password successfully changed"))
                }
            }
        }catch (e: Exception){
            emit(Resource.Error("Unable to reset password\nError Message: ${e.message}"))
        }
    }

    override suspend fun resetPersonnelPassword(uniquePersonnelId: String): Flow<Resource<String?>> = flow{
        try {
            emit(Resource.Loading())
            val context = MyShopManagerApp.applicationContext()
            val userPreferences = UserPreferences(context)
            val personnelIsLoggedIn = userPreferences.getPersonnelLoggedInState.first() ?: false
            val hasAdminRights = userPreferences.getPersonnelInfo.first().toPersonnelEntity()?.hasAdminRights ?: false
            val personnel = appDatabase.personnelDao.getPersonnel(uniquePersonnelId)
            val loggedInPersonnel = userPreferences.getPersonnelInfo.first()?.toPersonnelEntity()
            val personnelHimself = loggedInPersonnel?.uniquePersonnelId == personnel?.uniquePersonnelId

            when(true){
                !personnelIsLoggedIn->{
                    emit(Resource.Error("Could not reset password because you are not logged in"))
                }
                (!(personnelHimself || hasAdminRights))->{
                    emit(Resource.Error("Could not reset password because you do not have admin rights"))
                }
                (personnel == null)->{
                    emit(Resource.Error("Could not reset password because this personnel's details could not be loaded"))
                }

                else->{
                    val updatedPersonnel = personnel.copy(password = "1234")
                    appDatabase.personnelDao.updatePersonnel(updatedPersonnel)

                    val addedPersonnelIdsJson = AdditionEntityMarkers(context).getAddedPersonnelIds.first().toNotNull()
                    val addedPersonnelIds = addedPersonnelIdsJson.toUniqueIds().plus(UniqueId(personnel.uniquePersonnelId)).toSet().toList()
                    AdditionEntityMarkers(context).saveAddedPersonnelIds(addedPersonnelIds.toUniqueIdsJson())

                    val updatedPersonnelIdsJson = ChangesEntityMarkers(context).getChangedPersonnelIds.first().toNotNull()
                    val updatedPersonnelIds = updatedPersonnelIdsJson.toUniqueIds().plus(UniqueId(personnel.uniquePersonnelId)).toSet().toList()
                    ChangesEntityMarkers(context).saveChangedPersonnelIds(updatedPersonnelIds.toUniqueIdsJson())

                    emit(Resource.Success("Password successfully changed to 1234"))
                }
            }
        }catch (e: Exception){
            emit(Resource.Error("Unable to reset password\nError Message: ${e.message}"))
        }
    }

}