package com.example.myshopmanagerapp.feature_app.data.remote.dto.debt_repayment

import com.example.myshopmanagerapp.feature_app.domain.model.UniqueId


data class SmartDebtRepayments(
    val debtRepayments: List<DebtRepaymentInfoDto>,
    val uniqueDebtRepaymentIds: List<UniqueId>
)