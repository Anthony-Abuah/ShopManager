package com.example.myshopmanagerapp.feature_app.data.remote.dto.bank

import com.example.myshopmanagerapp.feature_app.domain.model.UniqueId

data class SmartBankAccount(
    val bankAccounts: List<BankAccountInfoDto>,
    val uniqueBankAccountIds: List<UniqueId>
)