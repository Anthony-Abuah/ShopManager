package com.example.myshopmanagerapp.feature_app.data.local.entities.banks

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.myshopmanagerapp.core.Constants.BankAccount_Table
import com.example.myshopmanagerapp.feature_app.data.remote.dto.bank.BankAccountInfoDto

@Entity(tableName = BankAccount_Table)
data class BankAccountEntity(
    @PrimaryKey(autoGenerate = true) val bankId: Int,
    val uniqueBankAccountId: String,
    val bankAccountName: String,
    val bankName: String,
    val bankContact: String,
    val bankLocation: String?,
    val otherInfo: String?,
    val accountBalance: Double?
){
    fun toBankAccountInfoDto(uniqueCompanyId: String): BankAccountInfoDto{
        return BankAccountInfoDto(
            uniqueBankAccountId = uniqueBankAccountId,
            uniqueCompanyId = uniqueCompanyId,
            bankAccountName = bankAccountName,
            bankName = bankName,
            bankContact = bankContact,
            bankLocation = bankLocation,
            accountBalance = accountBalance,
            otherInfo = otherInfo
        )
    }
}

