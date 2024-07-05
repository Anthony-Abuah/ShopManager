package com.example.myshopmanagerapp.feature_app.domain.repository

import com.example.myshopmanagerapp.core.PersonnelEntities
import com.example.myshopmanagerapp.core.Resource
import com.example.myshopmanagerapp.feature_app.data.local.entities.personnel.PersonnelEntity
import kotlinx.coroutines.flow.Flow


interface PersonnelRepository {

    fun getAllPersonnel(): Flow<Resource<PersonnelEntities?>>

    suspend fun registerAndLogIn(personnel: PersonnelEntity): Flow<Resource<String?>>

    suspend fun addPersonnel(personnel: PersonnelEntity): Flow<Resource<String?>>

    suspend fun addPersonnel(personnel: PersonnelEntities)

    suspend fun getPersonnel(uniquePersonnelId: String): PersonnelEntity?

    suspend fun getPersonnelByName(personnelName: String): PersonnelEntities?

    suspend fun updatePersonnel(personnel: PersonnelEntity): Flow<Resource<String?>>

    suspend fun deletePersonnel(uniquePersonnelId: String): Flow<Resource<String?>>

    suspend fun deleteAllPersonnel()

    suspend fun loginPersonnel(userName: String, password: String): Flow<Resource<String?>>

    suspend fun logoutPersonnel(): Flow<Resource<String?>>

    suspend fun changePersonnelPassword(currentPassword: String, newPassword: String): Flow<Resource<String?>>

    suspend fun resetPersonnelPassword(uniquePersonnelId: String): Flow<Resource<String?>>



}
