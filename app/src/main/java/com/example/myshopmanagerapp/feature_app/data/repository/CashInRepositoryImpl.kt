package com.example.myshopmanagerapp.feature_app.data.repository

import com.example.myshopmanagerapp.core.CashInEntities
import com.example.myshopmanagerapp.core.Functions.toDate
import com.example.myshopmanagerapp.core.Resource
import com.example.myshopmanagerapp.feature_app.data.local.entities.cash_in.CashInDao
import com.example.myshopmanagerapp.feature_app.data.local.entities.cash_in.CashInEntity
import com.example.myshopmanagerapp.feature_app.domain.repository.CashInRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.time.LocalDate

class CashInRepositoryImpl(
    private val cashInDao: CashInDao
): CashInRepository{
    override fun getAllCashIns(): Flow<Resource<CashInEntities?>> = flow{
        emit(Resource.Loading())
        val allCashIns: List<CashInEntity>?
        try {
            allCashIns = cashInDao.getAllCashIns()
            emit(Resource.Success(allCashIns))
        }catch (e: Exception){
            emit(Resource.Error(
                message = "Couldn't load all Cash ins from Database",
                data = emptyList()
            ))
        }
    }

    override suspend fun addCashIn(cashIn: CashInEntity): Flow<Resource<String?>> = flow {
        emit(Resource.Loading())
        try {
            val importantFieldsAreMissing = ((cashIn.cashInType.isBlank()) || (cashIn.cashInAmount < 0.1))
            val loanPaymentAmountIsRequired = ((cashIn.isLoan) && ((cashIn.paymentAmount == null) || (cashIn.paymentAmount < 1)))
            val interestAndPaymentAmountIsNull = ((cashIn.interestAmount == null) && ((cashIn.paymentAmount == null) || (cashIn.paymentAmount < 1)))
            val dateNow = LocalDate.now().toDate().time
            when(true){
                importantFieldsAreMissing ->{
                    emit(Resource.Error("Unable to save to database\nPlease ensure that the cash in type and amount values are valid and not empty"))
                }
                (dateNow < cashIn.date)->{
                    emit(Resource.Error("Unable to save to database\nThe selected date has not come yet"))
                }
                loanPaymentAmountIsRequired->{
                    emit(Resource.Error("Unable to save to database\nPlease ensure that the loan payment amount is valid and not empty"))
                }
                interestAndPaymentAmountIsNull->{
                    emit(Resource.Error("Unable to save to database\nSince this is a loan, please ensure that either the interestAmount or the payment amount is not empty"))
                }
                else->{
                    cashInDao.addCashIn(cashIn)
                    emit(Resource.Success("Successfully added"))
                }
            }
        }catch (e: Exception){
            emit(Resource.Error("Unable to add\nError message: ${e.message}"))
        }
    }

    override suspend fun addCashIns(cashIns: CashInEntities) {
        cashInDao.addCashIns(cashIns)
    }

    override suspend fun getCashIn(uniqueCashInId: String): CashInEntity? {
        return cashInDao.getCashIn(uniqueCashInId)
    }


    override suspend fun updateCashIn(cashIn: CashInEntity): Flow<Resource<String?>> = flow  {
        emit(Resource.Loading())
        try {
            val importantFieldsAreMissing = ((cashIn.cashInType.isBlank()) || (cashIn.cashInAmount < 0.1))
            val loanPaymentAmountIsRequired = ((cashIn.isLoan) && ((cashIn.paymentAmount == null) || (cashIn.paymentAmount < 1)))
            val interestAndPaymentAmountIsNull = ((cashIn.interestAmount == null) && ((cashIn.paymentAmount == null) || (cashIn.paymentAmount < 1)))
            val dateNow = LocalDate.now().toDate().time
            when(true) {
                importantFieldsAreMissing -> {
                    emit(Resource.Error("Unable to update\nPlease ensure that the cash in type and amount values are valid and not empty"))
                }
                (dateNow < cashIn.date) -> {
                    emit(Resource.Error("Unable to update\nThe selected date has not come yet"))
                }
                loanPaymentAmountIsRequired -> {
                    emit(Resource.Error("Unable to update\nPlease ensure that the loan payment amount is valid and not empty"))
                }
                interestAndPaymentAmountIsNull -> {
                    emit(Resource.Error("Unable to update\nSince this is a loan, please ensure that either the interestAmount or the payment amount is not empty"))
                }
                else -> {
                    cashInDao.addCashIn(cashIn)
                    emit(Resource.Success("Successfully updated"))
                }
            }
        }catch (e: Exception){
            emit(Resource.Error("Unable to add\nError message: ${e.message}"))
        }
    }


    override suspend fun deleteCashIn(uniqueCashInId: String): Flow<Resource<String?>> = flow  {
        emit(Resource.Loading())
        try {
            val cashIn = cashInDao.getCashIn(uniqueCashInId)
            if (cashIn == null){
                emit(Resource.Error("Unable to delete\nCould not load the details of this cash in entity"))
            }else{
                cashInDao.deleteCashIn(uniqueCashInId)
                emit(Resource.Success("Successfully deleted"))
            }

        }catch (e: Exception){
            emit(Resource.Error("Unable to add\nError message: ${e.message}"))
        }
    }

    override suspend fun deleteAllCashIns() {
        cashInDao.deleteAllCashIns()
    }
}