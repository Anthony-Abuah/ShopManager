package com.example.myshopmanagerapp.feature_app.domain.repository

import android.content.Context
import com.example.myshopmanagerapp.core.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow


interface BackupRepository {

    suspend fun backupDatabase(context: Context): Flow<Resource<String>>

    suspend fun restoreDatabase(context: Context, restart: Boolean = true): Flow<Resource<String>>

    suspend fun absoluteBackup()


    suspend fun smartBackup()

    suspend fun absoluteSyncCompanyInfo()

    suspend fun flowAbsoluteSyncCompanyInfo(): Flow<Resource<String>>

    suspend fun smartSyncCompanyInfo(coroutineScope: CoroutineScope): Flow<Resource<String>>

    suspend fun changePassword(currentPassword: String, newPassword: String): Flow<Resource<String>>

    suspend fun deleteAccount(): Flow<Resource<String>>

    suspend fun clearAllTables(): Flow<Resource<String>>

}
