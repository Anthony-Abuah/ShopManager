package com.example.myshopmanagerapp.feature_app.data.remote.dto.bank

import com.example.myshopmanagerapp.core.Functions.toNotNull
import com.example.myshopmanagerapp.feature_app.data.local.entities.banks.BankAccountEntity

data class BankAccountInfoDto(
    val uniqueBankAccountId: String,
    val uniqueCompanyId: String,
    val bankAccountName: String,
    val bankName: String,
    val bankContact: String,
    val bankLocation: String,
    val accountBalance: Double,
    val otherInfo: String
){
    fun toBankEntity(): BankAccountEntity{
        return BankAccountEntity(
            0,
            uniqueBankAccountId = uniqueBankAccountId,
            bankAccountName = bankAccountName.toNotNull(),
            bankName = bankName.toNotNull(),
            bankContact = bankContact.toNotNull(),
            bankLocation = bankLocation.toNotNull(),
            accountBalance = accountBalance.toNotNull(),
            otherInfo = otherInfo.toNotNull()
        )
    }
}
