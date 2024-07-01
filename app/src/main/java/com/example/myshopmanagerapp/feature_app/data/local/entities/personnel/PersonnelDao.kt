package com.example.myshopmanagerapp.feature_app.data.local.entities.personnel

import android.app.Person
import androidx.room.*
import com.example.myshopmanagerapp.core.Constants
import com.example.myshopmanagerapp.core.Constants.Personnel_Table
import com.example.myshopmanagerapp.core.PersonnelEntities
import com.example.myshopmanagerapp.feature_app.data.local.entities.company.CompanyEntity
import java.util.*


@Dao
interface PersonnelDao {

    @Query ("SELECT * FROM $Personnel_Table")
    suspend fun getAllPersonnel(): PersonnelEntities?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addPersonnel(personnel: PersonnelEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addPersonnel(personnel: PersonnelEntities)

    @Query ("SELECT * FROM $Personnel_Table WHERE uniquePersonnelId LIKE :uniquePersonnelId")
    suspend fun getPersonnel(uniquePersonnelId: String): PersonnelEntity?

    @Query ("SELECT * FROM $Personnel_Table WHERE firstName LIKE :firstName")
    suspend fun getPersonnelByName(firstName: String): List<PersonnelEntity>?

    @Query ("DELETE FROM $Personnel_Table")
    suspend fun deleteAllPersonnel()

    @Query ("DELETE FROM $Personnel_Table WHERE uniquePersonnelId LIKE :uniquePersonnelId")
    suspend fun deletePersonnel(uniquePersonnelId: String)

    @Query ("DELETE FROM $Personnel_Table WHERE personnelId LIKE :personnelId")
    suspend fun deletePersonnel(personnelId: Int)

    @Query ("DELETE FROM $Personnel_Table WHERE personnelId NOT IN (SELECT MIN(personnelId) FROM $Personnel_Table GROUP BY uniquePersonnelId)")
    suspend fun deleteDuplicatePersonnel()

    @Update
    suspend fun updatePersonnel(personnel: PersonnelEntity)

    @Update
    suspend fun updateCompany(company: CompanyEntity)

    @Transaction
    suspend fun registerPersonnel(personnel: PersonnelEntity, company: CompanyEntity){
        addPersonnel(personnel)
        updateCompany(company)
    }

}