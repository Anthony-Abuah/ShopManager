package com.example.myshopmanagerapp.feature_app.data.remote.dto.withdrawal

import com.example.myshopmanagerapp.feature_app.domain.model.UniqueId

data class SmartWithdrawals(
    val withdrawals: List<WithdrawalInfoDto>,
    val uniqueWithdrawalIds: List<UniqueId>
)