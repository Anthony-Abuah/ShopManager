package com.example.myshopmanagerapp.feature_app.data.repository

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import com.example.myshopmanagerapp.R
import com.example.myshopmanagerapp.core.*
import com.example.myshopmanagerapp.core.Constants.NotAvailable
import com.example.myshopmanagerapp.core.Functions.shortened
import com.example.myshopmanagerapp.core.Functions.toCompanyEntity
import com.example.myshopmanagerapp.core.Functions.toDate
import com.example.myshopmanagerapp.core.Functions.toDateString
import com.example.myshopmanagerapp.core.Functions.toEllipses
import com.example.myshopmanagerapp.core.Functions.toNotNull
import com.example.myshopmanagerapp.core.TypeConverters.toPersonnelEntity
import com.example.myshopmanagerapp.core.TypeConverters.toUniqueIds
import com.example.myshopmanagerapp.core.TypeConverters.toUniqueIdsJson
import com.example.myshopmanagerapp.feature_app.MyShopManagerApp
import com.example.myshopmanagerapp.feature_app.data.local.AppDatabase
import com.example.myshopmanagerapp.feature_app.data.local.entities.savings.SavingsEntity
import com.example.myshopmanagerapp.feature_app.domain.model.UniqueId
import com.example.myshopmanagerapp.feature_app.domain.repository.SavingsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.time.LocalDate

