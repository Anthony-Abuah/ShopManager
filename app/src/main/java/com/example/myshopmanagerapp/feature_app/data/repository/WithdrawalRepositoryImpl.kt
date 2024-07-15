package com.example.myshopmanagerapp.feature_app.data.repository

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import com.example.myshopmanagerapp.R
import com.example.myshopmanagerapp.core.*
import com.example.myshopmanagerapp.core.Functions.shortened
import com.example.myshopmanagerapp.core.Functions.toCompanyEntity
import com.example.myshopmanagerapp.core.Functions.toDate
import com.example.myshopmanagerapp.core.Functions.toDateString
import com.example.myshopmanagerapp.core.Functions.toEllipses
import com.example.myshopmanagerapp.core.Functions.toNotNull
import com.example.myshopmanagerapp.core.Functions.toTimestamp
import com.example.myshopmanagerapp.core.TypeConverters.toPersonnelEntity
import com.example.myshopmanagerapp.core.TypeConverters.toUniqueIds
import com.example.myshopmanagerapp.core.TypeConverters.toUniqueIdsJson
import com.example.myshopmanagerapp.feature_app.MyShopManagerApp
import com.example.myshopmanagerapp.feature_app.data.local.AppDatabase
import com.example.myshopmanagerapp.feature_app.data.local.entities.withdrawals.WithdrawalEntity
import com.example.myshopmanagerapp.feature_app.domain.model.ItemValue
import com.example.myshopmanagerapp.feature_app.domain.model.PeriodDropDownItem
import com.example.myshopmanagerapp.feature_app.domain.model.UniqueId
import com.example.myshopmanagerapp.feature_app.domain.repository.WithdrawalRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.time.LocalDate

