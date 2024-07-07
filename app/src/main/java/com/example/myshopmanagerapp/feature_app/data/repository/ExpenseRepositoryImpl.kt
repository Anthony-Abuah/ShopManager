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
import com.example.myshopmanagerapp.feature_app.data.local.entities.expenses.ExpenseEntity
import com.example.myshopmanagerapp.feature_app.domain.model.UniqueId
import com.example.myshopmanagerapp.feature_app.domain.repository.ExpenseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.time.LocalDate

class ExpenseRepositoryImpl(
    private val appDatabase: AppDatabase
): ExpenseRepository{
    override fun getAllExpenses(): Flow<Resource<ExpenseEntities?>> = flow{
        emit(Resource.Loading())
        val allExpenses: List<ExpenseEntity>?
        try {
            allExpenses = appDatabase.expenseDao.getAllExpenses()
            emit(Resource.Success(allExpenses))
        }catch (e: Exception){
            emit(Resource.Error(
                message = "Couldn't load all Expenses from Database",
                data = emptyList()
            ))
        }
    }

    override suspend fun addExpense(expense: ExpenseEntity): Flow<Resource<String?>> = flow {
        emit(Resource.Loading())
        try {
            val context = MyShopManagerApp.applicationContext()
            val userPreferences = UserPreferences(context)
            val isLoggedIn = userPreferences.getLoggedInState.first() ?: false
            val personnelIsLoggedIn = userPreferences.getPersonnelLoggedInState.first() ?: false
            val uniquePersonnelId = userPreferences.getPersonnelInfo.first()?.toPersonnelEntity()?.uniquePersonnelId.toNotNull()
            val personnel = appDatabase.personnelDao.getPersonnel(uniquePersonnelId)

            val importantFieldsAreMissing = ((expense.expenseName.isBlank()) || ( expense.expenseType.isBlank()) || ( expense.expenseAmount < 0.1))
            val dateNow = LocalDate.now().toDate().time
            when(true){
                importantFieldsAreMissing ->{
                    emit(Resource.Error("Unable to add expense\nPlease enter the expense route and type and amount"))
                }
                (dateNow < expense.date)->{
                    emit(Resource.Error("Unable to add expense\nThe selected date hasn't come yet"))
                }
                (personnel == null)->{
                    emit(Resource.Error("Unable to add expense\nCould not get personnel details"))
                }
                !isLoggedIn->{
                    emit(Resource.Error("Unable to add expense." +
                            "\nPlease log in into an account to save info" +
                            "\nCreate a new account if you don't have one" +
                            "\nYou can create an account even if offline"))
                }
                !personnelIsLoggedIn->{
                    emit(Resource.Error("Unable to expense." +
                            "\nPlease log in into a personnel account to save info" +
                            "\nCreate a new personnel account if you don't have one" +
                            "\nThis helps the app to determine which personnel is entering the information"))
                }
                else ->{
                    appDatabase.expenseDao.addExpense(expense.copy(uniquePersonnelId = uniquePersonnelId))
                    emit(Resource.Success("Expense successfully added"))
                }
            }
        }catch (e: Exception){
            emit(Resource.Error("Unable to add expense\nError Message: ${e.message}"))
        }
    }

    override suspend fun addExpenses(expenses: ExpenseEntities) {
        try {
            val allExpenses = appDatabase.expenseDao.getAllExpenses() ?: emptyList()
            val allUniqueExpenseIds = allExpenses.map { it.uniqueExpenseId }
            val newExpenses = expenses.filter { !allUniqueExpenseIds.contains(it.uniqueExpenseId) }
            appDatabase.expenseDao.addExpenses(newExpenses)
        }catch (_: Exception){}
    }

    override suspend fun getExpense(uniqueExpenseId: String): ExpenseEntity? {
        return appDatabase.expenseDao.getExpense(uniqueExpenseId)
    }

    override suspend fun getExpenseByName(expenseName: String): ExpenseEntities? {
        return appDatabase.expenseDao.getExpenseByName(expenseName)
    }

    override suspend fun updateExpense(expense: ExpenseEntity): Flow<Resource<String?>> = flow  {
        emit(Resource.Loading())
        try {
            val context = MyShopManagerApp.applicationContext()
            val userPreferences = UserPreferences(context)
            val isLoggedIn = userPreferences.getLoggedInState.first() ?: false
            val personnelIsLoggedIn = userPreferences.getPersonnelLoggedInState.first() ?: false
            val uniquePersonnelId = userPreferences.getPersonnelInfo.first()?.toPersonnelEntity()?.uniquePersonnelId.toNotNull()
            val personnel = appDatabase.personnelDao.getPersonnel(uniquePersonnelId)

            val importantFieldsAreMissing = ((expense.expenseName.isBlank()) || ( expense.expenseType.isBlank()))
            val dateNow = LocalDate.now().toDate().time
            val oldExpense = appDatabase.expenseDao.getExpense(expense.uniqueExpenseId)
            when(true){
                importantFieldsAreMissing ->{
                    emit(Resource.Error("Unable to update expense\nPlease enter the expense route and type"))
                }
                (dateNow < expense.date)->{
                    emit(Resource.Error("Unable to update expense\nThe selected date hasn't come yet"))
                }
                (oldExpense == null)->{
                    emit(Resource.Error("Unable to update expense\nCould not get this expense details"))
                }
                (personnel == null)->{
                    emit(Resource.Error("Unable to update expense\nCould not get personnel details"))
                }
                !isLoggedIn->{
                    emit(Resource.Error("Unable to update expense." +
                            "\nPlease log in into an account to update info" +
                            "\nCreate a new account if you don't have one" +
                            "\nYou can create an account even if offline"))
                }
                !personnelIsLoggedIn->{
                    emit(Resource.Error("Unable to update expense." +
                            "\nPlease log in into a personnel account to update info" +
                            "\nCreate a new personnel account if you don't have one" +
                            "\nThis helps the app to determine which personnel is entering the information"))
                }
                else ->{
                    appDatabase.expenseDao.updateExpense(expense.copy(uniquePersonnelId = uniquePersonnelId))
                    val updatedExpenseIdsJson = UpdateEntityMarkers(context).getUpdatedExpenseId.first().toNotNull()
                    val updatedExpenseIds = updatedExpenseIdsJson.toUniqueIds().plus(UniqueId(expense.uniqueExpenseId)).toSet().toList()
                    UpdateEntityMarkers(context).saveUpdatedExpenseIds(updatedExpenseIds.toUniqueIdsJson())
                    emit(Resource.Success("Expense successfully updated"))
                }
            }
        }catch (e: Exception){
            emit(Resource.Error("Unable to update expense\nError Message: ${e.message}"))
        }
    }
    
    override suspend fun deleteExpense(expenseId: Int) {
        appDatabase.expenseDao.deleteExpense(expenseId)
    }

    override suspend fun deleteExpense(uniqueExpenseId: String): Flow<Resource<String?>> = flow  {
        emit(Resource.Loading())
        try {
            val context = MyShopManagerApp.applicationContext()
            val userPreferences = UserPreferences(context)
            val isLoggedIn = userPreferences.getLoggedInState.first() ?: false
            val personnelIsLoggedIn = userPreferences.getPersonnelLoggedInState.first() ?: false
            val personnelIsAdmin = userPreferences.getPersonnelInfo.first()?.toPersonnelEntity()?.hasAdminRights?: false

            val expense = appDatabase.expenseDao.getExpense(uniqueExpenseId)
            when(true){
                (expense == null)->{
                    emit(Resource.Error("Unable to delete expense \nCould not get the expense details you want to delete"))
                }
                !isLoggedIn->{
                    emit(Resource.Error("Unable to delete expense." +
                            "\nPlease log in into an account to delete info" +
                            "\nCreate a new account if you don't have one" +
                            "\nYou can create an account even if offline"))
                }
                !personnelIsLoggedIn->{
                    emit(Resource.Error("Unable to delete expense." +
                            "\nPlease log in into a personnel account to delete info" +
                            "\nCreate a new personnel account if you don't have one" +
                            "\nThis helps the app to determine which personnel is deleting the information"))
                }
                !personnelIsAdmin->{
                    emit(Resource.Error("Unable to delete expense." +
                            "\nThis is because this personnel has not been given privileges to delete information" +
                            "\nTo be able to delete any information, you must have admin rights" +
                            "\nNB: Only an admin can bestow admin rights"))
                }
                else->{
                    appDatabase.expenseDao.deleteExpense(uniqueExpenseId)
                    val deletedExpenseIdsJson = DeleteEntityMarkers(context).getDeletedExpenseId.first().toNotNull()
                    val deletedExpenseIds = deletedExpenseIdsJson.toUniqueIds().plus(UniqueId(uniqueExpenseId)).toSet().toList()
                    DeleteEntityMarkers(context).saveDeletedExpenseIds(deletedExpenseIds.toUniqueIdsJson())
                    emit(Resource.Success("Expense successfully deleted"))
                }
            }
        }catch (e: Exception){
            emit(Resource.Error("Unable to delete expense\nError Message: ${e.message}"))
        }
    }

    override suspend fun deleteAllExpenses() {
        appDatabase.expenseDao.deleteAllExpenses()
    }


    override suspend fun generateExpenseList(
        context: Context,
        expenses: ExpenseEntities
    ): Flow<Resource<String?>> = flow{
        val pageHeight = expenses.size.plus(8).times(50)
        val pageWidth = 800
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

        // Write Expense title
        body.color = Color.WHITE
        body.textSize = 25f
        body.textAlign = Paint.Align.LEFT
        title.color = Color.WHITE
        title.textSize = 25f
        title.textAlign = Paint.Align.RIGHT
        canvas.drawText("No.", 25f, 177f, body)
        canvas.drawText("Date", 100f, 177f, body)
        canvas.drawText("Expense Type", canvas.width.minus(450f), 177f, body)
        canvas.drawText("Amount", canvas.width.minus(30f), 177f, title)

        var numberOfLines = 0

        val groupedExpenses = expenses.groupBy { it.date }
        groupedExpenses.keys.forEach { expenseDate->
            val expenseList = groupedExpenses[expenseDate] ?: emptyList()
            val dayOfWeek = expenseList.firstOrNull()?.dayOfWeek ?: NotAvailable
            val date = "${dayOfWeek.take(3)}, ${expenseDate.toDateString()}"
            val expenseTypes = expenseList.groupBy { it.expenseType }
            expenseTypes.keys.forEach{expenseType ->
                numberOfLines = numberOfLines.plus(1)
                val newLine = numberOfLines.times(50f)
                val groupedExpenseTypes = expenseTypes[expenseType] ?: emptyList()
                val expenseAmount = groupedExpenseTypes.sumOf { it.expenseAmount }.toString()

                // Draw Start Border
                paint.color = Color.rgb(150, 50, 50)
                canvas.drawRect(20f, newLine.plus(140f), 21f, newLine.plus(190f), paint)

                // Draw End Border
                paint.color = Color.rgb(150, 50, 50)
                canvas.drawRect(canvas.width.minus(21f), newLine.plus(140f), canvas.width.minus(20f), newLine.plus(190f), paint)

                // Write Invoice title
                body.color = Color.BLACK
                body.textSize = 25f
                body.textAlign = Paint.Align.LEFT
                title.color = Color.BLACK
                title.textSize = 25f
                title.textAlign = Paint.Align.RIGHT
                canvas.drawText(numberOfLines.toString().toEllipses(3), 25f, newLine.plus(183f), body)
                canvas.drawText(date.toEllipses(30), 100f, newLine.plus(183f), body)
                canvas.drawText(expenseType.toEllipses(30), canvas.width.minus(460f), newLine.plus(183f), body)
                canvas.drawText(expenseAmount.toEllipses(10), canvas.width.minus(30f), newLine.plus(183f), title)
            }

        }

        val nextLine = numberOfLines.plus(1).times(50f)
        // Draw Rectangle
        paint.color = Color.rgb(150, 50, 50)
        canvas.drawRect(20f, nextLine.plus(140f), canvas.width.minus(20f), nextLine.plus(190f), paint)


        // Write Invoice total
        body.color = Color.WHITE
        body.textSize = 25f
        body.textAlign = Paint.Align.LEFT
        canvas.drawText("Total Expenses", 25f, nextLine.plus(175f), body)
        body.textAlign = Paint.Align.RIGHT
        canvas.drawText("GHS ${expenses.sumOf { it.expenseAmount }.shortened()}", canvas.width.minus(30f), nextLine.plus(175f), body)


        pdfDocument.finishPage(page)
        val directory = getDirectory(context)
        val file = File(directory, "expenses.pdf")

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