class SavingsRepositoryImpl(
    private val appDatabase: AppDatabase
): SavingsRepository{
    override fun getAllSavings(): Flow<Resource<SavingsEntities?>> = flow{
        var allSavings: List<SavingsEntity>? = emptyList()
        emit(Resource.Loading(allSavings))
        try {
            allSavings = appDatabase.savingsDao.getAllSavings()
            emit(Resource.Success(allSavings))
        }catch (e: Exception){
            emit(Resource.Error(
                message = "Couldn't load all Savings from Database",
                data = emptyList()
            ))
        }
    }

    override suspend fun addSavings(savings: SavingsEntity): Flow<Resource<String?>> = flow {
        try {
            val context = MyShopManagerApp.applicationContext()
            val userPreferences = UserPreferences(context)
            val isLoggedIn = userPreferences.getLoggedInState.first() ?: false
            val personnelIsLoggedIn = userPreferences.getPersonnelLoggedInState.first() ?: false
            val uniquePersonnelId = userPreferences.getPersonnelInfo.first()?.toPersonnelEntity()?.uniquePersonnelId.toNotNull()
            val personnel = appDatabase.personnelDao.getPersonnel(uniquePersonnelId)

            val dateNow = LocalDate.now().toDate().time
            val bankAccount = appDatabase.bankAccountDao.getBankAccount(savings.uniqueBankAccountId)
            when(true){
                (dateNow < savings.date)->{
                    emit(Resource.Error("Unable to add savings\nThe selected date hasn't come yet"))
                }
                (savings.savingsAmount <= 0.0 )->{
                    emit(Resource.Error("Unable to add savings.\nPlease enter an amount that is greater than 0"))
                }
                (bankAccount == null)->{
                    emit(Resource.Error("Unable to add savings.\nCould not get bank account details. " +
                            "Please select bank account or add a new bank account to your bank account list"))
                }
                (personnel == null)->{
                    emit(Resource.Error("Unable to add savings\nCould not get personnel details"))
                }
                !isLoggedIn->{
                    emit(Resource.Error("Unable to add savings." +
                            "\nPlease log in into an account to save info" +
                            "\nCreate a new account if you don't have one" +
                            "\nYou can create an account even if offline"))
                }
                !personnelIsLoggedIn->{
                    emit(Resource.Error("Unable to add savings." +
                            "\nPlease log in into a personnel account to save info" +
                            "\nCreate a new personnel account if you don't have one" +
                            "\nThis helps the app to determine which personnel is entering the information"))
                }
                else->{
                    val bankAmount = bankAccount.accountBalance ?: 0.0
                    val totalSavingsAmount = bankAmount.plus(savings.savingsAmount)
                    appDatabase.savingsDao.insertSavings(savings.copy(uniquePersonnelId = uniquePersonnelId), bankAccount.uniqueBankAccountId, totalSavingsAmount)

                    val addedSavingsIdsJson = AdditionEntityMarkers(context).getAddedSavingsIds.first().toNotNull()
                    val addedSavingsIds = addedSavingsIdsJson.toUniqueIds().plus(UniqueId(savings.uniqueSavingsId)).toSet().toList()
                    AdditionEntityMarkers(context).saveAddedSavingsIds(addedSavingsIds.toUniqueIdsJson())

                    val addedBankAccountIdsJson = AdditionEntityMarkers(context).getAddedBankAccountIds.first().toNotNull()
                    val addedBankAccountIds = addedBankAccountIdsJson.toUniqueIds().plus(UniqueId(bankAccount.uniqueBankAccountId)).toSet().toList()
                    AdditionEntityMarkers(context).saveAddedBankAccountIds(addedBankAccountIds.toUniqueIdsJson())

                    val updatedBankAccountIdsJson = ChangesEntityMarkers(context).getChangedBankAccountIds.first().toNotNull()
                    val updatedBankAccountIds = updatedBankAccountIdsJson.toUniqueIds().plus(UniqueId(bankAccount.uniqueBankAccountId)).toSet().toList()
                    ChangesEntityMarkers(context).saveChangedBankAccountIds(updatedBankAccountIds.toUniqueIdsJson())

                    emit(Resource.Success("Savings successfully added"))
                }
            }
        }catch (e: Exception){
            emit(Resource.Error("Unable to add savings\nError Message: ${e.message}"))
        }
    }

    override suspend fun addSavings(savings: SavingsEntities) {
        try {
            val allSavings = appDatabase.savingsDao.getAllSavings() ?: emptyList()
            val allUniqueSavingsIds = allSavings.map { it.uniqueSavingsId }
            val newSavings = savings.filter { !allUniqueSavingsIds.contains(it.uniqueSavingsId) }
            appDatabase.savingsDao.addSavings(newSavings)
        }catch (_: Exception){}
    }

    override suspend fun getSavings(uniqueSavingsId: String): SavingsEntity? {
        return appDatabase.savingsDao.getSavings(uniqueSavingsId)
    }

    override suspend fun updateSavings(savings: SavingsEntity): Flow<Resource<String?>> = flow {
        try {
            val context = MyShopManagerApp.applicationContext()
            val userPreferences = UserPreferences(context)
            val isLoggedIn = userPreferences.getLoggedInState.first() ?: false
            val personnelIsLoggedIn = userPreferences.getPersonnelLoggedInState.first() ?: false
            val uniquePersonnelId = userPreferences.getPersonnelInfo.first()?.toPersonnelEntity()?.uniquePersonnelId.toNotNull()
            val personnel = appDatabase.personnelDao.getPersonnel(uniquePersonnelId)

            val dateNow = LocalDate.now().toDate().time
            val bankAccount = appDatabase.bankAccountDao.getBankAccount(savings.uniqueBankAccountId)
            val oldSavings = appDatabase.savingsDao.getSavings(savings.uniqueSavingsId)
            when(true){
                (dateNow < savings.date)->{
                    emit(Resource.Error("Unable to add savings\nThe selected date hasn't come yet"))
                }
                (savings.savingsAmount <= 0.0 )->{
                    emit(Resource.Error("Unable to add savings.\nPlease enter an amount that is greater than 0"))
                }
                (bankAccount == null)->{
                    emit(Resource.Error("Unable to add savings.\nCould not get bank account details. " +
                            "Please select bank account or add a new bank account to your bank account list"))
                }
                (personnel == null)->{
                    emit(Resource.Error("Unable to add savings\nCould not get personnel details"))
                }
                (oldSavings == null)->{
                    emit(Resource.Error("Unable to add savings\nCould not get personnel details"))
                }
                !isLoggedIn->{
                    emit(Resource.Error("Unable to update savings." +
                            "\nPlease log in into an account to update info" +
                            "\nCreate a new account if you don't have one" +
                            "\nYou can create an account even if offline"))
                }
                !personnelIsLoggedIn->{
                    emit(Resource.Error("Unable to update savings." +
                            "\nPlease log in into a personnel account to update info" +
                            "\nCreate a new personnel account if you don't have one" +
                            "\nThis helps the app to determine which personnel is entering the information"))
                }
                else->{
                    val oldSavingsAmount = oldSavings.savingsAmount
                    val accountBalance = bankAccount.accountBalance ?: 0.0
                    val totalSavingsAmount = accountBalance.minus(oldSavingsAmount).plus(savings.savingsAmount)
                    appDatabase.savingsDao.updateSavings(
                        savings.copy(uniquePersonnelId = uniquePersonnelId),
                        bankAccount.uniqueBankAccountId,
                        totalSavingsAmount
                    )

                    val addedSavingsIdsJson = AdditionEntityMarkers(context).getAddedSavingsIds.first().toNotNull()
                    val addedSavingsIds = addedSavingsIdsJson.toUniqueIds().plus(UniqueId(savings.uniqueSavingsId)).toSet().toList()
                    AdditionEntityMarkers(context).saveAddedSavingsIds(addedSavingsIds.toUniqueIdsJson())

                    val updatedSavingsIdsJson = ChangesEntityMarkers(context).getChangedSavingsIds.first().toNotNull()
                    val updatedSavingsIds = updatedSavingsIdsJson.toUniqueIds().plus(UniqueId(savings.uniqueSavingsId)).toSet().toList()
                    ChangesEntityMarkers(context).saveChangedSavingsIds(updatedSavingsIds.toUniqueIdsJson())

                    val addedBankAccountIdsJson = AdditionEntityMarkers(context).getAddedBankAccountIds.first().toNotNull()
                    val addedBankAccountIds = addedBankAccountIdsJson.toUniqueIds().plus(UniqueId(bankAccount.uniqueBankAccountId)).toSet().toList()
                    AdditionEntityMarkers(context).saveAddedBankAccountIds(addedBankAccountIds.toUniqueIdsJson())

                    val updatedBankAccountIdsJson = ChangesEntityMarkers(context).getChangedBankAccountIds.first().toNotNull()
                    val updatedBankAccountIds = updatedBankAccountIdsJson.toUniqueIds().plus(UniqueId(bankAccount.uniqueBankAccountId)).toSet().toList()
                    ChangesEntityMarkers(context).saveChangedBankAccountIds(updatedBankAccountIds.toUniqueIdsJson())

                    emit(Resource.Success("Savings successfully updated"))
                }
            }
        }catch (e: Exception){
            emit(Resource.Error("Unable to update savings\nError Message: ${e.message}"))
        }
    }

    override suspend fun deleteSavings(savingsId: Int) {
        appDatabase.savingsDao.deleteSavings(savingsId)
    }

    override suspend fun deleteSavings(uniqueSavingsId: String): Flow<Resource<String?>> = flow {
        try {
            val context = MyShopManagerApp.applicationContext()
            val userPreferences = UserPreferences(context)
            val isLoggedIn = userPreferences.getLoggedInState.first() ?: false
            val personnelIsLoggedIn = userPreferences.getPersonnelLoggedInState.first() ?: false
            val personnelIsAdmin = userPreferences.getPersonnelInfo.first()?.toPersonnelEntity()?.hasAdminRights?: false

            val savings = appDatabase.savingsDao.getSavings(uniqueSavingsId)
            val bankAccount = appDatabase.bankAccountDao.getBankAccount(savings?.uniqueBankAccountId.toNotNull())
            when(true){
                (savings == null)->{
                    emit(Resource.Error("Unable to delete savings.\nCould not get the savings info you want to delete"))
                }
                (bankAccount == null)->{
                    emit(Resource.Error("Unable to delete savings.\nCould not get the bank account that this savings was made to"))

                }
                !isLoggedIn->{
                    emit(Resource.Error("Unable to delete savings." +
                            "\nPlease log in into an account to delete info" +
                            "\nCreate a new account if you don't have one" +
                            "\nYou can create an account even if offline"))
                }
                !personnelIsLoggedIn->{
                    emit(Resource.Error("Unable to delete savings." +
                            "\nPlease log in into a personnel account to delete info" +
                            "\nCreate a new personnel account if you don't have one" +
                            "\nThis helps the app to determine which personnel is deleting the information"))
                }
                !personnelIsAdmin->{
                    emit(Resource.Error("Unable to delete savings." +
                            "\nThis is because this personnel has not been given privileges to delete information" +
                            "\nTo be able to delete any information, you must have admin rights" +
                            "\nNB: Only an admin can bestow admin rights"))
                }
                else->{
                    val savingsAmount = savings.savingsAmount
                    val bankAccountBalance = bankAccount.accountBalance ?: 0.0
                    val totalSavingsAmount = bankAccountBalance.minus(savingsAmount)
                    appDatabase.savingsDao.deleteSavings(
                        uniqueSavingsId,
                        bankAccount.uniqueBankAccountId,
                        totalSavingsAmount
                    )

                    val addedSavingsIdsJson = AdditionEntityMarkers(context).getAddedSavingsIds.first().toNotNull()
                    val addedSavingsIds = addedSavingsIdsJson.toUniqueIds().filter { it.uniqueId != uniqueSavingsId }.toSet().toList()
                    AdditionEntityMarkers(context).saveAddedSavingsIds(addedSavingsIds.toUniqueIdsJson())

                    val deletedSavingsIdsJson = ChangesEntityMarkers(context).getChangedSavingsIds.first().toNotNull()
                    val deletedSavingsIds = deletedSavingsIdsJson.toUniqueIds().plus(UniqueId(savings.uniqueSavingsId)).toSet().toList()
                    ChangesEntityMarkers(context).saveChangedSavingsIds(deletedSavingsIds.toUniqueIdsJson())

                    val addedBankAccountIdsJson = AdditionEntityMarkers(context).getAddedBankAccountIds.first().toNotNull()
                    val addedBankAccountIds = addedBankAccountIdsJson.toUniqueIds().plus(UniqueId(bankAccount.uniqueBankAccountId)).toSet().toList()
                    AdditionEntityMarkers(context).saveAddedBankAccountIds(addedBankAccountIds.toUniqueIdsJson())

                    val updatedBankAccountIdsJson = ChangesEntityMarkers(context).getChangedBankAccountIds.first().toNotNull()
                    val updatedBankAccountIds = updatedBankAccountIdsJson.toUniqueIds().plus(UniqueId(bankAccount.uniqueBankAccountId)).toSet().toList()
                    ChangesEntityMarkers(context).saveChangedBankAccountIds(updatedBankAccountIds.toUniqueIdsJson())

                    emit(Resource.Success("Savings successfully deleted"))
                }
            }
        }catch (e: Exception){
            emit(Resource.Error("Unable to delete savings\nError Message: ${e.message}"))
        }
    }

    override suspend fun deleteAllSavings() {
        appDatabase.savingsDao.deleteAllSavings()
    }

    override suspend fun generateSavingsList(
        context: Context, 
        savings: SavingsEntities,
        mapOfBankAccounts: Map<String, String>
    ): Flow<Resource<String?>> = flow{
        val pageHeight = savings.size.plus(8).times(50)
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

        savings.forEachIndexed { index, savings->
            val newLine = index.plus(1).times(50f)

            // Draw Start Border
            paint.color = Color.rgb(150, 50, 50)
            canvas.drawRect(20f, newLine.plus(140f), 21f, newLine.plus(190f), paint)

            // Draw End Border
            paint.color = Color.rgb(150, 50, 50)
            canvas.drawRect(canvas.width.minus(21f), newLine.plus(140f), canvas.width.minus(20f), newLine.plus(190f), paint)


            val date = "${savings.dayOfWeek.take(3)}, ${savings.date.toDateString()}"
            val bankAccount = mapOfBankAccounts[savings.uniqueBankAccountId] ?: NotAvailable
            val savingsAmount = savings.savingsAmount.shortened()

            // Write Invoice title
            body.color = Color.BLACK
            body.textSize = 25f
            body.textAlign = Paint.Align.LEFT
            title.color = Color.BLACK
            title.textSize = 25f
            title.textAlign = Paint.Align.RIGHT
            canvas.drawText(index.plus(1).toString().toEllipses(3), 25f, newLine.plus(177f), body)
            canvas.drawText(date.toEllipses(30), 85f, newLine.plus(177f), body)
            canvas.drawText(bankAccount.toEllipses(30),300f, newLine.plus(177f), body)
            canvas.drawText(savingsAmount.toEllipses(10), canvas.width.minus(30f), newLine.plus(177f), title)

        }

        val nextLine = savings.size.plus(1).times(50f)
        // Draw Rectangle
        paint.color = Color.rgb(150, 50, 50)
        canvas.drawRect(20f, nextLine.plus(140f), canvas.width.minus(20f), nextLine.plus(190f), paint)


        // Write Invoice total
        body.color = Color.WHITE
        body.textSize = 25f
        body.textAlign = Paint.Align.LEFT
        canvas.drawText("Total Savings Amount", 25f, nextLine.plus(175f), body)
        body.textAlign = Paint.Align.RIGHT
        canvas.drawText("GHS ${savings.sumOf { it.savingsAmount }.shortened()}", canvas.width.minus(30f), nextLine.plus(175f), body)


        pdfDocument.finishPage(page)
        val directory = getDirectory(context)
        val file = File(directory, "savings.pdf")

        withContext(Dispatchers.IO) {
            pdfDocument.writeTo(FileOutputStream(file))
        }
        pdfDocument.close()
        emit(Resource.Success("Pdf document successfully created"))

    }

    private fun getDirectory(context: Context): File{
        val mediaDir = context.externalMediaDirs.firstOrNull()?.let {
            File(it, context.resources.getString(R.string.app_name)).apply { mkdirs() }
        }
        return if (mediaDir != null && mediaDir.exists()) mediaDir else context.filesDir
    }

}