class WithdrawalRepositoryImpl(
    private val appDatabase: AppDatabase
): WithdrawalRepository{
    override fun getAllWithdrawals(): Flow<Resource<WithdrawalEntities?>> = flow{
        emit(Resource.Loading())
        val allWithdrawals: List<WithdrawalEntity>?
        try {
            allWithdrawals = appDatabase.withdrawalDao.getAllWithdrawals()
            emit(Resource.Success(allWithdrawals))
        }catch (e: Exception){
            emit(Resource.Error(
                message = "Couldn't load all Withdrawals from Database",
                data = emptyList()
            ))
        }
    }

    override suspend fun addWithdrawal(withdrawal: WithdrawalEntity): Flow<Resource<String?>> = flow{
        try {
            val context = MyShopManagerApp.applicationContext()
            val userPreferences = UserPreferences(context)
            val isLoggedIn = userPreferences.getLoggedInState.first() ?: false
            val personnelIsLoggedIn = userPreferences.getPersonnelLoggedInState.first() ?: false
            val uniquePersonnelId = userPreferences.getPersonnelInfo.first()?.toPersonnelEntity()?.uniquePersonnelId.toNotNull()
            val personnel = appDatabase.personnelDao.getPersonnel(uniquePersonnelId)

            val dateNow = LocalDate.now().toDate().time
            val bankAccount = appDatabase.bankAccountDao.getBankAccount(withdrawal.uniqueBankAccountId)
            when(true){
                (dateNow < withdrawal.date)->{
                    emit(Resource.Error("Unable to add withdrawal\nThe selected date hasn't come yet"))
                }
                (withdrawal.withdrawalAmount <= 0.0 )->{
                    emit(Resource.Error("Unable to add withdrawal.\nPlease enter an amount that is greater than 0"))
                }
                (bankAccount == null)->{
                    emit(Resource.Error("Unable to add withdrawal.\nCould not get bank account details. " +
                            "Please select bank account or add a new bank account to your bank account list"))
                }
                (personnel == null)->{
                    emit(Resource.Error("Unable to add withdrawal\nCould not get personnel details"))
                }
                !isLoggedIn->{
                    emit(Resource.Error("Unable to add withdrawal." +
                            "\nPlease log in into an account to save info" +
                            "\nCreate a new account if you don't have one" +
                            "\nYou can create an account even if offline"))
                }
                !personnelIsLoggedIn->{
                    emit(Resource.Error("Unable to add withdrawal." +
                            "\nPlease log in into a personnel account to save info" +
                            "\nCreate a new personnel account if you don't have one" +
                            "\nThis helps the app to determine which personnel is entering the information"))
                }
                else->{
                    val accountBalance = bankAccount.accountBalance ?: 0.0
                    val totalSavingsAmount = accountBalance.minus(withdrawal.withdrawalAmount)
                    appDatabase.withdrawalDao.insertWithdrawal(withdrawal.copy(uniquePersonnelId = uniquePersonnelId), bankAccount.uniqueBankAccountId, totalSavingsAmount)

                    val addedWithdrawalIdsJson = AdditionEntityMarkers(context).getAddedWithdrawalIds.first().toNotNull()
                    val addedWithdrawalIds = addedWithdrawalIdsJson.toUniqueIds().plus(UniqueId(withdrawal.uniqueWithdrawalId)).toSet().toList()
                    AdditionEntityMarkers(context).saveAddedWithdrawalIds(addedWithdrawalIds.toUniqueIdsJson())

                    val addedBankAccountIdsJson = AdditionEntityMarkers(context).getAddedBankAccountIds.first().toNotNull()
                    val addedBankAccountIds = addedBankAccountIdsJson.toUniqueIds().plus(UniqueId(bankAccount.uniqueBankAccountId)).toSet().toList()
                    AdditionEntityMarkers(context).saveAddedBankAccountIds(addedBankAccountIds.toUniqueIdsJson())

                    val updatedBankAccountIdsJson = ChangesEntityMarkers(context).getChangedBankAccountIds.first().toNotNull()
                    val updatedBankAccountIds = updatedBankAccountIdsJson.toUniqueIds().plus(UniqueId(bankAccount.uniqueBankAccountId)).toSet().toList()
                    ChangesEntityMarkers(context).saveChangedBankAccountIds(updatedBankAccountIds.toUniqueIdsJson())

                    emit(Resource.Success("Withdrawal successfully added"))
                }
            }
        }catch (e: Exception){
            emit(Resource.Error("Unable to add withdrawal\nError Message: ${e.message}"))
        }
    }

    override suspend fun addWithdrawals(withdrawals: WithdrawalEntities) {
        try {
            val allWithdrawals = appDatabase.withdrawalDao.getAllWithdrawals() ?: emptyList()
            val allUniqueWithdrawalIds = allWithdrawals.map { it.uniqueWithdrawalId }
            val newWithdrawals = withdrawals.filter { !allUniqueWithdrawalIds.contains(it.uniqueWithdrawalId) }
            appDatabase.withdrawalDao.addWithdrawals(newWithdrawals)
        }catch (_: Exception){}
    }

    override suspend fun getWithdrawal(uniqueWithdrawalId: String): WithdrawalEntity? {
        return appDatabase.withdrawalDao.getWithdrawal(uniqueWithdrawalId)
    }

    override suspend fun updateWithdrawal(withdrawal: WithdrawalEntity): Flow<Resource<String?>> = flow {
        try {
            val context = MyShopManagerApp.applicationContext()
            val userPreferences = UserPreferences(context)
            val isLoggedIn = userPreferences.getLoggedInState.first() ?: false
            val personnelIsLoggedIn = userPreferences.getPersonnelLoggedInState.first() ?: false
            val uniquePersonnelId = userPreferences.getPersonnelInfo.first()?.toPersonnelEntity()?.uniquePersonnelId.toNotNull()
            val personnel = appDatabase.personnelDao.getPersonnel(uniquePersonnelId)

            val dateNow = LocalDate.now().toDate().time
            val bankAccount = appDatabase.bankAccountDao.getBankAccount(withdrawal.uniqueBankAccountId)
            val oldWithdrawal = appDatabase.withdrawalDao.getWithdrawal(withdrawal.uniquePersonnelId)
            when(true){
                (dateNow < withdrawal.date)->{
                    emit(Resource.Error("Unable to update withdrawal\nThe selected date hasn't come yet"))
                }
                (oldWithdrawal == null)->{
                    emit(Resource.Error("Unable to update withdrawal\nCould not get this withdrawal's old details"))
                }
                (withdrawal.withdrawalAmount <= 0.0 )->{
                    emit(Resource.Error("Unable to update withdrawal.\nPlease enter an amount that is greater than 0"))
                }
                (bankAccount == null)->{
                    emit(Resource.Error("Unable to update withdrawal.\nCould not get bank account details. " +
                            "Please select bank account or update a new bank account to your bank account list"))
                }
                (personnel == null)->{
                    emit(Resource.Error("Unable to update withdrawal\nCould not get personnel details"))
                }
                !isLoggedIn->{
                    emit(Resource.Error("Unable to update withdrawal." +
                            "\nPlease log in into an account to update info" +
                            "\nCreate a new account if you don't have one" +
                            "\nYou can create an account even if offline"))
                }
                !personnelIsLoggedIn->{
                    emit(Resource.Error("Unable to update withdrawal." +
                            "\nPlease log in into a personnel account to update info" +
                            "\nCreate a new personnel account if you don't have one" +
                            "\nThis helps the app to determine which personnel is entering the information"))
                }
                else->{
                    val oldWithdrawalAmount = oldWithdrawal.withdrawalAmount
                    val accountBalance = bankAccount.accountBalance ?: 0.0
                    val totalSavingsAmount = accountBalance.plus(oldWithdrawalAmount).minus(withdrawal.withdrawalAmount)
                    appDatabase.withdrawalDao.updateWithdrawal(
                        withdrawal.copy(uniquePersonnelId = uniquePersonnelId),
                        bankAccount.uniqueBankAccountId,
                        totalSavingsAmount
                    )

                    val addedWithdrawalIdsJson = AdditionEntityMarkers(context).getAddedWithdrawalIds.first().toNotNull()
                    val addedWithdrawalIds = addedWithdrawalIdsJson.toUniqueIds().plus(UniqueId(withdrawal.uniqueWithdrawalId)).toSet().toList()
                    AdditionEntityMarkers(context).saveAddedWithdrawalIds(addedWithdrawalIds.toUniqueIdsJson())

                    val updatedWithdrawalIdsJson = ChangesEntityMarkers(context).getChangedWithdrawalIds.first().toNotNull()
                    val updatedWithdrawalIds = updatedWithdrawalIdsJson.toUniqueIds().plus(UniqueId(withdrawal.uniqueWithdrawalId)).toSet().toList()
                    ChangesEntityMarkers(context).saveChangedWithdrawalIds(updatedWithdrawalIds.toUniqueIdsJson())

                    val addedBankAccountIdsJson = AdditionEntityMarkers(context).getAddedBankAccountIds.first().toNotNull()
                    val addedBankAccountIds = addedBankAccountIdsJson.toUniqueIds().plus(UniqueId(bankAccount.uniqueBankAccountId)).toSet().toList()
                    AdditionEntityMarkers(context).saveAddedBankAccountIds(addedBankAccountIds.toUniqueIdsJson())

                    val updatedBankAccountIdsJson = ChangesEntityMarkers(context).getChangedBankAccountIds.first().toNotNull()
                    val updatedBankAccountIds = updatedBankAccountIdsJson.toUniqueIds().plus(UniqueId(bankAccount.uniqueBankAccountId)).toSet().toList()
                    ChangesEntityMarkers(context).saveChangedBankAccountIds(updatedBankAccountIds.toUniqueIdsJson())

                    emit(Resource.Success("Withdrawal successfully updated"))
                }
            }
        }catch (e: Exception){
            emit(Resource.Error("Unable to update withdrawal\nError Message: ${e.message}"))
        }
    }

    override suspend fun deleteWithdrawal(withdrawalId: Int) {
        appDatabase.withdrawalDao.deleteWithdrawal(withdrawalId)
    }

    override suspend fun deleteWithdrawal(uniqueWithdrawalId: String): Flow<Resource<String?>> = flow {
        try {
            val context = MyShopManagerApp.applicationContext()
            val userPreferences = UserPreferences(context)
            val isLoggedIn = userPreferences.getLoggedInState.first() ?: false
            val personnelIsLoggedIn = userPreferences.getPersonnelLoggedInState.first() ?: false
            val personnelIsAdmin = userPreferences.getPersonnelInfo.first()?.toPersonnelEntity()?.hasAdminRights?: false

            val withdrawal = appDatabase.withdrawalDao.getWithdrawal(uniqueWithdrawalId)
            val bankAccount = appDatabase.bankAccountDao.getBankAccount(withdrawal?.uniqueBankAccountId.toNotNull())
            when(true){
                (withdrawal == null)->{
                    emit(Resource.Error("Unable to delete withdrawal.\nCould not get the withdrawal info you want to delete"))
                }
                (bankAccount == null)->{
                    emit(Resource.Error("Unable to delete withdrawal.\nCould not get the bank account that this withdrawal was made from"))
                }
                !isLoggedIn->{
                    emit(Resource.Error("Unable to delete withdrawal." +
                            "\nPlease log in into an account to delete info" +
                            "\nCreate a new account if you don't have one" +
                            "\nYou can create an account even if offline"))
                }
                !personnelIsLoggedIn->{
                    emit(Resource.Error("Unable to delete withdrawal." +
                            "\nPlease log in into a personnel account to delete info" +
                            "\nCreate a new personnel account if you don't have one" +
                            "\nThis helps the app to determine which personnel is deleting the information"))
                }
                !personnelIsAdmin->{
                    emit(Resource.Error("Unable to delete withdrawal." +
                            "\nThis is because this personnel has not been given privileges to delete information" +
                            "\nTo be able to delete any information, you must have admin rights" +
                            "\nNB: Only an admin can bestow admin rights"))
                }
                else->{
                    val withdrawalAmount = withdrawal.withdrawalAmount
                    val bankAccountBalance = bankAccount.accountBalance ?: 0.0
                    val totalSavingsAmount = bankAccountBalance.plus(withdrawalAmount)
                    appDatabase.withdrawalDao.deleteWithdrawal(
                        uniqueWithdrawalId,
                        bankAccount.uniqueBankAccountId,
                        totalSavingsAmount
                    )

                    val addedWithdrawalIdsJson = AdditionEntityMarkers(context).getAddedWithdrawalIds.first().toNotNull()
                    val addedWithdrawalIds = addedWithdrawalIdsJson.toUniqueIds().filter { it.uniqueId != uniqueWithdrawalId }.toSet().toList()
                    AdditionEntityMarkers(context).saveAddedWithdrawalIds(addedWithdrawalIds.toUniqueIdsJson())

                    val deletedWithdrawalIdsJson = ChangesEntityMarkers(context).getChangedWithdrawalIds.first().toNotNull()
                    val deletedWithdrawalIds = deletedWithdrawalIdsJson.toUniqueIds().plus(UniqueId(withdrawal.uniqueWithdrawalId)).toSet().toList()
                    ChangesEntityMarkers(context).saveChangedWithdrawalIds(deletedWithdrawalIds.toUniqueIdsJson())

                    val addedBankAccountIdsJson = AdditionEntityMarkers(context).getAddedBankAccountIds.first().toNotNull()
                    val addedBankAccountIds = addedBankAccountIdsJson.toUniqueIds().plus(UniqueId(bankAccount.uniqueBankAccountId)).toSet().toList()
                    AdditionEntityMarkers(context).saveAddedBankAccountIds(addedBankAccountIds.toUniqueIdsJson())

                    val updatedBankAccountIdsJson = ChangesEntityMarkers(context).getChangedBankAccountIds.first().toNotNull()
                    val updatedBankAccountIds = updatedBankAccountIdsJson.toUniqueIds().plus(UniqueId(bankAccount.uniqueBankAccountId)).toSet().toList()
                    ChangesEntityMarkers(context).saveChangedBankAccountIds(updatedBankAccountIds.toUniqueIdsJson())

                    emit(Resource.Success("Withdrawal successfully deleted"))
                }
            }
        }catch (e: Exception){
            emit(Resource.Error("Unable to delete withdrawal\nError Message: ${e.message}"))
        }
    }

    override suspend fun deleteAllWithdrawals() {
        appDatabase.withdrawalDao.deleteAllWithdrawals()
    }

    override suspend fun generateWithdrawalsList(
        context: Context,
        withdrawals: WithdrawalEntities,
    mapOfBankAccounts: Map<String, String>
    ): Flow<Resource<String?>> = flow{
        val pageHeight = withdrawals.size.plus(8).times(50)
        val pageWidth = 900
        val pdfDocument = PdfDocument()
        val paint = Paint()
        val title = Paint()
        val body = Paint()
        val pageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 2).create()
        val page = pdfDocument.startPage(pageInfo)
        val canvas = page.canvas
        val centerWidth = canvas.width.div(2f)
        val userPreferences = UserPreferences(context)
        val company = userPreferences.getShopInfo.first().toCompanyEntity()
        val shopName = company?.companyName ?: "Shop route"
        val shopContact = company?.companyName ?: "Contact"
        val shopLocation = company?.companyLocation ?: "Location"

        // Write Shop route
        title.color = Color.BLACK
        title.textSize = 40f
        title.textAlign = Paint.Align.CENTER
        canvas.drawText(shopName.toEllipses(25), centerWidth, 60f, title)

        // Write Location
        body.color = Color.BLACK
        body.textSize = 25f
        body.textAlign = Paint.Align.CENTER
        canvas.drawText("Location: ${shopLocation.toEllipses(50)}", centerWidth, 95f, body)

        // Write Contact
        body.color = Color.BLACK
        body.textSize = 25f
        body.textAlign = Paint.Align.CENTER
        canvas.drawText("Contact: ${shopContact.toEllipses(50)}", centerWidth, 130f, body)

        // Draw Rectangle
        paint.color = Color.rgb(150, 50, 50)
        canvas.drawRect(20f, 140f, canvas.width.minus(20f), 190f, paint)

        // Write Revenue title
        body.color = Color.WHITE
        body.textSize = 25f
        body.textAlign = Paint.Align.LEFT
        title.color = Color.WHITE
        title.textSize = 25f
        title.textAlign = Paint.Align.RIGHT
        canvas.drawText("No.", 25f, 177f, body)
        canvas.drawText("Date", 85f, 177f, body)
        canvas.drawText("Bank Account", 300f, 177f, body)
        canvas.drawText("Amount", canvas.width.minus(30f), 177f, title)

        withdrawals.forEachIndexed { index, withdrawal->
            val newLine = index.plus(1).times(50f)

            // Draw Start Border
            paint.color = Color.rgb(150, 50, 50)
            canvas.drawRect(20f, newLine.plus(140f), 21f, newLine.plus(190f), paint)

            // Draw End Border
            paint.color = Color.rgb(150, 50, 50)
            canvas.drawRect(canvas.width.minus(21f), newLine.plus(140f), canvas.width.minus(20f), newLine.plus(190f), paint)


            val date = "${withdrawal.dayOfWeek.take(3)}, ${withdrawal.date.toDateString()}"
            val bankAccount = mapOfBankAccounts[withdrawal.uniqueBankAccountId] ?: Constants.NotAvailable
            val withdrawalAmount = withdrawal.withdrawalAmount.shortened()

            // Write Invoice title
            body.color = Color.BLACK
            body.textSize = 25f
            body.textAlign = Paint.Align.LEFT
            title.color = Color.BLACK
            title.textSize = 25f
            title.textAlign = Paint.Align.RIGHT
            canvas.drawText(index.plus(1).toString().toEllipses(3), 25f, newLine.plus(177f), body)
            canvas.drawText(date.toEllipses(30), 85f, newLine.plus(177f), body)
            canvas.drawText(bankAccount.toEllipses(30), 300f, newLine.plus(177f), body)
            canvas.drawText(withdrawalAmount.toEllipses(10), canvas.width.minus(30f), newLine.plus(177f), title)

        }

        val nextLine = withdrawals.size.plus(1).times(50f)
        // Draw Rectangle
        paint.color = Color.rgb(150, 50, 50)
        canvas.drawRect(20f, nextLine.plus(140f), canvas.width.minus(20f), nextLine.plus(190f), paint)


        // Write Invoice total
        body.color = Color.WHITE
        body.textSize = 25f
        body.textAlign = Paint.Align.LEFT
        canvas.drawText("Total Withdrawal Amount", 25f, nextLine.plus(175f), body)
        body.textAlign = Paint.Align.RIGHT
        canvas.drawText("GHS ${withdrawals.sumOf { it.withdrawalAmount }.shortened()}", canvas.width.minus(30f), nextLine.plus(175f), body)


        pdfDocument.finishPage(page)
        val directory = getDirectory(context)
        val file = File(directory, "withdrawals.pdf")

        withContext(Dispatchers.IO) {
            pdfDocument.writeTo(FileOutputStream(file))
        }
        pdfDocument.close()
        emit(Resource.Success("Pdf document successfully created"))

    }

    override suspend fun getPeriodicWithdrawalAmount(periodDropDownItem: PeriodDropDownItem): Flow<Resource<ItemValue?>> =flow{
        emit(Resource.Loading())
        try {
            val allWithdrawals = appDatabase.withdrawalDao.getAllWithdrawals() ?: emptyList()
            if (periodDropDownItem.isAllTime) {
                val totalWithdrawalAmount = allWithdrawals.sumOf { it.withdrawalAmount }
                emit(Resource.Success(ItemValue("Total Withdrawals", totalWithdrawalAmount)))
            }else{
                val firstDate = periodDropDownItem.firstDate.toTimestamp()
                val lastDate = periodDropDownItem.lastDate.toTimestamp()
                val allFilteredWithdrawals = allWithdrawals.filter { it.date in firstDate .. lastDate }
                val totalWithdrawalAmount = allFilteredWithdrawals.sumOf { it.withdrawalAmount }
                emit(Resource.Success(ItemValue("Total Withdrawals", totalWithdrawalAmount)))
            }
        }catch (e:Exception){
            emit(Resource.Error("Could not get value"))
        }
    }


    private fun getDirectory(context: Context): File {
        val mediaDir = context.externalMediaDirs.firstOrNull()?.let {
            File(it, context.resources.getString(R.string.app_name)).apply { mkdirs() }
        }
        return if (mediaDir != null && mediaDir.exists()) mediaDir else context.filesDir
    }


}